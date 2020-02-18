package com.ebda3.mwan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.GridView;
import android.widget.ImageView;
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
import com.ebda3.adapters.materialsListAdapter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ebda3.Helpers.Config.cartData;


/**
 * Created by work on 23/10/2017.
 */

public class MaterialActivity extends NavigationViewActivity {


    Activity activity = this;

    public Toolbar toolbar;
    public TextView headline;
    public ImageView back_image;
    public String cat_id;


    materialsListAdapter adapter;
    GridView listView;
    public TextView no_data;
    Typeface typeface;
    public int StartFrom = 0;
    public int LastStartFrom = 0;
    public int VolleyCurrentConnection = 0;
    public int LimitBerRequest = 5;
    String idFromCart;
    View footerView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public Activity context = this;
    public ArrayList<String> MaterialsName = new ArrayList<String>();
    public ArrayList<String> MaterialsPhoto = new ArrayList<String>();
    public ArrayList<String> MaterialsID = new ArrayList<String>();
    ArrayList<String> filter = new ArrayList<>();
    public ArrayList<String> companies = new ArrayList<String>();
    public Boolean setAdapterStatus = false;
    ProgressBar loadProgress;
    RelativeLayout notificationContainer;
    ImageView filtrationIcon;
    TextView notificationNumber;
    FrameLayout frameLayout;
    public String Location;
    SwipeRefreshLayout swipeRefreshLayout;

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
        View contentView = inflater.inflate(R.layout.material_activity, null, false);
        frameLayout = drawer.findViewById(R.id.frame_layout);
        toolbar = drawer.findViewById(R.id.app_toolbar);
        frameLayout.addView(contentView);
        sharedPreferences = getPreferences(0);
        editor = sharedPreferences.edit();
        setSupportActionBar(toolbar);
        headline = toolbar.findViewById(R.id.app_headline);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        notificationContainer = toolbar.findViewById(R.id.notification_container);
        notificationNumber = toolbar.findViewById(R.id.notificationNum);
        notificationContainer.setVisibility(View.VISIBLE);
        notificationContainer.setOnClickListener(click -> {
            Intent i = new Intent(context, NewCart.class);
            startActivity(i);
        });
        swipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });


        try {
            cat_id = getIntent().getStringExtra("id");
            if (cat_id.equals("121")) {
                headline.setText("الانشاءات  ");
            } else if (cat_id.equals("122")) {
                headline.setText("التشطيبات ");
            } else if (cat_id.equals("from_cart")) {
                cat_id = sharedPreferences.getString("cat_id", "");
            }
            Log.e("cat_id", cat_id);
        } catch (Exception e) {

        }


//        if ( getIntent().hasExtra("location") )
//        {
//            Location = getIntent().getStringExtra("location");
//        }
//        else
//        {
//            Intent intent = new Intent(context,SetLocation.class);
//            intent.putExtra("ID",id);
//            startActivity(intent);
//        }


        listView = (GridView) contentView.findViewById(R.id.offersList);
        loadProgress = (ProgressBar) contentView.findViewById(R.id.loadProgress);
        loadProgress.setVisibility(View.GONE);
        footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_footer, null, false);
        no_data = (TextView) contentView.findViewById(R.id.no_data);
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
                editor.putString("cat_id", cat_id);
                editor.apply();
                Intent intent = new Intent(MaterialActivity.this, ItemsActivity.class);
                intent.putExtra("ID", MaterialsID.get(position));
                intent.putExtra("companies", companies.get(position));
                intent.putExtra("Name", MaterialsName.get(position));
                intent.putExtra("location", Location);
                intent.putExtra("Photo", MaterialsPhoto.get(position));
                intent.putExtra("filter", filter.get(position));
                startActivity(intent);
            }
        });


    }

    public void refreshData() {
        if (setAdapterStatus) {
            MaterialsName.clear();
            MaterialsPhoto.clear();
            MaterialsID.clear();
            companies.clear();
        }
        VolleyCurrentConnection = 0;
        StartFrom = 0;
        loadData();
    }


    public void loadData() {

        Log.d("loadData", "loadData");
        if (VolleyCurrentConnection == 0) {
            VolleyCurrentConnection = 1;
            String VolleyUrl = "https://www.mawaneg.com/supplier/materials-edit-1.html?json=true&ajax_page=true&cats=" + cat_id + "&start=" + String.valueOf(StartFrom) + "&end=" + String.valueOf(LimitBerRequest);
            Log.d("responser", String.valueOf(VolleyUrl));
            //listView.addFooterView(footerView);
            try {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, VolleyUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response6666", response);
                        response = fixEncoding(response);
                        Log.d("response10098", response);

                        swipeRefreshLayout.setRefreshing(false);

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
                                    companies.add(row.getString("companies").toString());
                                    filter.add(row.getString("filter"));


                                }


                                if (!setAdapterStatus) {
                                    adapter = new materialsListAdapter(context, MaterialsName, MaterialsPhoto, MaterialsID);
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
                        swipeRefreshLayout.setRefreshing(false);


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
                RequestQueue queue = Volley.newRequestQueue(this);

                queue.add(stringRequest);
            } catch (Exception e) {
                //Log.d("ffffff",e.getMessage());
                VolleyCurrentConnection = 0;
                swipeRefreshLayout.setRefreshing(false);
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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());


                Location = String.valueOf(place.getLatLng().latitude + "," + place.getLatLng().longitude);

                SharedPreferences sp = getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed = sp.edit();
                Ed.putString("Location", Location);
                Ed.commit();


                Log.d("paramas", Location);

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent myIntent = new Intent(MaterialActivity.this, UserHomeActivity.class);
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
