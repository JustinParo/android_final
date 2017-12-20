package com.tingtingliu.projectassignment.foodnutrition;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.tingtingliu.projectassignment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodNutritionWelcomeFragment extends Fragment {

    private Button button;
    private Button snack;
    private FoodNutritionActivity foodNutrition;
    private OnFragmentWelcomeListener mListener;


    public FoodNutritionWelcomeFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public FoodNutritionWelcomeFragment(FoodNutritionActivity foodNutrition){
        this.foodNutrition = foodNutrition;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.food_nutrition_welcome_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        button = getActivity().findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentStart();
            }
        });
        snack = getActivity().findViewById(R.id.snack);
        snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View linearbottom = getActivity().findViewById(R.id.snackbar);
                Snackbar.make(linearbottom, "Food Nutrition Snack", Snackbar.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentWelcomeListener) {
            mListener = (OnFragmentWelcomeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentWelcomeListener {
        // TODO: Update argument type and name
        void onFragmentStart();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
