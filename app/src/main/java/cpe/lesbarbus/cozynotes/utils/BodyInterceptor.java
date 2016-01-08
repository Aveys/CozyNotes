package cpe.lesbarbus.cozynotes.utils;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import okio.Buffer;


/**
 * Created by arthurveys on 07/01/16.
 */
public class BodyInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        System.out.println(bodyToString(request.body()));
        long t1 = System.nanoTime();
        Log.i("CozyServerAuthenticate", String.format("Sending request %s on %s%n%s%n%s",
                request.url(), chain.connection(), request.headers(), request.body()));

        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        Log.i("CozyServerAuthenticate", String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        System.out.println("Code retour"+response.code());

        return response;
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            copy.writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

}

