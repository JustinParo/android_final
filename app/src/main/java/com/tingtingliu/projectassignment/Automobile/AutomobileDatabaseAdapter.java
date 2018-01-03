package com.tingtingliu.projectassignment.Automobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by justinparo on 2017-12-29.
 */

public class AutomobileDatabaseAdapter {

    //Database variables
    static String DATABASE_NAME = "Automobile";
    static int VERSION_NUM = 2;

    //Statistics table variables
    final static String TABLE_NAME = "STATISTICS";
    final static String KEY_ID = "ID";
    final static String KEY_LITERS = "LITERS";
    final static String KEY_PRICE = "PRICE";
    final static String KEY_KM = "KM";
    final static String KEY_DATE = "DATE";

    //Other Variables
    private AutomobileDatabaseHelper aDatabaseAdapter;
    private SQLiteDatabase aDB;
    private Context ctx;

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public AutomobileDatabaseAdapter(Context context) {this.ctx = context;}

    public AutomobileDatabaseAdapter open() throws SQLException {
        aDatabaseAdapter = new AutomobileDatabaseHelper(ctx);
        aDB = aDatabaseAdapter.getWritableDatabase();
        return this;
    }

    public void close() {
        if (aDatabaseAdapter != null) {
            aDatabaseAdapter.close();
        }
    }

    public void createRecord(String liters, String price, String km) {
        Log.i("RECORD CREATED", "Liters: "+liters+" Price: "+price+" km: "+km);
        ContentValues cv = new ContentValues();
        cv.put(KEY_LITERS, liters);
        cv.put(KEY_PRICE, price);
        cv.put(KEY_KM, km);
        cv.put(KEY_DATE,getDateTime());
        aDB.insert(TABLE_NAME, null, cv);
    }

    public AutomobileRecord fetchRecordByID(Integer id) {
        Log.i("ID", id.toString());
        Cursor cursor = aDB.rawQuery("select * from "+TABLE_NAME+" where "
        +KEY_ID+ " = "+ id,null);

        if (!cursor.moveToFirst()) {
            cursor.moveToFirst();
        }

        AutomobileRecord record = new AutomobileRecord();
        record.setId((cursor.getInt(0)));
        record.setLiters((cursor.getInt(1)));
        record.setPrice((cursor.getInt(2)));
        record.setKm((cursor.getInt(3)));
        record.setDate(cursor.getInt(4));
        return record;
    }

    public void deleteRecord(Integer id) {
        aDB.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] {id.toString()});
        close();
    }

    public ArrayList<AutomobileRecord> fetchAllRecords() {
        ArrayList<AutomobileRecord> recordList = new ArrayList<AutomobileRecord>();
        Cursor cursor = aDB.rawQuery("select * from " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                AutomobileRecord record = new AutomobileRecord();
                record.setId((cursor.getInt(0)));
                record.setLiters((cursor.getInt(1)));
                record.setPrice((cursor.getInt(2)));
                record.setKm((cursor.getInt(3)));
                record.setDate(cursor.getInt(4));
                recordList.add(record);
            } while (cursor.moveToNext());
        }
        return recordList;
    }

    public Integer[] lastMonthTotals() {
        Integer[] totals = new Integer[2];
        ArrayList<AutomobileRecord> records = fetchAllRecords();

        Integer literCounter = 0;
        Integer literTotal = 0;
        for (AutomobileRecord ar : records) {
            literCounter++;
            literTotal += ar.getLiters();
        }
        return totals;
    }

    private static class AutomobileDatabaseHelper extends SQLiteOpenHelper {
        public AutomobileDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION_NUM);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            final String CREATE_TABLE =
                    "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + KEY_LITERS + " INTEGER,"
                            + KEY_PRICE + " INTEGER,"
                            + KEY_KM + " INTEGER,"
                            + KEY_DATE + " INTEGER" + ")";
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
            Log.i("DATABASE", "Calling onUpgrade, oldVersion = " + oldVer + " newVersion = " + newVer);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }

        @Override
        public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
            Log.i("DATABASE", "Calling onUpgrade, oldVersion = " + oldVer + " newVersion = " + newVer);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }
}
