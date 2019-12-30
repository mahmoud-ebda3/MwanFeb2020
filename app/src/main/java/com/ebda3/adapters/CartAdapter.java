package com.ebda3.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebda3.Model.CalcCart;
import com.ebda3.mwan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.ebda3.Helpers.Config.cartData;
import static com.ebda3.Helpers.Config.imageupload;

/**
 * Created by work on 29/10/2017.
 */

public class CartAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> OfferName;
    private final ArrayList<String> ProductImage;
    private final ArrayList<Float> ProductPrice;
    private final ArrayList<Integer> ProductCount;

    private final ArrayList<String> ItemPartnerName;
    private final ArrayList<Integer> ItemAvailableAmount;
    private final ArrayList<Integer> ItemPartnerID;


    public CartAdapter(Activity context, ArrayList<String> OfferName, ArrayList<String> ProductImage, ArrayList<Float> ProductPrice, ArrayList<Integer> ProductCount, ArrayList<Integer> ItemAvailableAmount, ArrayList<Integer> ItemPartnerID, ArrayList<String> ItemPartnerName) {
        super(context, R.layout.cart_activity_item, ProductImage);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.OfferName = OfferName;
        this.ProductImage = ProductImage;
        this.ProductCount = ProductCount;
        this.ProductPrice = ProductPrice;
        this.ItemPartnerName = ItemPartnerName;
        this.ItemAvailableAmount = ItemAvailableAmount;
        this.ItemPartnerID = ItemPartnerID;
    }

    private boolean removeItemByPosition(int position) {
        try {
            cartData.remove(position);
            CalcCart calcCart = new CalcCart();
//            calcCart.GetAll();
            context.recreate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.cart_activity_item, null, true);


        ImageView product_image = (ImageView) rowView.findViewById(R.id.img_my_product_cart);
        ImageView delete_product = (ImageView) rowView.findViewById(R.id.delete_product);
        ImageView add_count_product = (ImageView) rowView.findViewById(R.id.plus_product_count);
        ImageView minus_count_product = (ImageView) rowView.findViewById(R.id.minus_product_count);
        TextView product_name = (TextView) rowView.findViewById(R.id.txt_product_name_cart);
        TextView PartnerName = (TextView) rowView.findViewById(R.id.txt_Partner_name);
        TextView productPrice = (TextView) rowView.findViewById(R.id.txt_product_price_cart);
        final TextView my_price_pre_dis = (TextView) rowView.findViewById(R.id.txt_product_count_cart);

        Picasso.with(this.getContext()).load(imageupload + ProductImage.get(position))
                .resize(626, 250)
                .centerCrop()
                .error(R.drawable.image_not_found)
                .into(product_image);

        product_name.setText(OfferName.get(position));
        productPrice.setText(String.valueOf(ProductPrice.get(position)) + " جنية ");
        PartnerName.setText("البائع : " + ItemPartnerName.get(position));
        my_price_pre_dis.setText(String.valueOf(ProductCount.get(position)));


        add_count_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int mycount = cartData.get(position).getAmount();
                mycount++;
                //Toast.makeText(context, String.valueOf(mycount) , Toast.LENGTH_SHORT).show();
                Log.d("myccccoo", String.valueOf(mycount));
                if (mycount >= 0) {
                    cartData.get(position).setAmount(mycount);
                    my_price_pre_dis.setText(String.valueOf(mycount));
                } else {

                    my_price_pre_dis.setText("1");
                }

                CalcCart calcCart = new CalcCart();
//                calcCart.GetAll();

            }
        });
        minus_count_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mycount = cartData.get(position).getAmount();
                mycount--;
                //Toast.makeText(context, String.valueOf(mycount) , Toast.LENGTH_SHORT).show();
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

        return rowView;
    }

}
