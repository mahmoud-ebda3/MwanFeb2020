package com.ebda3.mwan;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Helpers.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyPoints extends AppCompatActivity {

    Activity activity = this;

    public Toolbar toolbar;
    public TextView headline;
    public String id;

    Context context = this;
    String my_points="";

    ProgressBar loadProgress;

    TextView points_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_points);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);

        headline = (TextView) findViewById(R.id.app_headline);
        headline.setText("رصيد النقاط");

        points_txt = (TextView) findViewById(R.id.points_id);

        GetProfile();




    }


    public void GetProfile()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST , Config.webServiceURL , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("xxxxxx", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.has("ID"))
                    {
                        Log.d("xxxxxx", response);

                        my_points=jObj.getString("points");
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
                params.put("json_email", Config.getJsonEmail(context) );
                params.put("json_password", Config.getJsonPassword(context) );
                Log.d("xxxxxx",params.toString());
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
