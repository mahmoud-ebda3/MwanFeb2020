package com.ebda3.mwan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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


public class SplashActivity extends AppCompatActivity {
    public TextView toolbar_headline;
    Activity activity = this;
    public String u_email, u_password, u_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sp1 = this.getSharedPreferences("Login", 0);
        u_email = sp1.getString("email", " ");
        u_password = sp1.getString("normal_password", " ");
        u_type = sp1.getString("type", " ");

        Log.d("splashcontent", u_email + "   " + u_password);

        checklogin(u_email, u_password);

/*
        Intent intent1 = new Intent(Splash.this,MainActivity.class);
        startActivity(intent1);
*/

    }


    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void checklogin(final String user_email, final String user_password) {
        boolean t = isNetworkConnected();
        if (t) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, LoginUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("responseSplash", response);
                            try {
                                JSONObject jObj = new JSONObject(response);
                                if (jObj.has("ID")) {
                                    SharedPreferences sp = getSharedPreferences("Login", 0);
                                    SharedPreferences.Editor Ed = sp.edit();
                                    Ed.putString("photo", jObj.getString("photo"));
                                    Ed.putString("type", u_type);
                                    Ed.commit();
//                                    if (jObj.getString("type").equals("user")) {
                                    Intent intent = new Intent(SplashActivity.this, UserHomeActivity.class);
                                    startActivity(intent);
//                                    }
//                                    else
//                                    {
//                                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
//                                        startActivity(intent);
//                                    }
                                } else {
                                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }


                            } catch (JSONException e) {

                                e.printStackTrace();
                                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            //openProfile();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("do", "log");
                    map.put("email", user_email);
                    map.put("password", user_password);
                    Log.d("params", map.toString());
                    return map;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {
            showalertnetwork();
        }

    }


    public void showalertnetwork() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setTitle("خطأ الاتصال بالشبكه");
        builder1.setMessage("قم بالاتصال بالانترنت للقيام بعملية الدخول");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "الشبكه",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton(
                "الغاء",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
