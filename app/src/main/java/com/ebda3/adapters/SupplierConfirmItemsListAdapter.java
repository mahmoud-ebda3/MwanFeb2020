package com.ebda3.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ebda3.mwan.R;

import java.util.ArrayList;

public class SupplierConfirmItemsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> Name;
    private final ArrayList<String> Photo;
    private final ArrayList<String> Amount;
    private final ArrayList<String> ItemAvailableAmount;
    private final ArrayList<String> totalPrice;



    public SupplierConfirmItemsListAdapter(Activity context,  ArrayList<String> Name ,  ArrayList<String> Photo ,  ArrayList<String> Amount ,  ArrayList<String> ItemAvailableAmount ,  ArrayList<String> totalPrice  ) {
        super(context, R.layout.confrim_item  ,  Name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.Name=Name;
        this.Photo=Photo;
        this.Amount=Amount;
        this.ItemAvailableAmount=ItemAvailableAmount;
        this.totalPrice=totalPrice;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.confrim_item, null,true);


        TextView item_name = (TextView) rowView.findViewById(R.id.item_name);
        TextView item_amount = (TextView) rowView.findViewById(R.id.item_amount);
        TextView item_available = (TextView) rowView.findViewById(R.id.item_available);
        TextView item_total = (TextView) rowView.findViewById(R.id.item_total);

        item_name.setText(Name.get(position));
        item_amount.setText(Amount.get(position));
        item_available.setText(ItemAvailableAmount.get(position));
        item_total.setText(totalPrice.get(position));

        return rowView;

    };
}