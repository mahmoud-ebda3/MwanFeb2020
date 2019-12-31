package com.ebda3.mwan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ebda3.adapters.BidOffersListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BidOffers extends AppCompatActivity {

    Activity activity = this;

    public Toolbar toolbar;
    public TextView headline;
    public ImageView AddBidReplay;
    public String id;

    Context context = this;
    LinearLayout add;

    ListView listView;
    public TextView no_data;
    Typeface typeface;

    BidOffersListAdapter adapter;

    public int StartFrom = 0;
    public int LastStartFrom = 0;
    public int VolleyCurrentConnection = 0;
    public int LimitBerRequest = 10;

    View footerView;

    public ArrayList<String> ID = new ArrayList<String>();
    public ArrayList<String> Notes = new ArrayList<String>();
    public ArrayList<String> Date = new ArrayList<String>();
    public ArrayList<String> Amount = new ArrayList<String>();
    public ArrayList<String> SupplierName = new ArrayList<String>();
    public ArrayList<String> SupplierPhoto = new ArrayList<String>();
    public ArrayList<String> SupplierPhone = new ArrayList<String>();
    public ArrayList<String> SupplierAddress = new ArrayList<String>();


    public Boolean setAdapterStatus = false;

    ProgressBar loadProgress;

    public String BidID, Bids, addReplay = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_offers);
        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);
        AddBidReplay = (ImageView) toolbar.findViewById(R.id.add_bid_replay_img);
        listView = (ListView) findViewById(R.id.List);
        loadProgress = (ProgressBar) findViewById(R.id.loadProgress);
        footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_footer, null, false);
        no_data = (TextView) findViewById(R.id.no_data);
        listView.setVisibility(View.VISIBLE);
        BidID = getIntent().getStringExtra("ID");
        Bids = getIntent().getStringExtra("Bids");
        if (getIntent().hasExtra("addReplay")) {
            addReplay = getIntent().getStringExtra("addReplay");
            AddBidReplay.setVisibility(View.VISIBLE);
            AddBidReplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddBidReplay.class);
                    intent.putExtra("BidID", BidID);
                    startActivity(intent);
                }
            });
        }


        headline.setText("المناقصة رقم   " + BidID);

        if (Bids.length() > 3) {
            try {
                JSONArray jsonArray = new JSONArray(Bids);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject row = jsonArray.getJSONObject(i);
                    ID.add(row.getString("ID").toString());
                    Notes.add(row.getString("notes").toString());
                    Date.add(row.getString("add_date").toString());
                    Amount.add(row.getString("amount").toString());
                    SupplierName.add(row.getString("supplier_name").toString());
                    SupplierPhoto.add(row.getString("photo").toString());
                    SupplierPhone.add(row.getString("supplier_phone").toString());
                    SupplierAddress.add(row.getString("address").toString());
                }
                adapter = new BidOffersListAdapter(activity, ID, Notes, Date, Amount, SupplierName, SupplierPhoto, SupplierPhone, SupplierAddress);
                listView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
