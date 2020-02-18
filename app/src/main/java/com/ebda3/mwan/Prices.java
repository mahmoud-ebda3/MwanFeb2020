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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.adapters.GovernorateListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ebda3.mwan.UserHomeActivity.fixEncoding;


/**
 * Created by work on 23/10/2017.
 */

public class Prices extends NavigationViewActivity {


    Activity activity = this;

    public Toolbar toolbar;
    public TextView headline;
    public String id;


    GovernorateListAdapter adapter;

    ImageView Join, search;
    LinearLayout workers, workers_section;
    ListView listView;
    public TextView no_data;
    Typeface typeface;

    public int StartFrom = 0;
    public int LastStartFrom = 0;
    public int VolleyCurrentConnection = 0;
    public int LimitBerRequest = 50;

    View footerView;

    public ArrayList<String> ID = new ArrayList<String>();
    public ArrayList<String> Name = new ArrayList<String>();


    public Boolean setAdapterStatus = false;
    FrameLayout frameLayout;
    public Activity context = this;
    ProgressBar loadProgress;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_prices, null, false);

        toolbar = (Toolbar) drawer.findViewById(R.id.app_toolbar);
        frameLayout = drawer.findViewById(R.id.frame_layout);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);

        headline.setText("بورصة الاسعار  ");

        swipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });


        listView = (ListView) contentView.findViewById(R.id.List);
        loadProgress = (ProgressBar) contentView.findViewById(R.id.loadProgress);
        footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_footer, null, false);
        no_data = (TextView) contentView.findViewById(R.id.no_data);
        listView.setVisibility(View.VISIBLE);
        loadData();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view,
                                 int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                //Algorithm to check if the last item is visible or not
                final int lastItem = firstVisibleItem + visibleItemCount;
                Log.d("lastItem", String.valueOf(visibleItemCount));
                if (lastItem == totalItemCount) {
                    // loadData();
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listView.getLastVisiblePosition() - listView.getHeaderViewsCount() -
                        listView.getFooterViewsCount()) >= (adapter.getCount() - 3)) {

                    loadData();
                }
            }
        });


    }

    public void refreshData() {
        if (setAdapterStatus) {

            ID.clear();
            Name.clear();

        }
        VolleyCurrentConnection = 0;
        StartFrom = 0;
        loadData();
    }


    public void loadData() {

        Log.d("loadData", "loadData");
        if (VolleyCurrentConnection == 0) {
            VolleyCurrentConnection = 1;
            String VolleyUrl = "https://www.mawaneg.com/supplier/governorate-edit-1.html?json=true&ajax_page=true" + "&start=" + String.valueOf(StartFrom) + "&end=" + String.valueOf(LimitBerRequest);
            Log.d("responser", String.valueOf(VolleyUrl));
            listView.addFooterView(footerView);
            try {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, VolleyUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        response = fixEncoding(response);
                        //Log.d("response", response);
                        listView.removeFooterView(footerView);
                        loadProgress.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);


                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {

                                VolleyCurrentConnection = 0;
                                StartFrom += LimitBerRequest;
                                LastStartFrom = StartFrom;
                                for (int i = 0; i < array.length(); i++) {


                                    JSONObject row = array.getJSONObject(i);
                                    ID.add(row.getString("ID").toString());
                                    Name.add(row.getString("name").toString());


                                }


                                if (!setAdapterStatus) {
                                    adapter = new GovernorateListAdapter(context, Name, ID);
                                    listView.setAdapter(adapter);
                                    setAdapterStatus = true;
                                } else {
                                    adapter.notifyDataSetChanged();
                                }


                            }


                        } catch (JSONException e) {
                            Log.d("ffffff", e.getMessage());
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
                                swipeRefreshLayout.setRefreshing(false);
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


                        paramas.put("dos", "mytrips");
                        return paramas;
                    }
                };

                int socketTimeout = 10000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                RequestQueue queue = Volley.newRequestQueue(this);

                queue.add(stringRequest);
            } catch (Exception e) {
                swipeRefreshLayout.setRefreshing(false);
                //Log.d("ffffff",e.getMessage());
                VolleyCurrentConnection = 0;
                loadData();
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent myIntent = new Intent(Prices.this, UserHomeActivity.class);
            startActivity(myIntent);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
