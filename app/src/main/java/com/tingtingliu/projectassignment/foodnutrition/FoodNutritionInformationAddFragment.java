package com.tingtingliu.projectassignment.foodnutrition;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tingtingliu.projectassignment.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodNutritionInformationAddFragment extends Fragment {
    private Button button;
    private Button cancel;
    private FoodNutritionActivity foodNutrition;

    private EditText food;
    private EditText total_carbon;
    private EditText total_fat;
    private EditText calorie;
    private EditText date;
    private FoodNutritionDbAdapter dbHelper;
    private OnAddFoodFragmentListener mCallback;

    public FoodNutritionInformationAddFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public FoodNutritionInformationAddFragment(FoodNutritionActivity foodNutrition){
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
        return inflater.inflate(R.layout.food_nutrition_add_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        food = (EditText) getActivity().findViewById(R.id.food);
        total_carbon = (EditText) getActivity().findViewById(R.id.total_carbon);
        total_fat = (EditText) getActivity().findViewById(R.id.total_fat);
        calorie = (EditText) getActivity().findViewById(R.id.calorie);
        date = (EditText) getActivity().findViewById(R.id.date);
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        date.setText(dd.format(Calendar.getInstance().getTime()));
        button = getActivity().findViewById(R.id.addFoodNutrition);
        cancel = getActivity().findViewById(R.id.cancel);
        mCallback = (OnAddFoodFragmentListener) getActivity();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyAsync().execute(mCallback);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(foodNutrition != null){
                    foodNutrition.onFragmentStart();
                }
            }
        });
    }

    public interface OnAddFoodFragmentListener{
        void onFragmentInteraction();
    }

    class MyAsync extends AsyncTask<OnAddFoodFragmentListener,String, String> {

        @Override
        protected String doInBackground(OnAddFoodFragmentListener [] callback) {
            dbHelper.createRecord(food.getText().toString().trim(), total_carbon.getText().toString().trim(), calorie.getText().toString().trim(),
                    total_fat.getText().toString().trim(), date.getText().toString().trim());
            callback[0].onFragmentInteraction();
            return "Show all the records in a second";
        }
        @Override
        protected void onPostExecute(String result){
            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
        }
    }



}
