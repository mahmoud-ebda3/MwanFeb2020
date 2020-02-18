package com.ebda3.mwan;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Helpers.Config;
import com.ebda3.design.FontsOverride;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.ebda3.Helpers.Config.imageupload;
import static com.ebda3.Helpers.Config.isOpened;

public class NavigationViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public String u_email, u_name, u_photo, gfName, gfAccount, gfEmail;
    Intent myIntent = null;
    TextView creditPoints;
    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION};
    GoogleSignInOptions gso;
    GoogleSignInClient googleSignInClient;

    protected DrawerLayout drawer;
    Typeface typeface;
    LinearLayout profile;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;


    public Activity context = this;


    public Boolean setAdapterStatus = false;
    String my_points;

    public int MVersion = Build.VERSION.SDK_INT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (MVersion > 18) {
            FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Cairo-SemiBold.ttf");
        }
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        isOpened = true;
        SharedPreferences sp1 = this.getSharedPreferences("Login", 0);
        gfEmail = sp1.getString("email", "");
        u_email = sp1.getString("phone", " ");
        u_name = sp1.getString("username", " ");
        u_photo = sp1.getString("photo", " ");
        gfName = sp1.getString("name", "");

        Log.d("dfdfdf",sp1.getString("name", ""));
        gfAccount = sp1.getString("acc", "");
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getProfile();

        //Log.d("location",My_Loc);


//        offersRecyclerView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScroll(AbsListView view,
//                                 int firstVisibleItem, int visibleItemCount,
//                                 int totalItemCount) {
//                //Algorithm to check if the last item is visible or not
//                final int lastItem = firstVisibleItem + visibleItemCount;
//                Log.d("lastItem", String.valueOf(visibleItemCount));
//                if (lastItem == totalItemCount) {
//                    // loadData();
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
//                        && (offersRecyclerView.getLastVisiblePosition() - offersRecyclerView.getHeaderViewsCount() -
//                        offersRecyclerView.getFooterViewsCount()) >= (adapter.getCount() - 3)) {
//
//                    loadData();
//                }
//            }
//        });


        //        offersRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // TODO Auto-generated method stub
//                /*
//                String Slecteditem= OfferID.get(position);
//                Intent intent = new Intent(getContext(),offerDetails.class);
//                intent.putExtra("OfferID",OfferID.get(position));
//                intent.putExtra("OfferName",OfferName.get(position));
//                intent.putExtra("OfferPhoto",OfferPhoto.get(position));
//                intent.putExtra("OfferDetail",OfferDetail.get(position));
//                intent.putExtra("partnerID",partnerID.get(position));
//                intent.putExtra("partnerName",partnerName.get(position));
//                intent.putExtra("partnerLogo",partnerLogo.get(position));
//                intent.putExtra("partnerPhone",partnerPhone.get(position));
//                intent.putExtra("partnerEmail",partnerEmail.get(position));
//                intent.putExtra("partnerWebsite",partnerWebsite.get(position));
//                intent.putExtra("partnerDescription",partnerDescription.get(position));
//                intent.putExtra("partnerAddress",partnerAddress.get(position));
//                intent.putExtra("partnerCat",partnerCat.get(position));
//                startActivity(intent);
//                */
//            }
//        });
        //Toast.makeText(NavigationViewActivity.this,My_Loc,Toast.LENGTH_SHORT).show();
        // delay*/
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
        creditPoints = hView.findViewById(R.id.credit_pionts);
        if (gfAccount.equals("2")) {
            user_name2.setText(gfName);
            user_email2.setText(gfEmail);
        } else if (gfAccount.equals("1")) {
            user_name2.setText(gfName);
            user_email2.setText(u_email);
        }
        Picasso.get().
                load(imageupload + u_photo)
                .resize(160, 160)
                .centerCrop()
                .transform(new CropCircleTransformation())
                .error(R.drawable.ic_account_circle_white_48dp)
                .into(user_photo2);
        creditPoints.setOnClickListener(click -> {
            Intent i = new Intent(NavigationViewActivity.this, MyPoints.class);
            startActivity(i);
        });
    }


    @Override
    public void onBackPressed() {
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
        if (id == R.id.home_id) {
            myIntent = new Intent(NavigationViewActivity.this, UserHomeActivity.class);
            startActivity(myIntent);
        } else if (id == R.id.construction_id || id == R.id.finishing_id) {
            myIntent = new Intent(NavigationViewActivity.this, MaterialActivity.class);
            myIntent.putExtra("Market", false);
            if (id == R.id.construction_id) {
                myIntent.putExtra("id", "121");
            } else {
                myIntent.putExtra("id", "122");
            }
            startActivity(myIntent);
        } else if (id == R.id.workers_id) {
            myIntent = new Intent(NavigationViewActivity.this, Workers.class);
            startActivity(myIntent);
        } else if (id == R.id.builing_needs_id) {
            myIntent = new Intent(NavigationViewActivity.this, BuildingNeeds.class);
            startActivity(myIntent);
        } else if (id == R.id.offers_id) {
            myIntent = new Intent(NavigationViewActivity.this, OffersActivity.class);
            startActivity(myIntent);
        } else if (id == R.id.myrealstates_id) {
            myIntent = new Intent(NavigationViewActivity.this, MyProperties.class);
            startActivity(myIntent);
        } else if (id == R.id.exchange_id) {
            myIntent = new Intent(NavigationViewActivity.this, Prices.class);
            startActivity(myIntent);
        } else if (id == R.id.bid_id) {
            myIntent = new Intent(NavigationViewActivity.this, BidOffers.class);
            startActivity(myIntent);
        } else if (id == R.id.mypoint_id) {
            myIntent = new Intent(NavigationViewActivity.this, MyPoints.class);
            startActivity(myIntent);
        } else if (id == R.id.cart_id) {
            myIntent = new Intent(NavigationViewActivity.this, NewCart.class);
            startActivity(myIntent);
        } else if (id == R.id.MyOrders) {
            myIntent = new Intent(NavigationViewActivity.this, MyOrders.class);
            startActivity(myIntent);
        } else if (id == R.id.mwan_community) {

        } else if (id == R.id.report_problem) {
            myIntent = new Intent(NavigationViewActivity.this, Support.class);
            startActivity(myIntent);
        } else if (id == R.id.manage_account) {
            myIntent = new Intent(context, Profile.class);
            startActivity(myIntent);
        } else if (id == R.id.user_sign_out) {
            showSignOutDialog();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                myIntent = new Intent(NavigationViewActivity.this, LoginActivity.class);
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
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    public void getProfile() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.webServiceURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("xxxxxx22", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.has("ID")) {
                        Log.d("xxxxxx", response);
                        creditPoints.setVisibility(View.VISIBLE);
                        creditPoints.setText("رصيد النقاط: " + jObj.getString("points"));
                    }
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("do", "MyInfo");
                params.put("json_email", Config.getJsonEmail(context));
                params.put("json_password", Config.getJsonPassword(context));

                Log.d("xxxxxx", params.toString());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }
}
