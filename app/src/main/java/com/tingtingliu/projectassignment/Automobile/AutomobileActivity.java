package com.tingtingliu.projectassignment.Automobile;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tingtingliu.projectassignment.R;

public class AutomobileActivity extends AppCompatActivity {

    FloatingActionButton newRecordFAB;
    FragmentManager fragmentManager = getFragmentManager();
    AutomobileNewRecordFragment newRecordFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile);

        //Main Toolbar
        Toolbar AutomobileToolbar = (Toolbar)findViewById(R.id.automobile_toolbar);
        setSupportActionBar(AutomobileToolbar);
        AutomobileToolbar.setLogo(R.drawable.car);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //New Record Fragment Button
        newRecordFAB = (FloatingActionButton) findViewById(R.id.fabButton);
        newRecordFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFragment();
                newRecordFragment = new AutomobileNewRecordFragment();
                fragmentManager.beginTransaction().replace(R.id.automobile_frame,newRecordFragment)
                        .addToBackStack(null).commit();
                newRecordFAB.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void setButtonVisible() {
        newRecordFAB.setVisibility(View.VISIBLE);
    }

    public void removeFragment() {
        fragmentManager.popBackStack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.automobile_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home:
                RelativeLayout view = (RelativeLayout) findViewById(R.id.rootLayout);
                Snackbar.make(view, "Home", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.records:
                Toast.makeText(AutomobileActivity.this, "Records", Toast.LENGTH_SHORT).show();
                removeFragment();
                Intent intentRecords = new Intent(AutomobileActivity.this, AutomobileRecords.class);
                startActivity(intentRecords);
                break;
            case R.id.statistics:
                Toast.makeText(AutomobileActivity.this, "Statictics", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AutomobileActivity.this, AutomobileStatistics.class);
                startActivity(intent);
                break;
            case R.id.help:
                LayoutInflater inflater = getLayoutInflater();
                LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.automobile_help_menu, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(AutomobileActivity.this);
                builder.setView(rootView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).show();
                break;
        }
        return true;
    }
}
