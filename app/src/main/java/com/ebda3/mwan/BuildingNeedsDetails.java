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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ebda3.Model.KitchenView;
import com.ebda3.Model.RoomView;
import com.ebda3.adapters.MyOrdersListAdapter;

import java.util.ArrayList;

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

    ArrayList<EditText> Length_array = new ArrayList<>();


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
    String room_id,door_id,windos_id,bathroom_id,kitchen_id,plug_id;


    ImageView next_bu;

    public Boolean setAdapterStatus = false;

    LinearLayout RoomsLinear , DoorsLinear , WindowsLinear,BathroomLinear , KitchenLinear;

    String room_name;

    Button send_data_bu;

    ArrayList<RoomView> rooms_array = new ArrayList<>();
    ArrayList<KitchenView> kitchen_array = new ArrayList<>();




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

        send_data_bu = (Button) findViewById(R.id.finish_reg_bu);

        Intent intent = getIntent();
        NumOfRooms = intent.getStringExtra("num_of_rooms");
        NumOfDoors = intent.getStringExtra("num_of_doors");
        NumOfWindows = intent.getStringExtra("num_of_windows");
        NumOfBathrooms = intent.getStringExtra("num_of_bathrooms");
        NumOfKitchens = intent.getStringExtra("num_of_kitchens");
        NumOfPlugs = intent.getStringExtra("num_of_plugs");


        for (int i=0;i<Integer.parseInt(NumOfRooms) ; i++)
        {
            room_id="room_id_"+String.valueOf(i);
            room_name = "حجرة "+String.valueOf(i+1);

            addLayout(RoomsLinear,room_name,room_id);
        }

        for (int i=0;i<Integer.parseInt(NumOfDoors) ; i++)
        {
            door_id="door_id_"+String.valueOf(i);
            room_name = "شباك "+String.valueOf(i+1);
            addLayout(WindowsLinear,room_name,door_id);
        }

        for (int i=0;i<Integer.parseInt(NumOfWindows) ; i++)
        {
            windos_id="windows_id_"+String.valueOf(i);
            room_name = "باب "+String.valueOf(i+1);
            addLayout(DoorsLinear,room_name,windos_id);
        }

        for (int i=0;i<Integer.parseInt(NumOfBathrooms) ; i++)
        {
            bathroom_id="bathroom_id_"+String.valueOf(i);
            room_name = "حمام "+String.valueOf(i+1);

            addLayout2(BathroomLinear,room_name,bathroom_id);
        }
        for (int i=0;i<Integer.parseInt(NumOfKitchens) ; i++)
        {
            kitchen_id="kitchen_id_"+String.valueOf(i);
            room_name = "مطبخ "+String.valueOf(i+1);

            addLayout2(KitchenLinear,room_name,kitchen_id);
        }

        send_data_bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                boolean done = false;

                for (int i=0 ; i<rooms_array.size();i++)
                {
                    Log.d("room_object", rooms_array.get(i).getId()+"----"+rooms_array.get(i).getName_room()+"----"+rooms_array.get(i).getLength_txt().getText()+"----"+rooms_array.get(i).getWidth_txt().getText()+"----");
                }

                for (int i=0 ; i<kitchen_array.size();i++)
                {
                    Log.d("kitchen_object", kitchen_array.get(i).getId()+"----"+kitchen_array.get(i).getName_room()+"----"+kitchen_array.get(i).getLength_txt().getText()+"----"+kitchen_array.get(i).getWidth_txt().getText()+"----"+kitchen_array.get(i).getHeight_txt().getText()+"----");
                }

            }
        });




    }

    private void addLayout(LinearLayout myLinear,String name,String room_id) {
        View layout2 = LayoutInflater.from(this).inflate(R.layout.activtiy_building_needs_item, myLinear , false);




        TextView room_name = (TextView) layout2.findViewById(R.id.text_name_id);
        EditText length_txt = (EditText) layout2.findViewById(R.id.length_txt);
        EditText width_txt = (EditText) layout2.findViewById(R.id.width_txt);

        RoomView roomView = new RoomView();
        roomView.setId(room_id);
        roomView.setName_room(name);
        roomView.setLength_txt(length_txt);
        roomView.setWidth_txt(width_txt);

        rooms_array.add(roomView);


        Length_array.add(length_txt);



        room_name.setText(name);






        myLinear.addView(layout2);
    }

    private void addLayout2(LinearLayout myLinear, String name,String room_id) {
        View layout2 = LayoutInflater.from(this).inflate(R.layout.activity_building_needs_item_2, myLinear , false);

//        int LenID = getResources().getIdentifier(last_id_length, "id", getPackageName());
//        int WidthID = getResources().getIdentifier(last_id_width, "id", getPackageName());
//        int HeightID = getResources().getIdentifier(last_id_height, "id", getPackageName());

        TextView room_name = (TextView) layout2.findViewById(R.id.text_name_id);
        EditText length_txt = (EditText) layout2.findViewById(R.id.length_txt);
        EditText width_txt = (EditText) layout2.findViewById(R.id.width_txt);
        EditText height_txt = (EditText) layout2.findViewById(R.id.height_txt);


        KitchenView kitchenView = new KitchenView();
        kitchenView.setId(room_id);
        kitchenView.setName_room(name);
        kitchenView.setLength_txt(length_txt);
        kitchenView.setWidth_txt(width_txt);
        kitchenView.setHeight_txt(height_txt);

        kitchen_array.add(kitchenView);


        room_name.setText(name);

        Length_array.add(length_txt);



//        length_txt.setId(Integer.parseInt(length_id));
//        width_txt.setId(Integer.parseInt(width_id));
//        height_txt.setId(Integer.parseInt(height_id));


//        last_id_length = length_id;
//        last_id_width = width_id;
//        last_id_height =height_id;



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
