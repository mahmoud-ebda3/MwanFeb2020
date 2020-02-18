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
import com.ebda3.adapters.materialsListAdapter;

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

public class MarketMaterial extends AppCompatActivity {



    Activity activity = this;

    public Toolbar toolbar;
    public TextView headline;
    public String id;


    materialsListAdapter adapter ;
    GridView listView;
    public TextView no_data;
    Typeface typeface;

    public int StartFrom = 0;
    public int LastStartFrom = 0;
    public int VolleyCurrentConnection = 0;
    public int LimitBerRequest = 5;

    View footerView;

    public  Activity context = this;
    public  ArrayList<String> MaterialsName = new  ArrayList<String>() ;
    public  ArrayList<String> MaterialsPhoto  = new  ArrayList<String>()  ;
    public  ArrayList<String> MaterialsID  = new  ArrayList<String>()  ;
    public Boolean setAdapterStatus = false;
    ProgressBar loadProgress;

    public String GID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_material);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);

        id = getIntent().getStringExtra("id" );

        if ( id.equals("121") )
        {
            headline.setText("الانشاءات  ");
        }
        else if ( id.equals("122") )
        {
            headline.setText("التشطيبات ");
        }


        if ( getIntent().hasExtra("GID") )
        {
            GID = getIntent().getStringExtra("GID");
        }
        else
        {
            Intent intent = new Intent(context,Prices.class);
            startActivity(intent);
        }


        listView = (GridView)findViewById(R.id.offersList);
        loadProgress = (ProgressBar) findViewById(R.id.loadProgress);
        footerView = ((LayoutInflater)   getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_footer, null, false);
        no_data = (TextView) findViewById(R.id.no_data);
        listView.setVisibility(View.VISIBLE);
        loadData();

        listView.setOnScrollListener(new GridView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount) {

                    loadData();

                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                Intent intent = new Intent( MarketMaterial.this , MarketItemsActivity.class );

                intent.putExtra("ID",MaterialsID.get(position));
                intent.putExtra("Name",MaterialsName.get(position));
                intent.putExtra("GID", GID );
                intent.putExtra("Photo",MaterialsPhoto.get(position));
                startActivity(intent);

            }
        });




    }



    public void loadData() {

        Log.d("loadData","loadData");
        if (VolleyCurrentConnection == 0) {
            VolleyCurrentConnection = 1;
            String VolleyUrl = "https://www.mawaneg.com/supplier/materials-edit-1.html?json=true&ajax_page=true&cats="+ id + "&start=" + String.valueOf(StartFrom) + "&end=" + String.valueOf(LimitBerRequest);
            Log.d("responser", String.valueOf(VolleyUrl));
            //listView.addFooterView(footerView);
            try
            {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, VolleyUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        response = fixEncoding (response);
                        Log.d("response", response);
                        //listView.removeFooterView(footerView);
                        loadProgress.setVisibility(View.GONE);


                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {

                                VolleyCurrentConnection = 0;
                                StartFrom += LimitBerRequest;
                                LastStartFrom = StartFrom;
                                for (int i = 0; i < array.length(); i++) {


                                    JSONObject row = array.getJSONObject(i);

                                    MaterialsName.add(row.getString("name").toString());
                                    MaterialsPhoto.add(row.getString("photo").toString());
                                    MaterialsID.add(row.getString("ID").toString());



                                }



                                if (!setAdapterStatus) {
                                    adapter = new materialsListAdapter(context, MaterialsName, MaterialsPhoto , MaterialsID );
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
                                //listView.removeFooterView(footerView);
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


                        paramas.put("dos", "mytrips");
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
