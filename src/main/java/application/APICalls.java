package application;

import com.google.gson.Gson;
import okhttp3.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Objects;


public class APICalls{
    private String user, pw, server;
    final OkHttpClient o = new OkHttpClient();
    private boolean failure = false;

    public String getUser() {
        return user;
    }

    public boolean isFailure() {
        return failure;
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
        OkHttpClient o = new OkHttpClient();
        RequestBody regBody = RequestBody.create(s, JSON);
        Request req = new Request.Builder()
                .post(regBody)
                .url(server+"register")
                .build();


        return call(o, req);
    }
    private Response call(OkHttpClient o, Request req) throws IOException {
        Call call = o.newCall(req);
        Response resp;
        resp = call.execute();
        Objects.requireNonNull(resp.body()).close();
        return resp;

    }

}
