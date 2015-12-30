package cpe.lesbarbus.cozynotes.utils;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.android.AndroidContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cpe.lesbarbus.cozynotes.models.Note;


public class CouchBaseNote {

    public static final String DB_NAME = "couchbasenotes";
    public static final String TAG = "couchbasenotes";
    private Database database;
    private Manager manager;

    private Context context;


    public CouchBaseNote(Context context) {
        this.context = context;
        Log.d(TAG, "onCreate CochDB");
        manager = null;
        database = null;

        try {
            manager = getManagerInstance();
            database = getDatabaseInstance();
        } catch (Exception e) {
            Log.e(TAG, "Error getting database", e);
        }
    }

    /***
     * Implements Singleton Pattern
     * @return An instance of a the note manager object
     * @throws IOException
     */
    public Manager getManagerInstance() throws IOException {

        if (manager == null) {
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
        }
        return manager;
    }

    /***
     * Implements Singleton Pattern
     * @return An instance of the note database object
     * @throws CouchbaseLiteException
     */
    public Database getDatabaseInstance() throws CouchbaseLiteException {
        if ((database == null) & (manager != null)) {
            this.database = manager.getDatabase(DB_NAME);
        }
        return database;
    }

    /***
     * Add a note to the database
     * @param note note to create
     * @return String, document id of the created note, null if not created
     */
    public String createNote(Note note){
        Document document = database.createDocument();
        String documentId = document.getId();

        try {
            // Save the properties to the document
            document.putProperties(note.getMapFormat());
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
            //if an error occur, the document is deleted
            try {
                document.delete();
                documentId = null;
            } catch (CouchbaseLiteException e1) {
                Log.e (TAG, "Cannot delete document", e);
            }
        }

        return documentId;
    }

    /**
     * Update a note
     * @param note Note with updated fields
     */
    public void updateNote(Note note){


        if(note.get_id() != null){
            //Retrieve the document if the note has an id
            Document document = database.getDocument(note.get_id());
            Map<String, Object> noteMap = note.getMapFormat();

            try {
                // Update the document with the new data
                Map<String, Object> updatedProperties = new HashMap<>();
                updatedProperties.putAll(document.getProperties());
                //iterate each entry of the note
                for(Map.Entry<String, Object> entry : noteMap.entrySet()){
                    updatedProperties.put(entry.getKey(), entry.getValue());
                }

                document.putProperties(updatedProperties);
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Error putting", e);
            }
        }else{
            Log.e(TAG, "The note id is null");
        }
    }



    //TODO: Select an Note by its Id and Select all Note
    /**
     * Find an document stored in the database by its id
     * @param documentId id of the document
     * @return a document, null if not found
     */
    public Document getDocumentById(String documentId){
        return database.getDocument(documentId);
    }

    public void getNoteById(String documentId){
        JSONObject jsonObject = new JSONObject(database.getDocument(documentId).getProperties());
        System.out.println(jsonObject.toString());
    }

    /**
     * Delete a note by its id
     * @param documentId id of the note
     * @return boolean to indicate whether deleted or not
     */
    public boolean deleteNote(String documentId){

        boolean isDeleted = true;
        Document document = database.getDocument(documentId);

        if(document != null){
            try {
                isDeleted = document.delete();
                Log.d(TAG, "Deleted document, deletion status = " + document.isDeleted());
            } catch (CouchbaseLiteException e) {
                Log.e (TAG, "Cannot delete document", e);
            }
        }else{
            Log.e (TAG, "Cannot delete an unexisting document");
        }

        return isDeleted;
    }

    public List<Note> getAllNotes(){
        ArrayList<Note> ln = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        Note n = null;
        try {
            Query query = database.createAllDocumentsQuery();
            query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS);
            QueryEnumerator result = query.run();
            for(Iterator<QueryRow> it = result;it.hasNext();){
                Document doc = it.next().getDocument();
                ln.add(mapper.readValue(new JSONObject(doc.getProperties()).toString(),Note.class));
            }
        } catch (CouchbaseLiteException | IOException e) {
            e.printStackTrace();
        }
        return ln;
    }
}
