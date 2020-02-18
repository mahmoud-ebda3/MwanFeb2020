package com.ebda3.mwan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ebda3.Model.Cart;
import com.ebda3.Model.DetailsObject;
import com.ebda3.adapters.MaterialsDetailsListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static com.ebda3.Helpers.Config.cartData;
import static com.ebda3.Helpers.Config.imageupload;

public class MaterialsDetailsActivity extends AppCompatActivity {

    public static HashMap<String, String> selectedItems = new HashMap<>();
    public static HashMap<String, String> selectedItemsdetails = new HashMap<>();
    private ListView listView;
    LinearLayout unitContainer, descriptionContainer;
    Context context = this;
    private String ID, Name, Location, Photo, Details;
    private MaterialsDetailsListAdapter adapter;
    private Toolbar detailsToolbar;
    private TextView title, unittextView, descriptionTextView;
    private ImageView photo;
    InputMethodManager imm;
    LinearLayout linearLayout;
    private RelativeLayout cartInfo;
    EditText item_count;
    private ArrayList<DetailsObject> detailsObjects = new ArrayList<>();
    LinearLayout add_cart;
    LinearLayout cartIcon;
    Button cartButton;
    TextView numberIcon, specificationsTextView;
    String ItemName, ItemPhoto, ItemID, ItemUnit, ItemDescription;
    LinearLayout noData;
    ArrayList<String> DetailsName = new ArrayList<>();
    ArrayList<String> DetailsValue = new ArrayList<>();
    public static TextView notificationNum;
    public static RelativeLayout cart_info;
    public static CountDownTimer timer = null;

