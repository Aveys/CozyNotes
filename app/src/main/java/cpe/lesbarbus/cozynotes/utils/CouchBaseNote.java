package cpe.lesbarbus.cozynotes.utils;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.View;
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
    public static final String DOC_TYPE = "note";
    public static final String TAG = "couchbasenotes";
    private Database database;


    public CouchBaseNote(Context context) {
        Log.d(TAG, "onCreate CouchDB");
        database = null;

        try {
            CouchBaseManager couchManager = new CouchBaseManager(context);
            database = couchManager.getDatabaseInstance();
            createView();
        } catch (Exception e) {
            Log.e(TAG, "Error getting database", e);
        }
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

                if (DOC_TYPE.equals(doc.getProperty("type"))) {
                    ln.add(mapper.readValue(new JSONObject(doc.getProperties()).toString(), Note.class));
                }
            }
        } catch (CouchbaseLiteException | IOException e) {
            e.printStackTrace();
        }
        return ln;
    }

    public List<Note> getAllNotesByNotebook(String notebookId){
        ArrayList<Note> ln = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        Note n = null;
        try {
            Query query = database.createAllDocumentsQuery();
            query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS);
            QueryEnumerator result = query.run();
            for(Iterator<QueryRow> it = result;it.hasNext();){
                Document doc = it.next().getDocument();
                if (DOC_TYPE.equals(doc.getProperty("type")) && notebookId.equals(doc.getProperty("notebookId"))) {
                    ln.add(mapper.readValue(new JSONObject(doc.getProperties()).toString(), Note.class));
                }
            }
        } catch (CouchbaseLiteException | IOException e) {
            e.printStackTrace();
        }
        return ln;
    }

    //TODO: Use couchbase views
    public View createView(){
        View testView = database.getView("testView");
        if (testView.getMap() == null) {
            testView.setMap(
                    new Mapper(){
                        @Override
                        public void map(Map<String, Object> document, Emitter emitter) {
                            System.out.println("Document retrieved : "+document.toString());
                            if (document.get("type").equals("note")) {
                                List<Object> key = new ArrayList<Object>();
                                key.add(document.get("datetime"));
                                key.add(document.get("title"));
                                key.add(document.get("content"));
                                HashMap<String, Object> value = new HashMap<String, Object>();
                                emitter.emit(key, value);
                            }
                        }
                    }, "2" /* The version number of the mapper... */
            );
            Log.d(TAG, "View Created "+testView.toString());
        }

        Log.d(TAG, testView.toString());

        return null;
    }
}
