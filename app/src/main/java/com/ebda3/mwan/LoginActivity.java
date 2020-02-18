package com.ebda3.mwan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.ebda3.Helpers.Config.LoginUrl;


public class LoginActivity extends AppCompatActivity {

    public EditText emailtext, passtext;
    public TextView create_new_user, create_new_supplier;
    public Button logbutton;
    public String myresponse, myemail, mypassword, myid, username ,name, email, phone, u_type, photo;
    Activity activity = this;
    InputMethodManager imm;
    TextView googleText;
    private final static int GOOGLE_REQUEST = 2323;
    SignInButton googleSignIn;
    public String u_email, u_password;
    public ProgressDialog progressDialog;
    LoginButton fbLoginButton;
    CallbackManager callBackManager;
    GoogleSignInOptions gso;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        emailtext = (EditText) findViewById(R.id.log_supp_email);
        passtext = (EditText) findViewById(R.id.log_supp_pass);
        logbutton = (Button) findViewById(R.id.login_button);
        googleSignIn = findViewById(R.id.google_sign_in);
        fbLoginButton = findViewById(R.id.facebook_login_button);
        create_new_supplier = (TextView) findViewById(R.id.new_supplier_account);
        create_new_user = (TextView) findViewById(R.id.new_user_account);
        progressDialog = new ProgressDialog(activity, R.style.AppTheme_Dark_Dialog);
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
        fbLoginButton.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        callBackManager = CallbackManager.Factory.create();
        fbLoginButton.setPermissions(Arrays.asList("email", "public_profile"));
        fbLoginButton.registerCallback(callBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
                    Log.e("123132", response.toString());
                    try {
                        String name = object.getString("name");
                        String email = object.getString("email");
                        String id = object.getString("id");
                        String imageUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                        Log.e("123123", imageUrl.toString());
                        signInFacebookGoogle(id, name, email, "facebook", imageUrl.toString());
                        progressDialog.show();
                    } catch (Exception e) {

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("23123", "cancel");
                progressDialog.dismiss();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("23123", "error: " + error.getMessage());
                progressDialog.dismiss();
            }
        });
        googleText = (TextView) googleSignIn.getChildAt(0);
        googleText.setText("الدخول عن طريق جوجل");
        googleSignIn.setOnClickListener(click -> {
            signIn();
        });
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        create_new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, RegisterUserActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        emailtext.setOnFocusChangeListener((v, hasFocus) ->

        {
            if (hasFocus) {
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


    private void signIn() {
        Intent googleSignInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(googleSignInIntent, GOOGLE_REQUEST);
    }

    public void signInFacebookGoogle(String id_social, String name_social, String email_social, String type_social, String imageUrl) {
        boolean t = isNetworkConnected();
        if (t) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://www.mawaneg.com/supplier/include/webService.php?json=true&do=SignInFacebookGmail",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("1123123132312", response.toString());
                            try {
                                JSONObject object = new JSONObject(response);
                                JSONObject row = object.getJSONObject("Result");
                                SharedPreferences sp = getSharedPreferences("Login", 0);
                                SharedPreferences.Editor Ed = sp.edit();
                                Log.e("sppppppp", sp.getAll().toString());
                                Ed.putString("ID", row.getString("ID"));
                                Ed.putString("username", row.getString("username"));
                                Ed.putString("name", row.getString("name"));
                                Ed.putString("email", row.getString("email"));
                                Ed.putString("photo", "");
                                Ed.putString("phone", row.getString("username"));
                                Ed.putString("normal_password", row.getString("username"));
                                Ed.putString("password", row.getString("password"));
                                Ed.putString("type", u_type);
                                Ed.putString("acc", "2");
                                Ed.apply();
                                openProfile();


                            } catch (Exception e) {

                            }

                        }
                    },
                    error -> {
//                            Log.d("resss", "ssssss222");

                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("do", "SignInFacebookGmail");
                    map.put("provider", type_social);
                    map.put("userID", id_social);
                    map.put("name", name_social);
                    map.put("email", email_social);
                    map.put("photoURL", imageUrl);
                    Log.e("fdffd", map.toString());
                    return map;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(LoginActivity.this, "من فضلك تأكد من اتصالك بالإنترنت", Toast.LENGTH_LONG).show();
        }
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
                                    name = jObj.getString("name");
                                    username = jObj.getString("username");
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
                    map.put("username", user_email);
                    map.put("password", user_password);
                    Log.e("fdffd", map.toString());
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
        Log.e("sppppppp", sp.getAll().toString());
        Ed.putString("ID", myid);
        Ed.putString("username", phone);
        Ed.putString("name", name);
        Ed.putString("email", phone);
        Ed.putString("photo", photo);
        Ed.putString("phone", phone);
        Ed.putString("normal_password", passtext.getText().toString());
        Ed.putString("password", mypassword);
        Ed.putString("type", u_type);
        Ed.putString("acc", "1");
        Ed.apply();
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
        finishAffinity();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callBackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_REQUEST) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    progressDialog.show();
                    if (account.getPhotoUrl() != null) {
                        signInFacebookGoogle(account.getId(), account.getDisplayName(), account.getEmail(), "google", account.getPhotoUrl().toString());
                    } else {
                        signInFacebookGoogle(account.getId(), account.getDisplayName(), account.getEmail(), "google", "");
                    }
                }
            } catch (ApiException api) {

            }
        }
    }
}
