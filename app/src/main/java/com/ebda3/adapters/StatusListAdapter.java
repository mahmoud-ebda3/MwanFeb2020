package com.ebda3.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ebda3.mwan.R;

import java.util.ArrayList;

public class StatusListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> StatusDate;
    private final ArrayList<String> StatusText;




    public StatusListAdapter(Activity context,  ArrayList<String> StatusDate, ArrayList<String> StatusText) {
        super(context, R.layout.status_item  ,  StatusText);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.StatusDate=StatusDate;
        this.StatusText=StatusText;


    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.status_item, null,true);


        TextView add_date = (TextView) rowView.findViewById(R.id.add_date);
        TextView status = (TextView) rowView.findViewById(R.id.status);

        add_date.setText(StatusDate.get(position));
        status.setText(StatusText.get(position));






        return rowView;

    };
}