package gcu.mpd.myapp;

/**
 * @Author
 * Name: Drew Mclachlan
 * Student ID: S1511481
 * Programme of Study: Computing
*/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;



/**
 * Database Helper Class, that preforms operations for insertion and retrieval from the SQLlite database
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "earthquakes";


    /**
     * database helper constructor
     * @param context context of the current state
     */
    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 33);
    }

    /**
     * when object initialised creates a new earthquake table with the relevant fields
     * @param db database object
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    private static final String DATABASE_CREATE = "CREATE TABLE earthquakes(id INTEGER PRIMARY KEY NOT NULL, name TEXT, description TEXT, link TEXT, pubDate TEXT, category TEXT, lat INTEGER, long INTEGER, mag TEXT, depth TEXT);";

    /**
     * used to insert a new column into the database
     * @param id id
     * @param name title
     * @param desc description
     * @param link link
     * @param pub publication date
     * @param cat category
     * @param lat latitude
     * @param glong longitude
     * @param mag magnitude
     * @param depth depth
     * @return true if insertion successful
     */
    public boolean insert (Integer id, String name, String desc, String link, String pub, String cat, double lat, double glong, String mag, String depth) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("description", desc);
        contentValues.put("link", link);
        contentValues.put("pubDate", pub);
        contentValues.put("category", cat);
        contentValues.put("lat", lat);
        contentValues.put("long", glong);
        contentValues.put("mag", mag);
       contentValues.put("depth", depth);
        db.insert("earthquakes", null, contentValues);
        return true;
    }

    /**
     * deletes the current table of the database.
     * used to ensure tables do not persist of sessions
     * @return true if deletion successful
     */
    public boolean drop() {
        SQLiteDatabase db = this.getReadableDatabase();
        String count = "SELECT count(*) FROM earthquakes";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0) {
            db.delete("earthquakes", null, null);
            Log.e("Drop", "Deleted");
            return true;
        } else {
            Log.e("Drop", "not applicable");
            return true;
        }
    }


    /**
     * generated database helper method to upgrade the sqllight version if required
     * @param database database object
     * @param oldVersion old version of sqlite
     * @param newVersion new version of sqlite
     */
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS earthquakes");
        onCreate(database);
    }

    /**
     * iterates through each column of the database adding all the attributes to an earthquake object and adding the
     * object into an arralylist for all column in the database
     * @return arrayList of earthquake objects
     */
    public ArrayList<earthquake> returnall(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<earthquake> ea = new ArrayList<>();
        Cursor allRows  = db.rawQuery("SELECT * FROM  earthquakes", null);
        if(allRows.moveToFirst()){
            do {
                earthquake e = new earthquake();
                e.setTitle(allRows.getString(1));
                e.setDescription(allRows.getString(2));
                e.setLink(allRows.getString(3));
                e.setPubDate(allRows.getString(4));
                e.setCategory(allRows.getString(5));
                e.setgLat(allRows.getString(6));
                e.setgLong(allRows.getString(7));
               e.setMag(allRows.getString(8));
                e.setDepth(allRows.getString(9));
                ea.add(e);
            }while(allRows.moveToNext());
        return ea;
        }
        return null;
    }

    /**
     * takes an id argument and searched the database for a certain earthquake, adds it into an earthquake object and returns it
     * @param id id of object being located
     * @return earthquake object if found
     */
    public earthquake getObjById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor allRows  = db.rawQuery("SELECT * FROM  earthquakes WHERE id =" + id , null);
        if(allRows.moveToFirst()){
            earthquake e = new earthquake();
            e.setTitle(allRows.getString(1));
            e.setDescription(allRows.getString(2));
            e.setLink(allRows.getString(3));
            e.setPubDate(allRows.getString(4));
            e.setCategory(allRows.getString(5));
            e.setgLat(allRows.getString(6));
            e.setgLong(allRows.getString(7));
            Log.e("idobject", e.toString());
            return e;
        }
        return null;
    }
}