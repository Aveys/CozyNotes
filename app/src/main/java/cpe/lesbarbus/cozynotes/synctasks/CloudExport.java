package cpe.lesbarbus.cozynotes.synctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cpe.lesbarbus.cozynotes.utils.CouchBaseNote;
import cpe.lesbarbus.cozynotes.utils.CouchBaseNotebook;

/**
 * Created by arthurveys on 07/01/16.
 */
public class CloudExport extends AsyncTask<Context,Void,String> {

    @Override
    protected String doInBackground(Context... params) {
        Context c = params[0];
        String res=null;
        ConnectionManager conn = new ConnectionManager(c);

        String doc;
        DBHolder db = new DBHolder(new CouchBaseNote().getAllNotes(),new CouchBaseNotebook().getAllNotebooks());
        ObjectMapper mapper = new ObjectMapper();

        try {
            doc = mapper.writeValueAsString(db);

            res = conn.sendCozyDocument(1,doc);
            System.out.println(res);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return res;

    }
}
