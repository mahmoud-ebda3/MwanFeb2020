package com.ebda3.mwan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Helpers.Config;
import com.ebda3.adapters.SupplierConfirmItemsListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.ebda3.Helpers.Config.imageupload;

public class MyOrdersItems extends AppCompatActivity {

    public ProgressDialog progressDialog;

    ListView listView;

    public static GridView cart_product;
    SharedPreferences sp;
    public Button apply_buy_bu;
    public LinearLayout Totals;
    public String IntentID, IntentName, IntentItems, IntentTotal, IntentshippingCost, IntentNet, IntentSupplierName, IntentSupplierPhone, IntentSupplierPhoto, IntentStatus, IntentDate, ItemsArray, rated;
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
    private ArrayList<String> priceBeforeDiscount = new ArrayList<>();
    String OrderID;
    public ArrayList<String> ItemPartnerName = new ArrayList<String>();
    public ArrayList<Integer> ItemAvailableAmount = new ArrayList<Integer>();
    public ArrayList<Integer> ItemPartnerID = new ArrayList<Integer>();


    private ArrayList<String> ID = new ArrayList<String>();
    private ArrayList<String> Name = new ArrayList<String>();
    private ArrayList<String> Photo = new ArrayList<String>();
    private ArrayList<String> Amount = new ArrayList<String>();
    private ArrayList<String> ItemAvailableAmountArray = new ArrayList<String>();
    private ArrayList<String> totalPrice = new ArrayList<String>();
    private ArrayList<String> Info = new ArrayList<String>();
    float rating;
    String rating_msg;
    ArrayList<String> arrayList = new ArrayList<String>();

    public Toolbar toolbar;
    public TextView headline, PartnerName, PartnerPhone, OrderDate;
    public ImageView shopping_cart_image, photo;
    public SupplierConfirmItemsListAdapter adapter;
    ImageView rating_star;


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
        rating_star = toolbar.findViewById(R.id.rating_star);
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

        rating_star.setOnClickListener(click -> {
            BottomSheetDialog dialog = new BottomSheetDialog(context);
            dialog.setContentView(R.layout.rating_text_view_layout);
            RatingBar ratingBar = dialog.findViewById(R.id.rating_bar_item);
            EditText editText = dialog.findViewById(R.id.rating_bar_message);
            TextView confirmTV = dialog.findViewById(R.id.confirm_rating_tv_button);
            confirmTV.setOnClickListener(confirm -> {
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        if (fromUser) {
                            rating = ratingBar.getRating();
                            Log.e("rating23232", String.valueOf(rating));
                        }
                        Log.e("rating23232", String.valueOf(rating));
                    }
                });
                rating_msg = editText.getText().toString();
                if (rating_msg.isEmpty()) {
                    editText.setError("من فضلك أدخل تقييمك لمنتج أفضل");
                } else {
                    Log.e("rating", rating_msg);
                    dialog.dismiss();
                    putData(String.valueOf(ratingBar.getRating()));
                }
            });
            dialog.show();
        });

        IntentID =

                getIntent().

                        getStringExtra("ID");

        IntentName =

                getIntent().

                        getStringExtra("Name");

        IntentItems =

                getIntent().

                        getStringExtra("Items");

        IntentTotal =

                getIntent().

                        getStringExtra("Total");

        IntentshippingCost =

                getIntent().

                        getStringExtra("shippingCost");

        IntentNet =

                getIntent().

                        getStringExtra("Net");

        IntentSupplierPhone =

                getIntent().

                        getStringExtra("SupplierPhone");

        IntentSupplierName =

                getIntent().

                        getStringExtra("SupplierName");

        IntentSupplierPhoto =

                getIntent().

                        getStringExtra("SupplierPhoto");

        IntentStatus =

                getIntent().

                        getStringExtra("Status");

        IntentDate =

                getIntent().

                        getStringExtra("Date");

        ItemsArray = getIntent().getStringExtra("ItemsArray");
        rated = getIntent().getStringExtra("rated");

        Log.d("orrrrrddd", IntentID + "----" + IntentName + "----" + IntentItems + "----" + IntentTotal + "----"
                + IntentshippingCost + "----" + IntentNet + "----" + IntentSupplierPhone + "----"
                + IntentSupplierName + "----" + IntentSupplierPhoto + "----" + IntentStatus + "----" + IntentDate + "----");

        try {
            if (rated.equals("1")) {
                rating_star.setVisibility(View.GONE);
            } else {
                rating_star.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }
        if (IntentStatus.equals("2")) {
            step2.setVisibility(View.VISIBLE);
        }
        if (IntentStatus.equals("3")) {
            step3.setVisibility(View.VISIBLE);
        }


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
            Picasso.get().load(imageupload + IntentSupplierPhoto)
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
            Log.d("ordersitems", jsonArray.toString());
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject row = jsonArray.getJSONObject(i);
                    OrderID = row.getString("OrderID");
                    ID.add(row.getString("ID").toString());
                    Name.add(row.getString("ItemName").toString());
                    Photo.add("ffffffff");
                    Amount.add(row.getString("Amount").toString());
                    ItemAvailableAmountArray.add(row.getString("Price").toString());
                    totalPrice.add(row.getString("totalPrice").toString());
                    Info.add(row.getString("ItemOrderSpecs").toString());
                    priceBeforeDiscount.add(row.getString("totalPriceBeforeDiscount"));
                }
                adapter = new SupplierConfirmItemsListAdapter(MyOrdersItems.this, Name, Photo, Amount, ItemAvailableAmountArray, totalPrice, Info, priceBeforeDiscount);
                listView.setAdapter(adapter);
                orderDetails.setVisibility(View.VISIBLE);
            } else {
                orderDetails.setVisibility(View.GONE);
            }

        } catch (
                Exception e) {

        }

    }

    private void putData(String ratingBar) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://www.mawaneg.com/supplier/include/webService.php?json=true", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rating_star.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("asasda", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("do", "rate");
                params.put("json_email", Config.getJsonEmail(context));
                params.put("json_password", Config.getJsonPassword(context));
                params.put("rating_number", ratingBar);
                params.put("order_ID", OrderID);
                params.put("rating_msg", rating_msg);
                Log.d("paramxxxxxx", params.toString());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(activity);
        queue.add(stringRequest);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
