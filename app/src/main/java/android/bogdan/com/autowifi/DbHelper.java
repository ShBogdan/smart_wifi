package android.bogdan.com.autowifi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private final String MY_LOG = "MyLog";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "wifiPoints.db";
    public static final String TABLE_NAME = "wifis";
    public static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CELLS = "cells";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_CELLS + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    public void addWiFi(WFItem wifi) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, wifi.getName());
        values.put(KEY_CELLS, String.valueOf(wifi.getCells()));

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public WFItem getWiFi(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_NAME, KEY_CELLS}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        WFItem wifi = new WFItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), (ArrayList<String>) Arrays.asList(cursor.getString(2).split(",")));

        return wifi;
    }

    public List<WFItem> getAllWiFis() {
        List<WFItem> wifiList = new ArrayList<WFItem>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Log.d(MY_LOG, cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2));
                WFItem wifi = new WFItem();
                wifi.setId(Integer.parseInt(cursor.getString(0)));
                wifi.setName(cursor.getString(1));
                ArrayList<String> temp = new ArrayList<>();
                temp.add("1111");
                temp.add("22222222");
                wifi.setCells(temp /*Arrays.asList(cursor.getString(2).split(",")*/);
                // Adding contact to list
                wifiList.add(wifi);
            } while (cursor.moveToNext());
        }

        return wifiList;
    }

    public int updateWiFi(WFItem wifi) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, wifi.getName());
        values.put(KEY_CELLS, String.valueOf(wifi.getCells()));

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(wifi.getId())});
    }

    public void deleteContact(WFItem wifi) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[]{String.valueOf(wifi.getId())});
        db.close();
    }

    public void deleteContactById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[]{id});
        db.close();
    }
}