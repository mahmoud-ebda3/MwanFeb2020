package com.ebda3.mwan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ebda3.adapters.MyOrdersListAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class BuildingNeeds extends AppCompatActivity {


    Activity activity = this;

    public Toolbar toolbar;
    public TextView headline;
    public String id;


    LinearLayout header;

    MyOrdersListAdapter adapter ;

    ListView listView;
    public TextView no_data;
    Typeface typeface;

    public int StartFrom = 0;
    public int LastStartFrom = 0;
    public int VolleyCurrentConnection = 0;
    public int LimitBerRequest = 50;

    View footerView;

    EditText num_of_rooms,num_of_windows,num_of_doors,num_of_bathrooms,num_of_kitchens,num_of_plugs;

    String NumOfRooms , NumOfDoors , NumOfWindows, NumOfBathrooms, NumOfKitchens, NumOfPlugs;

    LinearLayout next_bu;

    public Boolean setAdapterStatus = false;





    public  Activity context = this;

    ProgressBar loadProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_needs);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);
        headline.setText("احتياجات عقارك");

        num_of_doors = (EditText) findViewById(R.id.num_of_doors);
        num_of_rooms = (EditText) findViewById(R.id.num_of_rooms);
        num_of_windows= (EditText) findViewById(R.id.num_of_windows);
        num_of_bathrooms= (EditText) findViewById(R.id.num_of_bathrooms);
        num_of_kitchens= (EditText) findViewById(R.id.num_of_kitchens);
        num_of_plugs= (EditText) findViewById(R.id.num_of_plugs);

        next_bu = (LinearLayout) findViewById(R.id.next_button_linear);

        next_bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if (checkfields())
                {
                    NumOfRooms = num_of_rooms.getText().toString();
                    NumOfDoors = num_of_doors.getText().toString();
                    NumOfWindows = num_of_windows.getText().toString();
                    NumOfBathrooms = num_of_bathrooms.getText().toString();
                    NumOfKitchens = num_of_kitchens.getText().toString();
                    NumOfPlugs = num_of_plugs.getText().toString();

                    Intent intent = new Intent(BuildingNeeds.this, BuildingNeedsDetails.class);

                    intent.putExtra("num_of_rooms", NumOfRooms);
                    intent.putExtra("num_of_doors", NumOfDoors);
                    intent.putExtra("num_of_windows", NumOfWindows);
                    intent.putExtra("num_of_bathrooms", NumOfBathrooms);
                    intent.putExtra("num_of_kitchens", NumOfKitchens);
                    intent.putExtra("num_of_plugs", NumOfPlugs);
                    startActivity(intent);
                }

            }
        });

        num_of_plugs.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /* Write your logic here that will be executed when user taps next button */

                    if (checkfields())
                    {
                        NumOfRooms = num_of_rooms.getText().toString();
                        NumOfDoors = num_of_doors.getText().toString();
                        NumOfWindows = num_of_windows.getText().toString();
                        NumOfBathrooms = num_of_bathrooms.getText().toString();
                        NumOfKitchens = num_of_kitchens.getText().toString();
                        NumOfPlugs = num_of_plugs.getText().toString();

                        Intent intent = new Intent(BuildingNeeds.this, BuildingNeedsDetails.class);

                        intent.putExtra("num_of_rooms", NumOfRooms);
                        intent.putExtra("num_of_doors", NumOfDoors);
                        intent.putExtra("num_of_windows", NumOfWindows);
                        intent.putExtra("num_of_bathrooms", NumOfBathrooms);
                        intent.putExtra("num_of_kitchens", NumOfKitchens);
                        intent.putExtra("num_of_plugs", NumOfPlugs);
                        startActivity(intent);
                    }

                    handled = true;
                }
                return handled;
            }
        });







    }

    public  boolean checkfields()
    {
        NumOfRooms = num_of_rooms.getText().toString();
        NumOfDoors = num_of_doors.getText().toString();
        NumOfWindows = num_of_windows.getText().toString();
        NumOfBathrooms = num_of_bathrooms.getText().toString();
        NumOfKitchens = num_of_kitchens.getText().toString();
        NumOfPlugs = num_of_plugs.getText().toString();


        if (num_of_rooms.getText().toString().length()<1)
        {
            num_of_rooms.setError("");
            num_of_rooms.requestFocus();
            num_of_rooms.setFocusable(true);
            num_of_rooms.setSelected(true);

        }
        else if (num_of_doors.getText().toString().length()<1)
        {
            num_of_doors.setError("");
            num_of_doors.requestFocus();
            num_of_doors.setFocusable(true);
            num_of_doors.setSelected(true);

        }
        else if (num_of_windows.getText().toString().length()<1)
        {
            num_of_windows.setError("");
            num_of_windows.requestFocus();
            num_of_windows.setFocusable(true);
            num_of_windows.setSelected(true);


        }
        else if (num_of_bathrooms.getText().toString().length()<1)
        {
            num_of_bathrooms.setError("");
            num_of_bathrooms.requestFocus();
            num_of_bathrooms.setFocusable(true);
            num_of_bathrooms.setSelected(true);

        }
        else if (num_of_kitchens.getText().toString().length()<1)
        {
            num_of_kitchens.setError("");
            num_of_kitchens.requestFocus();
            num_of_kitchens.setFocusable(true);
            num_of_kitchens.setSelected(true);


        }
        else if (num_of_plugs.getText().toString().length()<1)
        {
            num_of_plugs.setError("");
            num_of_plugs.setFocusable(true);
            num_of_plugs.setSelected(true);

        }
        else
        {
            return true;
        }

        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent myIntent = new Intent(BuildingNeeds.this, UserHomeActivity.class);
            startActivity(myIntent);
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent myIntent = new Intent(BuildingNeeds.this, UserHomeActivity.class);
            startActivity(myIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
