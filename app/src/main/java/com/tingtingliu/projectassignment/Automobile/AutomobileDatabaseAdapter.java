package com.tingtingliu.projectassignment.Automobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by justinparo on 2017-12-29.
 */

public class AutomobileDatabaseAdapter extends SQLiteOpenHelper {

    //Database variables
    static String DATABASE_NAME = "Automobile";
    static int VERSION_NUM = 1;

    //Statistics table variables
    final static String TABLE_NAME = "STATISTICS";
    final static String KEY_ID = "ID";
    final static String KEY_LITERS = "LITERS";
    final static String KEY_PRICE = "PRICE";
    final static String KEY_KM = "KM";
    final static String KEY_DATE = "DATE";

    //Other Variables
    private AutomobileDatabaseAdapter aDatabaseAdapter;
    private SQLiteDatabase aDB;
    private Context ctx;


    public AutomobileDatabaseAdapter(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                        + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + KEY_LITERS + " INTEGER,"
                        + KEY_PRICE + " INTEGER,"
                        + KEY_KM + " INTEGER,"
                        + KEY_DATE + " DATETIME DEFAULT CURRENT_DATE" + ")";
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

    public AutomobileDatabaseAdapter open() throws SQLException {
        aDatabaseAdapter = new AutomobileDatabaseAdapter(ctx);
        aDB = aDatabaseAdapter.getWritableDatabase();
        return this;
    }

    public void close() {
        if (aDatabaseAdapter != null) {
            aDatabaseAdapter.close();
        }
    }

    public long createRecord(String liters, String price, String km) {
        Log.i("DATABASE", "Record Created");
        ContentValues cv = new ContentValues();
        cv.put(KEY_LITERS, liters);
        cv.put(KEY_PRICE, price);
        cv.put(KEY_KM, km);

        return aDB.insert(TABLE_NAME, null, cv);
    }
}
