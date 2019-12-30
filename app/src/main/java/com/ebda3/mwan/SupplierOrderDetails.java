package com.ebda3.mwan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Helpers.Config;
import com.ebda3.adapters.MyPropertiesItemsListAdapter;
import com.ebda3.adapters.StatusListAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by work on 29/10/2017.
 */

public class SupplierOrderDetails extends AppCompatActivity   implements OnMapReadyCallback {

    public ProgressDialog progressDialog;

    ListView listView , listView1;

    public static GridView cart_product;
    SharedPreferences sp;
    public Button apply_buy_bu ;
    public LinearLayout Totals ;
    public String IntentID,IntentName,IntentItems,IntentTotal,IntentshippingCost,IntentNet , IntentClientName,IntentClientPhone,IntentOrderLocation,IntentStatus,IntentNotes,Intent_delivery_date;
    public static  TextView total_price,shiping_price,net_price , client_name , client_phone , delivery_date , client_notes ;
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
    private ArrayList<String> ProductPrice = new  ArrayList<String>();

    public  ArrayList<String> ItemPartnerName = new  ArrayList<String>() ;
    public  ArrayList<Integer> ItemAvailableAmount  = new  ArrayList<Integer>()  ;
    public  ArrayList<Integer> ItemPartnerID  = new  ArrayList<Integer>()  ;

    ArrayList<String> arrayList = new  ArrayList<String>();

    public Toolbar toolbar;
    public TextView headline;
    public ImageView shopping_cart_image;
    public MyPropertiesItemsListAdapter adapter;
    public StatusListAdapter StatusAdapter;

    RelativeLayout order_status_data;
    LinearLayout order_details , order_status , OrderDetails , listHeader;
    TextView orderDetailsButton;

    public String[] LocationArray  ;
    public  int OpenOrderDetailsStatus = 1;
    Button change_status;
    ProgressBar progressBar;

