package android.bogdan.com.autowifi;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

public class AutoWiFiProvider extends ContentProvider {

    private static final String PROVIDER_NAME = "com.bogdan.autowifi.AutoWiFiProvider";
    private static final String WIFI_WIFIS_PATH = "wifis";
    private static final String WIFI_CELLS_PATH = "cells";
    private static final Uri URI_WIFI = Uri.parse("content://" + PROVIDER_NAME + WIFI_WIFIS_PATH);
    private static final Uri URI_CELLS = Uri.parse("content://" + PROVIDER_NAME + WIFI_CELLS_PATH);
    private static final int WIFIS = 1;
    private static final int WIFI_ID = 2;
    private static final int CELLS = 3;
    private static final int CELLS_FK = 4;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(PROVIDER_NAME, WIFI_WIFIS_PATH, WIFIS);
        uriMatcher.addURI(PROVIDER_NAME, WIFI_WIFIS_PATH + "/#", WIFI_ID);
        uriMatcher.addURI(PROVIDER_NAME, WIFI_CELLS_PATH, CELLS);
        uriMatcher.addURI(PROVIDER_NAME, WIFI_CELLS_PATH + "/#", CELLS_FK);
    }

    private DbHelper wifiDataBase = null;
    private SQLiteDatabase db = null;

    @Override
    public boolean onCreate() {

        Context context = getContext();
        wifiDataBase = new DbHelper(context);
        db = wifiDataBase.getWritableDatabase();
        return true;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case WIFIS:
                return "vnd.android.cursor.dir/vnd.android.bogdan.com.autowifi.wifi";
            case WIFI_ID:
                return "vnd.android.cursor.item/vnd.android.bogdan.com.autowifi.wifi";
        }
        return "";
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case WIFI_ID:
                qb.setTables(wifiDataBase.TABLE_WIFIS);
                String id = uri.getPathSegments().get(1); //0 название таблицы, 1 то что идет после #
                qb.appendWhere(wifiDataBase.KEY_WIFIS_ID + "=" + id);
                break;

            case CELLS_FK:
                qb.setTables(wifiDataBase.TABLE_CELLS);
                qb.appendWhere(wifiDataBase.KEY_CELLS_FOREIGN_ID + "=" + uri.getPathSegments().get(1));
                break;

            case WIFIS:
                qb.setTables(wifiDataBase.TABLE_WIFIS);
                break;

            case CELLS:
                qb.setTables(wifiDataBase.TABLE_CELLS);
                break;

            default:
                throw new IllegalArgumentException("URI не найден!" + uri);
        }



        SQLiteDatabase dbr = wifiDataBase.getReadableDatabase();
        Cursor cursor = qb.query(dbr, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        long rowId = 0;

        if (!(uriMatcher.match(uri) != WIFIS || uriMatcher.match(uri) != CELLS)) {
            throw new IllegalArgumentException("URI не найден! " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }


        if (uriMatcher.match(uri) == WIFIS){
            rowId = db.insert(wifiDataBase.TABLE_WIFIS, null, values);
        }

        if (uriMatcher.match(uri) == CELLS){
            rowId = db.insert(wifiDataBase.TABLE_CELLS, null, values);
        }

        if (rowId > 0) {
            Uri resultUri = ContentUris.withAppendedId(URI_CELLS, rowId);
            getContext().getContentResolver().notifyChange(resultUri, null);
            return resultUri;
        }

        throw new SQLException("Ошибка вставки строки: " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {

        int count;
        switch (uriMatcher.match(uri)) {

            case WIFIS:
                count = db.update(wifiDataBase.TABLE_WIFIS, values, where, whereArgs);
                break;

            case WIFI_ID:
                String id = uri.getPathSegments().get(1);
                String finalWhere = wifiDataBase.KEY_WIFIS_ID + " = " + id;

                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.update(wifiDataBase.TABLE_WIFIS, values, finalWhere, whereArgs);
                break;

            default:
                throw new IllegalArgumentException("URI не найден!" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {

        int count;
        switch (uriMatcher.match(uri)) {
            case WIFIS:
                count = db.delete(wifiDataBase.TABLE_WIFIS, where, whereArgs);
                break;
            case WIFI_ID:
                String id = uri.getPathSegments().get(1);
                String finalWhere = wifiDataBase.KEY_WIFIS_ID + "=" + id;
                //Добавляем условие WHERE если оно задано
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.delete(wifiDataBase.TABLE_WIFIS, finalWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("URI не найден!" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
