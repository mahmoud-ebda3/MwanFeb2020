package com.ebda3.mwan;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Helpers.Config;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddBid extends AppCompatActivity {


    public EditText Details;
    public String id;


    Context context;
    LinearLayout progressBar;
    LinearLayout Add , ChooseDate;
    TextView ch_time;

    private static int mYear, mMonth, mDay, mHour, mMinute;
    private static String Today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bid);

        context = this;





        Details = (EditText) findViewById(R.id.details);


        Add = (LinearLayout) findViewById(R.id.add);
        progressBar = (LinearLayout) findViewById(R.id.progress1);


        ChooseDate = (LinearLayout) findViewById(R.id.choose_date);
        ch_time = (TextView) findViewById(R.id.ch_time);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        Today = String.valueOf(mYear) + "-" + String.valueOf(mMonth+1) + "-" + String.valueOf(mDay);

        ch_time.setText( Today );

        ChooseDate.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {



                DatePickerDialog dpd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Today = year + "-"    + (monthOfYear + 1) + "-" +  dayOfMonth;
                                ch_time.setText( Today );

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }


        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( Details.getText().toString().length() < 4  )
                {
                    Details.setError("من فضلك اكتب تفاصيل المناقصة");
                }
                else
                {
                    SendData();

                }
            }
        });


    }



    public void SendData ()
    {
        boolean t = isNetworkConnected();
        if(t) {

            progressBar.setVisibility(View.VISIBLE);
            Add.setVisibility(View.GONE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://adc-company.net/mwan/bids-edit-1.html?json=true&ajax_page=true",
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

                    map.put("details", Details.getText().toString() );
                    map.put("bid_date", Today );
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
        Add.setVisibility(View.VISIBLE);
    }
    public boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
