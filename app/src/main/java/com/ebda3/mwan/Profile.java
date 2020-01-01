package com.ebda3.mwan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Helpers.Config;
import com.ebda3.Helpers.MarshMallowPermission;
import com.ebda3.Helpers.MultiPartUtility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.ebda3.Helpers.Config.imageupload;

public class Profile extends AppCompatActivity {

    Activity activity = this;
    Context context = this;
    public Toolbar toolbar;
    public ImageView user_photo,upload;
    LinearLayout MainLinearLayout,LoadingLinearLayout,edit,save,account_id,edit_password_linear,status;
    TextView name,email,phone,address,job,code,purchases,points,headline;
    EditText edit_name,edit_email,edit_phone,edit_password,edit_address,edit_job;
    Button retry;
    ProgressBar progressBar;

    String imageURL = "";
    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);

        headline.setText("بياناتى");


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        retry = (Button) findViewById(R.id.retry);
        status = (LinearLayout) findViewById(R.id.status);
        purchases = (TextView) findViewById(R.id.purchases);
        points = (TextView) findViewById(R.id.points);

        edit_password_linear = (LinearLayout) findViewById(R.id.edit_password_linear);
        account_id = (LinearLayout) findViewById(R.id.account_id);
        edit = (LinearLayout) findViewById(R.id.edit);
        save = (LinearLayout) findViewById(R.id.save);
        MainLinearLayout = (LinearLayout) findViewById(R.id.MainLinearLayout);
        LoadingLinearLayout = (LinearLayout) findViewById(R.id.LoadingLinearLayout);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);
        job = (TextView) findViewById(R.id.job);
        code = (TextView) findViewById(R.id.code);
        user_photo = (ImageView) findViewById(R.id.user_photo);

        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_address = (EditText) findViewById(R.id.edit_address);
        edit_job = (EditText) findViewById(R.id.edit_job);
        upload = (ImageView) findViewById(R.id.upload);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetProfile();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_name.setVisibility(View.VISIBLE);
                //edit_email.setVisibility(View.VISIBLE);
                edit_phone.setVisibility(View.VISIBLE);
                //edit_address.setVisibility(View.VISIBLE);
               // edit_job.setVisibility(View.VISIBLE);
                edit_password_linear.setVisibility(View.VISIBLE);
                upload.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);

                account_id.setVisibility(View.GONE);
                name.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
                phone.setVisibility(View.GONE);
                address.setVisibility(View.GONE);
                job.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                status.setVisibility(View.GONE);


            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = edit_name.getText().toString().trim();
                //String UserEmail = edit_email.getText().toString().trim();
                String UserPhone = edit_phone.getText().toString().trim();
                String UserPassword = edit_password.getText().toString().trim();
                //String UserJob = edit_job.getText().toString().trim();
               // String UserAddress = edit_address.getText().toString().trim();

                if (Name.length() < 4 ) {
                    edit_name.setError( "من فضلك اكتب الاسم" );
                }
