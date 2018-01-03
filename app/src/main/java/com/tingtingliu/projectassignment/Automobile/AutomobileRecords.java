package com.tingtingliu.projectassignment.Automobile;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.tingtingliu.projectassignment.R;
import java.util.ArrayList;

public class AutomobileRecords extends AppCompatActivity {

    private AutomobileDatabaseAdapter aDatabaseAdapter;
    private recordAdapter recordAdapter;
    private ArrayList<AutomobileRecord> recordList;
    private ListView recordView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile_records);

        //Main Toolbar
        Toolbar AutomobileToolbar = (Toolbar)findViewById(R.id.automobile_toolbar);
        setSupportActionBar(AutomobileToolbar);
        AutomobileToolbar.setLogo(R.drawable.car);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Progress Bar
        progressBar = findViewById(R.id.progressBar);

        //Async Task
        new loadRecords().execute();

        //RecordView Listener
        recordView = (ListView) findViewById(R.id.recordView);
        recordView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("id",position+1);
                Intent intent = new Intent(AutomobileRecords.this, AutomobileRecordDetail.class);
                intent.putExtra("rDetails", bundle);
                startActivity(intent);
            }
        });


    }

    private class loadRecords extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            //Initialize Database
            aDatabaseAdapter = new AutomobileDatabaseAdapter(getApplicationContext());
            publishProgress(33);

            //Open Database
            aDatabaseAdapter.open();
            publishProgress(66);

            //Retrieve Records
            recordList = aDatabaseAdapter.fetchAllRecords();
            publishProgress(100);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int x = values[0];
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(x);
        }

        @Override
        protected void onPostExecute(String result) {
            //RecordAdapter Setup
            recordAdapter = new recordAdapter(AutomobileRecords.this, 0, recordList);
            recordView.setAdapter(recordAdapter);
        }
    }


    private class recordAdapter extends ArrayAdapter<AutomobileRecord> {
        public recordAdapter(Context context, int resource, ArrayList<AutomobileRecord> recordList) {
            super(context, resource, recordList);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AutomobileRecord record = getItem(position);
            LayoutInflater inflater = AutomobileRecords.this.getLayoutInflater();
            View view = inflater.inflate(R.layout.automobile_record_layout, null);

            TextView liters = view.findViewById(R.id.litersRecordInfo);
            liters.setText(record.getLiters().toString());

            TextView price = view.findViewById(R.id.priceRecordInfo);
            price.setText(record.getPrice().toString());

            TextView km = view.findViewById(R.id.kmRecordInfo);
            km.setText(record.getKm().toString());

            TextView date = view.findViewById(R.id.dateRecordInfo);
            date.setText(record.getDate().toString());

            return view;
        }
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

                Toast.makeText(AutomobileRecords.this, "Home", Toast.LENGTH_SHORT).show();
                Intent intentHome = new Intent(AutomobileRecords.this, AutomobileActivity.class);
                startActivity(intentHome);

                break;
            case R.id.records:
                LinearLayout view = (LinearLayout) findViewById(R.id.rootLayout);
                Snackbar.make(view, "Records", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.statistics:

                break;
        }
        return true;
    }
}
