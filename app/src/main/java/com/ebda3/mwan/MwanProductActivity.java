package com.ebda3.mwan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ebda3.Model.Product;
import com.ebda3.adapters.MaterialAdapter;
import com.ebda3.adapters.MwanProductAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by work on 29/10/2017.
 */

public class MwanProductActivity extends AppCompatActivity {

    Activity context = this;
    public Spinner material_spinner;
    public String chosen_material;

    MaterialAdapter adapter;
    public ProgressDialog progressDialog;
    Activity activity = this;

    public Toolbar toolbar;
    public TextView headline;
    RelativeLayout shopping_cart_image;
    GridView product_grid;
    public static ArrayList<Product> products = new ArrayList<>();

    public static ArrayList<String> CartProductID = new ArrayList<String>();
    public static ArrayList<String> CartProductName = new ArrayList<String>();
    public static ArrayList<String> CartProductPhoto = new ArrayList<String>();
    public static ArrayList<String> CartProductCount = new ArrayList<String>();
    public static ArrayList<String> CartProductPrice = new ArrayList<String>();

    MwanProductAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mwan_items_product_activity);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);
        shopping_cart_image = (RelativeLayout) toolbar.findViewById(R.id.notificationB);
        headline.setText("المنتجات");
        shopping_cart_image.setVisibility(View.VISIBLE);
        shopping_cart_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MwanProductActivity.this, CartActivity.class);
                startActivity(myIntent);
            }
        });
        products = new ArrayList<>();
        product_grid = (GridView) findViewById(R.id.products_grid_view);


        progressDialog = new ProgressDialog(activity,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("جارى التحميل...");

        CartProductID = new ArrayList<String>();
        CartProductName = new ArrayList<String>();
        CartProductPhoto = new ArrayList<String>();
        CartProductCount = new ArrayList<String>();
        CartProductPrice = new ArrayList<String>();


        SharedPreferences sp1 = this.getSharedPreferences("MyShoppingCart", 0);
        String strJson = sp1.getString("Cart", "0");
        JSONObject jsonData2 = null;
        if (strJson.equals("0") || strJson == null || strJson.isEmpty()) {
        } else {

            try {

                jsonData2 = new JSONObject(strJson);
                JSONArray Jarray = jsonData2.getJSONArray("ID");
                JSONArray Jarray2 = jsonData2.getJSONArray("Name");
                JSONArray Jarray3 = jsonData2.getJSONArray("Photo");
                JSONArray Jarray4 = jsonData2.getJSONArray("Count");
                JSONArray Jarray5 = jsonData2.getJSONArray("Price");
                for (int i = 0; i < Jarray.length(); i++) {
                    CartProductID.add(Jarray.get(i).toString());
                    CartProductName.add(Jarray2.get(i).toString());
                    CartProductPhoto.add(Jarray3.get(i).toString());
                    CartProductCount.add(Jarray4.get(i).toString());
                    CartProductPrice.add(Jarray5.get(i).toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 20; i++) {
            Product pr = new Product();
            pr.setProductName("تجربة  " + String.valueOf(i));
            pr.setProductPrice("100 جنية");
            pr.setProductImage("asdasdasd");

            products.add(pr);
        }

        adapter2 = new MwanProductAdapter(MwanProductActivity.this, products);
        product_grid.setAdapter(adapter2);
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
