package com.ebda3.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ebda3.mwan.R;

import java.util.ArrayList;

public class MyPropertiesItemsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> ProductName;
    private final ArrayList<String> ProductPrice;
    private final ArrayList<String> ProductCount;
    private final ArrayList<String> TotalPrice;


    public MyPropertiesItemsListAdapter(Activity context, ArrayList<String> ProductName , ArrayList<String> ProductPrice , ArrayList<String> ProductCount ,   ArrayList<String> TotalPrice  ) {
        super(context, R.layout.confrim_item  ,  ProductName);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.ProductName=ProductName;
        this.ProductPrice=ProductPrice;
        this.ProductCount=ProductCount;
        this.TotalPrice=TotalPrice;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.confrim_item, null,true);


        TextView item_name = (TextView) rowView.findViewById(R.id.item_name);
        TextView item_amount = (TextView) rowView.findViewById(R.id.item_amount);
        TextView item_available = (TextView) rowView.findViewById(R.id.item_available);
        TextView item_total = (TextView) rowView.findViewById(R.id.item_total);

        item_name.setText(ProductName.get(position));
        item_amount.setText(ProductCount.get(position));
        item_available.setText(ProductPrice.get(position));
        item_total.setText(TotalPrice.get(position));

        return rowView;

    };
}