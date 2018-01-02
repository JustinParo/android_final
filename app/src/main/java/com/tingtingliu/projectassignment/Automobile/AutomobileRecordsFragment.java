package com.tingtingliu.projectassignment.Automobile;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.INotificationSideChannel;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tingtingliu.projectassignment.R;

import org.w3c.dom.Text;

import java.sql.Date;
import java.util.ArrayList;

public class AutomobileRecordsFragment extends Fragment {

    private AutomobileDatabaseAdapter aDatabaseAdapter;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ArrayList<AutomobileRecord> list;
    private TextView liters, price, km;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aDatabaseAdapter = new AutomobileDatabaseAdapter(this.getActivity());
        aDatabaseAdapter.open();
        db = aDatabaseAdapter.getReadableDatabase();


        //add saved records to list
        cursor = db.rawQuery("select * from " + AutomobileDatabaseAdapter.TABLE_NAME, null);

        //add saved messages to list
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //create AutomobileRecord
            Integer id = cursor.getColumnIndex(AutomobileDatabaseAdapter.KEY_ID);
            Integer liters = cursor.getColumnIndex(AutomobileDatabaseAdapter.KEY_LITERS);
            Integer price = cursor.getColumnIndex(AutomobileDatabaseAdapter.KEY_PRICE);
            Integer km = cursor.getColumnIndex(AutomobileDatabaseAdapter.KEY_KM);
            Integer date = cursor.getColumnIndex(AutomobileDatabaseAdapter.KEY_DATE);
            AutomobileRecord record = new AutomobileRecord(id, liters, price, km, date);

            //Add record to list;
            list.add(record);
            cursor.moveToNext();
        }
    }

    private class recordAdapter extends ArrayAdapter<String>{
        public recordAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {return list.size();}
        public Integer getLiters() {return this.getLiters();}
        public Integer getPrice() {return this.getPrice();}
        public Integer getKm() {return this.getKm();}

        @RequiresApi(api = Build.VERSION_CODES.O)
        public View getView(int position, View convertView, ViewGroup parent) {

            liters = (TextView) getActivity().findViewById(R.id.litersText);
            liters.setText(getLiters());

            price = (TextView) getActivity().findViewById(R.id.priceText);
            price.setText(getPrice());

            km = (TextView) getActivity().findViewById(R.id.kmText);
            km.setText(getKm());

            return AutomobileRecordsFragment.this.getLayoutInflater()
                    .inflate(R.layout.automobile_record_layout, null);
        }
    }
}
