package com.tingtingliu.projectassignment.foodnutrition;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import android.support.v4.app.FragmentTransaction;

import android.support.v7.widget.Toolbar;
import com.tingtingliu.projectassignment.MainActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.tingtingliu.projectassignment.R;


public class FoodNutritionActivity extends AppCompatActivity implements FoodNutritionWelcomeFragment.OnFragmentWelcomeListener, FoodNutritionInformationAddFragment.OnAddFoodFragmentListener, FoodNutritionListViewFragment.OnListFoodFragmentListener, FoodNutritionListViewFragment.OnListViewFragmentCalcListener {
    private Snackbar snackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_nutrition_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher1);
        toolbar.setTitle("Project Assignment");
        toolbar.setSubtitle("Food Nutrition by Xiaoyu Wang");
        toolbar.inflateMenu(R.menu.food_nutrition_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_item1) {
                    Intent intent = new Intent(FoodNutritionActivity.this, MainActivity.class);
                    startActivity(intent);

                } else if (menuItemId == R.id.action_item2) {
                    helpOnlClick();
                }else if (menuItemId == R.id.action_item3) {
                    Intent intent = new Intent(FoodNutritionActivity.this, MainActivity.class);
                    startActivity(intent);

                } else if (menuItemId == R.id.action_item4) {
                    Toast.makeText(FoodNutritionActivity.this, R.string.item_04, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        //Final project demo POINT 2
        FoodNutritionWelcomeFragment welcome = new FoodNutritionWelcomeFragment(this);
        welcome.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_window, welcome).commit();

    }

    public void addFoodNutritionInformation(){
        FoodNutritionInformationAddFragment addInfo = new FoodNutritionInformationAddFragment(this);
        addInfo.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_window, addInfo).commit();

    }


    public void returnToAdd(){
        FoodNutritionInformationAddFragment addInfo = new FoodNutritionInformationAddFragment(this);
        addInfo.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_window, addInfo).commit();
        Toast toast = Toast.makeText(this, "return to add page good", Toast.LENGTH_LONG);
        toast.show();
    }



    @Override
    public void onFragmentStart() {
        FoodNutritionListViewFragment listInfo = new FoodNutritionListViewFragment(this);
        listInfo.setArguments(getIntent().getExtras());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Add the fragment to the 'fragment_container' FrameLayout
        transaction.replace(R.id.fragment_window, listInfo);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }


    @Override
    public void onFragmentInteraction() {
        FoodNutritionListViewFragment listInfo = new FoodNutritionListViewFragment(this);
        listInfo.setArguments(getIntent().getExtras());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Add the fragment to the 'fragment_container' FrameLayout
        transaction.replace(R.id.fragment_window, listInfo);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();

    }

    @Override
    public void onItemClickFragmentInteraction(long id) {
        FoodNutritionDeleteFragment newFragment = new FoodNutritionDeleteFragment(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_window, newFragment);
        transaction.addToBackStack(null);
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        newFragment.setArguments(bundle);
        // Commit the transaction
        transaction.commit();

    }

    @Override
    public void onCalc() {
        FoodNutritionCalcAvgCaloryFragment newFragment = new FoodNutritionCalcAvgCaloryFragment(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_window, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void helpOnlClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alertDialog_food_nutri_help);
        builder.setMessage(R.string.alertDialog_food_nutri_help_message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
