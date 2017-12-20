package com.tingtingliu.projectassignment.foodnutrition;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tingtingliu.projectassignment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodNutritionDeleteFragment extends Fragment {
    private Button delete;
    private FoodNutritionActivity foodNutrition;

    private long _id;
    private TextView food;
    private TextView total_carbon;
    private TextView total_fat;
    private TextView calorie;
    private TextView date;
    private FoodNutritionDbAdapter dbHelper;


    public FoodNutritionDeleteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new FoodNutritionDbAdapter(this.getActivity());
        dbHelper.open();
        if (getArguments() != null) {
            _id = getArguments().getLong("id");
        }
    }

    @SuppressLint("ValidFragment")
    public FoodNutritionDeleteFragment(FoodNutritionActivity foodNutrition){
        this.foodNutrition = foodNutrition;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.food_nutrition_delete_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        food = (TextView) getActivity().findViewById(R.id.food);
        total_carbon = (TextView) getActivity().findViewById(R.id.total_carbon);
        total_fat = (TextView) getActivity().findViewById(R.id.total_fat);
        calorie = (TextView) getActivity().findViewById(R.id.calorie);
        date = (TextView) getActivity().findViewById(R.id.date);



        Cursor cursor = dbHelper.fetchRecordById(_id);

        food.setText(cursor.getString(cursor.getColumnIndex(FoodNutritionDbAdapter.FOOD)));
        total_carbon.setText(cursor.getString(cursor.getColumnIndex(FoodNutritionDbAdapter.TOTAL_CARBON)));
        total_fat.setText(cursor.getString(cursor.getColumnIndex(FoodNutritionDbAdapter.TOTAL_FAT)));
        calorie.setText(cursor.getString(cursor.getColumnIndex(FoodNutritionDbAdapter.CALORIE)));
        date.setText(cursor.getString(cursor.getColumnIndex(FoodNutritionDbAdapter.DATE)));

        delete = getActivity().findViewById(R.id.deleteFoodNutrition);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(foodNutrition);

                builder.setMessage("Delete the record")
                        .setTitle(R.string.delete)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(foodNutrition != null){
                                    dbHelper.deleteRecordById(_id);
                                    foodNutrition.returnToAdd();
                                }

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        })
                        .show();

            }
        });
    }

}
