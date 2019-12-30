package com.ebda3.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ebda3.Model.DeliveryOrder;
import com.ebda3.mwan.R;

import java.util.ArrayList;

/**
 * Created by work on 17/10/2017.
 */

public class DeliveryAttackersAdapter extends ArrayAdapter<DeliveryOrder> {

    private final Activity context;
    private final ArrayList<DeliveryOrder> MyOrder;

    public DeliveryAttackersAdapter(Activity context, ArrayList<DeliveryOrder> MyOrder)
    {
        super(context, R.layout.delivery_attacker_item,MyOrder);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.MyOrder=MyOrder;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.delivery_attacker_item, null,true);

        TextView delivrey_order_address = (TextView) rowView.findViewById(R.id.deliver_address);
        TextView delivery_order_date = (TextView) rowView.findViewById(R.id.delivery_date);


        delivrey_order_address.setText(MyOrder.get(position).getDeliveryAddress());
        delivery_order_date.setText(MyOrder.get(position).getDeliveryDate());

        return rowView;

    }
}
