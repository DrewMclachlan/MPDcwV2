package gcu.mpd.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "earthquakes";
    static final String testtable = "TestTable";
    static final String colID = "Id";
    static final String colName="Name";
    static final String colAge="Age";
    static final String colDept="Dept";

    static final String deptTable="Dept";
    static final String colDeptID="DeptID";
    static final String colDeptName="DeptName";
    private SQLiteDatabase dbs;
    static final String viewEmps="ViewEmps";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 33);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }


   // Database creation sql statement;
   private static final String DATABASE_CREATE = "CREATE TABLE earthquakes(id INTEGER PRIMARY KEY NOT NULL, name TEXT, description TEXT, link TEXT, pubDate TEXT, category TEXT, lat INTEGER, long INTEGER, mag TEXT, depth TEXT);";


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
            Log.e("Drop", "notable");
            return true;
        }
    }



    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS earthquakes");
        onCreate(database);
    }

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
            Log.e("1","done");
            Log.e("2", e.toString());
            return e;
        }
        Log.e("3","3");
        return null;
    }
}