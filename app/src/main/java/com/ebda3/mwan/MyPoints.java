package com.ebda3.mwan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Helpers.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyPoints extends NavigationViewActivity {

    Activity activity = this;

    public Toolbar toolbar;
    public TextView headline;
    public String id;
    FrameLayout frameLayout;

    Context context = this;
    String my_points = "";

    ProgressBar loadProgress;

    TextView points_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_my_points, null, false);

        toolbar = (Toolbar) drawer.findViewById(R.id.app_toolbar);
        frameLayout = drawer.findViewById(R.id.frame_layout);
        frameLayout.addView(contentView);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);
        headline.setText("رصيد النقاط");

        points_txt = (TextView) contentView.findViewById(R.id.points_id);
        GetProfile();


    }


    public void GetProfile() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.webServiceURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("2342324342342343", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.has("ID")) {
                        Log.d("xxxxxx", response);

                        my_points = jObj.getString("points");
                        points_txt.setText(my_points);
                    }

                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("do", "MyInfo");
                params.put("json_email", Config.getJsonEmail(context));
                params.put("json_password", Config.getJsonPassword(context));
                Log.d("xxxxxx", params.toString());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(activity);
        queue.add(stringRequest);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.KEYCODE_BACK){
            Intent myIntent = new Intent(MyPoints.this, UserHomeActivity.class);
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