    public  ArrayList<String> StatusDate = new  ArrayList<String>() ;
    public  ArrayList<String> StatusText = new  ArrayList<String>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_order_details);

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
        listView1 = (ListView) findViewById(R.id.list1);
        change_status = (Button) findViewById(R.id.change_status);
        orderDetailsButton = (TextView) findViewById(R.id.orderDetailsButton);
        OrderDetails = (LinearLayout) findViewById(R.id.OrderDetails);
        listHeader = (LinearLayout) findViewById(R.id.listHeader);
        order_details = (LinearLayout) findViewById(R.id.order_details);
        order_status = (LinearLayout) findViewById(R.id.order_status);
        order_status_data = (RelativeLayout) findViewById(R.id.order_status_data);
        progressBar = (ProgressBar) findViewById(R.id.change_status_progressBar);






        IntentID = getIntent().getStringExtra("ID");

        IntentName = getIntent().getStringExtra("Name");
        IntentClientName = getIntent().getStringExtra("ClientName");
        IntentClientPhone = getIntent().getStringExtra("ClientPhone");
        IntentOrderLocation = getIntent().getStringExtra("OrderLocation");
        IntentStatus = getIntent().getStringExtra("Status");
        IntentNotes = getIntent().getStringExtra("Notes");
        Intent_delivery_date = getIntent().getStringExtra("delivery_date");



        IntentItems = getIntent().getStringExtra("Items");
        IntentTotal = getIntent().getStringExtra("Total");
        IntentshippingCost = getIntent().getStringExtra("shippingCost");
        IntentNet = getIntent().getStringExtra("Net");


        client_name = (TextView) findViewById(R.id.client_name);
        client_phone = (TextView) findViewById(R.id.client_phone);
        delivery_date = (TextView) findViewById(R.id.delivery_date);
        client_notes = (TextView) findViewById(R.id.client_notes);

        client_name.setText(IntentClientName);
        client_phone.setText(IntentClientPhone);
        delivery_date.setText(Intent_delivery_date);
        client_notes.setText(IntentNotes);


        total_price.setText(IntentTotal + " جنية" );
        shiping_price.setText(IntentshippingCost + " جنية" );
        net_price.setText(IntentNet + " جنية" );


        LinearLayout orderDetails = (LinearLayout) findViewById(R.id.orderDetails);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenOrderDetails();
            }
        });


        change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeStatus(1);
            }
        });

        ChangeStatus(0);

        client_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = IntentClientPhone;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        order_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OpenOrderStatus();
            }
        });


        try {
            JSONArray jsonArray = new JSONArray(IntentItems);
            if ( jsonArray.length() > 0 ) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject row = jsonArray.getJSONObject(i);
                    ProductName.add(row.getString("ItemName"));
                    ProductPrice.add(row.getString("Prices"));
                    ProductCount.add(row.getString("Amounts"));
                    TotalPrice.add(row.getString("totalPrice"));
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







    }

    public  void ChangeStatus(final int status)
    {
        change_status.setVisibility(View.GONE);
        boolean t = isNetworkConnected();
        if(t) {

            progressBar.setVisibility(View.VISIBLE);
            change_status.setVisibility(View.GONE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://adc-company.net/mwan/include/webService.php?json=true",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("params",response);
                            progressBar.setVisibility(View.GONE);

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String message = jsonObject.getString("message");
                                String status = jsonObject.getString("status");
                                if ( message.length() > 0 )
                                {
                                    change_status.setText(message);
                                    change_status.setVisibility(View.VISIBLE);
                                }

                                if ( !status.isEmpty() )
                                {
                                    StatusDate.clear();
                                    StatusText.clear();
                                    Log.d("StatusText",status);
                                    JSONArray jsonArray = new JSONArray(status);
                                    for ( int i = 0 ; i < jsonArray.length() ; i++ )
                                    {
                                        JSONObject row = jsonArray.getJSONObject(i);
                                        StatusDate.add(row.getString("add_date").toString());
                                        StatusText.add(row.getString("StatusText").toString());
                                        Log.d("StatusText",row.getString("StatusText").toString());

                                    }

                                    StatusAdapter = new StatusListAdapter(activity,StatusDate,StatusText );
                                    listView1.setAdapter(StatusAdapter);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            AddAgain();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();


                    map.put("do", "OrderStatus" );
                    map.put("OrderID", IntentID );
                    map.put("Change", String.valueOf(status) );
                    map.put("json_email", Config.getJsonEmail(context) );
                    map.put("json_password", Config.getJsonPassword(context));
                    Log.d("params",map.toString());
                    return map;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }
        else
        {
            Toast.makeText(context, "please Enable Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }


    public boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    public void AddAgain()
    {
        progressBar.setVisibility(View.GONE);
        change_status.setVisibility(View.VISIBLE);
    }

    public  void OpenOrderStatus()
    {
        order_status_data.setVisibility(View.VISIBLE);
        OrderDetails.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        listHeader.setVisibility(View.GONE);
    }

    public  void OpenOrderDetails()
    {
        order_status_data.setVisibility(View.GONE);
        if ( OpenOrderDetailsStatus == 1 ) {
            OpenOrderDetailsStatus = 2;
            OrderDetails.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            listHeader.setVisibility(View.GONE);
            orderDetailsButton.setText("الاصناف");
        }
        else {
            OpenOrderDetailsStatus = 1;
            OrderDetails.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            listHeader.setVisibility(View.VISIBLE);
            orderDetailsButton.setText("تفاصيل الطلب");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney;
        sydney = new LatLng(-33.852, 151.211);
        if ( IntentOrderLocation.length() > 2 )
        {
            LocationArray = IntentOrderLocation.split(",");
            sydney = new LatLng( Double.parseDouble(LocationArray[0]) ,  Double.parseDouble(LocationArray[1]) );
        }
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title(IntentClientName));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom( sydney , 12.0f));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q="+IntentOrderLocation+" (" + IntentClientName + ")"));
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
