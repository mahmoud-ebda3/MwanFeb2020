package com.ebda3.mwan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.ebda3.Helpers.Config.SignUp;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    public EditText corporation_name, corporation_tel, owner_name, owner_tel, address, user_name, password, password_confirmation, email_edit_text;
    public TextView forward_page;
    public ProgressBar register_progress;
    public static String reg_id;
    public String myresponse, myemail, mypassword, myid, name, email, phone, u_type, my_normal_pass, emailAddress;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        corporation_name = findViewById(R.id.corporation_name);
        corporation_tel = findViewById(R.id.corporation_telephone);
        owner_name = findViewById(R.id.owner_name);
        forward_page = findViewById(R.id.register_button);
        owner_tel = findViewById(R.id.owner_telephone);
        email_edit_text = findViewById(R.id.email_edit_text);
        address = findViewById(R.id.mawan_address);
        register_progress = findViewById(R.id.registration_progress_bar);
        user_name = findViewById(R.id.mawan_user_name);
        password = findViewById(R.id.password_edit_text);
        password_confirmation = findViewById(R.id.password_confirmation_edit_text);
        email_edit_text = findViewById(R.id.email_edit_text);

        forward_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String corporationName = corporation_name.getText().toString();
                String corporationTele = corporation_tel.getText().toString();
                String ownerName = owner_name.getText().toString();
                String ownerTele = owner_tel.getText().toString();
                String mawanAddress = address.getText().toString();
                String mawanUserName = user_name.getText().toString();
                String userPassword = password.getText().toString();
                emailAddress = email_edit_text.getText().toString();
                String userPasswordConfirmation = password_confirmation.getText().toString();
                if (corporationName.length() < 4) {
                    corporation_name.setError("من فضلك أدخل الأسم بطريقة صحيحة");
                } else if (ownerName.length() < 4) {
                    owner_name.setError("اكتب اسم المالك صحيحاً");
                } else if (userPassword.length() < 6) {
                    password.setError("كلمة السر من 6 إلي 20 حرف");
                } else if (userPasswordConfirmation.length() < 6 || !userPasswordConfirmation.equals(userPassword)) {
                    password_confirmation.setError("كلمة السر غير متطابقة");
                } else if (mawanAddress.equals("")) {
                    address.setError("من فضلك أدخل العنوان");
                } else if (mawanUserName.length() < 4) {
                    user_name.setError("من فضلك أدخل اسم المستخدم بطريقة صحيحة");
                } else if (!(corporationTele.length() == 11)) {
                    corporation_tel.setError("أدخل الرقم بطريقة صحيحة");
                } else if (!(ownerTele.length() == 11)) {
                    owner_tel.setError("أدخل ارقم بطريقة صحيحة");
                } else {
                    boolean t = isNetworkConnected();
                    if (t) {
                        password_confirmation.setEnabled(false);
                        password.setEnabled(false);
                        address.setEnabled(false);
                        corporation_name.setEnabled(false);
                        corporation_tel.setEnabled(false);
                        owner_name.setEnabled(false);
                        owner_tel.setEnabled(false);
                        user_name.setEnabled(false);
                        register_owner();
                    } else {
                        Toast.makeText(MainActivity.this, "من فضلك تأكد من اتصاللك بالإنترنت", Toast.LENGTH_LONG).show();
                    }
                    //sendData(Name,UserName, UserEmail, UserPhone, UserPassword);
                }
            }
        });
    }


    public void register_owner() {
        forward_page.setVisibility(View.GONE);
        register_progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SignUp,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        register_progress.setVisibility(View.GONE);
                        forward_page.setVisibility(View.VISIBLE);
                        Log.d("responseee", response.toString());
                        response = fixEncoding(response);


                        JSONObject jObj;
                        try {
                            jObj = new JSONObject(response);
                            if (jObj.has("ID")) {
                                name = corporation_name.getText().toString();
                                email = jObj.getString("username");
                                phone = jObj.getString("phone");
//                                myemail = jObj.getString("email");
                                my_normal_pass = password.getText().toString();
                                mypassword = jObj.getString("password");
                                u_type = jObj.getString("type");
                                myid = jObj.getString("ID");
                                savesharedprefernce();

                            } else {
                                Toast.makeText(MainActivity.this, "من فضلك تاكد من البيانات !", Toast.LENGTH_LONG).show();
                            }
                            //progressDialog.dismiss();
                        } catch (JSONException e) {
                            register_progress.setVisibility(View.GONE);
                            forward_page.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                            Log.d("responseee", String.valueOf(e));
//
                            JSONArray array = null;
                            try {
                                array = new JSONArray(response);
                                if (array.getString(0).equals("Field password  From 6 To 20")) {
                                    password.setEnabled(true);
                                    password.setError("كلمة المرور من 6 الى 20 حرف");
                                    password.setFocusable(true);
                                } else if (array.getString(0).equals("Field phone  Already exist")) {
                                    corporation_tel.setEnabled(true);
                                    corporation_tel.setError("هذا الرقم موجود مسبقا");
                                    corporation_tel.setText("");
                                    corporation_tel.setFocusable(true);
                                } else if (array.getString(0).equals("Field username  Already exist")) {
                                    user_name.setEnabled(true);
                                    user_name.setError("اسم المستخدم مسبقا");
                                    user_name.setText("");
                                    owner_tel.setFocusable(true);
                                }
                                password_confirmation.setEnabled(true);
                                password.setEnabled(true);
                                address.setEnabled(true);
                                corporation_name.setEnabled(true);
                                corporation_tel.setEnabled(true);
                                owner_name.setEnabled(true);
                                owner_tel.setEnabled(true);
                                user_name.setEnabled(true);
                            } catch (JSONException e1) {
                                register_progress.setVisibility(View.GONE);
                                forward_page.setVisibility(View.VISIBLE);
                                e1.printStackTrace();
                            }
                        }
                        //openProfile();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        register_progress.setVisibility(View.GONE);
                        forward_page.setVisibility(View.VISIBLE);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("do", "insert");
                map.put("name", corporation_name.getText().toString());
                map.put("phone", corporation_tel.getText().toString());
                map.put("supplier_name", owner_name.getText().toString());
                map.put("supplier_phone", owner_tel.getText().toString());
                map.put("address", address.getText().toString());
                map.put("username", user_name.getText().toString());
                map.put("password", password.getText().toString());
                map.put("email", email_edit_text.getText().toString());
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
        String emailAddress = email_edit_text.getText().toString();
        SharedPreferences sp = getSharedPreferences("Login", 0);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("ID", myid);
        Ed.putString("username", phone);
        if (!emailAddress.isEmpty()) {
            Ed.putString("email", emailAddress);
            Log.e("email_addresssssss", emailAddress);
        }
        Ed.putString("phone", phone);
        Ed.putString("normal_password", my_normal_pass);
        Ed.putString("password", mypassword);
        Ed.putString("type", u_type);
        Ed.commit();
        openProfile();
    }

    public void openProfile() {
        Intent myIntent = new Intent(MainActivity.this, OrganizationRegisterationActivity.class);
        startActivity(myIntent);
        finish();
    }


    public boolean isNetworkConnected() {
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(context, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
