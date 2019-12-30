package com.ebda3.mwan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Helpers.Config;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static com.ebda3.Helpers.Config.PropertyIDS;
import static com.ebda3.Helpers.Config.PropertyNames;
import static com.ebda3.Helpers.Config.cartData;
import static com.ebda3.mwan.MaterialsDetailsActivity.selectedItems;

public class ConfirmOrder extends AppCompatActivity {


    public LinearLayout Linear_text_property , Linear_spinner_property ;
    public EditText property_name  ;
    public boolean AddNewProperty = false;
    TextView add_new_property;

    Spinner Property;


    Activity activity = this;
    Context context = this;
    ProgressBar progress;
    Button submit;
    EditText notes;

    String ID,Items,Net;
    LinearLayout orderData,orderDetails;
    TextView order_number;
    Button my_orders;

    LinearLayout ChooseDate;
    TextView ch_time;
    String jsonselectedItems,ItemaJson;

    private static int mYear, mMonth, mDay, mHour, mMinute;
    private static String Today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        progress = (ProgressBar) findViewById(R.id.progress);
        submit = (Button) findViewById(R.id.submit);
        notes = (EditText) findViewById(R.id.notes);

        my_orders = (Button) findViewById(R.id.my_orders);

        my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, MyOrders.class);
                startActivity(myIntent);
            }
        });

        orderData = (LinearLayout) findViewById(R.id.orderData);
        orderDetails = (LinearLayout) findViewById(R.id.orderDetails);
        order_number = (TextView) findViewById(R.id.order_number);

        ID = getIntent().getStringExtra("ID" );
        Items = getIntent().getStringExtra("Items" );
        Net = getIntent().getStringExtra("Net" );



        ChooseDate = (LinearLayout) findViewById(R.id.choose_date);
        ch_time = (TextView) findViewById(R.id.ch_time);

        Gson gson = new Gson();
        ItemaJson = gson.toJson(cartData);
        jsonselectedItems = gson.toJson(selectedItems);

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


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean t = isNetworkConnected();
                if (t) {
                    String note = notes.getText().toString().trim();


                    if ( AddNewProperty &&  property_name.length() < 1 ) {
                        property_name.setError("من فضلك اكتب اسم العقار");
                    }
                    else
                    {
                        progress.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.GONE);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://adc-company.net/mwan/include/webService.php?json=true", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("mmmmtttt", response);
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    if (jObj.has("ID"))
                                    {
                                        orderDetails.setVisibility(View.VISIBLE);
                                        orderData.setVisibility(View.GONE);
                                        order_number.setText(jObj.getString("ID").toString());
                                        CartConfirm.buy_cart_product.setVisibility(View.GONE);
                                        CartConfirm.BackAction = true;
                                        cartData.clear();


                                    }
                                } catch (JSONException e) {
                                    progress.setVisibility(View.GONE);
                                    submit.setVisibility(View.VISIBLE);
                                    Toast.makeText(activity, "حدث خطا برجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ConfirmOrder.this,NewCart.class);
                                    startActivity(intent);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progress.setVisibility(View.GONE);
                                submit.setVisibility(View.VISIBLE);
                                Toast.makeText(activity, "حدث خطا برجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ConfirmOrder.this,NewCart.class);
                                startActivity(intent);
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("do", "SubmitOrder");
                                params.put("json_email", Config.getJsonEmail(context) );
                                params.put("json_password", Config.getJsonPassword(context) );
                                params.put("partnerID", ID );
                                params.put("Location", Config.GetUserLocation(context) );
                                params.put("items", ItemaJson );
                                params.put("delivery_date", Today );
                                params.put("Net", Net );
                                params.put("Notes", notes.getText().toString() );
                                if ( AddNewProperty )
                                {
                                    params.put("property_name", property_name.getText().toString() );
                                }
                                else if ( PropertyIDS.size() > 0   )
                                {
                                    params.put("property_id", PropertyIDS.get( (int) Property.getSelectedItemId()  ) );
                                }
                                Log.d("paramxxxxxx",params.toString());
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

                }
                else
                {
                    Toast.makeText(context,"please check your internet connection",Toast.LENGTH_LONG).show();
                }
            }
        });



        Linear_spinner_property = (LinearLayout) findViewById(R.id.Linear_spinner_property);
        Linear_text_property = (LinearLayout) findViewById(R.id.Linear_text_property);
        Property = (Spinner) findViewById(R.id.spinner_property);
        add_new_property = (TextView) findViewById(R.id.add_new_property);
        property_name = (EditText) findViewById(R.id.property_name);

        add_new_property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewProperty = true;
                Linear_spinner_property.setVisibility(View.GONE);
                Linear_text_property.setVisibility(View.VISIBLE);
            }
        });

        if ( PropertyNames.size() > 0) {
            AddNewProperty = false;
            Linear_spinner_property.setVisibility(View.VISIBLE);
            Linear_text_property.setVisibility(View.GONE);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, PropertyNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Property.setAdapter(adapter);
        }
        else
        {
            AddNewProperty = true;
            Linear_spinner_property.setVisibility(View.GONE);
            Linear_text_property.setVisibility(View.VISIBLE);
        }
    }

    public boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
