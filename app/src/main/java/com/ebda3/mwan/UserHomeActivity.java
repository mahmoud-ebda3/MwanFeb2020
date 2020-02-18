package com.ebda3.mwan;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Model.OptionObject;
import com.ebda3.adapters.OptionsItemAdapter;
import com.ebda3.adapters.offersListAdapter;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.ebda3.Helpers.Config.isOpened;

public class UserHomeActivity extends NavigationViewActivity implements OptionsItemAdapter.RecyclerViewItemOnClickListener {

    Intent myIntent = null;
    RecyclerView offersRecyclerView, optionsRecyclerView;
    public TextView no_data;
    offersListAdapter offersAdapter;
    OptionsItemAdapter optionsAdapter;
    TrackGPS gps;
    GPSTracker gps2;
    double latitude, longitude;
    public String My_Loc;
    GoogleSignInOptions gso;
    GoogleSignInClient googleSignInClient;
    public Activity context = this;
    public int StartFrom = 0;
    public int LastStartFrom = 0;
    public int VolleyCurrentConnection = 0;
    public int LimitBerRequest = 5;
    Timer timer;
    FrameLayout frameLayout;
    public ArrayList<String> OfferName = new ArrayList<String>();
    public ArrayList<String> OfferPhoto = new ArrayList<String>();
    public ArrayList<String> offerItems = new ArrayList<String>();
    private ArrayList<OptionObject> objectItems = new ArrayList<>();
    public int myposition = 0;
    View footerView;
    ProgressBar loadProgress;
    public Boolean setAdapterStatus = false;
    Toolbar toolbar;
    ImageView searchIcon;
    SearchView searchView;
    TextView head_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_user_home, null, false);
        frameLayout = drawer.findViewById(R.id.frame_layout);
        toolbar = drawer.findViewById(R.id.app_toolbar);
        searchIcon = toolbar.findViewById(R.id.search_icon);
        searchView = findViewById(R.id.search_view_layout);
        searchView.setVisibility(View.VISIBLE);
