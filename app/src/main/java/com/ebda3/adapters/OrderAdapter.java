package com.ebda3.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ebda3.Model.OrderDetails;
import com.ebda3.mwan.R;
import java.util.ArrayList;

/**
 * Created by work on 17/10/2017.
 */

public class OrderAdapter extends ArrayAdapter<OrderDetails> {

    private final Activity context;
    private final ArrayList<OrderDetails> MyOrder;

    public OrderAdapter(Activity context, ArrayList<OrderDetails> MyOrder)
    {
        super(context, R.layout.order_item,MyOrder);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.MyOrder=MyOrder;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.order_item, null,true);

        /*
        TextView order_kind = (TextView) rowView.findViewById(R.id.order_kind_id);
        TextView order_count = (TextView) rowView.findViewById(R.id.order_count_id);
        TextView order_cost = (TextView) rowView.findViewById(R.id.order_cost_id);


        order_kind.setText(MyOrder.get(position).getOrderKind());
        order_count.setText(MyOrder.get(position).getOrderCount());
        order_cost.setText(MyOrder.get(position).getOrderCost());
*/
        return rowView;

    }
}

