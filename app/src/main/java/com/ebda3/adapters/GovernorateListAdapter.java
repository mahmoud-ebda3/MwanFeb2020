package com.ebda3.adapters;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ebda3.mwan.MarketMaterial;
import com.ebda3.mwan.R;

import java.util.ArrayList;

public class GovernorateListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> ID;
    private final ArrayList<String> Name;


    public GovernorateListAdapter(Activity context, ArrayList<String> Name , ArrayList<String> ID  ) {
        super(context, R.layout.cat_item,  Name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.ID=ID;
        this.Name=Name;


    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.governorate_item, null,true);


        TextView name = (TextView) rowView.findViewById(R.id.name);
        Button construction = (Button) rowView.findViewById(R.id.construction);
        Button finishing = (Button) rowView.findViewById(R.id.finishing);


        construction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, MarketMaterial.class);
                myIntent.putExtra("GID", ID.get(position) );
                myIntent.putExtra("id","121");
                context.startActivity(myIntent);
            }
        });

        finishing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, MarketMaterial.class);
                myIntent.putExtra("GID", ID.get(position) );
                myIntent.putExtra("id","122");
                context.startActivity(myIntent);
            }
        });





        name.setText(Name.get(position));

        //imageView.setImageResource(map_img[position]);
        return rowView;

    };
}