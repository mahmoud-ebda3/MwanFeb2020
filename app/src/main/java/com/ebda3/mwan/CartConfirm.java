package com.ebda3.mwan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ebda3.adapters.SupplierConfirmItemsListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.ebda3.Helpers.Config.cartData;


/**
 * Created by work on 29/10/2017.
 */

public class CartConfirm extends AppCompatActivity {

    public static Boolean BackAction = false;


    public static Button buy_cart_product;
    public static TextView total_price, shiping_price, net_price;
    TextView no_data;
    Activity context = this;
    Activity activity = this;


    public LayoutInflater inflater;


    private ArrayList<String> ID = new ArrayList<String>();
    private ArrayList<String> Name = new ArrayList<String>();
    private ArrayList<String> Photo = new ArrayList<String>();
    private ArrayList<String> Amount = new ArrayList<String>();
    private ArrayList<String> ItemAvailableAmount = new ArrayList<String>();
    private ArrayList<String> totalPrice = new ArrayList<String>();
    private ArrayList<String> priceBeforeDiscount = new ArrayList<>();
    private ArrayList<String> Info = new ArrayList<String>();


    String items, IDS, Net;

    public Toolbar toolbar;
    public TextView headline;
    LinearLayout shopping_cart_image;
    public SupplierConfirmItemsListAdapter adapter;

    ListView listView;
    public static TextView notificationNum;

    @Override
    protected void onResume() {
        super.onResume();
        if (cartData.size() > 0) {
            notificationNum.setVisibility(View.VISIBLE);
            notificationNum.setText(String.valueOf(cartData.size()));
        } else {
            notificationNum.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_confirm);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);

        shopping_cart_image = toolbar.findViewById(R.id.notificationB);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);
        buy_cart_product = (Button) findViewById(R.id.buy_cart_product);

        total_price = (TextView) findViewById(R.id.total_price);
        shiping_price = (TextView) findViewById(R.id.shiping_price);
        net_price = (TextView) findViewById(R.id.net_price);

        notificationNum = (TextView) findViewById(R.id.notificationNum);


        listView = (ListView) findViewById(R.id.list);
        LinearLayout orderDetails = (LinearLayout) findViewById(R.id.orderDetails);

        try {
            String supplier_name = getIntent().getStringExtra("supplier_name");
            String shippingCost = getIntent().getStringExtra("shippingCost");
            String total = getIntent().getStringExtra("total");
            Net = getIntent().getStringExtra("Net");

            headline.setText(supplier_name);
            total_price.setText(total + " جنية");
            shiping_price.setText(shippingCost + " جنية");
            net_price.setText(Net + " جنية");

            items = getIntent().getStringExtra("Items");
            IDS = getIntent().getStringExtra("ID");
            JSONArray jsonArray = new JSONArray(items);
            Log.e("12333", jsonArray.toString());
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject row = jsonArray.getJSONObject(i);
                    ID.add(row.getString("ID").toString());
                    Name.add(row.getString("Name").toString());
                    Photo.add(row.getString("Photo").toString());
                    Amount.add(row.getString("Amount").toString());
                    ItemAvailableAmount.add(row.getString("ItemAvailableAmount").toString());
                    totalPrice.add(row.getString("totalPrice").toString());
                    Info.add(row.getString("Info").toString());
                    priceBeforeDiscount.add(row.getString("totalPriceBeforeDiscount"));
                }
                adapter = new SupplierConfirmItemsListAdapter(context, Name, Photo, Amount, ItemAvailableAmount, totalPrice, Info, priceBeforeDiscount);
                listView.setAdapter(adapter);
                orderDetails.setVisibility(View.VISIBLE);
            } else {
                orderDetails.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }


        /*
        private ArrayList<String> ID = new ArrayList<String>();
        private ArrayList<Integer> Name = new ArrayList<Integer>();
        private ArrayList<Integer> Photo = new  ArrayList<Integer>();
        private ArrayList<String> Amount = new  ArrayList<String>();
        private ArrayList<Float> ItemAvailableAmount = new  ArrayList<Float>();
        private ArrayList<Float> totalPrice = new  ArrayList<Float>();
        */


        shopping_cart_image.setVisibility(View.VISIBLE);
        shopping_cart_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(CartConfirm.this, NewCart.class);
                startActivity(myIntent);
            }
        });
        buy_cart_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(CartConfirm.this, ConfirmOrder.class);
                myIntent.putExtra("ID", IDS);
                myIntent.putExtra("Items", items);
                myIntent.putExtra("Net", Net);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (BackAction) {
            Intent intent = new Intent(context, UserHomeActivity.class);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
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
