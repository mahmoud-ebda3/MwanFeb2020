package com.ebda3.mwan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Helpers.Config;
import com.ebda3.Model.Product;
import com.ebda3.adapters.PriceProductAdapter;
import com.ebda3.design.FontsOverride;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ebda3.Helpers.Config.GetCategory;
import static com.ebda3.Helpers.Config.isOpened;

/**
 * Created by work on 15/10/2017.
 */

public class PriceProductActivity extends AppCompatActivity {
    public Spinner spinner_field,spinner_kind;
    public ListView list_price;
    public LinearLayout trade_linear,tax_linear;
    public Button go_home_page;
    public ArrayList<Product> products =  new ArrayList<>();
    public PriceProductAdapter adapter;
    public LinearLayout progress_step;
    public LayoutInflater inflater;
    public View dialoglayout ;
    AlertDialog.Builder builder;
    public ScrollView scrollprice;
    Activity context = this;
    Activity activity = this;
    TextView link_txt;


    public static String  chosen_type , chosen_cat;

    public ArrayList<String> ProducTypeName = new ArrayList<>();
    public ArrayList<String> ProducTypeID = new ArrayList<>();
    public ArrayList<String> ProductCatID = new ArrayList<>();
    public ArrayList<String> ProductCatName = new ArrayList<>();

    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Cairo-SemiBold.ttf");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.price_product_activity);

        chosen_cat = "";
        spinner_field = (Spinner) findViewById(R.id.spinner_field);
        spinner_kind = (Spinner) findViewById(R.id.spinner_kind);
        list_price = (ListView) findViewById(R.id.list_price);
        go_home_page = (Button) findViewById(R.id.go_home_button);
        link_txt = (TextView) findViewById(R.id.link_id);
        products =  new ArrayList<>();

        progressDialog = new ProgressDialog(activity,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("جارى التحميل...");


        link_txt.setText("http://adc-company.net/mwan/");

        LoadData();

        if(isOpened)
        {
            progress_step.setVisibility(View.GONE);

        }

//        for (int i =0 ; i<10 ; i++)
//        {
//            Product my_product = new Product();
//            my_product.setProductImage("sdas");
//            my_product.setProductName("تجربة "+ String.valueOf(i));
//            my_product.setProductPrice("100 جنية");
//
//            products.add(my_product);
//        }

        adapter = new PriceProductAdapter(PriceProductActivity.this,products);
        list_price.setAdapter(adapter);

        go_home_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(PriceProductActivity.this, UserHomeActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        spinner_field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Toast.makeText(getContext(),SpinCompany.get(position),Toast.LENGTH_SHORT).show();
                chosen_type= ProducTypeID.get(position);

                ProductCatID = new ArrayList<>();
                ProductCatName = new ArrayList<>();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, ProductCatName);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_kind.setAdapter(adapter);


                LoadKinds();



            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        spinner_kind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Toast.makeText(getContext(),SpinCompany.get(position),Toast.LENGTH_SHORT).show();
                chosen_cat= ProductCatID.get(position);
                load_items();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }
    public void LoadData()
    {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GetCategory,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("priceres","hello");
                        Log.d("priceres",response);
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0)
                            {
                                for (int i=0 ; i<jsonArray.length() ; i++)
                                {
                                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                    String id = jsonObject.getString("ID");
                                    String name = jsonObject.getString("name");
                                    ProducTypeID.add(jsonObject.getString("ID"));
                                    ProducTypeName.add( jsonObject.getString("name"));
                                    Log.d("my data",id);
                                }
                                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, ProducTypeName);
                                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_field.setAdapter(adapter2);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("priceres",String.valueOf(error));
                        //Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void LoadKinds()
    {
        products = new ArrayList<>();
        adapter = new PriceProductAdapter(PriceProductActivity.this,products);
        list_price.setAdapter(adapter);


        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://adc-company.net/mwan/include/cats_json.php?type=items&id="+chosen_type,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    Log.d("priceres","hello");
                    Log.d("priceres2",response);
                    //Toast.makeText(PriceProductActivity.this,response,Toast.LENGTH_LONG).show();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0)
                        {
                            for (int i=0 ; i<jsonArray.length() ; i++)
                            {
                                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                String id = jsonObject.getString("ID");
                                String name = jsonObject.getString("name");
                                ProductCatID.add(jsonObject.getString("ID"));
                                ProductCatName.add( jsonObject.getString("name"));
                                Log.d("my data",id);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, ProductCatName);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_kind.setAdapter(adapter);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Log.d("priceres",String.valueOf(error));
                    //Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> map = new HashMap<String, String>();
            return map;
        }
    };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void load_items()
    {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://adc-company.net/mwan/items-edit-1.html?json=true&ajax_page=true&cats="+chosen_cat,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        response = fixEncoding(response);
                        Log.d("items","hello");
                        Log.d("items",response);
                        products = new ArrayList<>();
                        //Toast.makeText(PriceProductActivity.this,response,Toast.LENGTH_LONG).show();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0)
                            {
                                for (int i=0 ; i<jsonArray.length() ; i++)
                                {
                                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                    Product my_product = new Product();
                                    my_product.setProductImage(jsonObject.getString("photo"));
                                    my_product.setProductName(jsonObject.getString("name"));
                                    if (jsonObject.getString("price").equals("null"))
                                    {
                                        my_product.setProductPrice("0" + " جنية");

                                    }
                                    else
                                    {
                                        my_product.setProductPrice(jsonObject.getString("price")+ " جنية");

                                    }
                                    my_product.setProductID(jsonObject.getString("ID"));

                                    products.add(my_product);

                                }
                                adapter = new PriceProductAdapter(PriceProductActivity.this,products);
                                list_price.setAdapter(adapter);
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressDialog.dismiss();
                        Log.d("items",String.valueOf(error));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("json_email", Config.getJsonEmail(context) );
                map.put("json_password", Config.getJsonPassword(context));
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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
    public boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent myIntent = new Intent(PriceProductActivity.this, HomeActivity.class);
            startActivity(myIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
