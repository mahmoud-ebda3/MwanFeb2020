package com.ebda3.mwan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ebda3.Model.UserOrder;
import com.ebda3.adapters.UserOrderAdapter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by work on 23/10/2017.
 */

public class UserOrderAvtivity  extends AppCompatActivity {

    public ArrayList<UserOrder> MyUserOrder = new ArrayList<>();
    UserOrderAdapter adapter ;
    ListView OrderList;
    LinearLayout delivery_date;
    public CheckBox choose_worker;
    public RadioGroup choose_nearest_cheapest;
    public EditText floor_number;
    public Button send_request;
    public TextView user_total_order , user_transport_cost;

    public Toolbar toolbar;
    public TextView headline;

    public ProgressDialog progressDialog;
    Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_order_activity);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);
        headline.setText("المواد");
        progressDialog = new ProgressDialog(activity,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("جارى التحميل...");

        OrderList = (ListView) findViewById(R.id.list_order_user);
        delivery_date = (LinearLayout) findViewById(R.id.show_date_picker);
        send_request = (Button) findViewById(R.id.button_send_my_order);
        choose_worker = (CheckBox) findViewById(R.id.worker_check_box);
        choose_nearest_cheapest = (RadioGroup) findViewById(R.id.customer_radio_group);
        floor_number = (EditText) findViewById(R.id.floor_number_id);
        user_total_order = (TextView) findViewById(R.id.order_total);
        user_transport_cost = (TextView) findViewById(R.id.transport_cost_id);

        send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(UserOrderAvtivity.this, UserRatingActivity.class);
                startActivity(myIntent);
            }
        });
        for (int i =0 ; i<20 ; i++)
        {
            UserOrder userOrder = new UserOrder();
            userOrder.setOrderID(String.valueOf(i));
            userOrder.setProductName("تجربة "+String.valueOf(i));
            userOrder.setProductCount(String.valueOf(2));
            userOrder.setPrice(String.valueOf(20+i));

            MyUserOrder.add(userOrder);
        }

        adapter = new UserOrderAdapter(UserOrderAvtivity.this,MyUserOrder);
        OrderList.setAdapter(adapter);

        final Calendar myCalendar = Calendar.getInstance();



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //updateLabel();
            }

        };

        delivery_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UserOrderAvtivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
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
