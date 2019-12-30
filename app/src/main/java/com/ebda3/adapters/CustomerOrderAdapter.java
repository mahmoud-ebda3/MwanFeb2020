package com.ebda3.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ebda3.Model.Order;
import com.ebda3.mwan.R;
import java.util.ArrayList;


/**
 * Created by work on 17/10/2017.
 */

public class CustomerOrderAdapter extends ArrayAdapter<Order> {

    private final Activity context;
    private final ArrayList<Order> MyOrder;

    public CustomerOrderAdapter(Activity context, ArrayList<Order> MyOrder)
    {
        super(context, R.layout.customer_order_item,MyOrder);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.MyOrder=MyOrder;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.customer_order_item, null,true);

        TextView order_address = (TextView) rowView.findViewById(R.id.customer_order_address);
        TextView order_id = (TextView) rowView.findViewById(R.id.customer_order_id);
        TextView order_cost = (TextView) rowView.findViewById(R.id.customer_order_cost);

        order_address.setText(MyOrder.get(position).getAddress());
        order_id.setText(MyOrder.get(position).getID());
        order_cost.setText(MyOrder.get(position).getPrice());

        return rowView;

    }
}