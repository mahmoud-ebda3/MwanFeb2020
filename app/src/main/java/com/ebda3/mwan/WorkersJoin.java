package com.ebda3.mwan;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Helpers.Config;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ebda3.Helpers.Config.WorkersCatsIDS;
import static com.ebda3.Helpers.Config.WorkersCatsNames;

public class WorkersJoin extends AppCompatActivity {


    public Toolbar toolbar;
    public TextView headline;
    public EditText Name,Address,Contact;

    public String id;


    Context context;
    Spinner spinner_cat;
    LinearLayout progressBar;
    Button Join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_join);
        context = this;


        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);



        Join = (Button) findViewById(R.id.join);
        progressBar = (LinearLayout) findViewById(R.id.progress1);

        spinner_cat = (Spinner) findViewById(R.id.spinner_cat);
        Name = (EditText) findViewById(R.id.full_name_reg);
        Address = (EditText) findViewById(R.id.address);
        Contact = (EditText) findViewById(R.id.contact);


        Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( Name.length() < 4  )
                {
                    Name.setError("من فضلك اكتب الاسم بالكامل");
                }
                else if ( Contact.length() < 10  )
                {
                    Contact.setError("من فضلك اكتب رقم التليفون");
                }
                else if ( Address.length() < 5  )
                {
                    Address.setError("من فضلك اكتب العنوان  ");
                }
                else
                {
                    SendData();

                }
            }
        });

        headline.setText("انضم الينا");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, WorkersCatsNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cat.setAdapter(adapter);

    }

    public void SendData ()
    {
        boolean t = isNetworkConnected();
        if(t) {

            progressBar.setVisibility(View.VISIBLE);
            Join.setVisibility(View.GONE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://adc-company.net/mwan/workers-edit-1.html?json=true&ajax_page=true",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject ( response );
                                if ( jsonObject.has("ID") )
                                {
                                    finish();
                                    Toast.makeText(context, "تم ارسال طلب الانضمام بنجاح وجارى مراجعتة", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    AddAgain();
                                }
                            }
                            catch (Exception e)
                            {
                                AddAgain();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            AddAgain();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();

                    map.put("name", Name.getText().toString() );
                    map.put("address", Address.getText().toString() );
                    map.put("contact", Contact.getText().toString() );
                    map.put("cats", WorkersCatsIDS.get( (int) spinner_cat.getSelectedItemId()  )  );
                    map.put("do", "insert" );
                    map.put("json_email", Config.getJsonEmail(context) );
                    map.put("json_password", Config.getJsonPassword(context));
                    Log.d("params",map.toString());
                    return map;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }
        else
        {
            Toast.makeText(context, "من فضلك تأكد من اتصالك بالإنترنت", Toast.LENGTH_LONG).show();
        }
    }

    public void AddAgain()
    {
        progressBar.setVisibility(View.GONE);
        Join.setVisibility(View.VISIBLE);
    }
    public boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
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
