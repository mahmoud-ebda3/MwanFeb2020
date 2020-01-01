package com.ebda3.mwan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telecom.Call;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebda3.Model.Cart;
import com.ebda3.Model.DetailsObject;
import com.ebda3.adapters.MaterialsDetailsListAdapter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.ebda3.Helpers.Config.cartData;
import static com.ebda3.Helpers.Config.imageupload;

public class MaterialsDetailsActivity extends AppCompatActivity {

    public static HashMap<String, String> selectedItems = new HashMap<>();
    public static HashMap<String, String> selectedItemsdetails = new HashMap<>();
    private ListView listView;
    Context context = this;
    private String ID, Name, Location, Photo, Details;
    private MaterialsDetailsListAdapter adapter;
    private Toolbar detailsToolbar;
    private TextView title;
    private ImageView photo;
    private RelativeLayout cartInfo;
    EditText item_count;
    private ArrayList<DetailsObject> detailsObjects = new ArrayList<>();
    LinearLayout add_cart;
    RelativeLayout cartIcon;
    Button cartButton;
    TextView numberIcon;
    String ItemName, ItemPhoto,ItemID;

    ArrayList<String> DetailsName = new ArrayList<>();
    ArrayList<String>  DetailsValue = new ArrayList<>();

    public static TextView notificationNum;
    public static RelativeLayout cart_info;
    public static CountDownTimer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        listView = findViewById(R.id.details_list_view);
        detailsToolbar = findViewById(R.id.app_toolbar);
        add_cart = findViewById(R.id.add_cart);
        photo = findViewById(R.id.item_photo);
        setSupportActionBar(detailsToolbar);
        cartButton = findViewById(R.id.cart_button);

        Intent intent = getIntent();
        ItemID = intent.getStringExtra("ID");
        ItemName = intent.getStringExtra("Name");
        ItemPhoto = intent.getStringExtra("Photo");


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        cart_info = (RelativeLayout) findViewById(R.id.cart_info);
        notificationNum = (TextView) findViewById(R.id.notificationNum);
        if (cartData.size() > 0) {
            notificationNum.setVisibility(View.VISIBLE);
            notificationNum.setText(String.valueOf(cartData.size()));
        }


        selectedItems.clear();


        cartInfo = findViewById(R.id.cart_info);
        cartIcon = detailsToolbar.findViewById(R.id.notificationB);
        numberIcon = detailsToolbar.findViewById(R.id.notificationNum);
        item_count = (EditText) findViewById(R.id.item_count);

        cartIcon.setVisibility(View.VISIBLE);
        Log.d("ppphhh",imageupload +ItemPhoto);
       // photo.setVisibility(View.VISIBLE);
        Picasso.with(context).load(imageupload +ItemPhoto)
                .resize(150, 150)
                .error(R.drawable.app_logo)
                .into(photo);

        cartIcon.setOnClickListener(click -> {
            Intent cartIntent = new Intent(context, NewCart.class);
            startActivity(cartIntent);
        });
        title = detailsToolbar.findViewById(R.id.app_headline);
        cartButton.setOnClickListener(click -> {
            Intent cartButton = new Intent(context, NewCart.class);
            startActivity(cartButton);
        });
        add_cart.setOnClickListener(click ->
        {




                String my_Count = item_count.getText().toString();
                Log.d("selectedItems",selectedItems.toString());
                Cart cart = new Cart();

                Boolean AddToArray = true;

                DetailsName=null;
                DetailsValue=null;
                DetailsName=new ArrayList<>();
                DetailsValue=new ArrayList<>();

//
//                for(Cart cart1 : cartData) {
//                    if(cart1.getID() != 0 && cart1.getID() == Integer.parseInt(ItemID) ) {
//
////                    cart1.setPrice( ItemPrice.get(position) );
////                    cart1.setShippingCost( shippingCost.get(position) );
//                        cart1.setAmount(cart1.getAmount()+1);
//                        AddToArray = false;
//                        //Toast.makeText(context, "Found it! " + String.valueOf(cart1.getAmount())   , Toast.LENGTH_SHORT).show();
//                    }
//                }

                if ( AddToArray ) {
                    cart.setID(Integer.parseInt(ItemID));
                    cart.setName(ItemName);
                    cart.setPhoto(ItemPhoto);
                    Gson gson = new Gson();
                    Log.d("selected_items","before :"+gson.toJson(selectedItems));
                    Log.d("selected_itemsdetails","before :"+gson.toJson(selectedItemsdetails));


//                cart.setPrice(Float.parseFloat(ItemID));
//                cart.setShippingCost(Float.parseFloat(ItemID));
//                cart.setItemAvailableAmount(Integer.parseInt(ItemID));
//                cart.setPartner_ID(Integer.parseInt(ItemID));
//                cart.setPartnerName(ItemName);

                    Iterator myVeryOwnIterator = selectedItemsdetails.keySet().iterator();
                    while(myVeryOwnIterator.hasNext()) {
                        String key=(String)myVeryOwnIterator.next();
                        DetailsName.add(key);
                        String value=(String)selectedItemsdetails.get(key);
                        DetailsValue.add(value);
                    }

                    selectedItems.clear();
                    selectedItemsdetails=null;
                    selectedItemsdetails=new HashMap<>();

                    Log.d("predetails",DetailsName.toString());
                    cart.setDetailsName(DetailsName);
                    cart.setDetailsValue(DetailsValue);
                    cart.setAmount(1);
                    cartData.add(cart);

                    Log.d("CartDataArray","before: "+String.valueOf(cartData.get(0).getDetailsName()));

                    cart.setSelectedItems(gson.toJson(selectedItems));


                    Log.d("selected_items","after :"+gson.toJson(selectedItems));
                    Log.d("selected_itemsdetails","after :"+gson.toJson(selectedItemsdetails));
                    //selectedItems.clear();
                }
                if ( cartData.size() > 0 ) {
                    notificationNum.setVisibility(View.VISIBLE);
                    notificationNum.setText( String.valueOf(cartData.size()) );
                }
                else
                {
                    notificationNum.setVisibility(View.GONE);
                }

                cart_info.setVisibility(View.VISIBLE);
                expand(ItemsActivity.cart_info);





            //Intent intent = new Intent(context, NewCart.class);
            //context.startActivity (intent);
        });
        ID = getIntent().getStringExtra("ID");
        Name = getIntent().getStringExtra("Name");
        Location = getIntent().getStringExtra("ID");
        Photo = getIntent().getStringExtra("ID");
        Details = getIntent().getStringExtra("Details");
        title.setText(Name);
//        Picasso.with(this).load(imageupload + Photo)
//                .into(photo);
        getData(Details);
    }

    private void getData(String response) {
        try {
            if (!response.isEmpty()) {
                JSONArray array = new JSONArray(response);
                Log.e("query", array.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    detailsObjects.add(new DetailsObject(
                            object.getString("ID"),
                            object.getString("name"),
                            object.getString("details")));
                }
                adapter = new MaterialsDetailsListAdapter(context, detailsObjects);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
        }
    }

    public static void expand(final View v) {
        v.measure(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? Toolbar.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);

        try {
            timer.cancel();
        } catch (Exception e) {

        }

        timer = new CountDownTimer(3000, 1000) {
            public void onFinish() {
                Log.d("123", "123");
                ItemsActivity.collapse(v);
                //ItemsActivity.cart_info.setVisibility(View.GONE);
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
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
