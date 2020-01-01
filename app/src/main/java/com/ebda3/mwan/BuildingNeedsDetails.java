package com.ebda3.mwan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import androidx.cardview.widget.CardView;

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

    EditText num_of_rooms,num_of_windows,num_of_doors,num_of_bathrooms,num_of_kitchens,num_of_plugs;

    String NumOfRooms , NumOfDoors , NumOfWindows, NumOfBathrooms, NumOfKitchens, NumOfPlugs;

    ImageView next_bu;

    public Boolean setAdapterStatus = false;

    LinearLayout RoomsLinear , DoorsLinear , WindowsLinear,BathroomLinear , KitchenLinear;

    String room_name;





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

        RoomsLinear = (LinearLayout) findViewById(R.id.rooms_card_id);
        WindowsLinear = (LinearLayout) findViewById(R.id.windows_card_id);
        DoorsLinear = (LinearLayout) findViewById(R.id.door_card_id);
        BathroomLinear = (LinearLayout) findViewById(R.id.bathroom_card_id);
        KitchenLinear = (LinearLayout) findViewById(R.id.kitchen_card_id);

        Intent intent = getIntent();
        NumOfRooms = intent.getStringExtra("num_of_rooms");
        NumOfDoors = intent.getStringExtra("num_of_doors");
        NumOfWindows = intent.getStringExtra("num_of_windows");
        NumOfBathrooms = intent.getStringExtra("num_of_bathrooms");
        NumOfKitchens = intent.getStringExtra("num_of_kitchens");
        NumOfPlugs = intent.getStringExtra("num_of_plugs");


        for (int i=0;i<Integer.parseInt(NumOfRooms) ; i++)
        {
            room_name = "حجرة "+String.valueOf(i+1);
            Log.d("hhhhhtttt",NumOfRooms);
            addLayout(RoomsLinear,room_name);
        }

        for (int i=0;i<Integer.parseInt(NumOfDoors) ; i++)
        {
            room_name = "شباك "+String.valueOf(i+1);
            addLayout(WindowsLinear,room_name);
        }

        for (int i=0;i<Integer.parseInt(NumOfWindows) ; i++)
        {
            room_name = "باب "+String.valueOf(i+1);
            addLayout(DoorsLinear,room_name);
        }

        for (int i=0;i<Integer.parseInt(NumOfBathrooms) ; i++)
        {
            room_name = "حمام "+String.valueOf(i+1);
            addLayout2(BathroomLinear,room_name);
        }
        for (int i=0;i<Integer.parseInt(NumOfKitchens) ; i++)
        {
            room_name = "مطبخ "+String.valueOf(i+1);
            addLayout2(KitchenLinear,room_name);
        }

    }

    private void addLayout(LinearLayout myLinear,String name) {
        View layout2 = LayoutInflater.from(this).inflate(R.layout.activtiy_building_needs_item, myLinear , false);

        TextView room_name = (TextView) layout2.findViewById(R.id.text_name_id);
        EditText length_txt = (EditText) layout2.findViewById(R.id.length_txt);
        EditText width_txt = (EditText) layout2.findViewById(R.id.width_txt);


        room_name.setText(name);




        myLinear.addView(layout2);
    }

    private void addLayout2(LinearLayout myLinear, String name) {
        View layout2 = LayoutInflater.from(this).inflate(R.layout.activity_building_needs_item_2, myLinear , false);

        TextView room_name = (TextView) layout2.findViewById(R.id.text_name_id);
        EditText length_txt = (EditText) layout2.findViewById(R.id.length_txt);
        EditText width_txt = (EditText) layout2.findViewById(R.id.width_txt);
        EditText height_txt = (EditText) layout2.findViewById(R.id.height_txt);

        room_name.setText(name);



        myLinear.addView(layout2);
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
