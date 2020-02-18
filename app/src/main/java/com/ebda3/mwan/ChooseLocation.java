package com.ebda3.mwan;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebda3.Helpers.Config;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import static com.ebda3.Helpers.Config.cartData;


public class ChooseLocation extends AppCompatActivity implements OnMapReadyCallback {

    Activity activity = this;
    Context context = this;

    private GoogleMap mMap;
    public Boolean waitGPS = true;

    Double mLat = 0.0;
    Double mLong = 0.0;
    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION};
    LocationManager locationManager;
    double latitude; // latitude
    double longitude;
    MarkerOptions mo;
    LinearLayout notificationContainer;
    TextView notificationNumber;
    private String Location = "";
    int REQUEST_PLACE_PICKER = 1;

    private LatLng destination = null;
    private LatLng origin;

    EditText address;
    Button LoadBranch;
    LinearLayout loading;

    ImageView map_load;
    Handler handler = new Handler();
    private Toolbar detailsToolbar;
    TextView headline;

    @Override
    protected void onResume() {
        super.onResume();
        if (cartData.size() > 0) {
            notificationNumber.setVisibility(View.VISIBLE);
            notificationNumber.setText(String.valueOf(cartData.size()));
        } else {
            notificationNumber.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        detailsToolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(detailsToolbar);
        notificationNumber = detailsToolbar.findViewById(R.id.notificationNum);
        notificationContainer = detailsToolbar.findViewById(R.id.notificationB);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) detailsToolbar.findViewById(R.id.app_headline);
        headline.setText("تحديد الموقع");


        map_load = (ImageView) findViewById(R.id.map_load);
        loading = (LinearLayout) findViewById(R.id.loading);
        LoadBranch = (Button) findViewById(R.id.LoadBranch);
        address = (EditText) findViewById(R.id.address);

        LoadBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, SuppliersCart.class);
                myIntent.putExtra("mylocation", Config.GetUserLocation(context));
                Log.e("123123", Config.GetUserLocation(context));
                startActivity(myIntent);
                //GetData();
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapFragment.getMapAsync((OnMapReadyCallback) activity);
        // mo = new MarkerOptions().position(new LatLng(0, 0)).title("موقعى الأن");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else {
            TrackGPS gps = new TrackGPS(activity);
            if (gps.canGetLocation()) {
                longitude = gps.getLongitude();
                latitude = gps.getLatitude();
                mo = new MarkerOptions().position(new LatLng(latitude, longitude)).title("موقعى الأن");
            }
        }
    }


    public void GetAgain() {
        loading.setVisibility(View.GONE);
        address.setEnabled(true);
        LoadBranch.setEnabled(true);
    }

    public void ShowDialog(String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog customDialog = builder.create();

        LayoutInflater layoutInflater
                = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog, null);
        Button btn = (Button) view.findViewById(R.id.idButton);
        TextView message = (TextView) view.findViewById(R.id.message);
        message.setText(Message);
        btn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                customDialog.hide();

            }
        });


        customDialog.setView(view);
        customDialog.show();
    }
