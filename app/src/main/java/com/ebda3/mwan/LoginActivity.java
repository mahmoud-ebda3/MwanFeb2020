package com.ebda3.mwan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ebda3.Helpers.Config.LoginUrl;


public class LoginActivity extends AppCompatActivity {

    public EditText emailtext, passtext;
    public TextView create_new_user, create_new_supplier;
    public Button logbutton;
    public String myresponse, myemail, mypassword, myid, name, email, phone, u_type, photo;
    Activity activity = this;
    InputMethodManager imm;
    public String u_email, u_password;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        emailtext = (EditText) findViewById(R.id.log_supp_email);
        passtext = (EditText) findViewById(R.id.log_supp_pass);
        logbutton = (Button) findViewById(R.id.login_button);
        create_new_supplier = (TextView) findViewById(R.id.new_supplier_account);
        create_new_user = (TextView) findViewById(R.id.new_user_account);

        progressDialog = new ProgressDialog(activity,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("جارى التحميل...");

        create_new_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
        create_new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, RegisterUserActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        emailtext.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                emailtext.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                emailtext.setHint("01xxxxxxxxx");
                imm.showSoftInput(emailtext, InputMethodManager.SHOW_FORCED);
            } else {
                emailtext.setHint("");
            }
        });

        logbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(emailtext.getWindowToken(), 0);
                u_email = emailtext.getText().toString();
                u_password = passtext.getText().toString();
                checklogin(u_email, u_password);
            }
        });


    }


    public void checklogin(final String user_email, final String user_password) {
        boolean t = isNetworkConnected();
        if (t) {
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, LoginUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("resss", response);
                            JSONObject jObj;
                            try {
                                jObj = new JSONObject(response);
                                if (jObj.has("ID")) {
                                    name = jObj.getString("username");
                                    email = jObj.getString("email");
                                    phone = jObj.getString("phone");
                                    photo = jObj.getString("photo");
                                    myemail = jObj.getString("email");
                                    mypassword = jObj.getString("password");
                                    u_type = jObj.getString("type");
                                    myid = jObj.getString("ID");
                                    savesharedprefernce();
                                } else {
                                    Toast.makeText(LoginActivity.this, "من فضلك تاكد من البيانات !", Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();

                            } catch (JSONException e) {
                                Log.d("resss", "sssssss");
                                progressDialog.dismiss();
                                e.printStackTrace();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("resss", "ssssss222");
                            progressDialog.dismiss();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("do", "log");
                    map.put("username", "002" + user_email);
                    map.put("password", user_password);
                    return map;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(LoginActivity.this, "من فضلك تأكد من اتصالك بالإنترنت", Toast.LENGTH_LONG).show();
        }

    }

    public void savesharedprefernce() {
        SharedPreferences sp = getSharedPreferences("Login", 0);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("ID", myid);
        Ed.putString("username", name);
        Ed.putString("email", phone);
        Ed.putString("photo", photo);
        Ed.putString("phone", phone);
        Ed.putString("normal_password", passtext.getText().toString());
        Ed.putString("password", mypassword);
        Ed.putString("type", u_type);
        Ed.commit();
        openProfile();
    }

    private void openProfile() {
        progressDialog.dismiss();
        //session.setlogin(true);
            Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
            startActivity(intent);
            finish();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
