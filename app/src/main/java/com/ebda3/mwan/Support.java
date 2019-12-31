package com.ebda3.mwan;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ebda3.adapters.SupportAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Support extends MainActivity {
    public TextView toolbar_headline;
    public Toolbar toolbar;
    ProgressDialog progressDialog;
    Context context = this;
    Activity activity = this;
    public Boolean setAdapterStatus = false;
    SupportAdapter adapter;
    public TextView d_subject, d_time, d_msg_content, d_msg_contentReplay, d_exit;


    private ArrayList<String> CustomerID = new ArrayList<String>();
    private ArrayList<String> Status = new ArrayList<String>();
    private ArrayList<String> StatusText = new ArrayList<String>();
    private ArrayList<String> Complaint = new ArrayList<String>();
    private ArrayList<String> fininshedComment = new ArrayList<String>();
    private ArrayList<String> AddDate = new ArrayList<String>();
    private ArrayList<String> finished_date = new ArrayList<String>();

    //GridView type_of_mobile;
    public int VolleyCurrentConnection = 0;
    public int LimitBerRequest = 5;
    public int StartFrom = 0;
    public int LastStartFrom = 0;
    ListView customer_messages;
    public String u_email, u_password;

    AlertDialog.Builder builder;
    public LayoutInflater inflater;
    public View dialoglayout;
    public Button btn_problem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        toolbar_headline = (TextView) findViewById(R.id.app_headline);
        toolbar_headline.setText("الدعم الفنى");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        customer_messages = (ListView) findViewById(R.id.msg_list_view);
        btn_problem = (Button) findViewById(R.id.btn_problem);


        btn_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(context);
                final View Fview = LayoutInflater.from(context).inflate(R.layout.report_problem, null);
                adb.setView(Fview);
                final Dialog dialog;
                dialog = adb.create();
                dialog.show();

                final EditText complaint = (EditText) dialog.findViewById(R.id.complaint);
                final Button btn_problem = (Button) dialog.findViewById(R.id.btn_problem);
                final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);


                btn_problem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (complaint.getText().length() > 0) {

                            boolean t = isNetworkConnected();
                            if (t) {
                                progressBar.setVisibility(View.VISIBLE);
                                btn_problem.setVisibility(View.GONE);
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://adc-company.net/mwan/include/webService.php?json=true", new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("Response", "zx");
                                        Log.d("Response", response);
                                        try {
                                            JSONObject jObj = new JSONObject(response);
                                            if (jObj.has("ID")) {
                                                Toast.makeText(activity, "تم ارسال الشكوى بنجاح", Toast.LENGTH_SHORT).show();

                                                dialog.dismiss();
                                                dialog.cancel();

                                                Intent intent = new Intent(getApplicationContext(), Support.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        } catch (JSONException e) {

                                            progressBar.setVisibility(View.GONE);
                                            btn_problem.setVisibility(View.VISIBLE);
                                            Log.d("Not Json", "Not Json");
                                            JSONArray array = null;
                                            Toast.makeText(activity, "حدث خطا فى ارسال البيانات", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressBar.setVisibility(View.GONE);
                                        btn_problem.setVisibility(View.VISIBLE);
                                        complaint.setError("برجاء كتابة المشكلة");

                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("do", "AddComplaint");
                                        params.put("json_email", Config.getJsonEmail(context));
                                        params.put("json_password", Config.getJsonPassword(context));
                                        params.put("complaint", complaint.getText().toString());
                                        Log.d("Response", params.toString());
                                        return params;
                                    }
                                };
                                RequestQueue queue = Volley.newRequestQueue(activity);
                                queue.add(stringRequest);
                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                            } else {
                                Toast.makeText(Support.this, "please check your internet connection", Toast.LENGTH_LONG).show();
                            }


                        } else {
                            complaint.setError("برجاء كتابة المشكلة");
                        }
                    }
                });


            }
        });


