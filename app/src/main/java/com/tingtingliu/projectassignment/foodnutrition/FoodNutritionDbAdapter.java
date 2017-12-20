package com.tingtingliu.projectassignment.foodnutrition;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashSet;

/**
 * Created by Algonquin on 2017-12-17.
 */

public class FoodNutritionDbAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String FOOD = "food";
    public static final String TOTAL_CARBON = "total_carbon";
    public static final String CALORIE = "calorie";
    public static final String TOTAL_FAT = "total_fat";
    public static final String DATE = "date";

    private static final String TAG = "FoodNutritionDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "FoodNutrition";
    private static final String SQLITE_TABLE = "Calorie";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    FOOD + "," +
                    TOTAL_CARBON + "," +
                    CALORIE + "," +
                    TOTAL_FAT + "," +
                    DATE +")";

    public void deleteRecordById(long id) {

        mDb.delete(SQLITE_TABLE, KEY_ROWID + "=" + id , null);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public FoodNutritionDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public FoodNutritionDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createRecord(String food, String total_carbon,
                           String calorie, String total_fat, String date) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(FOOD, food);
        initialValues.put(TOTAL_CARBON, total_carbon);
        initialValues.put(CALORIE, calorie);
        initialValues.put(TOTAL_FAT, total_fat);
        initialValues.put(DATE, date);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public Cursor fetchRecordById(long id){
        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        FOOD, TOTAL_CARBON, CALORIE,TOTAL_FAT, DATE},
                KEY_ROWID + "=" + id ,null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }



    public Cursor fetchAllRecords() {
        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        FOOD, TOTAL_CARBON, CALORIE,TOTAL_FAT, DATE},
                null, null, null, null, DATE);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public double getAverageCaloriesPerDay(){

        Cursor cursor = fetchAllRecords();
        double total = 0;
        HashSet<String> set = new HashSet<>();
        while(!cursor.isAfterLast()) {
            total += Double.parseDouble(cursor.getString(cursor.getColumnIndex(CALORIE)));
            set.add(cursor.getString(cursor.getColumnIndex(DATE)));
            cursor.moveToNext();
        }

        if(set.isEmpty()){
            return 0;
        }
        return total/(set.size());
    }

    public double getAverageCaloriesLastDay(){
        double total = 0;
        Cursor cursor = fetchAllRecords();
        String last = null;

        while(!cursor.isAfterLast()) {
            if(cursor.isLast()){
                last = cursor.getString(cursor.getColumnIndex(DATE));
            }
            cursor.moveToNext();
        }

        if(last != null){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                if(last.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(DATE)))){
                    total += Double.parseDouble(cursor.getString(cursor.getColumnIndex(CALORIE)));

                }
                cursor.moveToNext();
            }
        }

        return total;
    }



}
