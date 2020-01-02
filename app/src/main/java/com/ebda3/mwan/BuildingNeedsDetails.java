package com.ebda3.mwan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Helpers.Config;
import com.ebda3.Model.KitchenView;
import com.ebda3.Model.RoomView;
import com.ebda3.adapters.MyOrdersListAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    List<ArrayList<String>> rooms_array_send = new ArrayList<>();
    ArrayList<ArrayList<String>> kitchen_array_send = new ArrayList<>();

    ArrayList<String> building_data = new ArrayList<>();


    ProgressBar progressBar;

    public  Activity context = this;
    boolean done =true;

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

        progressBar = (ProgressBar) findViewById(R.id.register_progress_organization);

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
                done = true;



                for (int i=0 ; i<rooms_array.size();i++)
                {
                    if (rooms_array.get(i).getLength_txt().getText().toString().length()<1)
                    {
                        done=false;

                        rooms_array.get(i).getLength_txt().setError("");
                        rooms_array.get(i).getLength_txt().requestFocus();
                        rooms_array.get(i).getLength_txt().setFocusable(true);
                        rooms_array.get(i).getLength_txt().setSelected(true);


                        building_data=new ArrayList<>();
                        rooms_array_send =new ArrayList<>();

                        Toast.makeText(context,"برجاء استكمال جميع البيانات",Toast.LENGTH_SHORT).show();


                        break;
                    }
                    else if (rooms_array.get(i).getWidth_txt().getText().toString().length()<1)
                    {
                        done=false;

                        rooms_array.get(i).getWidth_txt().setError("");
                        rooms_array.get(i).getWidth_txt().requestFocus();
                        rooms_array.get(i).getWidth_txt().setFocusable(true);
                        rooms_array.get(i).getWidth_txt().setSelected(true);

                        building_data=new ArrayList<>();
                        rooms_array_send =new ArrayList<>();

                        Toast.makeText(context,"برجاء استكمال جميع البيانات",Toast.LENGTH_SHORT).show();

                        break;
                    }


                    building_data=new ArrayList<>();
                    building_data.add("\"len\" : " +rooms_array.get(i).getLength_txt().getText().toString()) ;
                    building_data.add("\"wid\" : "+rooms_array.get(i).getWidth_txt().getText().toString()) ;

                    Log.d("str_building",String.valueOf(building_data));

                    rooms_array_send.add(building_data);

                    Log.d("room_send_array",String.valueOf(rooms_array_send));

                    Log.d("room_object", rooms_array.get(i).getId()+"----"+rooms_array.get(i).getName_room()+"----"+rooms_array.get(i).getLength_txt().getText()+"----"+rooms_array.get(i).getWidth_txt().getText()+"----");
                }

                if (done)
                {
                    sennnd();
                }



            }
        });




    }

    public void sennnd()
    {

        for (int i=0 ; i<kitchen_array.size();i++)
        {

            if (kitchen_array.get(i).getLength_txt().getText().toString().length()<1)
            {
                done=false;

                kitchen_array.get(i).getLength_txt().setError("");
                kitchen_array.get(i).getLength_txt().requestFocus();
                kitchen_array.get(i).getLength_txt().setFocusable(true);
                kitchen_array.get(i).getLength_txt().setSelected(true);


                building_data=new ArrayList<>();
                kitchen_array_send =new ArrayList<>();

                Toast.makeText(context,"برجاء استكمال جميع البيانات",Toast.LENGTH_SHORT).show();

                break;
            }
            else if (kitchen_array.get(i).getWidth_txt().getText().toString().length()<1)
            {
                done=false;

                kitchen_array.get(i).getWidth_txt().setError("");
                kitchen_array.get(i).getWidth_txt().requestFocus();
                kitchen_array.get(i).getWidth_txt().setFocusable(true);
                kitchen_array.get(i).getWidth_txt().setSelected(true);

                building_data=new ArrayList<>();
                kitchen_array_send =new ArrayList<>();

                Toast.makeText(context,"برجاء استكمال جميع البيانات",Toast.LENGTH_SHORT).show();

                break;


            }
            else if (kitchen_array.get(i).getHeight_txt().getText().toString().length()<1)
            {
                done=false;

                kitchen_array.get(i).getHeight_txt().setError("");
                kitchen_array.get(i).getHeight_txt().requestFocus();
                kitchen_array.get(i).getHeight_txt().setFocusable(true);
                kitchen_array.get(i).getHeight_txt().setSelected(true);


                building_data=new ArrayList<>();
                kitchen_array_send =new ArrayList<>();

                Toast.makeText(context,"برجاء استكمال جميع البيانات",Toast.LENGTH_SHORT).show();

                break;
            }



            building_data=new ArrayList<>();
            building_data.add("\"len\" : " +kitchen_array.get(i).getLength_txt().getText().toString());
            building_data.add("\"wid\" : " +kitchen_array.get(i).getWidth_txt().getText().toString()) ;
            building_data.add("\"heig\" : " +kitchen_array.get(i).getHeight_txt().getText().toString());

            Log.d("str_building",String.valueOf(building_data));

            kitchen_array_send.add(building_data);

            Log.d("kitchen_send_array",String.valueOf(kitchen_array_send));


            Log.d("kitchen_object", kitchen_array.get(i).getId()+"----"+kitchen_array.get(i).getName_room()+"----"+kitchen_array.get(i).getLength_txt().getText()+"----"+kitchen_array.get(i).getWidth_txt().getText()+"----"+kitchen_array.get(i).getHeight_txt().getText()+"----");
        }

        if (done) {

            senddata();
        }
    }


    public void senddata()
    {
        boolean t = isNetworkConnected();

        if(t) {

            progressBar.setVisibility(View.VISIBLE);
            send_data_bu.setVisibility(View.GONE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "url",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                progressBar.setVisibility(View.GONE);
                                send_data_bu.setVisibility(View.VISIBLE);

                                JSONObject jsonObject = new JSONObject ( response );


                            }
                            catch (Exception e)
                            {

                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            send_data_bu.setVisibility(View.VISIBLE);

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("do", "AddComplaint");
                    params.put("json_email", Config.getJsonEmail(context));
                    params.put("json_password", Config.getJsonPassword(context));
                    params.put("NumOfRooms", NumOfRooms);
                    params.put("NumOfDoors", NumOfDoors);
                    params.put("NumOfWindows",NumOfWindows);
                    params.put("NumOfBathrooms", NumOfBathrooms);
                    params.put("NumOfKitchens", NumOfKitchens);
                    params.put("RoomsData",  String.valueOf(rooms_array_send.subList(0,Integer.parseInt(NumOfRooms))));
                    params.put("WindowsData",  String.valueOf(rooms_array_send.subList(Integer.parseInt(NumOfRooms),Integer.parseInt(NumOfRooms)+Integer.parseInt(NumOfWindows))));
                    params.put("DoorsData",  String.valueOf(rooms_array_send.subList(Integer.parseInt(NumOfRooms)+Integer.parseInt(NumOfWindows),Integer.parseInt(NumOfRooms)+Integer.parseInt(NumOfWindows)+Integer.parseInt(NumOfDoors))));
                    params.put("BathroomData",  String.valueOf(kitchen_array_send.subList(0,Integer.parseInt(NumOfBathrooms))));
                    params.put("KitchenData",  String.valueOf(kitchen_array_send.subList(Integer.parseInt(NumOfBathrooms),Integer.parseInt(NumOfBathrooms)+Integer.parseInt(NumOfKitchens))));


                    Log.d("building_send_data", params.toString());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            send_data_bu.setVisibility(View.VISIBLE);
            Toast.makeText(context, "من فضلك تأكد من اتصالك بالإنترنت", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
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
