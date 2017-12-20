package com.tingtingliu.projectassignment.foodnutrition;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.tingtingliu.projectassignment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodNutritionListViewFragment extends Fragment {
    private Button calcButton;
    private Button returnButton;
    private FoodNutritionActivity foodNutrition;

    private ListView listview;

    private FoodNutritionDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;

    private OnListFoodFragmentListener callback;
    private OnListViewFragmentCalcListener calc;


    public FoodNutritionListViewFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public FoodNutritionListViewFragment(FoodNutritionActivity foodNutrition){
        this.foodNutrition = foodNutrition;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new FoodNutritionDbAdapter(this.getActivity());
        dbHelper.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.food_nutrition_listview_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //calculate button
        calcButton = getActivity().findViewById(R.id.calculate);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calc = (OnListViewFragmentCalcListener) getActivity();
                calc.onCalc();
            }
        });
        //return to add page button
        returnButton = getActivity().findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(foodNutrition != null){
                    foodNutrition.returnToAdd();
                }
            }
        });

        listview = (ListView) getActivity().findViewById(R.id.list_food);

        //get the records from database
        Cursor cursor = dbHelper.fetchAllRecords();

        //retrieve the date and calorie
        String[] columns = new String[]{FoodNutritionDbAdapter.DATE, FoodNutritionDbAdapter.FOOD, FoodNutritionDbAdapter.CALORIE };
        int[] to = new int[]{ R.id.list_date, R.id.list_food, R.id.list_calorie };
        dataAdapter = new SimpleCursorAdapter(this.getActivity(), R.layout.food_nutrition_item, cursor, columns, to, 0);

        //populate the list view with data from DB
        listview.setAdapter(dataAdapter);

        callback = (OnListFoodFragmentListener) getActivity();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callback.onItemClickFragmentInteraction(id);
            }
        });
    }

    public interface OnListFoodFragmentListener {
        // TODO: Update argument type and name
        void onItemClickFragmentInteraction(long id);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    public interface OnListViewFragmentCalcListener{
        void onCalc();
    }

}
