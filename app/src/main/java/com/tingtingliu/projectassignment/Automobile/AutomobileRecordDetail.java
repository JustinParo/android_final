package com.tingtingliu.projectassignment.Automobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tingtingliu.projectassignment.R;

import java.util.ArrayList;

public class AutomobileRecordDetail extends Activity {

    private AutomobileDatabaseAdapter aDatabaseAdapter;
    private AutomobileRecord record;
    private Button back, delete;
    private TextView liters, price, km;
    private Bundle rDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile_record_detail);

        //Database Setup
        aDatabaseAdapter = new AutomobileDatabaseAdapter(getApplicationContext());
        aDatabaseAdapter.open();

        //Bundle Information
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            rDetails = bundle.getBundle("rDetails");
        }

        //Retrieve Single Record
        record = aDatabaseAdapter.fetchRecordByID(rDetails.getInt("id"));

        //Set Record Details
        liters = findViewById(R.id.litersRecordDetailInfo);
        liters.setText(record.getLiters().toString());

        price = findViewById(R.id.priceRecordDetailInfo);
        price.setText(record.getPrice().toString());

        km = findViewById(R.id.kmRecordDetailInfo);
        km.setText(record.getKm().toString());

        //Back Button
        back = (Button) findViewById(R.id.aBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AutomobileRecordDetail.this, AutomobileRecords.class);
                startActivity(intent);
            }
        });

        //Delete Button
        delete = (Button) findViewById(R.id.aDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DELETE", "Pressed");
                LayoutInflater inflater = getLayoutInflater();
                LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.automobile_custom_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(AutomobileRecordDetail.this);
                builder.setView(rootView)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                aDatabaseAdapter.deleteRecord((Integer) rDetails.get("id"));
                                Intent intent = new Intent(AutomobileRecordDetail.this, AutomobileRecords.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).show();
            }
        });
    }
}
