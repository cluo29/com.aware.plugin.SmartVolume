package com.aware.plugin.SmartVolume;

/**
 * Created by CLUO on 6/10/2015.
 */

import java.util.HashMap;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.util.Log;

import com.aware.Aware;
import com.aware.utils.DatabaseHelper;

public class Provider extends ContentProvider {
    public static final int DATABASE_VERSION = 1;
    public static String AUTHORITY = "com.aware.plugin.SmartVolume.provider.SmartVolume";
    private static final int SMARTVOLUME = 1;
    private static final int SMARTVOLUME_ID = 2;
    public static final String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/AWARE/SmartVolume.db";

    public static final String[] DATABASE_TABLES = {
            "plugin_SmartVolume"
    };
    public static final class ChuIO1_Data implements BaseColumns {
        private ChuIO1_Data(){};

        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/plugin_ChuIO1");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.aware.plugin.ChuIO1";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.aware.plugin.ChuIO1";

        public static final String _ID = "_id";
        public static final String TIMESTAMP = "timestamp";
        public static final String DEVICE_ID = "device_id";
        public static final String A_VALUES_0 = "double_A_values_0";   //x axis of accelerometer m/s^2
        public static final String A_VALUES_1 = "double_A_values_1";    //y
        public static final String A_VALUES_2 = "double_A_values_2";    //z
        public static final String L_VALUES_0 = "double_L_values_0";    //Light sensor
        public static final String M_VALUES_0 = "double_M_values_0";   //x axis of magnetometer uT
        public static final String M_VALUES_1 = "double_M_values_1";    //y
        public static final String M_VALUES_2 = "double_M_values_2";    //z
        public static final String GSMSTR_VALUES_0 = "double_GSMSTR_values_0";    //GSM signal strength
        public static final String GSMNSTR_VALUES_0 = "double_GSMNSTR_values_0";    //GSM Neighbors signal strength
        public static final String CDMASTR_VALUES_0 = "double_CDMASTR_values_0";    //CDMA RSSI
        public static final String WIFISTR_VALUES_0 = "double_WIFISTR_values_0";    //WIFI RSSI
        public static final String LA_VALUES_0 = "double_LA_values_0";   //x axis of linear accelerometer m/s^2
        public static final String LA_VALUES_1 = "double_LA_values_1";    //y
        public static final String LA_VALUES_2 = "double_LA_values_2";    //z
    }
    public static final String[] TABLES_FIELDS = {
            ChuIO1_Data._ID + " integer primary key autoincrement," +
                    ChuIO1_Data.TIMESTAMP + " real default 0," +
                    ChuIO1_Data.DEVICE_ID + " text default ''," +
                    ChuIO1_Data.A_VALUES_0 + " real default 0," +
                    ChuIO1_Data.A_VALUES_1 + " real default 0," +
                    ChuIO1_Data.A_VALUES_2 + " real default 0," +
                    ChuIO1_Data.L_VALUES_0 + " real default 0," +
                    ChuIO1_Data.M_VALUES_0 + " real default 0," +
                    ChuIO1_Data.M_VALUES_1 + " real default 0," +
                    ChuIO1_Data.M_VALUES_2 + " real default 0," +
                    ChuIO1_Data.GSMSTR_VALUES_0 + " real default 0," +
                    ChuIO1_Data.GSMNSTR_VALUES_0 + " real default 0," +
                    ChuIO1_Data.CDMASTR_VALUES_0 + " real default 0," +
                    ChuIO1_Data.WIFISTR_VALUES_0 + " real default 0," +
                    ChuIO1_Data.LA_VALUES_0 + " real default 0," +
                    ChuIO1_Data.LA_VALUES_1 + " real default 0," +
                    ChuIO1_Data.LA_VALUES_2 + " real default 0," +
                    "UNIQUE("+ ChuIO1_Data.TIMESTAMP+","+ ChuIO1_Data.DEVICE_ID+")"
    };
    private static UriMatcher URIMatcher;
    private static HashMap<String, String> databaseMap;
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        AUTHORITY = getContext().getPackageName() + ".provider.ChuIO1";

        URIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        URIMatcher.addURI(AUTHORITY, DATABASE_TABLES[0], SMARTVOLUME);
        URIMatcher.addURI(AUTHORITY, DATABASE_TABLES[0]+"/#", SMARTVOLUME_ID);
        databaseMap = new HashMap<String, String>();
        databaseMap.put(ChuIO1_Data._ID, ChuIO1_Data._ID);
        databaseMap.put(ChuIO1_Data.TIMESTAMP, ChuIO1_Data.TIMESTAMP);
        databaseMap.put(ChuIO1_Data.DEVICE_ID, ChuIO1_Data.DEVICE_ID);
        databaseMap.put(ChuIO1_Data.A_VALUES_0, ChuIO1_Data.A_VALUES_0);
        databaseMap.put(ChuIO1_Data.A_VALUES_1, ChuIO1_Data.A_VALUES_1);
        databaseMap.put(ChuIO1_Data.A_VALUES_2, ChuIO1_Data.A_VALUES_2);
        databaseMap.put(ChuIO1_Data.L_VALUES_0, ChuIO1_Data.L_VALUES_0);
        databaseMap.put(ChuIO1_Data.M_VALUES_0, ChuIO1_Data.M_VALUES_0);
        databaseMap.put(ChuIO1_Data.M_VALUES_1, ChuIO1_Data.M_VALUES_1);
        databaseMap.put(ChuIO1_Data.M_VALUES_2, ChuIO1_Data.M_VALUES_2);
        databaseMap.put(ChuIO1_Data.GSMSTR_VALUES_0, ChuIO1_Data.GSMSTR_VALUES_0);
        databaseMap.put(ChuIO1_Data.GSMNSTR_VALUES_0, ChuIO1_Data.GSMNSTR_VALUES_0);
        databaseMap.put(ChuIO1_Data.CDMASTR_VALUES_0, ChuIO1_Data.CDMASTR_VALUES_0);
        databaseMap.put(ChuIO1_Data.WIFISTR_VALUES_0, ChuIO1_Data.WIFISTR_VALUES_0);
        databaseMap.put(ChuIO1_Data.LA_VALUES_0, ChuIO1_Data.LA_VALUES_0);
        databaseMap.put(ChuIO1_Data.LA_VALUES_1, ChuIO1_Data.LA_VALUES_1);
        databaseMap.put(ChuIO1_Data.LA_VALUES_2, ChuIO1_Data.LA_VALUES_2);
        return true;
    }

    private boolean initializeDB() {

        if (databaseHelper == null) {

            databaseHelper = new DatabaseHelper( getContext(), DATABASE_NAME, null, DATABASE_VERSION, DATABASE_TABLES, TABLES_FIELDS );

        }
        if( databaseHelper != null && ( database == null || ! database.isOpen() )) {
            database = databaseHelper.getWritableDatabase();

        }
        return( database != null && databaseHelper != null);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return 0;
        }

        int count = 0;
        switch (URIMatcher.match(uri)) {
            case SMARTVOLUME:
                count = database.delete(DATABASE_TABLES[0], selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (URIMatcher.match(uri)) {
            case SMARTVOLUME:
                return ChuIO1_Data.CONTENT_TYPE;
            case SMARTVOLUME_ID:
                return ChuIO1_Data.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }
    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (!initializeDB()) {
            Log.w(AUTHORITY, "Database unavailable...");
            return null;
        }

        ContentValues values = (initialValues != null) ? new ContentValues(
                initialValues) : new ContentValues();

        switch (URIMatcher.match(uri)) {
            case SMARTVOLUME:
                long weather_id = database.insert(DATABASE_TABLES[0], ChuIO1_Data.DEVICE_ID, values);

                if (weather_id > 0) {
                    Uri new_uri = ContentUris.withAppendedId(
                            ChuIO1_Data.CONTENT_URI,
                            weather_id);
                    getContext().getContentResolver().notifyChange(new_uri,
                            null);
                    return new_uri;
                }
                throw new SQLException("Failed to insert row into " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return null;
        }

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (URIMatcher.match(uri)) {
            case SMARTVOLUME:
                qb.setTables(DATABASE_TABLES[0]);
                qb.setProjectionMap(databaseMap);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        try {
            Cursor c = qb.query(database, projection, selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        } catch (IllegalStateException e) {
            if (Aware.DEBUG)
                Log.e(Aware.TAG, e.getMessage());
            return null;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return 0;
        }

        int count = 0;
        switch (URIMatcher.match(uri)) {
            case SMARTVOLUME:
                count = database.update(DATABASE_TABLES[0], values, selection,
                        selectionArgs);
                break;
            default:

                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