//        searchIcon.setOnClickListener(click -> {
//            searchView.setVisibility(View.VISIBLE);
//            toolbar.setVisibility(View.GONE);
//        });
        searchView.setOnSearchClickListener(click -> {
            toolbar.setVisibility(View.GONE);
        });
        searchView.setOnCloseListener(() -> {
            searchView.clearFocus();
            toolbar.setVisibility(View.VISIBLE);
            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(context, ItemsActivity.class);
                i.putExtra("query_word", query);
                i.putExtra("companies", "");
                i.putExtra("filter", "");
                i.putExtra("extra", "one");
                startActivity(i);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        head_line = toolbar.findViewById(R.id.app_headline);
        frameLayout.addView(contentView);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        head_line.setText("الرئيسية");
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        isOpened = true;
        gps = new TrackGPS(UserHomeActivity.this);
        gps2 = new GPSTracker(UserHomeActivity.this);
        if (gps.canGetLocation()) {
            longitude = gps.getLongitude();
            latitude = gps.getLatitude();
            My_Loc = String.valueOf(longitude) + "\\|\\|" + String.valueOf(latitude);
        } else {
            gps2.showSettingsAlert();
        }

        SharedPreferences sp = getSharedPreferences("Location", 0);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("UserLocation", My_Loc);
        Ed.commit();

        offersRecyclerView = contentView.findViewById(R.id.offers_items_rv);
        optionsRecyclerView = contentView.findViewById(R.id.options_item_rv);
        optionsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        optionsRecyclerView.setLayoutFrozen(true);
        offersRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        loadProgress = contentView.findViewById(R.id.loadProgress);
        footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_footer, null, false);
        no_data = contentView.findViewById(R.id.no_data);
        offersRecyclerView.setVisibility(View.VISIBLE);
        objectItems.add(new OptionObject(R.drawable.ic_price, "العروض"));
        objectItems.add(new OptionObject(R.drawable.ic_account_balance_black_24dp, "انشاءات"));
        objectItems.add(new OptionObject(R.drawable.ic_brush_black_24dp, "تشطيبات"));
        objectItems.add(new OptionObject(R.drawable.ic_building_needs, "احتياجات عقارك"));
        objectItems.add(new OptionObject(R.drawable.ic_build_black_24dp, "طلب صنايعى"));
        objectItems.add(new OptionObject(R.drawable.ic_home_black_24dp, "متابعة عقارك"));
        objectItems.add(new OptionObject(R.drawable.ic_gavel_black_24dp, "مناقصة موان"));
        objectItems.add(new OptionObject(R.drawable.ic_mypoints, "نقاط الشراء"));
        objectItems.add(new OptionObject(R.drawable.ic_shopping_cart_black_24dp, "سلة المشتريات"));
        objectItems.add(new OptionObject(R.drawable.ic_mwan_community, "مجتمع موان"));
        objectItems.add(new OptionObject(R.drawable.ic_trending_up_black_24dp, "بورصة الاسعار"));
        objectItems.add(new OptionObject(R.drawable.ic_library_books_black_24dp, "مشترياتى"));
        objectItems.add(new OptionObject(R.drawable.ic_report_problem_black_24dp, "الإبلاغ عن مشكلة"));
        objectItems.add(new OptionObject(R.drawable.ic_manage_account, "إدارة الحساب"));
        objectItems.add(new OptionObject(R.drawable.ic_sign_out, "تسجيل الخروج"));
        optionsAdapter = new OptionsItemAdapter(this, objectItems, this);
        optionsRecyclerView.setAdapter(optionsAdapter);
        loadData();


        timer = new Timer();
        timer.scheduleAtFixedRate(new RemindTask(), 0, 3000);
    }

    public void loadData() {

        Log.d("loadData", "loadData");
        if (VolleyCurrentConnection == 0) {
            VolleyCurrentConnection = 1;
            String VolleyUrl = "https://www.mawaneg.com/supplier/offers-edit-1.html?json=true&ajax_page=true" + "&start=" + String.valueOf(StartFrom) + "&end=" + String.valueOf(LimitBerRequest);
            Log.d("responser", String.valueOf(VolleyUrl));
//            listView.addFooterView(footerView);
            try {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, VolleyUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("328944289", response);
                        response = fixEncoding(response);
                        Log.d("response", response);
//                        listView.removeFooterView(footerView);
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
                                    offersAdapter = new offersListAdapter(context, OfferName, OfferPhoto, offerItems);
                                    offersRecyclerView.setAdapter(offersAdapter);
                                    setAdapterStatus = true;
                                } else {
                                    offersAdapter.notifyDataSetChanged();
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
//                                listView.removeFooterView(footerView);
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

    private class RemindTask extends TimerTask {
        //int current = viewPager.getCurrentItem();

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (myposition == OfferName.size()) {
                        myposition = 0;
                        myposition++;
                    } else {
                        myposition++;
                    }
                    //offersRecyclerView.getLayoutManager().scrollToPosition(myposition);

                    // or use
                    offersRecyclerView.smoothScrollToPosition(myposition);
                }
            });

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showFinishDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showSignOutDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("تسجيل الخروج");
        alertDialog.setMessage("هل تود تسجيل الخروج من البرنامج ؟");
        alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LoginManager.getInstance().logOut();
                googleSignInClient.signOut();
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

    private void showFinishDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("الخروج");
        alertDialog.setMessage("هل تود الخروج من التطبيق ؟");
        alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });
        alertDialog.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                LoginManager.getInstance().logOut();
                googleSignInClient.signOut();
                SharedPreferences sp = getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed = sp.edit();
                Ed.putString("email", "");
                Ed.putString("normal_password", "");
                Ed.commit();
                myIntent = new Intent(UserHomeActivity.this, LoginActivity.class);
                startActivity(myIntent);

                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onItemClick(OptionObject object) {
        switch (object.getName()) {
            case "العروض":
                myIntent = new Intent(UserHomeActivity.this, OffersActivity.class);
                startActivity(myIntent);
                break;
            case "انشاءات":
                myIntent = new Intent(UserHomeActivity.this, MaterialActivity.class);
                myIntent.putExtra("Market", false);
                myIntent.putExtra("id", "121");
                startActivity(myIntent);
                break;
            case "تشطيبات":
                myIntent = new Intent(UserHomeActivity.this, MaterialActivity.class);
                myIntent.putExtra("Market", false);
                myIntent.putExtra("id", "122");
                startActivity(myIntent);
                break;
            case "احتياجات عقارك":
                myIntent = new Intent(UserHomeActivity.this, BuildingNeeds.class);
                startActivity(myIntent);
                break;
            case "طلب صنايعى":
                myIntent = new Intent(UserHomeActivity.this, Workers.class);
                startActivity(myIntent);
                break;
            case "متابعة عقارك":
                myIntent = new Intent(UserHomeActivity.this, MyProperties.class);
                startActivity(myIntent);
                break;
            case "مناقصة موان":
                myIntent = new Intent(UserHomeActivity.this, bid.class);
                startActivity(myIntent);
                break;
            case "نقاط الشراء":
                myIntent = new Intent(UserHomeActivity.this, MyPoints.class);
                startActivity(myIntent);
                break;
            case "سلة المشتريات":
                myIntent = new Intent(UserHomeActivity.this, NewCart.class);
                startActivity(myIntent);
                break;
            case "مجتمع موان":
                break;
            case "بورصة الاسعار":
                myIntent = new Intent(UserHomeActivity.this, Prices.class);
                startActivity(myIntent);
                break;
            case "مشترياتى":
                myIntent = new Intent(UserHomeActivity.this, MyOrders.class);
                startActivity(myIntent);
                break;
            case "الإبلاغ عن مشكلة":
                myIntent = new Intent(UserHomeActivity.this, Support.class);
                startActivity(myIntent);
                break;
            case "إدارة الحساب":
                Intent intent = new Intent(context, Profile.class);
                startActivity(intent);
                break;
            case "تسجيل الخروج":
                showSignOutDialog();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
