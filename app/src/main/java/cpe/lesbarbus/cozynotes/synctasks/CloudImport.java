package cpe.lesbarbus.cozynotes.synctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by arthurveys on 07/01/16.
 */
public class CloudImport extends AsyncTask<Context,Void,String> {
    @Override
    protected String doInBackground(Context... params) {
        Context c = params[0];
        String res=null;
        ConnectionManager conn = new ConnectionManager(c);
        DBHolder db;
        ObjectMapper mapper = new ObjectMapper();

        try {
            res = conn.getCozyDocument(1);
            db = mapper.readValue(res, DBHolder.class);
            System.out.println(db);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
