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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.adapters.ItemsListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by work on 23/10/2017.
 */

public class SupplierProductActivity extends AppCompatActivity {



    Activity activity = this;

    public Toolbar toolbar;
    public TextView headline;
    public String id,Name,Location;


    ItemsListAdapter adapter ;
    GridView product_grid;
    public TextView no_data;
    Typeface typeface;

    public int StartFrom = 0;
    public int LastStartFrom = 0;
    public int VolleyCurrentConnection = 0;
    public int LimitBerRequest = 5;

    View footerView;

    public  Activity context = this;
    public  ArrayList<String> ItemName = new  ArrayList<String>() ;
    public  ArrayList<String> ItemPhoto = new  ArrayList<String>() ;
    public  ArrayList<Float> ItemPrice  = new  ArrayList<Float>()  ;
    public  ArrayList<Float> shippingCost  = new  ArrayList<Float>()  ;
    public  ArrayList<Integer> ItemID  = new  ArrayList<Integer>()  ;
    public  ArrayList<String> ItemPartnerName = new  ArrayList<String>() ;
    public  ArrayList<Integer> ItemAvailableAmount  = new  ArrayList<Integer>()  ;
    public  ArrayList<Integer> ItemPartnerID  = new  ArrayList<Integer>()  ;
    public Boolean setAdapterStatus = false;
    ProgressBar loadProgress;

    ImageView shopping_cart_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_activity);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);

        try {
            id = getIntent().getStringExtra("ID" );
        }
        catch (Exception e)
        {
            id = "0";
        }

        Name = getIntent().getStringExtra("Name" );
        headline.setText(Name);

        if ( getIntent().hasExtra("location") )
        {
            Location = getIntent().getStringExtra("location");
        }
        else
        {
            Intent intent = new Intent(context,SetLocation.class);
            intent.putExtra("ID",id);
            intent.putExtra("Name",Name);
            startActivity(intent);
        }




        loadProgress = (ProgressBar) findViewById(R.id.loadProgress);

        footerView = ((LayoutInflater)    getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_footer, null, false);

        no_data = (TextView) findViewById(R.id.no_data);


        product_grid = (GridView)findViewById(R.id.grid_prod);



        loadData();

        product_grid.setOnScrollListener(new AbsListView.OnScrollListener() {


            @Override
            public void onScroll(AbsListView view,
                                 int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                //Algorithm to check if the last item is visible or not
                final int lastItem = firstVisibleItem + visibleItemCount;
                Log.d("lastItem", String.valueOf(visibleItemCount));
                if (lastItem == totalItemCount) {

                    loadData();
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    loadData();
                }
            }
        });

        product_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub





                //Intent intent = new Intent(ProductActivity.this,ProductDetailsActivity.class);
                //startActivity(intent);

            }
        });

    }


    public void loadData() {
        Log.d("loadData", "loadData");
        if (VolleyCurrentConnection == 0) {
            VolleyCurrentConnection = 1;
            String VolleyUrl = "http://adc-company.net/mwan/include/webService.php?json=true&do=getItems&materials="+ id + "&start=" + String.valueOf(StartFrom) + "&end=" + String.valueOf(LimitBerRequest);
            Log.d("responseee", VolleyUrl);
            try {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, VolleyUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadProgress.setVisibility(View.GONE);
                        Log.d("responseee", response);
                        //response = fixEncoding(response);
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {

                                VolleyCurrentConnection = 0;
                                StartFrom += LimitBerRequest;
                                LastStartFrom = StartFrom;
                                for (int i = 0; i < array.length(); i++)
                                {
                                    JSONObject row = array.getJSONObject(i);
                                    ItemID.add(row.getInt("ID"));
                                    ItemName.add(row.getString("name").toString());
                                    ItemPhoto.add(row.getString("photo").toString());
                                    ItemAvailableAmount.add(row.getInt("max_amount"));
                                    ItemPartnerName.add(row.getString("partner_name").toString());
                                    ItemPartnerID.add(row.getInt("partner_id"));
                                    shippingCost.add( Float.parseFloat(row.getString("shippingCost").toString()) );
                                    try {
                                        ItemPrice.add( Float.parseFloat(row.getString("price").toString()) );
                                    }
                                    catch (Exception e) {
                                        ItemPrice.add(0F);
                                    }
                                }

                                if (!setAdapterStatus)
                                {
                                    adapter = new ItemsListAdapter(context,ItemID,ItemName,ItemPhoto,ItemPrice,shippingCost,ItemAvailableAmount,ItemPartnerID,ItemPartnerName);
                                    product_grid.setAdapter(adapter);
                                    setAdapterStatus = true;
                                } else {
                                    adapter.notifyDataSetChanged();
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new CountDownTimer(3000, 1000) {
                            public void onFinish() {
                                VolleyCurrentConnection = 0;
                                loadData();
                            }
                            public void onTick(long millisUntilFinished) {
                                // millisUntilFinished    The amount of time until finished.
                            }
                        }.start();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> paramas = new HashMap();
                        //paramas.put("type", "1");
                        return paramas;
                    }
                };

                int socketTimeout = 10000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(stringRequest);
            } catch (Exception e) {
                VolleyCurrentConnection = 0;
                loadData();
            }
        }
    }

    public static String fixEncoding(String response) {
        try {
            byte[] u = response.toString().getBytes(
                    "ISO-8859-1");
            response = new String(u, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return response;
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
