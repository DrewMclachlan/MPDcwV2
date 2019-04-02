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
    private SQLiteDatabase db;
    static final String viewEmps="ViewEmps";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 33);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
       // db.execSQL("INSERT INTO earthquakes VALUES(null, 'drew', 'hfggfello', 'www.h1.com', '170878', 'UDF' , 23, 45)");
       // db.execSQL("INSERT INTO earthquakes VALUES(null,'drew1', 'hello1', 'www.h1.com', '170878', 'UDF' , 23, 45)");
    }


   // Database creation sql statement;
   private static final String DATABASE_CREATE = "CREATE TABLE earthquakes(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT, description TEXT, link TEXT, pubDate TEXT, category TEXT, lat INTEGER, long INTEGER, mag TEXT, depth TEXT);";


    public boolean insert (String name, String desc, String link, String pub, String cat, double lat, double glong, String mag, String depth) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
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


    public boolean drop(){
        SQLiteDatabase db = this.getWritableDatabase();
       db.execSQL("DELETE FROM earthquakes");
        Log.e("deleted", "deleted");
        return true;
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

    public String getTableAsString() {
        String tableString = String.format("Table earthquake:\n");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor allRows  = db.rawQuery("SELECT * FROM  earthquakes", null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }


        return tableString;
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
        return null;
    }


    public ArrayList<String> selectname() {
        ArrayList<String> quotes = new ArrayList<>();
        String query = "select * from  earthquakes order by name asc";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                // 0 = id, 1 = quote
                quotes.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        db.close();

        return quotes;
    }
}