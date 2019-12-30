package com.ebda3.mwan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ebda3.adapters.MyPropertiesItemsListAdapter;
import com.ebda3.adapters.SupplierConfirmItemsListAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.ebda3.Helpers.Config.imageupload;

public class MyOrdersItems extends AppCompatActivity {

    public ProgressDialog progressDialog;

    ListView listView;

    public static GridView cart_product;
    SharedPreferences sp;
    public Button apply_buy_bu;
    public LinearLayout Totals;
    public String IntentID, IntentName, IntentItems, IntentTotal, IntentshippingCost, IntentNet, IntentSupplierName, IntentSupplierPhone, IntentSupplierPhoto, IntentStatus, IntentDate,ItemsArray;
    public static TextView total_price, shiping_price, net_price;
    TextView no_data;
    Context context = this;
    Activity activity = this;

    public ProgressBar buy_progress;
    public LayoutInflater inflater;
    public View dialoglayout;
    AlertDialog.Builder builder;

    private ArrayList<String> ProductName = new ArrayList<String>();
    private ArrayList<String> ProductID = new ArrayList<String>();
    private ArrayList<String> ProductCount = new ArrayList<String>();
    private ArrayList<String> TotalPrice = new ArrayList<String>();
    private ArrayList<String> ProductPrice = new ArrayList<String>();

    public ArrayList<String> ItemPartnerName = new ArrayList<String>();
    public ArrayList<Integer> ItemAvailableAmount = new ArrayList<Integer>();
    public ArrayList<Integer> ItemPartnerID = new ArrayList<Integer>();


    private ArrayList<String> ID = new ArrayList<String>();
    private ArrayList<String> Name = new ArrayList<String>();
    private ArrayList<String> Photo = new  ArrayList<String>();
    private ArrayList<String> Amount = new  ArrayList<String>();
    private ArrayList<String> ItemAvailableAmountArray = new  ArrayList<String>();
    private ArrayList<String> totalPrice = new  ArrayList<String>();
    private ArrayList<String> Info = new  ArrayList<String>();

    ArrayList<String> arrayList = new ArrayList<String>();

    public Toolbar toolbar;
    public TextView headline, PartnerName, PartnerPhone, OrderDate;
    public ImageView shopping_cart_image, photo;
    public SupplierConfirmItemsListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_items);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);

        LinearLayout step1 = (LinearLayout) findViewById(R.id.step1);
        LinearLayout step2 = (LinearLayout) findViewById(R.id.step2);
        LinearLayout step3 = (LinearLayout) findViewById(R.id.step3);


        cart_product = (GridView) findViewById(R.id.grid_cart_products);
        Totals = (LinearLayout) findViewById(R.id.Totals);

        PartnerName = (TextView) findViewById(R.id.PartnerName);
        PartnerPhone = (TextView) findViewById(R.id.PartnerPhone);
        photo = (ImageView) findViewById(R.id.photo);
        OrderDate = (TextView) findViewById(R.id.OrderDate);

        total_price = (TextView) findViewById(R.id.total_price);
        shiping_price = (TextView) findViewById(R.id.shiping_price);
        net_price = (TextView) findViewById(R.id.net_price);
        no_data = (TextView) findViewById(R.id.no_data);
        listView = (ListView) findViewById(R.id.list);


        IntentID = getIntent().getStringExtra("ID");
        IntentName = getIntent().getStringExtra("Name");
        IntentItems = getIntent().getStringExtra("Items");
        IntentTotal = getIntent().getStringExtra("Total");
        IntentshippingCost = getIntent().getStringExtra("shippingCost");
        IntentNet = getIntent().getStringExtra("Net");
        IntentSupplierPhone = getIntent().getStringExtra("SupplierPhone");
        IntentSupplierName = getIntent().getStringExtra("SupplierName");
        IntentSupplierPhoto = getIntent().getStringExtra("SupplierPhoto");
        IntentStatus = getIntent().getStringExtra("Status");
        IntentDate = getIntent().getStringExtra("Date");
        ItemsArray = getIntent().getStringExtra("ItemsArray");

        Log.d("orrrrrddd",IntentID +"----"+IntentName +"----"+IntentItems +"----"+IntentTotal +"----"
                +IntentshippingCost +"----"+IntentNet +"----"+IntentSupplierPhone +"----"
                +IntentSupplierName +"----"+IntentSupplierPhoto +"----"+IntentStatus +"----"+IntentDate +"----");


//        if (IntentStatus.equals("1")) {
            step2.setVisibility(View.VISIBLE);
//        }
//        if (IntentStatus.equals("2")) {
//            step2.setVisibility(View.VISIBLE);
//            step3.setVisibility(View.VISIBLE);
//        }


        PartnerName.setText(IntentSupplierName);
        if (IntentSupplierPhone.length() > 1) {
            PartnerPhone.setText(IntentSupplierPhone);
            PartnerPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent DialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + IntentSupplierPhone));
                    startActivity(DialIntent);
                }
            });
        } else {
            PartnerPhone.setVisibility(View.GONE);
        }

        if (IntentSupplierPhoto.length() > 1) {
            Picasso.with(this).load(imageupload + IntentSupplierPhoto)
                    .resize(70, 70)
                    .centerCrop()
                    .transform(new CropCircleTransformation())
                    .error(R.drawable.ic_account_circle_white_48dp)
                    .into(photo);
        } else {
            photo.setVisibility(View.GONE);
        }

        total_price.setText(IntentTotal + "");
        shiping_price.setText(IntentshippingCost + "");
        net_price.setText(IntentNet + "");

        headline.setText(IntentName);

        LinearLayout orderDetails = (LinearLayout) findViewById(R.id.orderDetails);



        try {
            JSONArray jsonArray = new JSONArray(ItemsArray);
            Log.d("ordersitems",jsonArray.toString());
            if ( jsonArray.length() > 0 ) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject row = jsonArray.getJSONObject(i);
                    ID.add(row.getString("ID").toString());
                    Name.add(row.getString("ItemName").toString());
                    Photo.add("ffffffff");
                    Amount.add(row.getString("Amount").toString());
                    ItemAvailableAmountArray.add(row.getString("Price").toString());
                    totalPrice.add(row.getString("totalPrice").toString());
                    Info.add(row.getString("ItemOrderSpecs").toString());
                }
                adapter = new SupplierConfirmItemsListAdapter(MyOrdersItems.this, Name, Photo , Amount , ItemAvailableAmountArray , totalPrice ,Info  );
                listView.setAdapter(adapter);
                orderDetails.setVisibility(View.VISIBLE);
            } else {
                orderDetails.setVisibility(View.GONE);
            }

        } catch (Exception e) {

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