//        customer_messages.setOnScrollListener(new AbsListView.OnScrollListener() {
//
//            @Override
//            public void onScroll(AbsListView view,
//                                 int firstVisibleItem, int visibleItemCount,
//                                 int totalItemCount) {
//                //Algorithm to check if the last item is visible or not
//                final int lastItem = firstVisibleItem + visibleItemCount;
//                Log.d("lastItem",String.valueOf(visibleItemCount));
//                if(lastItem == totalItemCount){
//                    loadData();
//
//                }
//            }
//            @Override
//            public void onScrollStateChanged(AbsListView view,int scrollState) {
//                loadData();
//                }
//
//        });


        boolean t = isNetworkConnected();
        if (t) {
            progressDialog = ProgressDialog.show(Support.this, "", "جارى التحميل", true, true);

            loadData();


            customer_messages.setOnScrollListener(new AbsListView.OnScrollListener() {


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
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                            && (customer_messages.getLastVisiblePosition() - customer_messages.getHeaderViewsCount() -
                            customer_messages.getFooterViewsCount()) >= (adapter.getCount() - 100)) {
                        loadData();
                    }
                }
            });

            customer_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    String Slecteditem = CustomerID.get(position);
                    inflater = getLayoutInflater();
                    dialoglayout = inflater.inflate(R.layout.custom_dialog_support, null);
                    builder = new AlertDialog.Builder(Support.this);
                    builder.setView(dialoglayout);

                    final AlertDialog ad = builder.show();

                    d_exit = (TextView) dialoglayout.findViewById(R.id.dialog_msg_exit);
                    d_msg_content = (TextView) dialoglayout.findViewById(R.id.dialog_msg_content);
                    d_msg_contentReplay = (TextView) dialoglayout.findViewById(R.id.dialog_msg_repaly);
                    d_time = (TextView) dialoglayout.findViewById(R.id.dialog_msg_time);

                    d_time.setText(AddDate.get(position));
                    d_msg_content.setText(Complaint.get(position));
                    d_msg_contentReplay.setText(fininshedComment.get(position));


                    d_exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ad.dismiss();
                        }
                    });
                }
            });
        } else {
            Toast.makeText(Support.this, "please check your internet connection", Toast.LENGTH_LONG).show();
        }


    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public void loadData() {
        Log.d("loadData", "loadData");
        boolean t = isNetworkConnected();
        if (t) {
            if (VolleyCurrentConnection == 0) {
                VolleyCurrentConnection = 1;
                //"http://falcon-egy.com/module-driver_message?ajax_page_l=yes&json=true"
                String VolleyUrl = "http://adc-company.net/mwan/include/webService.php?json=true" + "&start=" + String.valueOf(StartFrom) + "&end=" + String.valueOf(LimitBerRequest);
                Log.d("myurlll", VolleyUrl);
                try {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, VolleyUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            Log.d("response", "xcxcx");
                            Log.d("response", response);
                            //response = fixEncoding(response);
                            //Toast.makeText(Messages.this,response,Toast.LENGTH_LONG).show();
                            try {
                                JSONArray array = new JSONArray(response);
                                if (array.length() > 0) {
                                    VolleyCurrentConnection = 0;
                                    StartFrom += LimitBerRequest;
                                    LastStartFrom = StartFrom;
                                    for (int i = 0; i < array.length(); i++) {

                                        JSONObject row = array.getJSONObject(i);
                                        CustomerID.add(row.getString("ID").toString());
                                        Status.add(row.getString("status").toString());
                                        StatusText.add(row.getString("StatusText").toString());
                                        AddDate.add(row.getString("add_date").toString());
                                        Complaint.add(row.getString("complaint").toString());
                                        fininshedComment.add(row.getString("fininshedComment").toString());
                                        finished_date.add(row.getString("finished_date").toString());

                                    }

                                    if (!setAdapterStatus) {
                                        adapter = new SupportAdapter(activity, Status, StatusText, AddDate, Complaint, fininshedComment);
                                        customer_messages.setAdapter(adapter);
                                        setAdapterStatus = true;
                                    } else {
                                        adapter.notifyDataSetChanged();
                                    }

                                }
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            new CountDownTimer(3000, 1000) {
                                public void onFinish() {
                                    progressDialog.dismiss();
                                    VolleyCurrentConnection = 0;
                                    loadData();
                                }

                                public void onTick(long millisUntilFinished) {
                                    progressDialog.dismiss();
                                    // millisUntilFinished    The amount of time until finished.
                                }
                            }.start();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> paramas = new HashMap();
                            paramas.put("do", "getComplaint");
                            paramas.put("json_email", Config.getJsonEmail(context));
                            paramas.put("json_password", Config.getJsonPassword(context));
                            return paramas;
                        }
                    };

                    int socketTimeout = 10000;//30 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    stringRequest.setRetryPolicy(policy);
                    RequestQueue queue = Volley.newRequestQueue(context);
                    queue.add(stringRequest);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    VolleyCurrentConnection = 0;
                    Toast.makeText(Support.this, e.toString(), Toast.LENGTH_LONG).show();
                    loadData();
                }
            }
        } else {
            Toast.makeText(Support.this, "please check your internet connection", Toast.LENGTH_LONG).show();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(context, UserHomeActivity.class);
        startActivity(i);
        finish();
    }
}
