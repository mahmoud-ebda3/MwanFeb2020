package com.ebda3.mwan;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.ebda3.Helpers.Config.SignUpUser;

/**
 * Created by work on 23/10/2017.
 */

public class RegisterUserActivity extends AppCompatActivity {

    public EditText supplier_name, supplier_password, supplier_phone;
    //EditText supplier_address,supplier_phone,supplier_email , job, company;
    public ImageView forward_page;
    public ProgressBar register_progress;
    public static String reg_id;
    public String myresponse, myemail, mypassword, myid, name, email, phone, u_type, my_normal_pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user_activity);
        supplier_name = (EditText) findViewById(R.id.user_name);
        supplier_password = (EditText) findViewById(R.id.user_password);
        // supplier_address = (EditText)findViewById(R.id.user_address);
        supplier_phone = (EditText) findViewById(R.id.user_phone);
        //supplier_email = (EditText)findViewById(R.id.user_email);
        register_progress = (ProgressBar) findViewById(R.id.register_user_progress);
        forward_page = (ImageView) findViewById(R.id.forward_page_user);
        //job = (EditText) findViewById(R.id.user_job);
        //company = (EditText) findViewById(R.id.user_company);

        supplier_phone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                supplier_phone.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                supplier_phone.setHint("01xxxxxxxxx");
            } else {
                supplier_phone.setHint("");
            }
        });

        forward_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = supplier_name.getText().toString().trim();
                //String Email = supplier_email.getText().toString().trim();
                //String UserAddress = supplier_address.getText().toString().trim();
                String UserPhone = supplier_phone.getText().toString().trim();
                String UserPassword = supplier_password.getText().toString().trim();


                if (Name.length() < 4) {
                    supplier_name.setError("من فضلك أدخل الأسم بطريقة صحيحة");
                }
//                else if ( Email.length() < 4)
//                {
//                    supplier_email.setError("من فضلك اكتب البريد الإلكترونى بطريقة صحيحة");
//                }
                else if (UserPassword.length() < 6) {
                    supplier_password.setError("كلمة المرور من 6 الى 20 حرف");
                } else if (!(UserPhone.length() == 11)) {
                    supplier_phone.setError("من فضلك أدخل رقم الجوال بطريقة صحيحة");
                } else {
                    boolean t = isNetworkConnected();
                    if (t) {
                        register_owner();
                    } else {
                        Toast.makeText(RegisterUserActivity.this, "من فضلك تأكد من اتصاللك بالإنترنت", Toast.LENGTH_LONG).show();
                    }
                    //sendData(Name,UserName, UserEmail, UserPhone, UserPassword);
                }
            }
        });
    }

    public void register_owner() {
        forward_page.setVisibility(View.GONE);
        register_progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SignUpUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        register_progress.setVisibility(View.GONE);
                        forward_page.setVisibility(View.VISIBLE);
                        Log.d("responseee", response);
                        response = fixEncoding(response);
                        // Toast.makeText(MainActivity.this, String.valueOf(response), Toast.LENGTH_LONG).show();

                        JSONObject jObj;
                        try {
                            jObj = new JSONObject(response);
                            if (jObj.has("ID")) {

                                name = supplier_name.getText().toString();
                                email = jObj.getString("email");
                                phone = jObj.getString("phone");
                                myemail = jObj.getString("email");
                                my_normal_pass = supplier_password.getText().toString();
                                mypassword = jObj.getString("password");
                                u_type = jObj.getString("type");
                                myid = jObj.getString("ID");
                                savesharedprefernce();


                            } else {
                                Toast.makeText(RegisterUserActivity.this, "من فضلك تاكد من البيانات !", Toast.LENGTH_LONG).show();
                            }
                            //progressDialog.dismiss();


                        } catch (JSONException e) {

                            register_progress.setVisibility(View.GONE);
                            forward_page.setVisibility(View.VISIBLE);


                            e.printStackTrace();
                            Log.d("responseee", String.valueOf(e));
                            Toast.makeText(RegisterUserActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                            JSONArray array = null;
                            try {
                                array = new JSONArray(response);
                                if (array.getString(0).equals("Field password  From 6 To 20")) {
                                    supplier_password.setError("كلمة المرور من 6 الى 20 حرف");
                                    supplier_password.setFocusable(true);
                                } else if (array.getString(0).equals("Field phone  Already exist")) {
                                    supplier_phone.setError("هذا الرقم موجود مسبقا");
                                    supplier_phone.setText("");
                                    supplier_phone.setFocusable(true);
                                }
//                                else if ( array.getString(0).equals("Please type the e-mail correctly") ) {
//                                    supplier_email.setError("من فضلك اكتب البريد الالكترونى بطريقة صحيحة");
//                                }
//                                else if ( array.getString(0).equals("Field email  Already exist") ) {
//                                    supplier_email.setError("هذا البريد موجود مسبقا");
//                                    supplier_email.setText("");
//                                    supplier_email.setFocusable(true);
//                                }
                                else if (array.getString(0).equals("Field username  Already exist")) {
                                    supplier_name.setError("اسم المستخدم مسبقا");
                                    supplier_name.setText("");
                                    supplier_name.setFocusable(true);
                                }
                            } catch (JSONException e1) {

                                register_progress.setVisibility(View.GONE);
                                forward_page.setVisibility(View.VISIBLE);
                                e1.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        register_progress.setVisibility(View.GONE);
                        forward_page.setVisibility(View.VISIBLE);
                        Toast.makeText(RegisterUserActivity.this, "حدث خطأ فى ارسال البيانات", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("do", "insert");
                map.put("name", supplier_name.getText().toString());
                map.put("password", supplier_password.getText().toString());
                //map.put("address", supplier_address.getText().toString());
                map.put("phone", "002" + supplier_phone.getText().toString());
                map.put("username", "002" + supplier_phone.getText().toString());
                //map.put("username", supplier_email.getText().toString());
                //map.put("job", job.getText().toString());
                //map.put("company", company.getText().toString());
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

    public void savesharedprefernce() {
        SharedPreferences sp = getSharedPreferences("Login", 0);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("ID", myid);
        Ed.putString("username", name);
        Ed.putString("email", phone);
        Ed.putString("phone", phone);
        Ed.putString("normal_password", my_normal_pass);
        Ed.putString("password", mypassword);
        Ed.putString("type", u_type);
        Ed.commit();
        openProfile();
    }

    public void openProfile() {

        Intent myIntent = new Intent(RegisterUserActivity.this, UserHomeActivity.class);
        startActivity(myIntent);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        finish();
    }
}