    @Override
    protected void onResume() {
        super.onResume();
        if (cartData.size() > 0) {
            notificationNum.setVisibility(View.VISIBLE);
            notificationNum.setText(String.valueOf(cartData.size()));
        } else {
            notificationNum.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        listView = findViewById(R.id.details_list_view);
        detailsToolbar = findViewById(R.id.app_toolbar);
        add_cart = findViewById(R.id.add_cart);
        descriptionContainer = findViewById(R.id.item_description_container);
        unitContainer = findViewById(R.id.item_unit_container);
        unittextView = findViewById(R.id.item_unit);
        descriptionTextView = findViewById(R.id.item_description);
        photo = findViewById(R.id.item_photo);
        setSupportActionBar(detailsToolbar);
        specificationsTextView = findViewById(R.id.choose_specifications_text_view);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        Intent intent = getIntent();
        ItemID = intent.getStringExtra("ID");
        ItemName = intent.getStringExtra("Name");
        ItemPhoto = intent.getStringExtra("Photo");
        ItemUnit = intent.getStringExtra("Unit");
        ItemDescription = intent.getStringExtra("Description");
        if (!ItemUnit.isEmpty()) {
            unitContainer.setVisibility(View.VISIBLE);
            unittextView.setText(ItemUnit);
        }
        if (!ItemDescription.isEmpty()) {
            descriptionContainer.setVisibility(View.VISIBLE);
            descriptionTextView.setText(ItemDescription);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        cart_info = (RelativeLayout)

                findViewById(R.id.cart_info);

        notificationNum = (TextView)

                findViewById(R.id.notificationNum);

        selectedItems.clear();
        cartInfo =

                findViewById(R.id.cart_info);

        cartIcon = detailsToolbar.findViewById(R.id.notificationB);
        numberIcon = detailsToolbar.findViewById(R.id.notificationNum);

        cartIcon.setVisibility(View.VISIBLE);
        Log.d("ppphhh", imageupload + ItemPhoto);
        // photo.setVisibility(View.VISIBLE);
        Picasso.get().

                load(imageupload + ItemPhoto)
                .

                        resize(150, 150)
                .

                        error(R.drawable.app_logo)
                .

                        into(photo);

        cartIcon.setOnClickListener(click ->

        {
            Intent cartIntent = new Intent(context, NewCart.class);
            startActivity(cartIntent);
        });
        title = detailsToolbar.findViewById(R.id.app_headline);
        add_cart.setOnTouchListener((v, event) ->

        {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                v.setBackgroundResource(R.drawable.add_to_cart_red);
            } else {
                v.setBackgroundResource(R.drawable.add_to_cart);
            }
            return false;
        });
        add_cart.setOnClickListener(click ->

        {
            BottomSheetDialog dialog = new BottomSheetDialog(context);
            dialog.setContentView(R.layout.dialog_sheet_for_quantity);
            EditText editText = dialog.findViewById(R.id.quantity_edit_text);
            TextView confirmButton = dialog.findViewById(R.id.confirm_button);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            confirmButton.setOnClickListener(confirm -> {
                String quantity = editText.getText().toString();
                if (quantity.isEmpty() || quantity.charAt(0) == '0') {
                    editText.setError("من فضلك أدخل كمية صحيحة");
                } else {
                    String my_Count = editText.getText().toString();
                    Log.d("adapter_selected", selectedItems.toString());
                    Cart cart = new Cart();

                    Boolean AddToArray = true;

                    DetailsName = null;
                    DetailsValue = null;
                    DetailsName = new ArrayList<>();
                    DetailsValue = new ArrayList<>();

                    /*
//
                for(Cart cart1 : cartData) {
                    if(cart1.getID() != 0 && cart1.getID() == Integer.parseInt(ItemID) ) {

//                    cart1.setPrice( ItemPrice.get(position) );
//                    cart1.setShippingCost( shippingCost.get(position) );
                        cart1.setAmount(cart1.getAmount()+1);
                        AddToArray = false;
                        //Toast.makeText(context, "Found it! " + String.valueOf(cart1.getAmount())   , Toast.LENGTH_SHORT).show();
                    }
                }*/

                    if (AddToArray) {
                        cart.setID(Integer.parseInt(ItemID));
                        cart.setName(ItemName);
                        cart.setPhoto(ItemPhoto);
                        Gson gson = new Gson();
                        Log.d("selected_items", "before :" + gson.toJson(selectedItems));
                        Log.d("selected_itemsdetails", "before :" + gson.toJson(selectedItemsdetails));


                        cart.setPrice(Float.parseFloat(ItemID));
                        cart.setShippingCost(Float.parseFloat(ItemID));
                        cart.setItemAvailableAmount(Integer.parseInt(ItemID));
                        cart.setPartner_ID(Integer.parseInt(ItemID));
                        cart.setPartnerName(ItemName);

                        Iterator myVeryOwnIterator = selectedItemsdetails.keySet().iterator();
                        while (myVeryOwnIterator.hasNext()) {
                            String key = (String) myVeryOwnIterator.next();
                            DetailsName.add(key);
                            String value = (String) selectedItemsdetails.get(key);
                            DetailsValue.add(value);
                        }

                        //selectedItems.clear();
                        selectedItemsdetails = null;
                        selectedItemsdetails = new HashMap<>();

                        Log.d("predetails", DetailsName.toString());


                        cart.setDetailsName(DetailsName);
                        cart.setDetailsValue(DetailsValue);
                        cart.setAmount(Integer.valueOf(quantity));
                        cartData.add(cart);
                        cart.setSelectedItems(gson.toJson(selectedItems));
                        Log.d("CartDataArray", "before: " + String.valueOf(cartData.get(0).getDetailsName()));


                        Log.d("selected_items", "after :" + gson.toJson(selectedItems));
                        Log.d("selected_itemsdetails", "after :" + gson.toJson(selectedItemsdetails));
                        //selectedItems.clear();
                    }
//            cart_info.setVisibility(View.VISIBLE);
//            expand(ItemsActivity.cart_info);
                    dialog.dismiss();
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    Intent cartIntent = new Intent(context, NewCart.class);
                    startActivity(cartIntent);
                }
            });
            dialog.show();
        });
        ID =

                getIntent().

                        getStringExtra("ID");

        Name =

                getIntent().

                        getStringExtra("Name");

        Location =

                getIntent().

                        getStringExtra("ID");

        Photo =

                getIntent().

                        getStringExtra("ID");

        Details =

                getIntent().

                        getStringExtra("Details");
        title.setText(Name);

        getData(Details);

    }

    private void getData(String response) {
        try {
            if (!response.isEmpty()) {
                JSONArray array = new JSONArray(response);
                Log.e("query", array.toString());
                Log.e("query", String.valueOf(array.length()));
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
                if (array.length() == 0) {
                    listView.setVisibility(View.GONE);
                    specificationsTextView.setVisibility(View.GONE);
                }
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