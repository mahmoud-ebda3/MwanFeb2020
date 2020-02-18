package com.ebda3.mwan;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;

import com.ebda3.Model.CalcCart;
import com.ebda3.Model.Cart;
import com.ebda3.adapters.NewCartAdapter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.ebda3.Helpers.Config.cartData;


/**
 * Created by work on 29/10/2017.
 */

public class NewCart extends NavigationViewActivity implements OnMapReadyCallback {

    public ProgressDialog progressDialog;

    public static GridView cart_product;
    SharedPreferences sp;
    public Button apply_buy_bu, buy_cart_product;
    public LinearLayout Totals;
    public static TextView total_price, shiping_price, net_price;
    TextView no_data;
    Context context = this;
    Activity activity = this;

    public ProgressBar buy_progress;
    public LayoutInflater inflater;
    public View dialoglayout;
    AlertDialog.Builder builder;
    private ArrayList<String> ProductName = new ArrayList<String>();
    private ArrayList<Integer> ProductID = new ArrayList<Integer>();
    private ArrayList<Integer> Productcount = new ArrayList<Integer>();
    private ArrayList<String> Productphoto = new ArrayList<String>();
    private ArrayList<Float> ProductPrice = new ArrayList<Float>();

    public ArrayList<String> ItemPartnerName = new ArrayList<String>();
    public ArrayList<Integer> ItemAvailableAmount = new ArrayList<Integer>();
    public ArrayList<Integer> ItemPartnerID = new ArrayList<Integer>();
    LinearLayout my_location, buy_linear;
    Button bu_my_location, productReturnButton;

    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayList<ArrayList<String>> DetailsName = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> DetailsValue = new ArrayList<ArrayList<String>>();

    public Toolbar toolbar;
    public TextView headline;
    public static NewCartAdapter adapter;

    String u_location;
    private GoogleMap mMap;
    public Boolean waitGPS = true;
    String Location, LastLocation, LanLongLoc;
    RelativeLayout shopping_cart_image;
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
    SharedPreferences sharedPreferences;
    String[] LocationData;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_new_cart, null, false);

        toolbar = (Toolbar) drawer.findViewById(R.id.app_toolbar);
        frameLayout = drawer.findViewById(R.id.frame_layout);
        frameLayout.addView(contentView);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);

        shopping_cart_image = toolbar.findViewById(R.id.notification_container);
        shopping_cart_image.setVisibility(View.VISIBLE);
        buy_cart_product = (Button) contentView.findViewById(R.id.buy_cart_product);
        productReturnButton = contentView.findViewById(R.id.product_return_button);
        SharedPreferences sp1 = this.getSharedPreferences("Location", 0);
        u_location = sp1.getString("mylocation", " ");
        bu_my_location = (Button) contentView.findViewById(R.id.get_my_location_button);
        my_location = (LinearLayout) contentView.findViewById(R.id.linear_my_locaion_button);
        buy_linear = (LinearLayout) contentView.findViewById(R.id.linear_buy_button);
//        if (u_location.length()<3)
//        {
//            my_location.setVisibility(View.VISIBLE);
//            buy_linear.setVisibility(View.GONE);
//        }
//
//        else
//        {
//            my_location.setVisibility(View.GONE);
//            buy_linear.setVisibility(View.VISIBLE);
//        }


        headline.setText("سلة المشتريات");
        shopping_cart_image.setVisibility(View.VISIBLE);
        shopping_cart_image.setVisibility(View.GONE);
        buy_cart_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewCart.this, SuppliersCart.class);
                myIntent.putExtra("mylocation", u_location);
                startActivity(myIntent);
            }
        });
        productReturnButton.setOnClickListener(click -> {
            Intent i = new Intent(this, MaterialActivity.class);
            i.putExtra("id", "from_cart");
            startActivity(i);
        });
        bu_my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChooseLocation.class);
                startActivity(intent);
            }
        });


        DetailsName.clear();
        DetailsValue.clear();


        Cart cart = new Cart();

        // Log.d("CartDataArray","after: "+String.valueOf(cartData.get(0).getDetailsName()));

        for (Cart cart1 : cartData) {
            Log.e("1312", cart1.toString());
            ProductID.add(cart1.getID());
            ProductName.add(cart1.getName());
            Productphoto.add(cart1.getPhoto());
            Productcount.add(cart1.getAmount());
            ProductPrice.add(cart1.getPrice());
            ItemAvailableAmount.add(cart1.getItemAvailableAmount());
            ItemPartnerID.add(cart1.getPartner_ID());
            ItemPartnerName.add(cart1.getPartnerName());
            DetailsName.add(cart1.getDetailsName());
            DetailsValue.add(cart1.getDetailsValue());
            Log.d("CartDataArray", "after: " + String.valueOf(cart1.getDetailsName()));
            Log.d("CartDataArray", "after: " + String.valueOf(cart1.getDetailsValue()));
        }


        cart_product = (GridView) contentView.findViewById(R.id.grid_cart_products);
        Totals = (LinearLayout) contentView.findViewById(R.id.Totals);


        no_data = (TextView) contentView.findViewById(R.id.no_data);

        Log.d("detailssss", DetailsName.toString());

        adapter = new NewCartAdapter(activity, ProductName, Productphoto, ProductPrice, Productcount, ItemAvailableAmount, ItemPartnerID, ItemPartnerName, DetailsName, DetailsValue);
        cart_product.setAdapter(adapter);
        CalcCart calcCart = new CalcCart();
        calcCart.GetAll();
        apply_buy_bu = (Button) contentView.findViewById(R.id.buy_cart_product);
        if (ProductID.isEmpty()) {
            no_data.setVisibility(View.VISIBLE);
            apply_buy_bu.setVisibility(View.GONE);
            //Totals.setVisibility(View.GONE);
        } else {
            no_data.setVisibility(View.GONE);
            apply_buy_bu.setVisibility(View.VISIBLE);
            //Totals.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PLACE_PICKER && resultCode == Activity.RESULT_OK) {
            // The user has selected a place. Extract the name and address.
            Place place = PlacePicker.getPlace(data, this);
            origin = place.getLatLng();
            mMap.clear();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16));
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


                if (u_location.length() < 3) {

                    latitude = lastlatitude;
                    longitude = lastlongitude;
                    Location = latitude + "||" + longitude;
                } else {
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

                if (destination != null) {


                    //getCost();

                } else {

                    mLat = mMap.getCameraPosition().target.latitude;
                    mLong = mMap.getCameraPosition().target.longitude;

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
            Intent myIntent = new Intent(NewCart.this, UserHomeActivity.class);
            startActivity(myIntent);
        }
        return super.onKeyDown(keyCode, event);
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
