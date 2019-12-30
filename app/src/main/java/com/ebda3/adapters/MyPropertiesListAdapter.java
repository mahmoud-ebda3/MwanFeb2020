package com.ebda3.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ebda3.mwan.R;

import java.util.ArrayList;

public class MyPropertiesListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> ID;
    private final ArrayList<String> Name;
    private final ArrayList<String> Net;


    public MyPropertiesListAdapter(Activity context, ArrayList<String> Name , ArrayList<String> ID , ArrayList<String> Net  ) {
        super(context, R.layout.properties_item,  Name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.ID=ID;
        this.Name=Name;
        this.Net=Net;


    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.properties_item, null,true);


        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView net = (TextView) rowView.findViewById(R.id.net);





        name.setText(Name.get(position));
        net.setText(Net.get(position));

        //imageView.setImageResource(map_img[position]);
        return rowView;

    };
}