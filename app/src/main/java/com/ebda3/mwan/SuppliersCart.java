package com.ebda3.mwan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Helpers.Config;
import com.ebda3.adapters.SuppliersCartListAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.ebda3.Helpers.Config.PropertyIDS;
import static com.ebda3.Helpers.Config.PropertyNames;
import static com.ebda3.Helpers.Config.cartData;
import static com.ebda3.mwan.MaterialsDetailsActivity.selectedItems;


/**
 * Created by work on 23/10/2017.
 */

public class SuppliersCart extends AppCompatActivity {



    Activity activity = this;

    public Toolbar toolbar;
    public TextView headline;
    public String id;




    SuppliersCartListAdapter adapter ;

    Button Join;
    ListView listView;
    public TextView no_data;
    Typeface typeface;

    public int StartFrom = 0;
    public int LastStartFrom = 0;
    public int VolleyCurrentConnection = 0;
    public int LimitBerRequest = 50;

    View footerView;

    public  ArrayList<String> ID = new  ArrayList<String>() ;
    public  ArrayList<String> supplier_name = new  ArrayList<String>() ;
    public  ArrayList<String> supplier_photo = new  ArrayList<String>() ;
    public  ArrayList<String> supplier_phone  = new  ArrayList<String>()  ;
    public  ArrayList<String> distance  = new  ArrayList<String>()  ;
    public  ArrayList<String> shippingCost  = new  ArrayList<String>()  ;
    public  ArrayList<String> Net  = new  ArrayList<String>()  ;
    public  ArrayList<String> total  = new  ArrayList<String>()  ;
    public  ArrayList<String> TotalItemsCount  = new  ArrayList<String>()  ;
    public  ArrayList<String> itemsCount  = new  ArrayList<String>()  ;
    public  ArrayList<String> items  = new  ArrayList<String>()  ;




    public Boolean setAdapterStatus = false;

    String my_location;

    String ItemaJson;
    String jsonselectedItems;


    public  Activity context = this;

    ProgressBar loadProgress;

