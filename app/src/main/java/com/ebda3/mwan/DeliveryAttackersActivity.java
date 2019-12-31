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

import com.ebda3.Model.DeliveryOrder;
import com.ebda3.adapters.DeliveryAttackersAdapter;

import java.util.ArrayList;

/**
 * Created by work on 17/10/2017.
 */

public class DeliveryAttackersActivity extends AppCompatActivity {
    public ListView delivery_list;
    public TextView delivery_points;
    public ArrayList<DeliveryOrder> deliveryOrders = new ArrayList<>();
    DeliveryAttackersAdapter adapter;
//    private HomeActivity mClass;

    public Toolbar toolbar;
    public TextView headline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_attacker_activity);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);

        headline.setText("مراقبة التوصيل");

        delivery_list = (ListView) findViewById(R.id.list_order_id);
        delivery_points = (TextView) findViewById(R.id.delivery_points_id);

        delivery_points.setText("50000");
         for (int i =0  ; i< 20 ; i++)
         {
             DeliveryOrder del = new DeliveryOrder();
             del.setDeliveryAddress("الهرم - الجيزة - مصر");
             del.setDeliveryDate("الثلاثاء");
             deliveryOrders.add(del);
         }

         adapter = new DeliveryAttackersAdapter(DeliveryAttackersActivity.this,deliveryOrders);
        delivery_list.setAdapter(adapter);

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
