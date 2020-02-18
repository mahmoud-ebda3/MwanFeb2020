package com.ebda3.mwan;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.ebda3.Helpers.Config.SignUpUser;

/**
 * Created by work on 23/10/2017.
 */

public class RegisterUserActivity extends AppCompatActivity {

    Context context = this;
    Activity activity = this;
    public EditText supplier_name, supplier_password, supplier_phone, email_address, confirm_user_password;
    //EditText supplier_address,supplier_phone,supplier_email , job, company;
    public ImageView forward_page;
    InputMethodManager imm;
    public ProgressBar register_progress;
    public static String reg_id;
    public String myresponse, myemail, mypassword, myid, name, email, phone, u_type, my_normal_pass;
    private final static int GOOGLE_REQUEST = 2323;
    SignInButton googleSignIn;
    LoginButton fbLoginButton;
    CallbackManager callBackManager;
    TextView googleText;
    GoogleSignInOptions gso;
    public ProgressDialog progressDialog;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user_activity);
        supplier_name = (EditText) findViewById(R.id.user_name);
        supplier_password = (EditText) findViewById(R.id.user_password);
        // supplier_address = (EditText)findViewById(R.id.user_address);
        googleSignIn = findViewById(R.id.google_sign_in_register);
        fbLoginButton = findViewById(R.id.facebook_login_button_register);
        supplier_phone = (EditText) findViewById(R.id.user_phone);
        confirm_user_password = findViewById(R.id.confirm_user_password);
        email_address = findViewById(R.id.user_email_address);
        //supplier_email = (EditText)findViewById(R.id.user_email);
        register_progress = (ProgressBar) findViewById(R.id.register_user_progress);
        forward_page = (ImageView) findViewById(R.id.forward_page_user);
        progressDialog = new ProgressDialog(activity, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("جارى التحميل...");
        //job = (EditText) findViewById(R.id.user_job);
        //company = (EditText) findViewById(R.id.user_company);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        supplier_phone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                supplier_phone.setHint("01xxxxxxxxx");
                imm.showSoftInput(supplier_phone, InputMethodManager.SHOW_FORCED);
            } else {
                supplier_phone.setHint("");
            }
        });

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
                progressDialog.dismiss();
            }

            @Override
            public void onError(FacebookException error) {
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


        forward_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = supplier_name.getText().toString().trim();
                //String Email = supplier_email.getText().toString().trim();
                //String UserAddress = supplier_address.getText().toString().trim();
                String UserPhone = supplier_phone.getText().toString().trim();
                String UserPassword = supplier_password.getText().toString().trim();
                String confirmPassword = confirm_user_password.getText().toString().trim();
                imm.hideSoftInputFromWindow(supplier_phone.getWindowToken(), 0);
                if (Name.length() < 4) {
                    supplier_name.setError("من فضلك أدخل الأسم بطريقة صحيحة");
                }
//                else if ( Email.length() < 4)
//                {
//                    supplier_email.setError("من فضلك اكتب البريد الإلكترونى بطريقة صحيحة");
//                }
                else if (UserPassword.length() < 6) {
                    supplier_password.setError("كلمة المرور من 6 الى 20 حرف");
                } else if (confirmPassword.length() < 6 || !confirmPassword.equals(UserPassword)) {
                    confirm_user_password.setError("كلمة المرور غير صحيحة");
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
            Toast.makeText(activity, "من فضلك تأكد من اتصالك بالإنترنت", Toast.LENGTH_LONG).show();
        }
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
                map.put("email", email_address.getText().toString());
                map.put("phone", supplier_phone.getText().toString());
                map.put("username", supplier_phone.getText().toString());
                //map.put("username", supplier_email.getText().toString());
                //map.put("job", job.getText().toString());
                //map.put("company", company.getText().toString());
                Log.e("13123", map.toString());
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
        String emailAddress = email_address.getText().toString();
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
        progressDialog.dismiss();
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
        Intent i = new Intent(context, LoginActivity.class);
        startActivity(i);
        finish();
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
