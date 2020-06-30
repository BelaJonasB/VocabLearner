package application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class APICalls{
    private String user, pw, server;
    final OkHttpClient o = new OkHttpClient();


    //Contructor for when not instantiated in login screen
    public APICalls() {

        //Enryption key with mac adress
        String mac = "DiddiKong"; //Default, if no mac adress
        try {
            NetworkInterface net = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            mac = new String(net.getHardwareAddress());
        } catch (SocketException | UnknownHostException e) {
            System.out.println("No Mac-Address found");
        }
        Crypt c = new Crypt(mac);
        Gson g = new Gson();

        //set Variables for Requests
        try {
            User u = g.fromJson(new FileReader("src/main/resources/logInfo.json"), User.class);
            this.user = u.getEmail();
            this.pw = c.decrypt(u.getPassword());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.server = Variables.getServer();
    }

    //Constructor for when instantiated by login window
    public APICalls(String userIn, String pwIn, String serverIn) {
        this.user = userIn;
        this.pw = pwIn;
        this.server = serverIn;
        Variables.setServer(serverIn);
    }


    public Response login() throws IOException {

        Request req = new Request.Builder()
                .header("email", user)
                .header("password", pw)
                .url(server+"login")
                .build();

        return call(o, req);

    }

    public Response register() throws IOException {

        //User data to Object
        User u = new User(user, pw);
        Gson g = new Gson();
        String s = g.toJson(u);
        System.out.println(s);

        //For Media Type in request body
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        //Call
        RequestBody regBody = RequestBody.create(s, JSON);
        Request req = new Request.Builder()
                .post(regBody)
                .url(server+"register")
                .build();
        return call(o, req);
    }

    //Get the existing Vocab of the User and set it as a Variable in Variables
    public void getUsersVocab() throws IOException {
        Request req = new Request.Builder()
                .header("email", user)
                .header("password", pw)
                .url(server+"voc")
                .build();
        Response re = call(o, req);
        Gson g = new Gson();
        ObjectMapper o = new ObjectMapper();

        //Vocab[] wholeVocab = g.fromJson(Objects.requireNonNull(re.body()).string(), Vocab[].class);
        List<Vocab> vocabList = Arrays.asList(o.readValue(Objects.requireNonNull(re.body()).string(), Vocab[].class));
        Variables.setUsersVocab(vocabList);
        Objects.requireNonNull(re.body()).close();
    }


    //Create new Entry in Vocabulary
    public void postToVoc(Vocab vocab) throws IOException {

        Gson g = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        //User data to Object
        String s = g.toJson(vocab);
        System.out.println(s);

        //For Media Type in request body
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        //Build and call
        RequestBody regBody = RequestBody.create(s, JSON);
        Request req = new Request.Builder()
                .header("email", user)
                .header("password", pw)
                .url(server+"voc")
                .post(regBody)
                .build();
        call(o, req);

        //Update the Vocab List in Variables (with id and phase)
        getUsersVocab();
    }

    //Delete Voc
    public int deleteVoc(String v) {
        Request req = new Request.Builder()
                .header("email", user)
                .header("password", pw)
                .url(server+"voc/"+v)
                .delete()
                .build();
        try {
            Response r = call(o, req);
            int i = r.code();
            Objects.requireNonNull(r.body()).close();
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 404;
        }
    }

    //Edit Voc
    public int editVoc(Vocab v) {
        Gson g = new GsonBuilder()
                .create();

        //User data to Object
        String s = g.toJson(v);
        System.out.println(s);

        //For Media Type in request body
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        //Build and call
        RequestBody regBody = RequestBody.create(s, JSON);
        Request req = new Request.Builder()
                .header("email", user)
                .header("password", pw)
                .url(server+"voc/"+v.getId())
                .patch(regBody)
                .build();
        try {
            Response r = call(o, req);
            int i = r.code();
            Objects.requireNonNull(r.body()).close();
            return i;
        } catch (Exception e) {
            return 404;
        }
    }

    //Call to API
    private Response call(OkHttpClient o, Request req) throws IOException {
        Call call = o.newCall(req);
        Response resp;
        resp = call.execute();
        return resp;

    }

}
