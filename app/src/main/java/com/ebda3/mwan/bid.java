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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.ebda3.adapters.BidsListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class bid extends NavigationViewActivity {

    Activity activity = this;

    public Toolbar toolbar;
    public TextView headline;
    public String id;

    Context context = this;
    LinearLayout add;
    FrameLayout frameLayout;
    ListView listView;
    public TextView no_data;
    Typeface typeface;

    BidsListAdapter adapter;

    public int StartFrom = 0;
    public int LastStartFrom = 0;
    public int VolleyCurrentConnection = 0;
    public int LimitBerRequest = 10;

    View footerView;

    public ArrayList<String> ID = new ArrayList<String>();
    public ArrayList<String> Details = new ArrayList<String>();
    public ArrayList<String> BidDate = new ArrayList<String>();
    public ArrayList<String> BidsCount = new ArrayList<String>();
    public ArrayList<String> Bids = new ArrayList<String>();


    public Boolean setAdapterStatus = false;
    SwipeRefreshLayout swipeRefreshLayout;

    ProgressBar loadProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_bid, null, false);
        toolbar = drawer.findViewById(R.id.app_toolbar);
        frameLayout = drawer.findViewById(R.id.frame_layout);
        frameLayout.addView(contentView);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);

        swipeRefreshLayout = contentView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });


        headline.setText("المناقصات  ");


        add = (LinearLayout) contentView.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddBid.class);
                startActivity(intent);
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(context, BidOffers.class);
                intent.putExtra("ID", ID.get(position));
                intent.putExtra("Details", Details.get(position));
                intent.putExtra("BidDate", BidDate.get(position));
                intent.putExtra("BidsCount", BidsCount.get(position));
                intent.putExtra("Bids", Bids.get(position));
                startActivity(intent);

            }
        });
    }

    public void refreshData() {
        if (setAdapterStatus) {

            ID.clear();
            Details.clear();
            BidDate.clear();
            BidsCount.clear();
            Bids.clear();

        }
        VolleyCurrentConnection = 0;
        StartFrom = 0;
        loadData();
    }


    public void loadData() {

        Log.d("loadData", "loadData");
        if (VolleyCurrentConnection == 0) {
            VolleyCurrentConnection = 1;
            String VolleyUrl = "https://www.mawaneg.com/supplier/bids-edit-1.html?json=true&ajax_page=true&my_bids=true" + "&start=" + String.valueOf(StartFrom) + "&end=" + String.valueOf(LimitBerRequest);
            Log.d("responser", String.valueOf(VolleyUrl));
            listView.addFooterView(footerView);
            try {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, VolleyUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responser", response);
                        response = fixEncoding(response);
                        Log.d("responser", response);
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
                                    Details.add(row.getString("details").toString());
                                    BidDate.add(row.getString("bid_date").toString());
                                    BidsCount.add(row.getString("bids_count").toString());
                                    Bids.add(row.getString("bids").toString());
                                }


                                if (!setAdapterStatus) {
                                    adapter = new BidsListAdapter(activity, ID, Details, BidDate, BidsCount, Bids);
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
                        paramas.put("json_email", Config.getJsonEmail(context));
                        paramas.put("json_password", Config.getJsonPassword(context));
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
        if (event.getAction() == KeyEvent.KEYCODE_BACK) {
            Intent myIntent = new Intent(bid.this, UserHomeActivity.class);
            startActivity(myIntent);
            return true;
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
