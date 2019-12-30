package com.ebda3.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ebda3.Model.UserOrder;
import com.ebda3.mwan.R;

import java.util.ArrayList;

/**
 * Created by work on 23/10/2017.
 */

public class UserOrderAdapter extends ArrayAdapter<UserOrder> {

    private final Activity context;
    private final ArrayList<UserOrder> MyOrder;

    public UserOrderAdapter(Activity context, ArrayList<UserOrder> MyOrder)
    {
        super(context, R.layout.user_order_item,MyOrder);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.MyOrder=MyOrder;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.user_order_item, null,true);

        TextView product_name = (TextView) rowView.findViewById(R.id.user_product_name);
        TextView product_count = (TextView) rowView.findViewById(R.id.user_product_count);
        TextView product_cost = (TextView) rowView.findViewById(R.id.user_product_cost);

        product_name.setText(MyOrder.get(position).getProductName());
        product_count.setText(MyOrder.get(position).getProductCount());
        product_cost.setText(MyOrder.get(position).getPrice());

        return rowView;

    }
}
