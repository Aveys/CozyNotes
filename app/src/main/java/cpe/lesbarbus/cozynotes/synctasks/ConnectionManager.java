package cpe.lesbarbus.cozynotes.synctasks;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

import cpe.lesbarbus.cozynotes.authenticator.AccountGeneral;
import cpe.lesbarbus.cozynotes.utils.BodyInterceptor;
import cpe.lesbarbus.cozynotes.utils.NoBodyInterceptor;

/**
 * Created by arthurveys on 07/01/16.
 */
public class ConnectionManager {

    private final String url;
    private final String authToken;
    private OkHttpClient client;
    public static final MediaType JSON = MediaType.parse("application/json");
    private Context mContext;
    private Account account;


    public ConnectionManager(Context c) {
        this.client = new OkHttpClient();
        mContext = c;
        AccountManager am = AccountManager.get(mContext);
        account = AccountGeneral.getAccountCozy(mContext);
        url = AccountGeneral.getUrlFromAccount(mContext);
        authToken = am.peekAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL);

        client.setAuthenticator(new Authenticator() {
            @Override
            public Request authenticate(Proxy proxy, Response response) throws IOException {
                String credentials = Credentials.basic(AccountGeneral.DEVICE_NAME, authToken);
                return response.request().newBuilder().header("Authorization", credentials).build();
            }

            @Override
            public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
                return null;
            }
        });
    }

    public String getCozyDocument(int id) {
        String res = null;
        try {
            String requestURL = "https://" + AccountGeneral.DEVICE_NAME + ":" + authToken + "@" + url + "/ds-api/data/"+id;
            res = get(requestURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public String sendCozyDocument(int id,String body){
        String res = null;
        try {
            String requestURL = "https://" + AccountGeneral.DEVICE_NAME + ":" + authToken + "@" + url + "/ds-api/data/"+id;
            res = post(requestURL, body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    public String deleteCozyDocument(int id){
        String res =null;
        try{
            String requestURL = "https://" + AccountGeneral.DEVICE_NAME + ":" + authToken + "@" + url + "/ds-api/data/"+id;
            res = delete(requestURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private String post(String url, String json) throws IOException {
        client.networkInterceptors().add(new BodyInterceptor());
        RequestBody body = RequestBody.create(JSON, json);
        System.out.println("Url save cloud: "+url);
        Request req = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Accept", "*/*")
                .build();
        Response res = client.newCall(req).execute();
        String reponse = res.body().string();
        System.out.println("Responde body : "+reponse);
        return reponse;

    }
    private String delete(String url) throws IOException {
        client.networkInterceptors().add(new NoBodyInterceptor());
        Request req = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Accept", "*/*")
                .build();
        Response res = client.newCall(req).execute();
        String reponse = res.body().string();
        System.out.println("Responde body : "+reponse);
        return reponse;
    }

    private String get(String url) throws IOException {
        client.networkInterceptors().add(new NoBodyInterceptor());
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "*/*")
                .build();
        Response res = client.newCall(request).execute();
        String response = res.body().string();
        System.out.println("Response from GET :"+response);
        return response;

    }
}
