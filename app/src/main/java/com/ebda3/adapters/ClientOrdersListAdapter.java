package com.ebda3.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebda3.mwan.R;

import java.util.ArrayList;

public class ClientOrdersListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> ID;
    private final ArrayList<String> Date;
    private final ArrayList<String> Total;
    private final ArrayList<String> Status;


    public ClientOrdersListAdapter(Activity context, ArrayList<String> ID , ArrayList<String> Date  , ArrayList<String> Total, ArrayList<String> Status  ) {
        super(context, R.layout.client_order_item,  ID);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.ID=ID;
        this.Date=Date;
        this.Total=Total;
        this.Status=Status;


    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.client_order_item, null,true);


        TextView order_id = (TextView) rowView.findViewById(R.id.order_id);
        TextView order_date = (TextView) rowView.findViewById(R.id.order_date);
        TextView order_total = (TextView) rowView.findViewById(R.id.order_total);
        LinearLayout step1 = (LinearLayout) rowView.findViewById(R.id.step1);
        LinearLayout step2 = (LinearLayout) rowView.findViewById(R.id.step2);
        LinearLayout step3 = (LinearLayout) rowView.findViewById(R.id.step3);



        if ( Status.get(position).equals("1") )
        {
            step2.setVisibility(View.VISIBLE);
        }
        else if ( Status.get(position).equals("2") )
        {
            step2.setVisibility(View.VISIBLE);
            step3.setVisibility(View.VISIBLE);
        }


        order_id.setText(ID.get(position));
        order_date.setText(Date.get(position));
        order_total.setText(Total.get(position));

        //imageView.setImageResource(map_img[position]);
        return rowView;

    };
}