package com.tingtingliu.projectassignment.Automobile;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tingtingliu.projectassignment.R;

public class AutomobileNewRecordFragment extends Fragment {

    protected static final String ACTIVITY_NAME = "MessageFragment";

    Button aRecordButton, aResetButton, aCancelButton;
    EditText liters, price, km;
    AutomobileDatabaseAdapter aDatabaseAdapter;
    Context ctx = getActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aDatabaseAdapter = new AutomobileDatabaseAdapter(this.getActivity());
        aDatabaseAdapter.open();
    }

    @Override
    public void onStart() {
        super.onStart();

        liters = (EditText) getActivity().findViewById(R.id.litersText);
        price = (EditText) getActivity().findViewById(R.id.priceText);
        km = (EditText) getActivity().findViewById(R.id.kmText);

        aRecordButton = (Button) getActivity().findViewById(R.id.aRecordButton);
        aRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean recordCheck = false;
                //TODO: check inputs
                if (liters.toString() == null || liters.toString() == "0") {
                    Toast.makeText(ctx, "Please Enter Liters", Toast.LENGTH_SHORT).show();
                } else if (price.toString() == null) {
                    Toast.makeText(ctx, "Please Enter Price", Toast.LENGTH_SHORT).show();
                } else if (km.toString() == null) {
                    Toast.makeText(ctx, "Please Enter km", Toast.LENGTH_SHORT).show();
                } else {
                    ((AutomobileActivity)getActivity()).setButtonVisible();
                    aDatabaseAdapter.createRecord(liters.getText().toString(),
                            price.getText().toString(),
                            km.getText().toString());
                    ((AutomobileActivity)getActivity()).removeFragment();
                }
            }
        });

        aResetButton = (Button) getActivity().findViewById(R.id.aResetButton);
        aResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liters.setText(null);
                price.setText(null);
                km.setText(null);
            }
        });

        aCancelButton = (Button) getActivity().findViewById(R.id.aCancelButton);
        aCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AutomobileActivity)getActivity()).setButtonVisible();
                ((AutomobileActivity)getActivity()).removeFragment();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_automobile_new_recrod_fragment,parent,false);
        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //this.callingActivity = activity;
    }
}
