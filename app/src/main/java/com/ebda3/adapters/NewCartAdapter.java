package com.ebda3.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebda3.Model.CalcCart;
import com.ebda3.design.MyTextView;
import com.ebda3.mwan.MaterialsDetailsActivity;
import com.ebda3.mwan.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;

import static com.ebda3.Helpers.Config.cartData;
import static com.ebda3.Helpers.Config.imageupload;

/**
 * Created by work on 29/10/2017.
 */

public class NewCartAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> OfferName;
    private final ArrayList<String> ProductImage;
    private final ArrayList<Float> ProductPrice;
    private final ArrayList<Integer> ProductCount;
    private final ArrayList<String> ItemPartnerName;
    private final ArrayList<ArrayList<String>> CartDetailsName;
    private final ArrayList<ArrayList<String>> CartDetailsValuew;;
    private final ArrayList<Integer> ItemAvailableAmount;
    private final ArrayList<Integer> ItemPartnerID;


    public NewCartAdapter(Activity context, ArrayList<String> OfferName, ArrayList<String> ProductImage, ArrayList<Float> ProductPrice, ArrayList<Integer> ProductCount, ArrayList<Integer> ItemAvailableAmount, ArrayList<Integer> ItemPartnerID, ArrayList<String> ItemPartnerName,ArrayList<ArrayList<String>> CartDetailsName,ArrayList<ArrayList<String>> CartDetailsValuew) {
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
    }

    private boolean removeItemByPosition(int position) {
        try {
            cartData.remove(position);
            CalcCart calcCart = new CalcCart();
            calcCart.GetAll();
            context.recreate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.cart_new_activity_item, null, true);
        ImageView product_image = (ImageView) rowView.findViewById(R.id.img_my_product_cart);
        ImageView delete_product = (ImageView) rowView.findViewById(R.id.delete_product);
        ImageView add_count_product = (ImageView) rowView.findViewById(R.id.plus_product_count);
        ImageView minus_count_product = (ImageView) rowView.findViewById(R.id.minus_product_count);
        TextView product_name = (TextView) rowView.findViewById(R.id.txt_product_name_cart);
        final TextView my_price_pre_dis = (TextView) rowView.findViewById(R.id.txt_product_count_cart);
        LinearLayout information_linaer = (LinearLayout) rowView.findViewById(R.id.details_linear_id);

        Picasso.with(this.getContext()).load(imageupload + ProductImage.get(position))
                .resize(250, 250)
                .centerCrop()
                .error(R.drawable.image_not_found)
                .into(product_image);
        product_name.setText(OfferName.get(position));
        my_price_pre_dis.setText(String.valueOf(ProductCount.get(position)));
        add_count_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mycount = cartData.get(position).getAmount();
                mycount++;

                Log.d("myccccoo", String.valueOf(mycount));
                if (mycount >= 0) {
                    cartData.get(position).setAmount(mycount);
                    my_price_pre_dis.setText(String.valueOf(mycount));
                } else {
                    my_price_pre_dis.setText("1");
                }
                CalcCart calcCart = new CalcCart();
                calcCart.GetAll();
            }
        });
        minus_count_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mycount = cartData.get(position).getAmount();
                mycount--;

                Log.d("myccccoo", String.valueOf(mycount));
                if (mycount <= 0) {
                    cartData.get(position).setAmount(1);
                    my_price_pre_dis.setText("1");
                } else {
                    cartData.get(position).setAmount(mycount);
                    my_price_pre_dis.setText(String.valueOf(mycount));
                }
                CalcCart calcCart = new CalcCart();
                calcCart.GetAll();
            }
        });

        delete_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeItemByPosition(position);


            }
        });



        Log.d("detailsinadapterss",CartDetailsName.toString());
       if (CartDetailsName.get(position).size()>= 0)
       {
           for (int i = 0; i < CartDetailsName.get(position).size(); i++)
           {
               Log.d("ttrrrttt",CartDetailsName.get(position).get(i));
               String name = CartDetailsName.get(position).get(i);
               String value = CartDetailsValuew.get(position).get(i);
               MyTextView valueTV = new MyTextView(context);
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



        return rowView;
    }

}
