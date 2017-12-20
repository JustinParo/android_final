package com.tingtingliu.projectassignment.ActivityTracking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tingtingliu.projectassignment.R;
import com.tingtingliu.projectassignment.ActivityTracking.db.ActivityTrackingDbAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by DEREK on 2017-12-03.
 */

public class ActivityTrackingListFragment extends ListFragment {
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    OnHeadlineSelectedListener mCallback;
    CheckBoxAdapter dataAdapter;
    Cursor cursor;
    ProgressBar bar;
    Map<Integer, ActivitySummary> map;
    String summary;
    String lastMonthSummary = "";
    private int mActivatedPosition = ListView.INVALID_POSITION;

    public ActivityTrackingListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        getActivity().findViewById(R.id.buttonHolder).setVisibility(View.VISIBLE);
        bar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
        if (bar.getVisibility() == View.INVISIBLE) {
            bar.setVisibility(View.VISIBLE);
        }
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityTrackingDetailFragment activityFragment = new ActivityTrackingDetailFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, activityFragment).addToBackStack(null).commit();
            }
        });

        new AsyncTask<Void, Integer, CheckBoxAdapter>() {
            ActivityTrackingDbAdapter dbHelper;

            @Override
            protected CheckBoxAdapter doInBackground(Void... voids) {
                int layout = R.layout.list_view;
                dbHelper = new ActivityTrackingDbAdapter(getActivity());
                dbHelper.open();
                dbHelper.insertSomeActivities();

                publishProgress(20);
                cursor = dbHelper.fetchAllRecords();
                publishProgress(50);
                String[] columns = new String[]{
                        ActivityTrackingDbAdapter.KEY_DATE, ActivityTrackingDbAdapter.KEY_DURATION + "|| ' Minutes - Type:' ||" + ActivityTrackingDbAdapter.KEY_ACTIVITY_TRACKING_TYPE, ActivityTrackingDbAdapter.KEY_ROWID
                };
                //check parameters
                dataAdapter = new CheckBoxAdapter(getActivity(),
                        layout,
                        dbHelper.fetchRecords(), columns,
                        new int[]{R.id.text1, R.id.text2, R.id.row_id_activity_list},
                        R.id.chkIosExpense,
                        ActivityTrackingDbAdapter.KEY_ROWID,
                        0);
                publishProgress(75);
                return dataAdapter;
            }

            @Override
            protected void onProgressUpdate(Integer... progresses) {
                bar.setProgress(progresses[0]);
            }

            @Override
            protected void onPostExecute(CheckBoxAdapter dataAdapter) {
                map = new HashMap<Integer, ActivitySummary>();
                int mapKey = 0;
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    ActivitySummary aTemp = new ActivitySummary();
                    aTemp.setMonth(cursor.getString(cursor.getColumnIndex(ActivityTrackingDbAdapter.KEY_DATE)));
                    if ((cursor.getString(cursor.getColumnIndex(ActivityTrackingDbAdapter.KEY_DURATION))).equals(""))
                        aTemp.setMinutes(0);
                    else
                        aTemp.setMinutes(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ActivityTrackingDbAdapter.KEY_DURATION))));
                    map.put(mapKey, aTemp);

                    mapKey++;
                }
                setListAdapter(dataAdapter);
                publishProgress(100);
//                bar.setVisibility(View.INVISIBLE);
            }
        }.execute();


        final Button delete = (Button) getActivity().findViewById(R.id.button2);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteOnlClick();
            }
        });

        final Button summary = (Button) getActivity().findViewById(R.id.button1);
        summary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                summaryOnlClick();
            }
        });
        final Button help = (Button) getActivity().findViewById(R.id.button3);
        help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                helpOnlClick();
            }
        });
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.list_view, container, false);
//        //View view = getListView().findViewById(R.id.list);
////        ListView ls = (ListView) view.findViewById(android.R.id.list);
//        //return view;
//        createNewButtons(view);
//        return view;
//    }

