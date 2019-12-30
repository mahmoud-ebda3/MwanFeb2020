package com.ebda3.mwan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ebda3.Model.CalcCart;
import com.ebda3.Model.Cart;
import com.ebda3.adapters.CartAdapter;

import java.util.ArrayList;

import static com.ebda3.Helpers.Config.cartData;


/**
 * Created by work on 29/10/2017.
 */

public class CartActivity extends AppCompatActivity {

    public ProgressDialog progressDialog;

    public static GridView cart_product;
    SharedPreferences sp;
    public Button apply_buy_bu;
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

    ArrayList<String> arrayList = new ArrayList<String>();

    public Toolbar toolbar;
    public TextView headline;
    public ImageView back_image;
    RelativeLayout shopping_cart_image;
    public static CartAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);
        shopping_cart_image = (RelativeLayout) toolbar.findViewById(R.id.notificationB);
        headline.setText("سلة المشتريات");
        shopping_cart_image.setVisibility(View.VISIBLE);
        shopping_cart_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(CartActivity.this, CartActivity.class);
                startActivity(myIntent);
            }
        });

        Cart cart = new Cart();

        for (Cart cart1 : cartData) {
            ProductID.add(cart1.getID());
            ProductName.add(cart1.getName());
            Productphoto.add(cart1.getPhoto());
            Productcount.add(cart1.getAmount());
            ProductPrice.add(cart1.getPrice());
            ItemAvailableAmount.add(cart1.getItemAvailableAmount());
            ItemPartnerID.add(cart1.getPartner_ID());
            ItemPartnerName.add(cart1.getPartnerName());
        }


        cart_product = (GridView) findViewById(R.id.grid_cart_products);
        Totals = (LinearLayout) findViewById(R.id.Totals);

        total_price = (TextView) findViewById(R.id.total_price);
        shiping_price = (TextView) findViewById(R.id.shiping_price);
        net_price = (TextView) findViewById(R.id.net_price);
        no_data = (TextView) findViewById(R.id.no_data);

        adapter = new CartAdapter(activity, ProductName, Productphoto, ProductPrice, Productcount, ItemAvailableAmount, ItemPartnerID, ItemPartnerName);
        cart_product.setAdapter(adapter);
        CalcCart calcCart = new CalcCart();
        calcCart.GetAll();


        apply_buy_bu = (Button) findViewById(R.id.buy_cart_product);
        if (ProductID.isEmpty()) {
            no_data.setVisibility(View.VISIBLE);
            apply_buy_bu.setVisibility(View.GONE);
            Totals.setVisibility(View.GONE);
        } else {
            no_data.setVisibility(View.GONE);
            apply_buy_bu.setVisibility(View.VISIBLE);
            Totals.setVisibility(View.VISIBLE);
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
