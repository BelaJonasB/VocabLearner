package application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;


public class APICalls{
    private String user, pw, server;
    final OkHttpClient o = new OkHttpClient();

    public String getUser() {
        return user;
    }


    public Response login(String userIn, String pwIn, String serverIn) throws IOException {
        user = userIn;
        pw = pwIn;
        server = serverIn;

        Request req = new Request.Builder()
                .header("email", user)
                .header("password", pw)
                .url(server+"login")
                .build();

        return call(o, req);

    }

    public Response register(String userIn, String pwIn, String serverIn) throws IOException {
        user = userIn;
        pw = pwIn;
        server = serverIn;

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

        Vocab[] wholeVocab = g.fromJson(Objects.requireNonNull(re.body()).string(), Vocab[].class);
        for(Vocab v : wholeVocab) {
            System.out.println(v.toString());
        }
        Variables.setUsersVocab(wholeVocab);
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

    //Call to API
    private Response call(OkHttpClient o, Request req) throws IOException {
        Call call = o.newCall(req);
        Response resp;
        resp = call.execute();
        return resp;

    }

}
