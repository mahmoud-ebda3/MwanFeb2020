package com.ebda3.mwan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ebda3.adapters.MyPropertiesItemsListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by work on 29/10/2017.
 */

public class MyPropertiesItems extends AppCompatActivity {

    public ProgressDialog progressDialog;

    ListView listView;

    public static GridView cart_product;
    SharedPreferences sp;
    public Button apply_buy_bu ;
    public LinearLayout Totals ;
    public String IntentID,IntentName,IntentItems,IntentTotal,IntentshippingCost,IntentNet;
    public static  TextView total_price,shiping_price,net_price ;
    TextView no_data;
    Context context = this;
    Activity activity = this;

    public ProgressBar buy_progress;
    public LayoutInflater inflater;
    public View dialoglayout ;
    AlertDialog.Builder builder;

    private ArrayList<String> ProductName = new ArrayList<String>();
    private ArrayList<String> ProductID = new ArrayList<String>();
    private ArrayList<String> ProductCount = new  ArrayList<String>();
    private ArrayList<String> TotalPrice = new  ArrayList<String>();
    private ArrayList<String> SubItems = new  ArrayList<String>();
    private ArrayList<String> ProductPrice = new  ArrayList<String>();

    public  ArrayList<String> ItemPartnerName = new  ArrayList<String>() ;
    public  ArrayList<Integer> ItemAvailableAmount  = new  ArrayList<Integer>()  ;
    public  ArrayList<Integer> ItemPartnerID  = new  ArrayList<Integer>()  ;

    ArrayList<String> arrayList = new  ArrayList<String>();

    public Toolbar toolbar;
    public TextView headline;
    public ImageView shopping_cart_image;
    public MyPropertiesItemsListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_properties_items);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);

        cart_product = (GridView)findViewById(R.id.grid_cart_products);
        Totals = (LinearLayout) findViewById(R.id.Totals);

        total_price = (TextView) findViewById(R.id.total_price);
        shiping_price = (TextView) findViewById(R.id.shiping_price);
        net_price = (TextView) findViewById(R.id.net_price);
        no_data = (TextView) findViewById(R.id.no_data);
        listView = (ListView) findViewById(R.id.list);





        IntentID = getIntent().getStringExtra("ID");
        IntentName = getIntent().getStringExtra("Name");
        IntentItems = getIntent().getStringExtra("Items");
        IntentTotal = getIntent().getStringExtra("Total");
        IntentshippingCost = getIntent().getStringExtra("shippingCost");
        IntentNet = getIntent().getStringExtra("Net");



        total_price.setText(IntentTotal + " جنية" );
        shiping_price.setText(IntentshippingCost + " جنية" );
        net_price.setText(IntentNet + " جنية" );

        headline.setText( IntentName );

        LinearLayout orderDetails = (LinearLayout) findViewById(R.id.orderDetails);

        try {
            JSONArray jsonArray = new JSONArray(IntentItems);
            if ( jsonArray.length() > 0 ) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject row = jsonArray.getJSONObject(i);
                    ProductName.add(row.getString("ItemName"));
                    ProductPrice.add(row.getString("Prices"));
                    ProductCount.add(row.getString("Amounts"));
                    TotalPrice.add(row.getString("totalPrice"));
                    SubItems.add(row.getString("SubItems"));

                }
                adapter = new MyPropertiesItemsListAdapter(activity, ProductName , ProductPrice , ProductCount , TotalPrice   );
                listView.setAdapter(adapter);
                orderDetails.setVisibility(View.VISIBLE);
            }
            else
            {
                orderDetails.setVisibility(View.GONE);
            }

        }catch (Exception e)
        {

        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                Intent intent = new Intent( context , MyPropertiesSubItems.class );
                intent.putExtra("Name",ProductName.get(position));
                intent.putExtra("SubItems",SubItems.get(position));
                startActivity(intent);

            }
        });




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
