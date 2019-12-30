package com.ebda3.mwan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.ebda3.adapters.MyPropertiesItemsListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyPropertiesSubItems extends AppCompatActivity {

    Activity activity = this;
    Context context = this;
    ListView listView;
    String ProductName,SubItems;
    TextView name;

    public MyPropertiesItemsListAdapter adapter;
    private ArrayList<String> Name = new ArrayList<String>();
    private ArrayList<String> ProductID = new ArrayList<String>();
    private ArrayList<String> ProductCount = new  ArrayList<String>();
    private ArrayList<String> TotalPrice = new  ArrayList<String>();
    private ArrayList<String> ProductPrice = new  ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_properties_sub_items);

        name = (TextView) findViewById(R.id.name);
        listView = (ListView) findViewById(R.id.list);

        Intent intent = getIntent();
        ProductName = intent.getStringExtra("Name");
        SubItems = intent.getStringExtra("SubItems");

        Log.e("response",SubItems);
        name.setText(ProductName);

        try {
            JSONArray jsonArray = new JSONArray(SubItems);
            if ( jsonArray.length() > 0 ) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject row = jsonArray.getJSONObject(i);
                    Name.add(row.getString("ItemName"));
                    ProductPrice.add(row.getString("Prices"));
                    ProductCount.add(row.getString("Amounts"));
                    TotalPrice.add(row.getString("totalPrice"));

                }
                adapter = new MyPropertiesItemsListAdapter(activity, Name , ProductPrice , ProductCount , TotalPrice   );
                listView.setAdapter(adapter);
            }

        }catch (Exception e)
        {
            Log.e("response",e.getMessage().toString());
        }

    }
}