//                else if (UserEmail.equals("")) {
//                    edit_email.setError("من فضلك اكتب البريد الالكترونى");
//                }
                else if (UserPhone.length() < 9 ) {
                    edit_phone.setError("من فضلك اكتب رفم الموبيل");
                }
                else if ( UserPassword.length() > 0 && UserPassword.length() < 4  ) {
                    edit_password.setError("كلمة السر صغيرة جدا");
                }
                else {
                    sendData(Name, UserPhone, UserPassword);
                }
            }
        });



        final MarshMallowPermission marshMallowPermission = new MarshMallowPermission(activity);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marshMallowPermission.checkPermissionForRead()) {
                    marshMallowPermission.requestPermissionForRecord();
                } else {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
                    /*
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                    */

                }

            }
        });

        GetProfile();

    }



    public boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void sendData(final String name,  final String phone, final String password) {

        boolean t = isNetworkConnected();
        if(t) {
            progressBar.setVisibility(View.VISIBLE);
            save.setVisibility(View.GONE);
            edit_name.setEnabled(false);
            edit_password.setEnabled(false);
            edit_email.setEnabled(false);
            edit_phone.setEnabled(false);
            edit_address.setEnabled(false);
            edit_job.setEnabled(false);
            upload.setEnabled(false);
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        String charset = "UTF-8";
                        MultiPartUtility multipart = new MultiPartUtility(charset);
                        HashMap<String, String> params = new HashMap<>();
                        params.put("do", "updates");
                        params.put("id", Config.getUserID(context));
                        params.put("name", name);
                       // params.put("email", "002"+phone);
                        params.put("username", phone);
                        params.put("phone", phone);
                        if ( password.length() > 0 ) {
                            params.put("password", password);
                        }
                       // params.put("job", job);
                        //params.put("address", address);

                        params.put("json_email", Config.getJsonEmail(context));
                        params.put("json_password", Config.getJsonPassword(context));
                        Pair<String, String> imag = new Pair<>("image", imageURL);
                        multipart.SetParams(params, imag);
                        multipart.execute(Config.SignUpUser);
                        final String Response = multipart.get();
                        Log.e("Response", params.toString());
                        Log.e("Response", Response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    JSONObject object = new JSONObject(Response);
                                    if ( object.has("ID") )
                                    {
                                        SharedPreferences sp=getSharedPreferences("Login", 0);
                                        SharedPreferences.Editor Ed=sp.edit();
                                        Ed.putString("name",object.getString("name").toString() );
                                        Ed.putString("email",object.getString("phone").toString() );
                                        Ed.putString("photo",object.getString("photo").toString() );
                                        if ( edit_password.getText().toString().length() > 0 ) {
                                            Ed.putString("password", edit_password.getText().toString());
                                        }
                                        Ed.putString("json_password",object.getString("password") );
                                        Ed.putString("json_email",object.getString("phone") );
                                        Ed.commit();
                                        GetProfile();
                                    }
                                }catch (Exception e)
                                {
                                    edit_name.setEnabled(true);
                                    edit_password.setEnabled(true);
                                    edit_email.setEnabled(true);
                                    edit_phone.setEnabled(true);
                                    edit_address.setEnabled(true);
                                    edit_job.setEnabled(true);
                                    upload.setEnabled(true);
                                    save.setVisibility(View.VISIBLE);
                                    JSONArray array = null;
                                    try {
                                        array = new JSONArray(Response);
                                        if ( array.getString(0).equals("Field password  From 6 To 20") ) {
                                            edit_password.setError("كلمة السر صعيفة جدا");
                                            edit_password.setText("");
                                        }
                                        else if ( array.getString(0).equals("Field phone  Already exist") ) {
                                            edit_phone.setError("رقم الموبيل مستخدم مسبقا");
                                            edit_phone.setText("");
                                        }
                                        else if ( array.getString(0).equals("Please type the e-mail correctly") ) {
                                            edit_email.setError("من فضلك اكتب البريد الالكترونى بطريقة صحيحة");
                                            edit_email.setText("");
                                        }
                                        else if ( array.getString(0).equals("Field email  Already exist") ) {
                                            edit_email.setError("البريد الالكترونى مستخدم مسبقا");
                                            edit_email.setText("");
                                        }

                                        Log.d("response",  array.getString(0));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        });

                    }
                    catch (Exception e)
                    {
                        Log.e("Response", e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "برجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }



                }
            });
            th.start();
        }
        else
        {
            Toast.makeText(context, "please Enable Your Internet Connection", Toast.LENGTH_LONG).show();
        }












    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, s);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            user_photo.setImageBitmap(imageBitmap);
            Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
            imageURL = getRealPathFromURI(tempUri);
        }
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                user_photo.setImageBitmap(bitmap);
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                imageURL = cursor.getString(idx);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void GetProfile()
    {
        edit_name.setEnabled(true);
        edit_password.setEnabled(true);
        edit_email.setEnabled(true);
        edit_phone.setEnabled(true);
        edit_address.setEnabled(true);
        edit_job.setEnabled(true);
        upload.setEnabled(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST , Config.webServiceURL , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("xxxxxx22", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.has("ID"))
                    {
                        Log.d("xxxxxx", response);
                        status.setVisibility(View.VISIBLE);
                        MainLinearLayout.setVisibility(View.VISIBLE);
                        LoadingLinearLayout.setVisibility(View.GONE);
                        retry.setVisibility(View.GONE);

                        code.setText(jObj.getString("ID"));
                        name.setText(jObj.getString("name"));
                        email.setText(jObj.getString("email"));
                        address.setText(jObj.getString("address"));
                        job.setText(jObj.getString("job"));
                        phone.setText(jObj.getString("phone"));

                        edit_name.setText(jObj.getString("name"));
                        edit_email.setText(jObj.getString("email"));
                        edit_address.setText(jObj.getString("address"));
                        edit_job.setText(jObj.getString("job"));
                        edit_phone.setText(jObj.getString("phone"));
                        purchases.setText(jObj.getString("purchases"));
                        points.setText(jObj.getString("points"));

                        Picasso.with(context).load(imageupload + jObj.getString("photo"))
                                .resize(360,256)
                                .centerCrop()
                                .transform(new CropCircleTransformation())
                                .error(R.drawable.ic_account_circle_white_48dp)
                                .into(user_photo);

                        edit_name.setVisibility(View.GONE);
                        edit_email.setVisibility(View.GONE);
                        edit_phone.setVisibility(View.GONE);
                        edit_address.setVisibility(View.GONE);
                        edit_job.setVisibility(View.GONE);
                        edit_password_linear.setVisibility(View.GONE);
                        upload.setVisibility(View.GONE);
                        save.setVisibility(View.GONE);

                        account_id.setVisibility(View.VISIBLE);
                        name.setVisibility(View.VISIBLE);
                        email.setVisibility(View.VISIBLE);
                        phone.setVisibility(View.VISIBLE);
                        address.setVisibility(View.VISIBLE);
                        job.setVisibility(View.VISIBLE);
                        edit.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    retry.setVisibility(View.VISIBLE);
                    MainLinearLayout.setVisibility(View.GONE);
                    LoadingLinearLayout.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                retry.setVisibility(View.VISIBLE);
                MainLinearLayout.setVisibility(View.GONE);
                LoadingLinearLayout.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("do", "MyInfo");
                params.put("json_email", Config.getJsonEmail(context) );
                params.put("json_password", Config.getJsonPassword(context) );

                Log.d("xxxxxx",params.toString());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent (context,UserHomeActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent (context,UserHomeActivity.class);
        startActivity(intent);
    }

}