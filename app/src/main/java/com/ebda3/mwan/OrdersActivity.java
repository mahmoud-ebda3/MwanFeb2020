package com.ebda3.mwan;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ebda3.Model.OrderDetails;
import com.ebda3.adapters.OrderAdapter;

import java.util.ArrayList;

/**
 * Created by work on 17/10/2017.
 */

public class OrdersActivity extends AppCompatActivity
{

    public ListView order_list;
    public TextView order_sum , order_available;
    public ArrayList<OrderDetails> orderDetailses = new ArrayList<>();
    OrderAdapter adapter;

    public Toolbar toolbar;
    public TextView headline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);
//        LayoutInflater inflater = (LayoutInflater) this
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View contentView = inflater.inflate(R.layout.order_activity, null, false);
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.addView(contentView, 0);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);
        headline.setText("الطلبات");
        order_list = (ListView) findViewById(R.id.list_order_details_id);
        order_available = (TextView) findViewById(R.id.order_available_id);
        order_sum = (TextView) findViewById(R.id.order_sum_id);

        for (int i =0  ; i< 20 ; i++)
        {
            OrderDetails odel = new OrderDetails();
            odel.setOrderKind("جلاد");
            odel.setOrderCount("2");
            odel.setOrderCost("2000");
            orderDetailses.add(odel);
        }

        adapter = new OrderAdapter(OrdersActivity.this,orderDetailses);
        order_list.setAdapter(adapter);

    }

    public boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
