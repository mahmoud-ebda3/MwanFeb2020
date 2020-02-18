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
import android.widget.RelativeLayout;
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
import com.ebda3.Helpers.Config;
import com.ebda3.adapters.MyOrdersListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ebda3.Helpers.Config.cartData;

public class MyOrders extends NavigationViewActivity {


    Activity activity = this;

    public Toolbar toolbar;
    public TextView headline;
    public String id;


    LinearLayout header;

    MyOrdersListAdapter adapter;

    ListView listView;
    public TextView no_data;
    TextView ratingtv;
    Typeface typeface;

    public int StartFrom = 0;
    public int LastStartFrom = 0;
    public int VolleyCurrentConnection = 0;
    public int LimitBerRequest = 50;

    View footerView;

    public ArrayList<String> ID = new ArrayList<String>();
    public ArrayList<String> Date = new ArrayList<String>();
    public ArrayList<String> Total = new ArrayList<String>();
    public ArrayList<String> shippingCost = new ArrayList<String>();
    public ArrayList<String> Net = new ArrayList<String>();
    public ArrayList<String> Status = new ArrayList<String>();
    public ArrayList<String> Items = new ArrayList<String>();
    public ArrayList<String> SupplierName = new ArrayList<String>();
    public ArrayList<String> SupplierPhone = new ArrayList<String>();
    public ArrayList<String> SupplierPhoto = new ArrayList<String>();
    public ArrayList<String> ItemsArray = new ArrayList<String>();
    ArrayList<String> itemRated = new ArrayList<>();

    FrameLayout frameLayout;
    public Boolean setAdapterStatus = false;

    SwipeRefreshLayout swipeRefreshLayout;

    RelativeLayout notificationContainer;
    TextView notificationNumber;
    public Activity context = this;

    ProgressBar loadProgress;

    @Override
    protected void onResume() {
        super.onResume();
        if (cartData.size() > 0) {
            notificationNumber.setVisibility(View.VISIBLE);
            notificationNumber.setText(String.valueOf(cartData.size()));
        } else {
            notificationNumber.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_my_orders, null, false);
        toolbar = (Toolbar) drawer.findViewById(R.id.app_toolbar);
        frameLayout = drawer.findViewById(R.id.frame_layout);
        frameLayout.addView(contentView);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        notificationNumber = toolbar.findViewById(R.id.notificationNum);
        notificationContainer = toolbar.findViewById(R.id.notification_container);
        headline = (TextView) toolbar.findViewById(R.id.app_headline);

        headline.setText(" مشترياتى   ");

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(context, MyOrdersItems.class);

                intent.putExtra("ID", ID.get(position));
                intent.putExtra("Name", "طلب رقم " + ID.get(position));
                intent.putExtra("Items", Items.get(position));
                intent.putExtra("Total", Total.get(position));
                intent.putExtra("shippingCost", shippingCost.get(position));
                intent.putExtra("Net", Net.get(position));
                intent.putExtra("SupplierName", SupplierName.get(position));
                intent.putExtra("SupplierPhone", SupplierPhone.get(position));
                intent.putExtra("SupplierPhoto", SupplierPhoto.get(position));
                intent.putExtra("Date", Date.get(position));
                intent.putExtra("Status", Status.get(position));
                intent.putExtra("ItemsArray", ItemsArray.get(position));
                intent.putExtra("Rated", itemRated.get(position));
                startActivity(intent);

            }
        });

    }

    public void refreshData() {
        if (setAdapterStatus) {
            ID.clear();
            Date.clear();
            Items.clear();
            Net.clear();
            Total.clear();
            shippingCost.clear();
            Status.clear();
            SupplierName.clear();
            SupplierPhone.clear();
            SupplierPhoto.clear();
            ItemsArray.clear();
        }
        VolleyCurrentConnection = 0;
        StartFrom = 0;
        loadData();
    }


    public void loadData() {

        Log.d("loadData", "loadData");
        if (VolleyCurrentConnection == 0) {
            VolleyCurrentConnection = 1;
            String VolleyUrl = "https://www.mawaneg.com/supplier/include/webService.php?json=true&ajax_page=true" + "&start=" + String.valueOf(StartFrom) + "&end=" + String.valueOf(LimitBerRequest);
            Log.d("responser", String.valueOf(VolleyUrl));
            listView.addFooterView(footerView);
            try {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, VolleyUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ordersresponse", response);
                        //response = fixEncoding (response);
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
                                //header.setVisibility(View.VISIBLE);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject row = array.getJSONObject(i);
                                    ID.add(row.getString("ID").toString());
                                    Date.add(row.getString("add_date").toString());
                                    Items.add(row.getString("Items").toString());
                                    Net.add(row.getString("net").toString());
                                    Total.add(row.getString("total").toString());
                                    shippingCost.add(row.getString("shippingCost").toString());
                                    Status.add(row.getString("status").toString());
                                    itemRated.add(row.getString("rated"));
                                    SupplierName.add(row.getString("supplier_name").toString());
                                    SupplierPhone.add(row.getString("supplier_phone").toString());
                                    SupplierPhoto.add(row.getString("supplier_photo").toString());
                                    ItemsArray.add(row.getString("items").toString());
                                }
                                if (!setAdapterStatus) {
                                    adapter = new MyOrdersListAdapter(context, ID, Date, Net, Status);
                                    listView.setAdapter(adapter);
                                    setAdapterStatus = true;
                                } else {
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            Log.d("ffffff", e.getMessage());
                            swipeRefreshLayout.setRefreshing(false);
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
                        paramas.put("do", "GetOrders");
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent myIntent = new Intent(MyOrders.this, UserHomeActivity.class);
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
