package android.bogdan.com.autowifi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "wifiPoints";
    private static final String TABLE_WIFI = "wifis";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CELLS = "cells";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_WIFI + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_CELLS + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WIFI);
        // Create tables again
        onCreate(db);
    }

    public void addWiFi(WFItem wifi) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, wifi.getName());
        values.put(KEY_CELLS, String.valueOf(wifi.getCells()));

        // Inserting Row
        db.insert(TABLE_WIFI, null, values);
        db.close(); // Closing database connection
    }

    public WFItem getWiFi(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_WIFI, new String[] { KEY_ID,
                        KEY_NAME, KEY_CELLS }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        WFItem wifi = new WFItem(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                (ArrayList<String>) Arrays.asList(cursor.getString(2).split(",")));
        // return contact
        return wifi;
    }


    public List<WFItem> getAllContacts() {
        List<WFItem> wifiList = new ArrayList<WFItem>();

        String selectQuery = "SELECT  * FROM " + TABLE_WIFI;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WFItem wifi = new WFItem();
                wifi.setId(Integer.parseInt(cursor.getString(0)));
                wifi.setName(cursor.getString(1));
                wifi.setCells((ArrayList<String>) Arrays.asList(cursor.getString(2).split(",")));
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
        return db.update(TABLE_WIFI, values, KEY_ID + " = ?",
                new String[] { String.valueOf(wifi.getId()) });
    }

    public void deleteContact(WFItem wifi) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WIFI, KEY_ID + " = ?",
                new String[]{String.valueOf(wifi.getId())});
        db.close();
    }

}