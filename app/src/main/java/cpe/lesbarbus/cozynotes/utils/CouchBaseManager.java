package cpe.lesbarbus.cozynotes.utils;

import android.content.Context;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import java.io.IOException;

/**
 * Manager for couchbase lite
 */
public class CouchBaseManager {

    public static final String DB_NAME = "couchbasenotes";
    public static Database database;
    public static Manager manager;
    public static Context context;

    public static Database setDatabaseInstance(Context c) throws IOException, CouchbaseLiteException {
        context=c;
        manager = getManagerInstance();
        database = getDatabaseInstance();
        return database;
    }

    /***
     * Implements Singleton Pattern
     * @return An instance of a the note manager object
     * @throws IOException
     */
    private static Manager getManagerInstance() throws IOException {

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
    public static Database getDatabaseInstance() throws CouchbaseLiteException {
        if ((database == null) & (manager != null)) {
            database = manager.getDatabase(DB_NAME);
        }
        return database;
    }
    /*HOW TO QUERY VIEW IN COUCHDBLITE
    try {

            Database db = CouchBaseManager.getDatabaseInstance();
            Query qy = db.getView("noteView").createQuery();
            qy.setLimit(20);
            qy.setDescending(true);
            QueryEnumerator result = qy.run();
            for (; result.hasNext(); ) {

                QueryRow row = result.next();
                System.out.println("Row Key: "+row.getKey().toString());
                System.out.println("Row Value: "+row.getValue().toString());
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }*/


}
