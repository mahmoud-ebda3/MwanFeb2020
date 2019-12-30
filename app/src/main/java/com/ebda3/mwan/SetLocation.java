package com.ebda3.mwan;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;


public class SetLocation extends AppCompatActivity {

    Activity activity = this;
    Context context = this;

    int REQUEST_PLACE_PICKER = 1;

    public ImageView SetLocation ;
    public Toolbar toolbar;
    public TextView headline;
    public String id,Name,Location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);
        headline.setText("تحديد موقعك");
        try {
            id = getIntent().getStringExtra("ID" );
        }
        catch (Exception e)
        {
            id = "0";
        }
        SetLocation = (ImageView)  findViewById(R.id.set_location);
        SetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onPickButtonClick(view);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                //Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                Location = String.valueOf(place.getLatLng().latitude + "," + place.getLatLng().longitude ) ;

                SharedPreferences sp=getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("Location",Location);
                Ed.commit();


            //Toast.makeText(activity, Location, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent( SetLocation.this , MaterialActivity.class );

            intent.putExtra("id",id );
            intent.putExtra("location",Location);
             startActivity(intent);

                Log.d("paramas",Location);

            }
        }
    }

    public void onPickButtonClick(View v) {
        // Construct an intent for the place picker
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), REQUEST_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
