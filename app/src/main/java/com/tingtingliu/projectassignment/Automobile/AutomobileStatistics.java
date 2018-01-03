package com.tingtingliu.projectassignment.Automobile;

import android.app.Activity;
import android.os.Bundle;

import com.tingtingliu.projectassignment.R;

import java.util.ArrayList;

public class AutomobileStatistics extends Activity {

    private AutomobileDatabaseAdapter aDatabaseAdapter;
    private ArrayList<AutomobileRecord> recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile_statistics);

        //Database Setup
        aDatabaseAdapter = new AutomobileDatabaseAdapter(getApplicationContext());
        aDatabaseAdapter.open();
        recordList = aDatabaseAdapter.fetchAllRecords();
    }
}
