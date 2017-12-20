package com.tingtingliu.projectassignment.ActivityTracking;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tingtingliu.projectassignment.ActivityTracking.db.ActivityTrackingDbAdapter;
import com.tingtingliu.projectassignment.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ActivityTrackingDetailFragment extends Fragment {
    public final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    private long rowId;
    private Cursor cursor;
    private LinearLayout linearLayout;
    private View rootView;
    private Button submit;
    private Button reset;
    private Button update;
    private Button delete;
    FloatingActionButton fab;
    private Calendar myCalendar;
    private EditText dateEtxt;
    private DatePickerDialog.OnDateSetListener date;
    final String[] items = new String[]{"Running", "Walking", "Biking", "Swimming", "Skating"};

    public ActivityTrackingDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // If activity recreated (such summaryArrayList from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
            updateDetailView(mCurrentPosition);
        }
        rootView = inflater.inflate(R.layout.fragment_activity_tracker_detail, container, false);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.button_container);
        Spinner dropdown = rootView.findViewById(R.id.editActivityType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            updateDetailView(args.getInt(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            updateDetailView(mCurrentPosition);
        }

        if (mCurrentPosition != -1) {
            linearLayout.removeAllViews();
            addUpdateButtons();

        } else {
            addCreateNewButtons();
        }
        setDate();
    }

    public void updateDetailView(int position) {
        mCurrentPosition = position;


        new AsyncTask<Void, Void, Cursor>() {
            ActivityTrackingDbAdapter dbHelper;

            @Override
            protected Cursor doInBackground(Void... params) {

                dbHelper = new ActivityTrackingDbAdapter(getActivity());
                dbHelper.open();

                String[] columns = new String[]{
                        ActivityTrackingDbAdapter.KEY_ROWID,
                        ActivityTrackingDbAdapter.KEY_DURATION,
                        ActivityTrackingDbAdapter.KEY_ACTIVITY_TRACKING_TYPE,
                        ActivityTrackingDbAdapter.KEY_COMMENT,
                        ActivityTrackingDbAdapter.KEY_DATE
                };

                SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(getActivity(),
                        R.layout.fragment_activity_tracker_detail,
                        dbHelper.fetchAllRecords(),
                        columns,
                        new int[]{R.id.row_id, R.id.editActivityType, R.id.editActivityDuration, R.id.dateCreated, R.id.editActivityComment},
                        0);
                cursor = (Cursor) dataAdapter.getItem(mCurrentPosition);
                return cursor;
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                rowId = cursor.getLong(cursor.getColumnIndex(ActivityTrackingDbAdapter.KEY_ROWID));
                String activityDuration = cursor.getString(cursor.getColumnIndex(ActivityTrackingDbAdapter.KEY_DURATION));
                String activitytype = cursor.getString(cursor.getColumnIndex(ActivityTrackingDbAdapter.KEY_ACTIVITY_TRACKING_TYPE));
                int index = -1;
                for (int i = 0; i < items.length; i++) {
                    if (items[i].equals(activitytype)) {
                        index = i;
                        ((Spinner) rootView.findViewById(R.id.editActivityType)).setSelection(index);
                        break;
                    }
                }
                ((EditText) rootView.findViewById(R.id.editActivityDuration)).setText(activityDuration);
                String dateCreated = cursor.getString(cursor.getColumnIndex(ActivityTrackingDbAdapter.KEY_DATE));
                EditText date = (EditText) rootView.findViewById(R.id.dateCreated);
                date.setText(String.valueOf(dateCreated), TextView.BufferType.EDITABLE);
                String comments = cursor.getString(cursor.getColumnIndex(ActivityTrackingDbAdapter.KEY_COMMENT));
                EditText comment = (EditText) rootView.findViewById(R.id.editActivityComment);
                comment.setText(String.valueOf(comments), TextView.BufferType.EDITABLE);
                dbHelper.close();
            }
        }.execute();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (fab != null)
            fab.setVisibility(View.VISIBLE);
        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.buttonHolder);
        if (linearLayout != null)
            linearLayout.setVisibility(View.VISIBLE);
        Button deleteButton = (Button) getActivity().findViewById(R.id.button2);
        if (deleteButton != null)
            deleteButton.setVisibility(View.INVISIBLE);
        ProgressBar bar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
        bar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        Log.d(getClass().getSimpleName(), "onDestroyView()");
        super.onDestroyView();
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (fab != null)
            fab.setVisibility(View.INVISIBLE);
        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.buttonHolder);
        linearLayout.setVisibility(View.INVISIBLE);


    }

    public void addCreateNewButtons() {
        submit = new Button(getActivity());
        submit.setText(getActivity().getResources().getString(R.string.submit));
        reset = new Button(getActivity());
        reset.setText(getActivity().getResources().getString(R.string.reset));
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100);
        ll.setMargins(10, 10, 10, 10);
        submit.setLayoutParams(ll);
        reset.setLayoutParams(ll);
        submit.setHeight(25);
        reset.setHeight(25);
        submit.setTextColor(Color.parseColor("#FFFFFF"));
        submit.setShadowLayer(
                5f, // radius
                0.0f, // dx
                0.0f, // dy
                Color.parseColor("#3D3D3D") // shadow color
        );

        reset.setTextColor(Color.parseColor("#FFFFFF"));
        reset.setShadowLayer(
                5f, // radius
                0.0f, // dx
                0.0f, // dy
                Color.parseColor("#3D3D3D") // shadow color
        );
        submit.setBackgroundResource(R.drawable.buttonshape);
        reset.setBackgroundResource(R.drawable.buttonshape);
        linearLayout.addView(submit);
        linearLayout.addView(reset);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickSubmit();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickReset();
            }
        });
    }

    public void addUpdateButtons() {
        update = new Button(getActivity());
        update.setText(getActivity().getResources().getString(R.string.update));
        delete = new Button(getActivity());
        delete.setText(getActivity().getResources().getString(R.string.del));
        update.setBackgroundResource(R.drawable.buttonshape);
        delete.setBackgroundResource(R.drawable.buttonshape);
        update.setTextColor(Color.parseColor("#FFFFFF"));
        update.setShadowLayer(
                5f, // radius
                0.0f, // dx
                0.0f, // dy
                Color.parseColor("#3D3D3D") // shadow color
        );
        delete.setTextColor(Color.parseColor("#FFFFFF"));
        delete.setShadowLayer(
                5f, // radius
                0.0f, // dx
                0.0f, // dy
                Color.parseColor("#3D3D3D") // shadow color
        );
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100);
        ll.setMargins(10, 10, 10, 10);
        update.setLayoutParams(ll);
        delete.setLayoutParams(ll);
        linearLayout.addView(update);
        linearLayout.addView(delete);
        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickUpdate();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickDelete();
            }
        });
    }

    public void onClickReset() {
        ((EditText) rootView.findViewById(R.id.editActivityDuration)).setText("");
        ((EditText) rootView.findViewById(R.id.dateCreated)).setText("");
        ((EditText) rootView.findViewById(R.id.editActivityComment)).setText("");
        ((Spinner) rootView.findViewById(R.id.editActivityType)).setSelection(0);
    }

    public void onClickSubmit() {
        String activityDuration = ((EditText) rootView.findViewById(R.id.editActivityDuration)).getText().toString();
        String date = ((EditText) rootView.findViewById(R.id.dateCreated)).getText().toString();
        String comments = ((EditText) rootView.findViewById(R.id.editActivityComment)).getText().toString();
        String activityType = ((Spinner) rootView.findViewById(R.id.editActivityType)).getSelectedItem().toString();
        ActivityTrackingDbAdapter dbHelper = new ActivityTrackingDbAdapter(getActivity());
        dbHelper.open();
        try {
            dbHelper.createActivity(activityDuration, activityType, comments, date);

        } catch (Exception e) {
            CharSequence text = "Transaction Failed!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getActivity(), text, duration);
            toast.show();
        }
        CharSequence text = "Transaction executed successfully!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getActivity(), text, duration);
        toast.show();
        dbHelper.close();
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }

    public void onClickUpdate() {
        DialogFragment customDialog = new CustomDialog();
        Bundle args = new Bundle();
        String activityDuration = ((EditText) rootView.findViewById(R.id.editActivityDuration)).getText().toString();
        String date = ((EditText) rootView.findViewById(R.id.dateCreated)).getText().toString();
        String comment = ((EditText) rootView.findViewById(R.id.editActivityComment)).getText().toString();
        String activityType = ((Spinner) rootView.findViewById(R.id.editActivityType)).getSelectedItem().toString();
        args.putString("activityDuration", activityDuration);
        args.putString("date", date);
        args.putString("notes", comment);
        args.putString("activityType", activityType);
        args.putLong("rowId", rowId);
        customDialog.setArguments(args);
        customDialog.show(getActivity().getSupportFragmentManager(), "CustomDialog");
    }

    public void onClickDelete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.alertDialog_message);
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ActivityTrackingDbAdapter dbHelper = new ActivityTrackingDbAdapter(getActivity());
                dbHelper.open();
                try {
                    dbHelper.deleteActivity(rowId);
                } catch (Exception e) {
                    CharSequence text = getString(R.string.TransFail);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                }
                CharSequence text = getString(R.string.TransSuc);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getActivity(), text, duration);
                toast.show();
                dbHelper.close();
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setDate() {
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        dateEtxt = rootView.findViewById(R.id.dateCreated);
        dateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);
        EditText dateEtxt = rootView.findViewById(R.id.dateCreated);
        dateEtxt.setText(sdf.format(myCalendar.getTime()));
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getActivity().finish();
        startActivity(getActivity().getIntent());
    }
}