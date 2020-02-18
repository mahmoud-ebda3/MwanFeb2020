package com.ebda3.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.ebda3.Model.CalcCart;
import com.ebda3.mwan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.ebda3.Helpers.Config.cartData;
import static com.ebda3.Helpers.Config.imageupload;

/**
 * Created by work on 29/10/2017.
 */

public class NewCartAdapter extends ArrayAdapter<String> {

    private Context context;
    private final ArrayList<String> OfferName;
    private final ArrayList<String> ProductImage;
    private final ArrayList<Float> ProductPrice;
    private final ArrayList<Integer> ProductCount;
    private final ArrayList<String> ItemPartnerName;
    private final ArrayList<ArrayList<String>> CartDetailsName;
    private final ArrayList<ArrayList<String>> CartDetailsValuew;
    InputMethodManager imm;

    private final ArrayList<Integer> ItemAvailableAmount;
    private final ArrayList<Integer> ItemPartnerID;

    public NewCartAdapter(Activity context, ArrayList<String> OfferName, ArrayList<String> ProductImage, ArrayList<Float> ProductPrice, ArrayList<Integer> ProductCount, ArrayList<Integer> ItemAvailableAmount, ArrayList<Integer> ItemPartnerID, ArrayList<String> ItemPartnerName, ArrayList<ArrayList<String>> CartDetailsName, ArrayList<ArrayList<String>> CartDetailsValuew) {
        super(context, R.layout.cart_new_activity_item, ProductImage);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.OfferName = OfferName;
        this.ProductImage = ProductImage;
        this.ProductCount = ProductCount;
        this.ProductPrice = ProductPrice;
        this.ItemPartnerName = ItemPartnerName;
        this.ItemAvailableAmount = ItemAvailableAmount;
        this.ItemPartnerID = ItemPartnerID;
        this.CartDetailsName = CartDetailsName;
        this.CartDetailsValuew = CartDetailsValuew;
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_new_activity_item, parent, false);
        }
        ImageView product_image = view.findViewById(R.id.img_my_product_cart);
        ImageView delete_product = view.findViewById(R.id.delete_product);
        ImageView add_count_product = view.findViewById(R.id.plus_product_count);
        ImageView minus_count_product = view.findViewById(R.id.minus_product_count);
        TextView product_name = view.findViewById(R.id.txt_product_name_cart);
        TextView my_price_pre_dis_tv = view.findViewById(R.id.txt_product_count_cart);
        LinearLayout information_linaer = view.findViewById(R.id.details_linear_id);
        Picasso.get().load(imageupload + ProductImage.get(position))
                .resize(250, 250)
                .centerCrop()
                .error(R.drawable.image_not_found)
                .into(product_image);
        my_price_pre_dis_tv.setText(String.valueOf(ProductCount.get(position)));
        product_name.setText(OfferName.get(position));
        add_count_product.setOnClickListener(click -> {
            int mycount = Integer.valueOf(my_price_pre_dis_tv.getText().toString());
            mycount++;
            if (mycount >= 0) {
                cartData.get(position).setAmount(mycount);
                my_price_pre_dis_tv.setText(String.valueOf(mycount));
            } else {
                my_price_pre_dis_tv.setText("1");
            }
            CalcCart calcCart = new CalcCart();
            calcCart.GetAll();
        });
        minus_count_product.setOnClickListener(click -> {
            int mycount = Integer.valueOf(my_price_pre_dis_tv.getText().toString());
            mycount--;
            Log.d("myccccoo", String.valueOf(mycount));
            if (mycount <= 0) {
                cartData.get(position).setAmount(1);
                my_price_pre_dis_tv.setText("1");
            } else {
                cartData.get(position).setAmount(mycount);
                my_price_pre_dis_tv.setText(String.valueOf(mycount));
            }
            CalcCart calcCart = new CalcCart();
            calcCart.GetAll();
        });

        delete_product.setOnClickListener(click -> {
            cartData.remove(position);
            this.remove(this.getItem(position));
            this.notifyDataSetChanged();
            CalcCart calcCart = new CalcCart();
            calcCart.GetAll();
        });


        Log.d("detailsinadapterss", CartDetailsName.toString());
        Log.d("detailsinadapterss", CartDetailsValuew.toString());

        if (CartDetailsName.get(position).size() > 0) {
            information_linaer.removeAllViews();
            for (int i = 0; i < CartDetailsName.get(position).size(); i++) {
                Log.d("ttrrrttt", CartDetailsName.get(position).get(i));
                Log.d("ttrrrttt", String.valueOf(CartDetailsName.get(position).size()));
                String name = CartDetailsName.get(position).get(i);
                String value = CartDetailsValuew.get(position).get(i);
                TextView valueTV = new TextView(context);
                valueTV.setText(name + " : " + value);
                valueTV.setTextSize(10);
                valueTV.setGravity(Gravity.CENTER);
                valueTV.setTextColor(Color.parseColor("#0087C2"));
                valueTV.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                information_linaer.addView(valueTV);
            }
        }
        return view;
    }
}
