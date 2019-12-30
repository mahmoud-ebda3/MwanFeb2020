package com.ebda3.mwan;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class chooseMap extends AppCompatActivity  implements OnMapReadyCallback {

    SharedPreferences sp;

    public ProgressBar buy_progress;
    public LayoutInflater inflater;
    public View dialoglayout ;
    AlertDialog.Builder builder;


    Double mLat = 0.0;
    Double mLong = 0.0;
    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION};
    LocationManager locationManager;
    double latitude; // latitude
    double longitude;
    double lastlatitude; // latitude
    double lastlongitude;
    MarkerOptions mo;

    int REQUEST_PLACE_PICKER = 1;

    private LatLng destination = null;
    private LatLng origin;

    String[] LocationData;


    String u_location = "";
    private GoogleMap mMap;
    public Boolean waitGPS = true;
    String Location , LastLocation , LanLongLoc;


    Context context = this;
    Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        final Button reservation_button = (Button) findViewById(R.id.reserv_bu);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        try {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.maps);

            mapFragment.getMapAsync((OnMapReadyCallback) activity);

            mo = new MarkerOptions().position(new LatLng(0, 0)).title("موقعى الأن");
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

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

        reservation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.d("locationnnnn",Location);

                SharedPreferences sp=getSharedPreferences("Location", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("mylocation",Location);
                Ed.commit();


                if (Location.length()>1)
                {
                    Intent myIntent = new Intent(context, SuppliersCart.class);
                    myIntent.putExtra("mylocation", u_location);
                    startActivity(myIntent);
                }
                else
                {
                    Toast.makeText(context,"لم يتم تحديد الموقع",Toast.LENGTH_SHORT).show();
                    recreate();
                }





            }
        });




    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;


        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else {
            TrackGPS gps = new TrackGPS(activity);
            if (gps.canGetLocation()) {


                if (u_location.length()<3)
                {

                    latitude = lastlatitude;
                    longitude = lastlongitude;
                    Location = latitude + "||" + longitude;
                }

                else
                {
                    longitude = gps.getLongitude();
                    latitude = gps.getLatitude();
                    Location = latitude + "||" + longitude;
                }

                LatLng sydney = new LatLng(latitude, longitude);
                origin = sydney;

                mMap.addMarker(new MarkerOptions().position(sydney).title("موقعى."));
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
                Location = arg0.latitude + "||" + arg0.longitude;
                LatLng sydney = new LatLng(arg0.latitude, arg0.longitude);
                origin = sydney;
                mMap.addMarker(new MarkerOptions().position(sydney).title("موقعى."));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                //Toast.makeText(activity, Location, Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {





                /*Double lat = 0.0;
                lat = destination.latitude;

                Double lng = 0.0;
                lng = destination.longitude;
                String lat_s = String.valueOf(lat);
                String lng_s = String.valueOf(lng);*/

                if( destination != null ){


                    //getCost();

                }else{

                    mLat = Double.valueOf(mMap.getCameraPosition().target.latitude);
                    mLong = Double.valueOf(mMap.getCameraPosition().target.longitude);

                    origin = new LatLng(mLat, mLong);
                    String sPlace = "";


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
                                sPlace = splitAddress[0] + " ";
                                if (city != null && !city.isEmpty()) {
                                    String[] splitCity = city.split(",");
                                    sPlace += splitCity[0];
                                }

//                            TextView _address_line1 = (TextView) findViewById(R.id.address_line1);
//                            _address_line1.setText(address);


                            }
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Location = mLat + "||" + mLong + "||" + sPlace;

                }




                //Toast.makeText(activity, Location, Toast.LENGTH_SHORT).show();

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
        final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(this);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
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
