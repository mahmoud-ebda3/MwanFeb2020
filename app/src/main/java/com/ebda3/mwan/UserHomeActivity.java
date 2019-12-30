package com.ebda3.mwan;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.adapters.offersListAdapter;
import com.ebda3.design.FontsOverride;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.ebda3.Helpers.Config.imageupload;
import static com.ebda3.Helpers.Config.isOpened;

public class UserHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public String u_email, u_name, u_photo;

    Intent myIntent = null;
    TrackGPS gps;
    double latitude, longitude;
    public String My_Loc;
    GPSTracker gps2;
    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION};

    offersListAdapter adapter;

    ListView listView;
    public TextView no_data;
    Typeface typeface;
    LinearLayout profile;

    public int StartFrom = 0;
    public int LastStartFrom = 0;
    public int VolleyCurrentConnection = 0;
    public int LimitBerRequest = 5;

    View footerView;

    public Activity context = this;
    public ArrayList<String> OfferName = new ArrayList<String>();
    public ArrayList<String> OfferPhoto = new ArrayList<String>();
    public ArrayList<String> offerItems = new ArrayList<String>();

    public Boolean setAdapterStatus = false;

    ProgressBar loadProgress;

    public int MVersion = Build.VERSION.SDK_INT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (MVersion > 18) {
            FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Cairo-SemiBold.ttf");
        }

        isOpened = true;
        SharedPreferences sp1 = this.getSharedPreferences("Login", 0);
        u_email = sp1.getString("phone", " ");
        u_name = sp1.getString("username", " ");
        u_photo = sp1.getString("photo", " ");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        gps = new TrackGPS(UserHomeActivity.this);
        gps2 = new GPSTracker(UserHomeActivity.this);


        if (gps.canGetLocation()) {
            longitude = gps.getLongitude();
            // Toast.makeText(UserHomeActivity.this,String.valueOf(longitude),Toast.LENGTH_SHORT).show();
            latitude = gps.getLatitude();
            My_Loc = String.valueOf(longitude) + "\\|\\|" + String.valueOf(latitude);
        } else {
            gps2.showSettingsAlert();
        }

        //Log.d("location",My_Loc);


        SharedPreferences sp = getSharedPreferences("Location", 0);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("UserLocation", My_Loc);
        Ed.commit();


        listView = (ListView) findViewById(R.id.offersList);
        loadProgress = (ProgressBar) findViewById(R.id.loadProgress);
        footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_footer, null, false);
        no_data = (TextView) findViewById(R.id.no_data);
        listView.setVisibility(View.VISIBLE);
        loadData();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view,
                                 int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                //Algorithm to check if the last item is visible or not
                final int lastItem = firstVisibleItem + visibleItemCount;
                Log.d("lastItem", String.valueOf(visibleItemCount));
                if (lastItem == totalItemCount) {
                    // loadData();
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listView.getLastVisiblePosition() - listView.getHeaderViewsCount() -
                        listView.getFooterViewsCount()) >= (adapter.getCount() - 3)) {

                    loadData();
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                /*
                String Slecteditem= OfferID.get(position);
                Intent intent = new Intent(getContext(),offerDetails.class);
                intent.putExtra("OfferID",OfferID.get(position));
                intent.putExtra("OfferName",OfferName.get(position));
                intent.putExtra("OfferPhoto",OfferPhoto.get(position));
                intent.putExtra("OfferDetail",OfferDetail.get(position));
                intent.putExtra("partnerID",partnerID.get(position));
                intent.putExtra("partnerName",partnerName.get(position));
                intent.putExtra("partnerLogo",partnerLogo.get(position));
                intent.putExtra("partnerPhone",partnerPhone.get(position));
                intent.putExtra("partnerEmail",partnerEmail.get(position));
                intent.putExtra("partnerWebsite",partnerWebsite.get(position));
                intent.putExtra("partnerDescription",partnerDescription.get(position));
                intent.putExtra("partnerAddress",partnerAddress.get(position));
                intent.putExtra("partnerCat",partnerCat.get(position));
                startActivity(intent);
                */
            }
        });
        //Toast.makeText(UserHomeActivity.this,My_Loc,Toast.LENGTH_SHORT).show();
        View hView = navigationView.getHeaderView(0);

        profile = (LinearLayout) hView.findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Profile.class);
                startActivity(intent);
            }
        });

        TextView user_name2 = (TextView) hView.findViewById(R.id.nav_user_name);
        TextView user_email2 = (TextView) hView.findViewById(R.id.nav_user_email);
        ImageView user_photo2 = (ImageView) hView.findViewById(R.id.nav_user_photo);

        user_name2.setText(u_name);
        user_email2.setText(u_email);

        Picasso.with(this).load(imageupload + u_photo)
                .resize(160, 160)
                .centerCrop()
                .transform(new CropCircleTransformation())
                .error(R.drawable.ic_account_circle_white_48dp)
                .into(user_photo2);
    }


    public void loadData() {

        Log.d("loadData", "loadData");
        if (VolleyCurrentConnection == 0) {
            VolleyCurrentConnection = 1;
            String VolleyUrl = "http://adc-company.net/mwan/offers-edit-1.html?json=true&ajax_page=true" + "&start=" + String.valueOf(StartFrom) + "&end=" + String.valueOf(LimitBerRequest);
            Log.d("responser", String.valueOf(VolleyUrl));
            listView.addFooterView(footerView);
            try {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, VolleyUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("328944289", response);
                        response = fixEncoding(response);
                        Log.d("response", response);
                        listView.removeFooterView(footerView);
                        loadProgress.setVisibility(View.GONE);


                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {

                                VolleyCurrentConnection = 0;
                                StartFrom += LimitBerRequest;
                                LastStartFrom = StartFrom;
                                for (int i = 0; i < array.length(); i++) {


                                    JSONObject row = array.getJSONObject(i);

                                    OfferName.add(row.getString("name").toString());
                                    OfferPhoto.add(row.getString("photo").toString());
                                    offerItems.add(row.getString("items").toString());


                                }


                                if (!setAdapterStatus) {
                                    adapter = new offersListAdapter(context, OfferName, OfferPhoto, offerItems);
                                    listView.setAdapter(adapter);
                                    setAdapterStatus = true;
                                } else {
                                    adapter.notifyDataSetChanged();
                                }


                            }


                        } catch (JSONException e) {
                            Log.d("ffffff", e.getMessage());
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("ffffff",error.getMessage());
                        loadProgress.setVisibility(View.GONE);


                        new CountDownTimer(3000, 1000) {
                            public void onFinish() {
                                VolleyCurrentConnection = 0;
                                listView.removeFooterView(footerView);
                                loadData();
                            }

                            public void onTick(long millisUntilFinished) {
                                // millisUntilFinished    The amount of time until finished.
                            }
                        }.start();

                        //Log.d("ErrorResponse", error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {


                        Map<String, String> paramas = new HashMap();


                        paramas.put("dos", "mytrips");
                        return paramas;
                    }
                };

                int socketTimeout = 10000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                RequestQueue queue = Volley.newRequestQueue(this);

                queue.add(stringRequest);
            } catch (Exception e) {
                //Log.d("ffffff",e.getMessage());
                VolleyCurrentConnection = 0;
                loadData();
            }
        }
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.user_home, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.construction_id || id == R.id.finishing_id) {
            myIntent = new Intent(UserHomeActivity.this, MaterialActivity.class);
            myIntent.putExtra("Market", false);
            if (id == R.id.construction_id) {
                myIntent.putExtra("id", "121");
            } else {
                myIntent.putExtra("id", "122");
            }
            startActivity(myIntent);
        } else if (id == R.id.workers_id) {
            myIntent = new Intent(UserHomeActivity.this, Workers.class);
            startActivity(myIntent);
        } else if (id == R.id.builing_needs_id) {


        } else if (id == R.id.myrealstates_id) {
            myIntent = new Intent(UserHomeActivity.this, MyProperties.class);
            startActivity(myIntent);
        } else if (id == R.id.exchange_id) {
            myIntent = new Intent(UserHomeActivity.this, Prices.class);
            startActivity(myIntent);
        } else if (id == R.id.bid_id) {
            myIntent = new Intent(UserHomeActivity.this, bid.class);
            startActivity(myIntent);
        } else if (id == R.id.mypoint_id) {

            myIntent = new Intent(UserHomeActivity.this, MyPoints.class);
            startActivity(myIntent);

        } else if (id == R.id.cart_id) {
            myIntent = new Intent(UserHomeActivity.this, NewCart.class);
            startActivity(myIntent);
        } else if (id == R.id.MyOrders) {
            myIntent = new Intent(UserHomeActivity.this, MyOrders.class);
            startActivity(myIntent);
        } else if (id == R.id.mwan_community) {


        } else if (id == R.id.report_problem) {
            myIntent = new Intent(UserHomeActivity.this, Support.class);
            startActivity(myIntent);
        } else if (id == R.id.manage_account) {

            Intent intent = new Intent(context, Profile.class);
            startActivity(intent);

        } else if (id == R.id.user_sign_out) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setTitle("تسجيل الخروج");
            alertDialog.setMessage("هل تود تسجيل الخروج من البرنامج ؟");
            alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences sp = getSharedPreferences("Login", 0);
                    SharedPreferences.Editor Ed = sp.edit();
                    Ed.putString("email", "");
                    Ed.putString("normal_password", "");
                    Ed.commit();

                    myIntent = new Intent(UserHomeActivity.this, LoginActivity.class);
                    startActivity(myIntent);
                }
            });


            alertDialog.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                }
            });

            alertDialog.show();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isPermissionGranted() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v("mylog", "Permission is granted");
            return true;
        } else {
            Log.v("mylog", "Permission not granted");
            return false;
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
