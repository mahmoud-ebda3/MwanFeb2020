package com.ebda3.mwan;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Helpers.Config;

import java.util.HashMap;
import java.util.Map;

public class AddBidReplay extends AppCompatActivity {


    public EditText Cost , Details;
    public String id,BidID;


    Context context;
    LinearLayout progressBar;
    LinearLayout Add ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bid_replay);

        context = this;

        Add = (LinearLayout) findViewById(R.id.add);

        progressBar = (LinearLayout) findViewById(R.id.progress1);


        Details = (EditText) findViewById(R.id.details);
        Cost = (EditText) findViewById(R.id.cost);


        BidID = getIntent().getStringExtra("BidID");

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( Details.getText().toString().length() < 4  )
                {
                    Details.setError("من فضلك اكتب تفاصيل المناقصة");
                }
                else if ( Cost.getText().toString().isEmpty()  )
                {
                    Cost.setError("من فضلك اكتب السعر ");
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

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://www.mawaneg.com/supplier/include/webService.php?json=true",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Intent intent = new Intent(context , BidOffers.class );
                            intent.putExtra("ID",BidID);
                            intent.putExtra("addReplay",1);
                            intent.putExtra("Bids",response);
                            startActivity(intent);
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
                    map.put("price", Cost.getText().toString() );
                    map.put("BidID", BidID );
                    map.put("do", "InsertBidOffers" );
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
