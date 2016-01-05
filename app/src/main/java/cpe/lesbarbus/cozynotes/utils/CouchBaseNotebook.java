package cpe.lesbarbus.cozynotes.utils;

import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.View;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpe.lesbarbus.cozynotes.models.Note;
import cpe.lesbarbus.cozynotes.models.Notebook;


public class CouchBaseNotebook {

    public static final String DOC_TYPE = "notebook";
    public static final String TAG = "couchbasenotes";
    private Database database;



    public CouchBaseNotebook() {
        Log.d(TAG, "onCreate CouchDBNotebook");
        database = null;
        try {
            database = CouchBaseManager.getDatabaseInstance();
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, Arrays.toString(e.getStackTrace()));
        }
    }

    private void initNotebook() {
        View viewNotebook = database.getView("NotebookView");
        viewNotebook.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                if (document.get("type").equals("notebook")) {
                    List<Object> key = new ArrayList<Object>();
                    key.add(document.get("datetime"));
                    key.add(document.get("title"));
                    key.add(document.get("content"));
                    HashMap<String, Object> value = new HashMap<String, Object>();
                    emitter.emit(key, value);
                }
            }
        }, "2");
    }

    /***
     * Add a notebook to the database
     *
     * @param notebook note to create
     * @return String, document id of the created note, null if not created
     */
    public String createNotebook(Notebook notebook) {
        Document document = database.createDocument();
        String documentId = document.getId();

        try {
            // Save the properties to the document
            document.putProperties(notebook.getMapFormat());
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
            //if an error occur, the document is deleted
            try {
                document.delete();
                documentId = null;
            } catch (CouchbaseLiteException e1) {
                Log.e(TAG, "Cannot delete document", e);
            }
        }

        return documentId;
    }

    /**
     * Update a notebook
     *
     * @param notebook Note with updated fields
     */
    public void updateNotebook(Notebook notebook) {


        if (notebook.get_id() != null) {
            //Retrieve the document if the note has an id
            Document document = database.getDocument(notebook.get_id());
            Map<String, Object> noteMap = notebook.getMapFormat();

            try {
                // Update the document with the new data
                Map<String, Object> updatedProperties = new HashMap<>();
                updatedProperties.putAll(document.getProperties());
                //iterate each entry of the note
                for (Map.Entry<String, Object> entry : noteMap.entrySet()) {
                    updatedProperties.put(entry.getKey(), entry.getValue());
                }

                document.putProperties(updatedProperties);
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Error putting", e);
            }
        } else {
            Log.e(TAG, "The note id is null");
        }
    }

    /**
     * Find an document stored in the database by its id
     *
     * @param documentId id of the document
     * @return a document, null if not found
     */
    public Document getDocumentById(String documentId) {
        return database.getDocument(documentId);
    }

    public void getNotebookById(String documentId) {
        JSONObject jsonObject = new JSONObject(database.getDocument(documentId).getProperties());
        System.out.println(jsonObject.toString());
    }

    /**
     * Delete a notebook by its id
     *
     * @param documentId id of the note
     * @return boolean to indicate whether deleted or not
     */
    public boolean deleteNotebook(String documentId) {

        boolean isDeleted = true;
        Document document = database.getDocument(documentId);

        if (document != null) {
            try {
                isDeleted = document.delete();
                Log.d(TAG, "Deleted document, deletion status = " + document.isDeleted());
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Cannot delete document", e);
            }
        } else {
            Log.e(TAG, "Cannot delete an unexisting document");
        }

        return isDeleted;
    }

    public List<Notebook> getAllNotebooks() {
        ArrayList<Notebook> ln = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        Note n = null;
        try {
            Query query = database.createAllDocumentsQuery();
            query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS);
            QueryEnumerator result = query.run();
            for (; result.hasNext(); ) {
                Document doc = result.next().getDocument();

                if (DOC_TYPE.equals(doc.getProperty("type"))) {
                    ln.add(mapper.readValue(new JSONObject(doc.getProperties()).toString(), Notebook.class));
                }
            }
        } catch (CouchbaseLiteException | IOException e) {
            e.printStackTrace();
        }
        return ln;
    }

    /**
     * Create or retrieve the view to find notebook
     * In keys, the notebook name
     * In values, the notebook object under notebook key
     *
     * @return the view
     */
    public View createView() {
        View notebookView = database.getView("notebookView");
        notebookView.setMap(
                new Mapper() {
                    @Override
                    public void map(Map<String, Object> document, Emitter emitter) {
                        Log.d(TAG, "Document retrieved in notebookView: " + document.toString());
                        if (document.get("type").equals("notebook")) {
                            List<Object> key = new ArrayList<Object>();
                            key.add(document.get("name"));
                            HashMap<String, Object> value = new HashMap<String, Object>();
                            value.put("notebook", document.toString());
                            emitter.emit(key, value);
                        }
                    }
                }, "4" /* The version number of the mapper... */
        );
        Log.d(TAG, "View Created for Notebook" + notebookView.toString());
        return notebookView;
    }

}
