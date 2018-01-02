package com.tingtingliu.projectassignment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tingtingliu.projectassignment.ActivityTracking.ActivityTrackingDetailFragment;
import com.tingtingliu.projectassignment.ActivityTracking.ActivityTrackingListFragment;
import com.tingtingliu.projectassignment.Automobile.AutomobileActivity;
import com.tingtingliu.projectassignment.foodnutrition.FoodNutritionActivity;

public class MainActivity extends AppCompatActivity implements ActivityTrackingListFragment.OnHeadlineSelectedListener {
    Snackbar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ActivityTrackingListFragment activityTrackingListFragment = new ActivityTrackingListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, activityTrackingListFragment).addToBackStack(null).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher1);
        toolbar.setTitle(R.string.name);
        toolbar.inflateMenu(R.menu.menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int menuItemId = item.getItemId();
                final View coordinatorLayoutView = findViewById(R.id.snackbarPosition);
                if (menuItemId == R.id.action_item1) {
                    Toast.makeText(MainActivity.this, R.string.item_01, Toast.LENGTH_SHORT).show();
                    ActivityTrackingListFragment activityTrackingListFragment = new ActivityTrackingListFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, activityTrackingListFragment).addToBackStack(null).commit();
                    bar = Snackbar
                            .make(coordinatorLayoutView, R.string.item_01, Snackbar.LENGTH_LONG);
                    bar.show();
                } else if (menuItemId == R.id.action_item2) {
                    Intent intent = new Intent(MainActivity.this, FoodNutritionActivity.class);
                    startActivity(intent);

                } else if (menuItemId == R.id.action_item3) {
                    Toast.makeText(MainActivity.this, R.string.item_03, Toast.LENGTH_SHORT).show();

                } else if (menuItemId == R.id.action_item4) {
                    Toast.makeText(MainActivity.this, R.string.item_04, Toast.LENGTH_SHORT).show();
                    Intent AutomobileIntent = new Intent(MainActivity.this, AutomobileActivity.class);
                    startActivity(AutomobileIntent);
                }
                return true;
            }
        });
    }

    @Override
    public void onItemSelected(int position) {

        //activityTractingPosition = position;
        if (findViewById(R.id.detail_container) != null) {
            // we're in two-pane layout...
            ActivityTrackingDetailFragment detailFragment = new ActivityTrackingDetailFragment();
            Bundle args = new Bundle();
            args.putInt(ActivityTrackingDetailFragment.ARG_POSITION, position);
            detailFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.detail_container, detailFragment);
            Log.w("test main", "executed");
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            // we're in the one-pane layout and must swap frags...
            ActivityTrackingDetailFragment detailFragment = new ActivityTrackingDetailFragment();
            Bundle args = new Bundle();
            args.putInt(ActivityTrackingDetailFragment.ARG_POSITION, position);
            detailFragment.setArguments(args);
            Log.w("test oncreate", "new list fragement executed");
            FragmentTransaction transactiondetail = getSupportFragmentManager().beginTransaction();
            transactiondetail.replace(R.id.fragment_container, detailFragment);
            transactiondetail.addToBackStack(null);
            transactiondetail.commit();
        }
    }
}