//    public void GetData() {
//        Log.d("loadData","loadData");
//
//            loading.setVisibility(View.VISIBLE);
//            address.setEnabled(false);
//            LoadBranch.setEnabled(false);
//
//            String VolleyUrl = RestaurantURL ;
//            Log.d("responser", String.valueOf(VolleyUrl));
//            try
//            {
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, VolleyUrl, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("response", response);
//                        Log.d("response", "response");
//                        //response = fixEncoding (response);
//
//
//                        SetUserAddress(context,address.getText().toString());
//
//                        try {
//                            JSONArray array = new JSONArray(response);
//                            if (array.length() > 0) {
//                                JSONObject row = array.getJSONObject(0);
//                                String ID = row.getString("ID").toString();
//                                String ShippingCost = row.getString("ShippingCost").toString();
//                                String NameBranch = row.getString("name").toString();
//                                String Photo = row.getString("photo").toString();
//                                String Address = row.getString("address").toString();
//                                String MapLocation = row.getString("location").toString();
//                                String KMS = row.getString("distance_").toString();
//                                String Available = row.getString("Opened").toString();
//                                String Phone = row.getString("phone_num1").toString();
//                                String Phone1 = row.getString("phone_num2").toString();
//
//                                Intent intent = new Intent(context,FoodMenu.class);
//                                intent.putExtra("ID",ID);
//                                intent.putExtra("ShippingCost",ShippingCost);
//                                intent.putExtra("Address",Address);
//                                intent.putExtra("MapLocation",MapLocation);
//                                intent.putExtra("NameBranch",NameBranch);
//                                intent.putExtra("Photo",Photo);
//                                intent.putExtra("KMS",KMS);
//                                intent.putExtra("Phone",Phone);
//                                intent.putExtra("Phone1",Phone1);
//                                intent.putExtra("Available",Available);
//                                startActivity(intent);
//                                GetAgain();
//
//                            }
//                            else
//                            {
//                                GetAgain();
//                                ShowDialog("لا يوجد فرع متاح حاليا فى هذا المنطقة برجاء البحث فى منطقة اخرى");
//                            }
//
//
//                        } catch (JSONException e) {
//                            Log.d("ffffff",e.getMessage());
//                            e.printStackTrace();
//                            GetAgain();
//                            ShowDialog("لا يوجد فرع متاح حاليا فى هذا المنطقة برجاء البحث فى منطقة اخرى");
//                        }
//
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //Log.d("ffffff",error.getMessage());
//                        GetAgain();
//                        ShowDialog("يوجد مشكلة برجاء المحاولة مرة اخرى");
//                    }
//                }) {
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//
//
//                        Map<String, String> paramas = new HashMap();
//
//
//                        //paramas.put("json_email", Config.getJsonEmail(context) );
//                        //paramas.put("json_password", Config.getJsonPassword(context));
//                        paramas.put("Location", Location );
//                        paramas.put("SortBy", "NearestBranch" );
//                        Log.d("params",paramas.toString());
//                        return paramas;
//                    }
//                };
//
//                int socketTimeout = 10000;//30 seconds - change to what you want
//                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//                stringRequest.setRetryPolicy(policy);
//                RequestQueue queue = Volley.newRequestQueue( context );
//
//                queue.add(stringRequest);
//            }
//            catch (Exception e)
//            {
//                GetAgain();
//                ShowDialog("يوجد مشكلة برجاء المحاولة مرة اخرى");
//            }
//        }


    public void onPickButtonClick(View v) {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, REQUEST_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException e) {
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_PLACE_PICKER && resultCode == Activity.RESULT_OK) {
            // The user has selected a place. Extract the name and address.
            Place place = PlacePicker.getPlace(data, this);
            origin = place.getLatLng();
            mMap.clear();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16));


        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else {
            TrackGPS gps = new TrackGPS(activity);
            if (gps.canGetLocation()) {
                longitude = gps.getLongitude();
                latitude = gps.getLatitude();
                Location = latitude + "," + longitude;
                LatLng sydney = new LatLng(latitude, longitude);
                origin = sydney;
                //mMap.addMarker(new MarkerOptions().position(sydney).title("موقعى."));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
            }
        }

        if (!isLocationEnabled()) {
            showAlert(1);
            waitGPS = true;
        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
                mMap.clear();
                Location = arg0.latitude + "," + arg0.longitude;
                LatLng sydney = new LatLng(arg0.latitude, arg0.longitude);
                origin = sydney;
                //mMap.addMarker(new MarkerOptions().position(sydney).title("موقعى."));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


            }
        });

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {


                map_load.setVisibility(View.GONE);
                handler.removeCallbacksAndMessages(context);


                /*Double lat = 0.0;
                lat = destination.latitude;

                Double lng = 0.0;
                lng = destination.longitude;
                String lat_s = String.valueOf(lat);
                String lng_s = String.valueOf(lng);*/

                if (destination != null) {


                    //getCost();

                } else {

                    mLat = Double.valueOf(mMap.getCameraPosition().target.latitude);
                    mLong = Double.valueOf(mMap.getCameraPosition().target.longitude);

                    origin = new LatLng(mLat, mLong);
                    String sPlace = "";

                    Config.SetUserLocation(context, mLat + "," + mLong);

                    String address = "";
                    Geocoder geocoder;
                    List<Address> addresses;
                    try {
                        geocoder = new Geocoder(context, Locale.getDefault());
                        Log.d("mLat", mLat.toString());
                        addresses = geocoder.getFromLocation(mLat, mLong, 1);
                        if (!addresses.isEmpty()) {
                            Log.d("addresses", addresses.toString());
                            address = addresses.get(0).getAddressLine(0);
                            String city = addresses.get(0).getAddressLine(1);
                            String country = addresses.get(0).getAddressLine(2);
                            if (address != null) {
                                String[] splitAddress = address.split(",");

                                for (int x = 0; x < splitAddress.length; x++) {
                                    sPlace += splitAddress[x] + " , ";
                                }
                                if (city != null && !city.isEmpty()) {
                                    String[] splitCity = city.split(",");
                                    sPlace += splitCity[0];
                                }
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        map_load.setVisibility(View.VISIBLE);
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                map_load.setVisibility(View.GONE);
                                            }
                                        }, 5000);
                                    }
                                }, 1000);

                                Config.SetUserLocation(context, mLat + "," + mLong + "," + sPlace.replace(",", "."));


//                            TextView _address_line1 = (TextView) findViewById(R.id.address_line1);
//                            _address_line1.setText(address);

                                TextView _address_line2 = (TextView) findViewById(R.id.address_line2);
                                _address_line2.setText(sPlace);
                            }
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Location = mLat + "," + mLong;

                }


            }
        });

//        mMap.setMinZoomPreference(17.0f);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
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

    private void showAlert(final int status) {
        String message, title, btnText;
        if (status == 1) {
            message = "Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                    "use this app";
            title = "Enable Location";
            btnText = "Location Settings";
        } else {
            message = "Please allow this app to access location!";
            title = "Permission access";
            btnText = "Grant";
        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnText, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (status == 1) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        } else
                            requestPermissions(PERMISSIONS, PERMISSION_ALL);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
