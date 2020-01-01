package com.ebda3.mwan;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Model.Section;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ebda3.Helpers.Config.GetSection;
import static com.ebda3.Helpers.Config.SignUp;
import static com.ebda3.Helpers.Config.isOpened;

/**
 * Created by work on 15/10/2017.
 */

public class OrganizationRegisterationActivity extends AppCompatActivity {
    public EditText organiztion_name,organization_id_number,organiztion_address;
    public Spinner organiztion_spin;
    public LinearLayout trade_linear,tax_linear , time_from , time_to , trade_select_done , tax_select_done;
    public TextView time_from_txt , time_to_txt , trade_change_image , tax_change_image;
    public Button finish_reg ;
    public String My_time , My_time_2;
    public static boolean isTax = false;
    public ProgressBar org_progressbar;


    ArrayList<Section> sections = new ArrayList<>();
    ArrayList<String> SectionsName = new ArrayList<>();

    public String chosen_section;


    ProgressDialog progressDialog;


    String u_email ,u_password;
    ArrayList<Uri> image_uris_tax = new ArrayList<Uri>();
    ArrayList<Uri> image_uris_trade = new ArrayList<Uri>();


    ArrayList<String> urls = new ArrayList<>();
    ArrayList<ImageView> images = new ArrayList<>();
    ArrayList<Image> images2;
    ArrayList<String> paths;
    String imageURL = "";
    Activity context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organization_details_activity);

        isOpened = false;

        org_progressbar = (ProgressBar) findViewById(R.id.register_progress_organization);
        progressDialog = ProgressDialog.show(OrganizationRegisterationActivity.this,"", "جارى التحميل",true,true);


        organiztion_name = (EditText) findViewById(R.id.organization_name);
        organization_id_number = (EditText)findViewById(R.id.organiztion_phone);
        organiztion_address = (EditText)findViewById(R.id.organization_address);

        organiztion_spin = (Spinner) findViewById(R.id.spinner_organiztion);

        trade_linear = (LinearLayout) findViewById(R.id.trade_linear);
        tax_linear  = (LinearLayout) findViewById(R.id.tax_linaer_layout);
        time_from = (LinearLayout)findViewById(R.id.time_from_linear);
        time_to = (LinearLayout)findViewById(R.id.time_to_linear);
        trade_select_done = (LinearLayout)findViewById(R.id.trade_select_done);
        tax_select_done = (LinearLayout)findViewById(R.id.tax_select_done);


        time_from_txt = (TextView) findViewById(R.id.time_from_text);
        time_to_txt = (TextView) findViewById(R.id.time_to_text);
        trade_change_image = (TextView) findViewById(R.id.change_trade_photo);
        tax_change_image = (TextView) findViewById(R.id.tax_change_image);


        finish_reg = (Button) findViewById(R.id.finish_reg_bu);

        sections = new ArrayList<>();
        SectionsName = new ArrayList<>();

        SharedPreferences sp1 = this.getSharedPreferences("Login", 0);
        u_email = sp1.getString("username"," ");
        u_password = sp1.getString("password"," ");

        LoadSections();

        organiztion_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                chosen_section= sections.get(position).getID();


            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        trade_change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTax = false;
                openImagePicker();
            }
        });

        tax_change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTax = true;
                openImagePicker();
            }
        });


        trade_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                    isTax = false;
                    openImagePicker();

            }
        });

        tax_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTax = true;
                openImagePicker();

            }
        });


        time_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = 10;
                int minute = 10;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Calendar mcurrentTime = Calendar.getInstance();
                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute= mcurrentTime.get(Calendar.MINUTE);
                }

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(OrganizationRegisterationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        My_time = Integer.toString(selectedHour) + ":"+ Integer.toString(selectedMinute);

                        String AM_PM ;
                        int Hours;
                        if((selectedHour < 12) && (selectedHour !=0)) {
                            Hours = selectedHour;
                            AM_PM = "AM";
                        } else if (selectedHour == 12) {
                            Hours = selectedHour;
                            AM_PM = "PM";
                        }
                        else if (selectedHour == 0) {
                            Hours = 12;
                            AM_PM = "AM";
                        }else {
                            Hours = selectedHour - 12;
                            AM_PM = "PM";
                        }



                        time_from_txt.setText( Hours + ":" + selectedMinute + " "+AM_PM);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("تحديد الوقت");
                mTimePicker.show();
            }
        });

        time_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int hour = 10;
                int minute = 10;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    minute= mcurrentTime.get(Calendar.MINUTE);
                }

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(OrganizationRegisterationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        My_time_2 = Integer.toString(selectedHour) + ":"+ Integer.toString(selectedMinute);
                        String AM_PM ;
                        int Hours;
                        if((selectedHour < 12) && (selectedHour !=0)) {
                            Hours = selectedHour;
                            AM_PM = "AM";
                        } else if (selectedHour == 12) {
                            Hours = selectedHour;
                            AM_PM = "PM";
                        }
                        else if (selectedHour == 0) {
                            Hours = 12;
                            AM_PM = "AM";
                        }else {
                            Hours = selectedHour - 12;
                            AM_PM = "PM";
                        }
                        time_to_txt.setText( Hours + ":" + selectedMinute + " "+AM_PM);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("تحديد الوقت");
                mTimePicker.show();

            }
        });


        finish_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OrgName = organiztion_name.getText().toString().trim();
                String OrgAddress = organiztion_address.getText().toString().trim();
                String OrgUserPhone = organization_id_number.getText().toString().trim();
                String OrgTimeFrom = time_from_txt.getText().toString().trim();
                String OrgTimeTo = time_to_txt.getText().toString().trim();

                if (OrgName.length() < 4 ) {
                    organiztion_name.setError("من فضلك أدخل الأسم بطريقة صحيحة");
                }
                else if ( OrgAddress.length() < 4  )
                {
                    organiztion_address.setError("من فضلك ادخل العنوان");
                }
                else if (!(OrgUserPhone.length() == 11))
                {
                    organization_id_number.setError("من فضلك أدخل رقم الجوال بطريقة صحيحة");
                }
                else if (OrgTimeFrom.length() < 2 )
                {
                    time_from_txt.setError("من فضلك أدخل الوقت");
                }
                else if (OrgTimeTo.length() < 2)
                {
                    time_to_txt.setError("من فضلك أدخل الوقت");
                }
                else
                {
                    boolean t = isNetworkConnected();
                    if (t)
                    {
                        register_organization();
                    }
                    else
                    {
                        Toast.makeText(OrganizationRegisterationActivity.this,"من فضلك تأكد من اتصاللك بالإنترنت" , Toast.LENGTH_LONG).show();

                    }


                    //sendData(Name,UserName, UserEmail, UserPhone, UserPassword);
                }
            }
        });
    }
    public void LoadSections()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GetSection,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Log.d("responseee2222",response);
                        response = fixEncoding(response);

                        try {


                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0)
                            {
                                for (int i=0 ; i<jsonArray.length() ; i++)
                                {
                                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                    String id = jsonObject.getString("ID");
                                    String name = jsonObject.getString("name");

                                    Section my_section = new Section();
                                    my_section.setID(id);
                                    my_section.setName(name);

                                    sections.add(my_section);
                                    SectionsName.add(name);

                                    Log.d("my data",id);
                                }
                                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, SectionsName);
                                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                organiztion_spin.setAdapter(adapter2);
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
                        Log.d("responseee2222",String.valueOf(error));

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

    public void register_organization()
    {
        finish_reg.setVisibility(View.GONE);
        org_progressbar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SignUp,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        org_progressbar.setVisibility(View.GONE);
                        finish_reg.setVisibility(View.VISIBLE);
                        response = fixEncoding(response);

                        Intent myIntent = new Intent(OrganizationRegisterationActivity.this, PriceProductActivity.class);
                        startActivity(myIntent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("responseee2222",String.valueOf(error));
                        org_progressbar.setVisibility(View.GONE);
                        finish_reg.setVisibility(View.VISIBLE);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("do", "updates");
                map.put("username", u_email);
                map.put("password", u_password);
                map.put("supplier_name", organiztion_name.getText().toString());
                map.put("id_number", organization_id_number.getText().toString());
                map.put("section", chosen_section);
                map.put("work_from", time_from_txt.getText().toString());
                map.put("work_to", time_to_txt.getText().toString());
                map.put("map", organiztion_address.getText().toString());

                Log.d("map",String.valueOf(map));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null)
        {
            images2= new ArrayList<>();
            images2 = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);

            if (isTax)
            {
                tax_linear.setVisibility(View.GONE);
                tax_select_done.setVisibility(View.VISIBLE);

                image_uris_tax = new ArrayList<Uri>();

                for (int i=0 ; i<images2.size();i++)
                {
                    Log.d("myphoto", String.valueOf(images2.get(0).getPath()));
                    image_uris_tax.add(Uri.parse(images2.get(i).getPath().toString()));
                }
            }
            else
            {
                trade_linear.setVisibility(View.GONE);
                trade_select_done.setVisibility(View.VISIBLE);

                image_uris_trade = new ArrayList<Uri>();

                for (int i=0 ; i<images2.size();i++)
                {
                    Log.d("myphoto", String.valueOf(images2.get(0).getPath()));
                    image_uris_trade.add(Uri.parse(images2.get(i).getPath().toString()));
                }
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {

        ImagePicker.with(this)                      //  Initialize ImagePicker with activity or fragment context
                .setToolbarColor("#212121")         //  Toolbar color
                .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                .setProgressBarColor("#4CAF50")     //  ProgressBar color
                .setBackgroundColor("#212121")      //  Background color
                .setCameraOnly(false)               //  Camera mode
                .setMultipleMode(true)             //  Select multiple images or single image
                .setFolderMode(true)                //  Folder mode
                .setShowCamera(true)                //  Show camera button
                .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                .setDoneTitle("اختيار")             //  Done button title
                .setMaxSize(1)
                .setSavePath("Mwan Images")         //  Image capture folder name
                .start();
    }
    public void openImagePicker()
    {
        if (ContextCompat.checkSelfPermission(OrganizationRegisterationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(OrganizationRegisterationActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED  )
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(OrganizationRegisterationActivity.this,  Manifest.permission.WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(OrganizationRegisterationActivity.this,  Manifest.permission.CAMERA))
            {

            } else {
                ActivityCompat.requestPermissions(OrganizationRegisterationActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

            }


        }
        else
        {

            ImagePicker.with(this)                         //  Initialize ImagePicker with activity or fragment context
                    .setToolbarColor("#212121")         //  Toolbar color
                    .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                    .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                    .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                    .setProgressBarColor("#4CAF50")     //  ProgressBar color
                    .setBackgroundColor("#212121")      //  Background color
                    .setCameraOnly(false)               //  Camera mode
                    .setMultipleMode(true)              //  Select multiple images or single image
                    .setFolderMode(true)                //  Folder mode
                    .setShowCamera(true)                //  Show camera button
                    .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                    .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                    .setDoneTitle("اختيار")               //  Done button title
                    .setMaxSize(1)
                    .setSavePath("Mwan Images")              //  Image capture folder name
                    .start();                           //  Start ImagePicker
        }
    }

    public boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


}
