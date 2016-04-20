package edu.dartmouth.cs.actiontabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xuehanyu on 4/19/16.
 */
public class DataBaseHelper extends SQLiteOpenHelper{
    private final static String DATABASE_NAME = "RUN";
    private final static int DATABASE_VERSION = 1;
    private String TABLE_NAME = "ManualEntry";

    public DataBaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create table Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text);
        String sql = "create table if not exists " + TABLE_NAME + " (ID integer primary key, Date text, Time text," +
                "Duration integer, Distance integer, Calories integer, HeartRate integer, Comment text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public void addItem(databaseItem item){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int ID = 0;
            Cursor cursor = db.rawQuery("select id from "+TABLE_NAME+" order by ID DESC", new String[]{});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                ID = cursor.getInt(0)+1;
            }
            String sqlAdd = "insert into table " + TABLE_NAME + " (ID, Date, Time, Duration, Calories, Distance, Calories, " +
                    "HeartRate, Comment values (" + ID + item.Date + item.Time + item.Duration + item.Distance + item.Calories +
                    item.HeartRate + item.Comment + ")";
            db.execSQL(sqlAdd);
        }catch (Exception exc){
            Log.d("addItem", exc.getMessage());
        }
    }

    public void deleteItem(int ID){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("delete from "+TABLE_NAME+" where ID=?", new String[]{ID+""});
        }catch (Exception exc){
            Log.d("deleteItem", exc.getMessage());
        }
    }

    /*
     * Get all items in the database
     * If UnitPreference equals 0, metric
     * If UnitPreference equals 1, imperial
     */
    public List<databaseItem> allItems(int UnitPreference){
        List<databaseItem> result = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " order by ID ASC", new String[]{});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    databaseItem thisItem = new databaseItem(cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4),
                            cursor.getInt(5), cursor.getInt(6), cursor.getString(7));
                    thisItem.ID = cursor.getInt(0);
                    result.add(thisItem);
                    cursor.moveToNext();
                }
            }
        }catch (Exception exc){
            Log.d("addItem", exc.getMessage());
        }
        return result;
    }
}
