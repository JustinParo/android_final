package com.tingtingliu.projectassignment.ActivityTracking;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tingtingliu.projectassignment.ActivityTracking.db.ActivityTrackingDbAdapter;
import com.tingtingliu.projectassignment.R;

/**
 * Created by DEREK on 2017-12-12.
 */

public class CustomDialog extends DialogFragment {
    String activityId;
    String activityDuration;
    String date;
    String notes;
    String activityType;
    long rowId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        if (args != null) {
            activityId = args.getString("id");
            activityDuration = args.getString("activityDuration");
            date = args.getString("date");
            notes = args.getString("notes");
            activityType = args.getString("activityType");
            rowId = args.getLong("rowId");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText text = (EditText) getDialog().findViewById(R.id.username);
                        String message = text.getText().toString();
                        ActivityTrackingDbAdapter dbHelper = new ActivityTrackingDbAdapter(getActivity());
                        dbHelper.open();
                        try {
                            dbHelper.updateActivity(rowId, activityId, message,activityType,  notes, date);

                        } catch (Exception e) {
                            CharSequence t = getString(R.string.TransFail);
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(getActivity(), t, duration);
                            toast.show();
                        }
                        CharSequence t = getString(R.string.TransSuc);
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getActivity(), t, duration);
                        toast.show();
                        dbHelper.close();

                        ActivityTrackingListFragment activityTrackingListFragment = new ActivityTrackingListFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, activityTrackingListFragment).addToBackStack(null).commit();
                        final View coordinatorLayoutView = getActivity().findViewById(R.id.snackbarPosition);
                        Snackbar
                                .make(coordinatorLayoutView, getString(R.string.updateDuration) + message + getString(R.string.minutes), Snackbar.LENGTH_LONG)
                                .show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CustomDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        EditText text = (EditText) getDialog().findViewById(R.id.username);
        text.setText(activityDuration);
    }
}