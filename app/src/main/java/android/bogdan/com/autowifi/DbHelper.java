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

    public static final String TABLE_WIFIS = "wifis";
    public static final String KEY_WIFIS_ID = "id";
    public static final String KEY_NAME = "name";
//    private static final String KEY_CELLS = "cells";

    public static final String TABLE_CELLS = "cells";
    public static final String KEY_CELLS_ID = "id";
    public static final String KEY_CELLS_FOREIGN_ID = "fkId";
    public static final String KEY_CELLS_CEll = "cell";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WIFIS_TABLE = "CREATE TABLE " + TABLE_WIFIS + "("
                + KEY_WIFIS_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT" + ")";

        String CREATE_CELLS_TABLE = "CREATE TABLE " + TABLE_CELLS + "("
                + KEY_CELLS_ID + " INTEGER PRIMARY KEY,"
                + KEY_CELLS_FOREIGN_ID + " INTEGER,"
                + KEY_CELLS_CEll + " TEXT" + ")";


        db.execSQL(CREATE_CELLS_TABLE);
        db.execSQL(CREATE_WIFIS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WIFIS);
        // Create tables again
        onCreate(db);
    }

    public void addWiFi(WFItem wifi) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        ContentValues values;
        //есть ли эелемент в базе?
        String query = "SELECT " + KEY_WIFIS_ID +", "
                + KEY_NAME + " FROM "
                + TABLE_WIFIS + " WHERE "
                + KEY_NAME + " = " + "\""+ wifi.getName() + "\"";
        cursor = db.rawQuery(query, null);
        Log.d(MY_LOG, cursor.getCount()+" количество одинаковых елементов");
        if(cursor.moveToLast()){
            Log.d(MY_LOG, cursor.getInt(0) + " "+ cursor.getString(1) + " сеть уже добавлена");;
        }else {
            //создаем в таблице сеть
            values = new ContentValues();
            values.put(KEY_NAME, wifi.getName());
            db.insert(TABLE_WIFIS, null, values);
            //создаем елемент по внешнему ключу в таблице cells
            cursor = db.rawQuery(query, null);
            cursor.moveToLast();
            values = new ContentValues();
            values.put(KEY_CELLS_FOREIGN_ID, cursor.getInt(0));
            values.put(KEY_CELLS_CEll, wifi.getCells().get(0)); //в толькочто созданной сети будет только один элемент cells
            wifi.setId(cursor.getInt(0));
            db.insert(TABLE_CELLS, null, values);
            Log.d(MY_LOG, "Создана сеть: " + wifi.getName() +" Индекс" + cursor.getInt(0) + " Ячейка:" + wifi.getCells().get(0));
        }
        db.close(); // Closing database connection
    }

    public WFItem getWiFi(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WIFIS, new String[]{KEY_WIFIS_ID, KEY_NAME}, KEY_WIFIS_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        WFItem wifi = new WFItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), (ArrayList<String>) Arrays.asList(cursor.getString(2).split(",")));

        return wifi;
    }

    public List<WFItem> getAllWiFis() {
        List<WFItem> wifiList = new ArrayList<WFItem>();

        String selectQuery = "SELECT  * FROM " + TABLE_WIFIS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor wifi_cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (wifi_cursor.moveToFirst()) {
            do {
                Log.d(MY_LOG, wifi_cursor.getInt(0) + " " + wifi_cursor.getString(1));
                WFItem wifi = new WFItem();
                wifi.setId(wifi_cursor.getInt(0));
                wifi.setName(wifi_cursor.getString(1));

                //wifi.setCells
                ArrayList<String> listCells = new ArrayList<>();
                String query = "SELECT " + KEY_CELLS_CEll +" FROM " + TABLE_CELLS + " WHERE " + KEY_CELLS_FOREIGN_ID + " = " + wifi_cursor.getInt(0);
//                SQLiteDatabase db1 = this.getWritableDatabase();
                Cursor cell_cursor = db.rawQuery(query, null);
                if(cell_cursor.moveToFirst()){
                    do{
                        listCells.add(cell_cursor.getString(0));
                        Log.d(MY_LOG, "В " + wifi.getName() + " add " + cell_cursor.getString(0));
                        Log.d(MY_LOG, "'Элементов курсора " + cell_cursor.getCount());
                    }while (cell_cursor.moveToNext());
                }
                wifi.setCells(listCells);
                //

                wifiList.add(wifi);
            } while (wifi_cursor.moveToNext());
        }

        return wifiList;
    }

//  если cell нет в загруженом списке добавляем ее в базу и обновляем список.
    public void updateWiFi(WFItem wifi) {
        ArrayList<String> cells = wifi.getCells();
        Integer id = wifi.getId();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO " + TABLE_CELLS + "(" + KEY_CELLS_ID +", " + KEY_CELLS_CEll + ")" + " VALUES " + "("+ id + ", " + cells.get(0) +");";
        db.rawQuery(query, null);
        db.close();
    }

//    возможно глючит
    public void deleteContact(WFItem wifi) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WIFIS, KEY_WIFIS_ID + " = ?",
                new String[]{String.valueOf(wifi.getId())});
        db.delete(TABLE_CELLS, KEY_CELLS_FOREIGN_ID + " = ?",
                new String[]{String.valueOf(wifi.getId())});
        db.close();
    }
}