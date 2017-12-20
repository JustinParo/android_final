package com.tingtingliu.projectassignment.ActivityTracking;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class CheckBoxAdapter extends SimpleCursorAdapter {
    private ArrayList<Long> selection = new ArrayList<>();
    private int mCheckBoxId = 0;
    private String mIdColumn;//database KEY_ROWID

    public CheckBoxAdapter(Context context, int layout, Cursor c,
                           String[] from, int[] to, int checkBoxId, String idColumn,
                           int flags) {
        super(context, layout, c, from, to, flags);
        mCheckBoxId = checkBoxId;
        mIdColumn = idColumn;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        final CheckBox checkbox = (CheckBox) view.findViewById(mCheckBoxId);
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getCursor();
                cursor.moveToPosition(position);

                checkbox.setChecked(checkbox.isChecked());
                if (checkbox.isChecked())//put the selected item into arraylist
                {
                    selection.add(cursor.getLong(cursor.getColumnIndex(mIdColumn)));
                    Log.w("test checkbox", String.valueOf(cursor.getInt(cursor.getColumnIndex(mIdColumn)) ));

                } else
                {
                    selection.remove(new Long(cursor.getInt(cursor.getColumnIndex(mIdColumn))));
                    Log.w("test checkbox deselect", String.valueOf(cursor.getInt(cursor.getColumnIndex(mIdColumn))));

//                    Toast.makeText(context, "has removed " + cursor.getInt(cursor.getColumnIndex(mIdColumn)), 0).show();
                }
            }
        });

        return view;
    }

    //return the selected items
    public ArrayList<Long> getSelectedItems() {
        return selection;
    }
}