package com.ebda3.mwan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ebda3.adapters.MyOrdersListAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BuildingNeedsDetails  extends AppCompatActivity {


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

    EditText num_of_rooms,num_of_windows,num_of_doors;

    String NumOfRooms , NumOfDoors , NumOfWindows;

    ImageView next_bu;

    public Boolean setAdapterStatus = false;





    public  Activity context = this;

    ProgressBar loadProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_needs_details);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);
        headline.setText("احتياجات عقارك");



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent myIntent = new Intent(BuildingNeedsDetails.this, BuildingNeeds.class);
            startActivity(myIntent);
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent myIntent = new Intent(BuildingNeedsDetails.this, BuildingNeeds.class);
            startActivity(myIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