    RelativeLayout shopping_cart_image;
    public static TextView notificationNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppilers_cart);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);
        shopping_cart_image = (RelativeLayout) toolbar.findViewById(R.id.notificationB);
        headline.setText("سلة المشتريات");
        shopping_cart_image.setVisibility(View.VISIBLE);



        my_location = Config.GetUserLocation(context);

        notificationNum = (TextView) findViewById(R.id.notificationNum) ;
        if ( cartData.size() > 0 ) {
            notificationNum.setVisibility(View.VISIBLE);
            notificationNum.setText(String.valueOf(cartData.size()));
        }

        shopping_cart_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, NewCart.class);
                startActivity(myIntent);
            }
        });

        headline.setText("اختيار الموان  ");


        Gson gson = new Gson();
        ItemaJson = gson.toJson(cartData);
        jsonselectedItems = gson.toJson(selectedItems);


        Log.d("json",ItemaJson);

        listView = (ListView)findViewById(R.id.suppliers_List);
        loadProgress = (ProgressBar) findViewById(R.id.loadProgress);
        footerView = ((LayoutInflater)   getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_footer, null, false);
        no_data = (TextView) findViewById(R.id.no_data);
        listView.setVisibility(View.VISIBLE);
        loadData();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view,
                                 int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                //Algorithm to check if the last item is visible or not
                final int lastItem = firstVisibleItem + visibleItemCount;
                Log.d("lastItem",String.valueOf(visibleItemCount));
                if(lastItem == totalItemCount){
                    // loadData();
                }
            }
            @Override
            public void onScrollStateChanged(AbsListView view,int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listView.getLastVisiblePosition() - listView.getHeaderViewsCount() -
                        listView.getFooterViewsCount()) >= (adapter.getCount() - 3)) {

                    loadData();
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(SuppliersCart.this , CartConfirm.class );
                intent.putExtra("ID",ID.get(position));
                intent.putExtra("Items",items.get(position));
                intent.putExtra("supplier_name",supplier_name.get(position));
                intent.putExtra("total",total.get(position));
                intent.putExtra("shippingCost",shippingCost.get(position));
                intent.putExtra("Net",Net.get(position));
                startActivity(intent);

            }
        });


    }




    public void loadData() {

        Log.d("loadData","loadData");
        if (VolleyCurrentConnection == 0) {
            VolleyCurrentConnection = 1;
            String VolleyUrl = "http://adc-company.net/mwan/include/webService.php?json=true&do=getSuppliers&start=" + String.valueOf(StartFrom) + "&end=" + String.valueOf(LimitBerRequest);
            Log.d("responser", String.valueOf(VolleyUrl));
            listView.addFooterView(footerView);
            try
            {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, VolleyUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response12", response);
                        //response = fixEncoding (response);
                        Log.d("Suppliers_response", response);
                        listView.removeFooterView(footerView);
                        loadProgress.setVisibility(View.GONE);


                        try {
                            JSONObject m_array = new JSONObject(response);

                            JSONArray Properties = new JSONArray( m_array.getString("Properties") );
                            if (Properties.length() > 0) {
                                PropertyIDS.clear();
                                PropertyNames.clear();
                                for (int i = 0; i < Properties.length(); i++) {
                                    JSONObject row = Properties.getJSONObject(i);
                                    {
                                        PropertyIDS.add(row.getString("ID").toString());
                                        PropertyNames.add(row.getString("name").toString());
                                    }
                                }
                            }

                            JSONArray array = new JSONArray( m_array.getString("Suppliers") );
                            if (array.length() > 0) {

                                VolleyCurrentConnection = 0;
                                StartFrom += LimitBerRequest;
                                LastStartFrom = StartFrom;
                                for (int i = 0; i < array.length(); i++)
                                {
                                    JSONObject row = array.getJSONObject(i);
                                    ID.add(row.getString("ID").toString());
                                    supplier_name.add(row.getString("supplier_name").toString());
                                    supplier_photo.add(row.getString("supplier_photo").toString());
                                    supplier_phone.add(row.getString("supplier_phone").toString());
                                    distance.add(row.getString("distance").toString());
                                    shippingCost.add(row.getString("shippingCost").toString());
                                    Net.add(row.getString("Net").toString());
                                    total.add(row.getString("total").toString());
                                    TotalItemsCount.add(row.getString("TotalItemsCount").toString());
                                    itemsCount.add(row.getString("itemsCount").toString());
                                    items.add(row.getString("items").toString());
                                }

                                if (!setAdapterStatus) {
                                    adapter = new SuppliersCartListAdapter(context, supplier_name, supplier_photo , supplier_phone , distance , shippingCost  ,total , TotalItemsCount, itemsCount );
                                    listView.setAdapter(adapter);
                                    setAdapterStatus = true;
                                } else {
                                    adapter.notifyDataSetChanged();
                                }


                            }



                        } catch (JSONException e) {
                            Log.d("ffffff",e.getMessage());
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("ffffff",error.getMessage());
                        loadProgress.setVisibility(View.GONE);


                        new CountDownTimer(3000, 1000) {
                            public void onFinish() {
                                VolleyCurrentConnection = 0;
                                listView.removeFooterView(footerView);
                                loadData();
                            }

                            public void onTick(long millisUntilFinished) {
                                // millisUntilFinished    The amount of time until finished.
                            }
                        }.start();

                        //Log.d("ErrorResponse", error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {


                        Map<String, String> paramas = new HashMap();

                        paramas.put("json_email", Config.getJsonEmail(context) );
                        paramas.put("json_password", Config.getJsonPassword(context));

                        paramas.put("Items", ItemaJson );
                        //paramas.put("jsonselectedItems", jsonselectedItems );
                        paramas.put("Location",my_location);
                        Log.d("getmwanparamas",paramas.toString());
                        return paramas;
                    }
                };

                int socketTimeout = 10000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                RequestQueue queue = Volley.newRequestQueue( this );

                queue.add(stringRequest);
            }
            catch (Exception e)
            {
                //Log.d("ffffff",e.getMessage());
                VolleyCurrentConnection = 0;
                loadData();
            }
        }
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
