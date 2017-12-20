package com.tingtingliu.projectassignment.foodnutrition;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tingtingliu.projectassignment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodNutritionCalcAvgCaloryFragment extends Fragment {
    private Button button;
    private FoodNutritionActivity foodNutrition;
    private FoodNutritionDbAdapter dbHelper;

    private TextView per;
    private TextView last;

    private ProgressBar bar;



    public FoodNutritionCalcAvgCaloryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new FoodNutritionDbAdapter(this.getActivity());
        dbHelper.open();
    }

    @SuppressLint("ValidFragment")
    public FoodNutritionCalcAvgCaloryFragment(FoodNutritionActivity foodNutrition){
        this.foodNutrition = foodNutrition;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.food_nutrition_calc_avg_calory, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        bar = getActivity().findViewById(R.id.progressBar);
        bar.setScaleY(3f);
        bar.setProgress(20);
        per = (TextView)getActivity().findViewById(R.id.perday);
        last = (TextView)getActivity().findViewById(R.id.lastday);
        per.setText(": " + dbHelper.getAverageCaloriesPerDay());
        bar.setProgress(50);
        last.setText(": " + dbHelper.getAverageCaloriesLastDay());
        bar.setProgress(100);

        bar.setVisibility(View.VISIBLE);

        button = getActivity().findViewById(R.id.returnToAdd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foodNutrition != null) {
                    foodNutrition.returnToAdd();
                }
            }
        });
    }
}
