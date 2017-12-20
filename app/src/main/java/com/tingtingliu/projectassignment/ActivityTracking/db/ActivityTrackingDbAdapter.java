package com.tingtingliu.projectassignment.ActivityTracking.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ActivityTrackingDbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_DURATION = "ActivityDuration";
    public static final String KEY_ACTIVITY_TRACKING_TYPE = "type";
    public static final String KEY_COMMENT = "notes";
    public static final String KEY_DATE = "dateCreated";
    private static final String TAG = "ActivityDbAdapter";
    private static final String DATABASE_NAME = "ActivityTracking";
    private static final String SQLITE_TABLE = "ActivityTracker";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_DURATION + "," +
                    KEY_ACTIVITY_TRACKING_TYPE + "," +
                    KEY_COMMENT + "," +
                    KEY_DATE +
                    ");";
    private final Context mCtx;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public ActivityTrackingDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public ActivityTrackingDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createActivity(String duration,
                               String type, String comment, String date) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DURATION, duration);
        initialValues.put(KEY_ACTIVITY_TRACKING_TYPE, type);
        initialValues.put(KEY_COMMENT, comment);
        initialValues.put(KEY_DATE, date);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteActivity(long rowId) {
        return mDb.delete(SQLITE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateActivity(long rowId, String id, String duration,
                                  String type, String comment, String date) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DURATION, duration);
        initialValues.put(KEY_ACTIVITY_TRACKING_TYPE, type);
        initialValues.put(KEY_COMMENT, comment);
        initialValues.put(KEY_DATE, date);

        return mDb.update(SQLITE_TABLE, initialValues, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteAllActivities() {
        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }


    public Cursor fetchRecords() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[]{KEY_ROWID, KEY_DATE, KEY_DURATION + "|| ' Minutes - Type:' ||" + KEY_ACTIVITY_TRACKING_TYPE},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllRecords() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[]{KEY_ROWID, KEY_DURATION, KEY_ACTIVITY_TRACKING_TYPE, KEY_DATE, KEY_COMMENT},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomeActivities() {
//        createActivity("10", "Running",  "3.Each Activity must have a ListView to present items. Selecting an item from the ListView must show detailed information about the item selected.", "10/10/2017");
//        createActivity("20", "Walking",  "7.Each activity must have at least 1 button", "12/15/2017");
//        createActivity( "30", "Skating",  "8.Each activity must have at least 1 edit text with appropriate text input method.", "11/12/2017");
//        createActivity( "40", "Biking",  "8.Each activity must have at least 1 edit text with appropriate text input method.", "12/01/2017");
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
}