//    public void createNewButtons(View view) {
//        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.listFragement);
//        Button summary = new Button(getActivity());
//        summary.setText("Summary");
//        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100);
//        ll.setMargins(10, 10, 10, 10);
//        summary.setLayoutParams(ll);
//        summary.setHeight(25);
//        summary.setTextColor(Color.parseColor("#FFFFFF"));
//        summary.setShadowLayer(
//                5f, // radius
//                0.0f, // dx
//                0.0f, // dy
//                Color.parseColor("#3D3D3D") // shadow color
//        );
//        summary.setBackgroundResource(R.drawable.buttonshape);
//        linearLayout.addView(summary);
//        summary.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                summaryOnlClick();
//            }
//        });
//    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (fab != null)
            fab.setVisibility(View.VISIBLE);
        //createNewButtons(view);
        // Restore the previously serialized activated item position.
        Button deleteButton = (Button) getActivity().findViewById(R.id.button2);
        if (deleteButton != null&& (getActivity().findViewById(R.id.fragment_container))!=null)
            deleteButton.setVisibility(View.VISIBLE);
        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.buttonHolder);
        linearLayout.setVisibility(View.VISIBLE);

        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onDestroyView() {
        Log.d(getClass().getSimpleName(), "onDestroyView()");
        super.onDestroyView();

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (fab != null)
            fab.setVisibility(View.INVISIBLE);
        //createNewButtons(view);
        // Restore the previously serialized activated item position.

        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.buttonHolder);
        if (linearLayout != null)
            linearLayout.setVisibility(View.INVISIBLE);
        ProgressBar bar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
        if (bar != null)
            bar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onItemSelected(position);
        getListView().setSelector(android.R.color.holo_blue_bright);
        // Set the item summaryArrayList checked to be highlighted when in two-pane layout
        l.setItemChecked(position, true);
        Log.w("test", Integer.toString(position));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }
        mActivatedPosition = position;
    }

    public void deleteOnlClick() {
        ActivityTrackingDbAdapter dbHelper = new ActivityTrackingDbAdapter(getActivity());
        dbHelper.open();
        ArrayList<Long> checked = dataAdapter.getSelectedItems();

        for (int i = 0; i < checked.size(); i++) {
            Cursor c = (Cursor) dataAdapter.getItem(i);
            long rowId = c.getLong(c.getColumnIndex(ActivityTrackingDbAdapter.KEY_ROWID));
            dbHelper.deleteActivity(rowId);
        }

        CharSequence text = "Transaction executed successfully!";
        if (checked.isEmpty()) text = "There is nothing to delete!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getActivity(), text, duration);
        toast.show();
        dbHelper.close();
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }

    public void summaryOnlClick() {

        process(map);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Activity Summary");
        builder.setMessage(lastMonthSummary + summary);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void helpOnlClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.alertDialog_activity_help);
        builder.setMessage(R.string.alertDialog_activity_help_message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void process(Map<Integer, ActivitySummary> activities) {
        HashMap<String, HashMap<String, Integer>> m = new HashMap<String, HashMap<String, Integer>>();
        HashMap<String, Integer> month = null;
        for (Integer recordKey : activities.keySet()) {
            ActivitySummary act = activities.get(recordKey);
            String s[] = act.getMonth().split("/");
            if (!m.containsKey(s[2])) {
                month = new HashMap<String, Integer>();
                m.put(s[2], month);
            }
            HashMap<String, Integer> m1 = m.get(s[2]);
            if (!m1.containsKey(s[0])) {
                m1.put(s[0], activities.get(recordKey).getMinutes());
            } else {
                int flag = m1.get(s[0]);
                m1.put(s[0], flag + activities.get(recordKey).getMinutes());
            }
        }
        summary = "All Summary:\n";
        Date today = new Date(); // Fri Jun 17 14:54:28 PDT 2016
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.MONTH, -1);
        int lastMonth = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int i = 0;
        for (String s : m.keySet()) {
            Map<String, Integer> mt = m.get(s);
            for (String s1 : mt.keySet()) {
                if (Integer.parseInt(s) == year && s1.equals(String.valueOf(lastMonth))) {
                    lastMonthSummary = "Last month:\n\t" + s + " - " + s1 + "   \t" + "Activity Duration : " + "\t" + mt.get(s1) + " minutes" + "\n\n";
                } else {
                    lastMonthSummary = "Last month:\n\t" + year + " - " + lastMonth + "   \t" + "Activity Duration : " + "\t" + "0  minutes" + "\n\n";
                }
                summary += "\t" + s + " - " + s1 + "   \t" + "Activity Duration : " + "\t" + mt.get(s1) + " minutes" + "\n";
            }
        }
    }

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnHeadlineSelectedListener {

        void onItemSelected(int position);
    }

    public void convertStringToDate() {
        int total;
        String string = "MM/dd/yyyy";
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA);
        try {
            Date date = format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    class ActivitySummary {
        private String month;
        private int minutes;
        private long rowId;


        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            this.minutes = minutes;
        }

        public long getRowId() {
            return rowId;
        }

        public void setRowId(long rowId) {
            this.rowId = rowId;
        }
    }
}
