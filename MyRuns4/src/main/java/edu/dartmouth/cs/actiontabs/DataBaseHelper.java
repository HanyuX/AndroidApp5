package edu.dartmouth.cs.actiontabs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
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
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
     * called when the activity is created
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create table Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text);
        String sql = "create table if not exists " + TABLE_NAME + " (ID text primary key,Date text,Time text,Duration real," +
                "Distance real, Calories integer, HeartRate integer, Comment text, InputType text, ActivityType text," +
                "Climb real, AvgSpeed real, CurSpeed real, Positions BLOB)";
        sqLiteDatabase.execSQL(sql);
    }

    /*
     * called when needs to upgrade
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    /*
     * add an item into the database
     */
    public void addItem(databaseItem item){
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//            String sqlAdd = "insert into " + TABLE_NAME + " (ID, Date, Time, Duration, Distance, Calories, " +
//                    "HeartRate, Comment, InputType, ActivityType, Climb, AvgSpeed, CurSpeed, Positions) values ('" +
//                    (item.ID.equals("") ? "-1" : item.ID) +"','"+
//                    (item.Date.equals("") ? "0" : item.Date) +"','"+
//                    (item.Time.equals("") ? "0" : item.Time) +"',"+
//                    (item.Duration < 0 ? 0 : item.Duration)  +","+
//                    (item.Distance < 0 ? 0 : item.Distance)  +","+
//                    (item.Calories < 0 ? 0 : item.Calories)  +","+
//                    (item.HeartRate < 0 ? 0 : item.HeartRate)+",'"+
//                    (item.Comment.equals("") ? "0" : item.Comment) + "','" +
//                    (item.InputType.equals("") ? "0" : item.InputType) +"','"+
//                    (item.ActivityType.equals("") ? "0" : item.ActivityType) +"')";
//            db.execSQL(sqlAdd);
//        }catch (Exception exc){
//            Log.d("addItem", exc.getMessage());
//        }
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            String sql   =   "INSERT INTO " + TABLE_NAME + " (ID, Date, Time, Duration, Distance, Calories, " +
                    "HeartRate, Comment, InputType, ActivityType, Climb, AvgSpeed, CurSpeed, Positions)" +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            SQLiteStatement insertStmt = db.compileStatement(sql);
            insertStmt.clearBindings();
            insertStmt.bindString(1, item.ID.equals("") ? "-1" : item.ID);
            insertStmt.bindString(2, item.Date.equals("") ? "0" : item.Date);
            insertStmt.bindString(3, item.Time.equals("") ? "0" : item.Time);
            insertStmt.bindDouble(4, item.Duration < 0 ? 0 : item.Duration);
            insertStmt.bindDouble(5, item.Distance < 0 ? 0 : item.Distance);
            insertStmt.bindDouble(6, item.Calories < 0 ? 0 : item.Calories);
            insertStmt.bindDouble(7, item.HeartRate < 0 ? 0 : item.HeartRate);
            insertStmt.bindString(8, item.Comment.equals("") ? "0" : item.Comment);
            insertStmt.bindString(9, item.InputType.equals("") ? "0" : item.InputType);
            insertStmt.bindString(10, item.ActivityType.equals("") ? "0" : item.ActivityType);
            insertStmt.bindDouble(11, item.Climb < 0 ? 0 : item.Climb);
            insertStmt.bindDouble(12, item.AvgSpeed < 0 ? 0 : item.AvgSpeed);
            insertStmt.bindDouble(13, item.CurSpeed < 0 ? 0 : item.CurSpeed);
            insertStmt.bindBlob(14, getLocationByteArray(item.Latlngs));
            insertStmt.executeInsert();

            db.setTransactionSuccessful();
            db.endTransaction();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*
     * delete an item in the database
     */
    public void deleteItem(String ID){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("delete from "+TABLE_NAME+" where ID=?", new String[]{ID});
        }catch (Exception exc) {
            Log.d("deleteItem", exc.getMessage());
        }
    }

    /*
     * Get all items in the database
     */
    public List<databaseItem> allItems(){
        Log.d("database", "all");
        List<databaseItem> result = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " order by ID ASC", new String[]{});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    databaseItem thisItem = new databaseItem(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4),
                            cursor.getInt(5), cursor.getInt(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getDouble(10),
                            cursor.getDouble(11), cursor.getDouble(12), setLocationListFromByteArray(cursor.getBlob(13)));
                    result.add(thisItem);
                    cursor.moveToNext();
                }
            }
        }catch (Exception exc){
            Log.d("addItem", exc.getMessage());
        }
        return result;
    }

    // Convert Location ArrayList to byte array, to store in SQLite database
    public byte[] getLocationByteArray(List<LatLng> mLocationList) {
        int[] intArray = new int[mLocationList.size() * 2];

        for (int i = 0; i < mLocationList.size(); i++) {
            intArray[i * 2] = (int) (mLocationList.get(i).latitude * 1E6);
            intArray[(i * 2) + 1] = (int) (mLocationList.get(i).longitude * 1E6);
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(intArray.length
                * Integer.SIZE);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(intArray);

        return byteBuffer.array();
    }

    // Convert byte array to Location ArrayList
    public List<LatLng> setLocationListFromByteArray(byte[] bytePointArray) {
        List<LatLng> result = new ArrayList<>();
        if(bytePointArray.length == 0)
            return result;
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytePointArray);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();

        int[] intArray = new int[bytePointArray.length / Integer.SIZE];
        intBuffer.get(intArray);

        int locationNum = intArray.length / 2;

        for (int i = 0; i < locationNum; i++) {
            LatLng latLng = new LatLng((double) intArray[i * 2] / 1E6F,
                    (double) intArray[i * 2 + 1] / 1E6F);
            result.add(latLng);
        }
        return result;
    }
}