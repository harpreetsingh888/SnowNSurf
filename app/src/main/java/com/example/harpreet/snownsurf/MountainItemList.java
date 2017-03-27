package com.example.harpreet.snownsurf;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by harpreet on 6/06/16.
 */
public class MountainItemList implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ViewGroup vg = (ViewGroup) view;
        TextView tv = (TextView)vg.findViewById(R.id.searchText);
        String string;
    }
}
