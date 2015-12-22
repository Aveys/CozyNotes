package cpe.lesbarbus.cozynotes.authenticator;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squareup.okhttp.ConnectionSpec;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by arthurveys on 22/12/15.
 */
public class CozyServerAuthenticate implements ServerAuthenticate {

    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    @Override
    public String userSignIn(String email, String pass, String url) throws Exception {
        String passRes=null;
        String formedURL = "https://owner:"+pass+"@"+url+"/device";
        String body = "{\"login\":\"cozynotes\",\"permissions\":{\"File\":{\"description\":\"Synchronise files\"}}}";
        Log.d("CozyServerAuthenticate","Requested URL : "+formedURL+" \n Request Body : "+body);
        String res = post(formedURL,body);
        Log.d("CozyServerAuthenticate","result="+res);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootnode = null;
        try {
            rootnode = mapper.readValue(res,ObjectNode.class);
            passRes=rootnode.get("password").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return passRes;

    }

    private String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON,json);
        Request req = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response res = client.newCall(req).execute();
        return res.body().string();
    }
}
