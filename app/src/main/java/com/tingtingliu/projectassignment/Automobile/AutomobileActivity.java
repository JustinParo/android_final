package com.tingtingliu.projectassignment.Automobile;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.tingtingliu.projectassignment.R;

public class AutomobileActivity extends AppCompatActivity {

    FloatingActionButton newRecordFAB;
    FragmentManager fragmentManager = getFragmentManager();
    AutomobileNewRecordFragment newRecordFragment;
    AutomobileRecordsFragment automobileRecordsFragment;

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
            case R.id.records:
                Toast.makeText(AutomobileActivity.this, "Records", Toast.LENGTH_SHORT).show();
                removeFragment();
                automobileRecordsFragment = new AutomobileRecordsFragment();
                fragmentManager.beginTransaction().replace(R.id.automobile_frame, automobileRecordsFragment)
                        .addToBackStack(null).commit();
                break;
            case R.id.statistics:

                break;
        }
        return true;
    }
}
