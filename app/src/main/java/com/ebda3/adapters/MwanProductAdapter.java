package com.ebda3.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebda3.Model.Product;
import com.ebda3.mwan.CartActivity;
import com.ebda3.mwan.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.ebda3.Helpers.Config.imageupload;
import static com.ebda3.mwan.MwanProductActivity.CartProductCount;
import static com.ebda3.mwan.MwanProductActivity.CartProductID;
import static com.ebda3.mwan.MwanProductActivity.CartProductName;
import static com.ebda3.mwan.MwanProductActivity.CartProductPhoto;
import static com.ebda3.mwan.MwanProductActivity.CartProductPrice;

/**
 * Created by work on 29/10/2017.
 */

public class MwanProductAdapter extends ArrayAdapter<Product> {

    private final Activity context;
    private final ArrayList<Product> MyProduct;

    public MwanProductAdapter(Activity context, ArrayList<Product> MyProduct) {
        super(context, R.layout.mwan_items_activity,MyProduct);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.MyProduct=MyProduct;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mwan_items_activity, null, true);

        final Boolean[] found = {false};

        ImageView dialog_material_image = (ImageView) rowView.findViewById(R.id.img_my_product);
        ImageView cart_image = (ImageView) rowView.findViewById(R.id.add_to_cart_img);
        TextView dialog_material_name = (TextView) rowView.findViewById(R.id.txt_product_name);
        TextView dialog_material_price = (TextView) rowView.findViewById(R.id.txt_product_price);
        TextView dialog_material_buying= (TextView) rowView.findViewById(R.id.buy_product_txt);

        Picasso.with(context).load(imageupload + MyProduct.get(position).getProductImage())
                .resize(626, 250)
                .centerCrop()
                .error(R.drawable.image_not_found)
                .into(dialog_material_image);

        dialog_material_name.setText(MyProduct.get(position).getProductName());
        dialog_material_price.setText(MyProduct.get(position).getProductPrice());



        cart_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                for (int i = 0; i < CartProductID.size(); i++)
                {
                    if (CartProductID.get(i).equals(MyProduct.get(position).getProductID())) {
                        found[0] = true;
                    }
                }


                Intent myIntent = new Intent(context, CartActivity.class);
                if (found[0] == false)
                {

                    CartProductID.add(MyProduct.get(position).getProductID());
                    CartProductName.add(MyProduct.get(position).getProductName());
                    Log.d("fault", String.valueOf(CartProductName));
                    CartProductPhoto.add(MyProduct.get(position).getProductImage());
                    CartProductCount.add("1");
                    CartProductPrice.add(MyProduct.get(position).getProductPrice());

                    SharedPreferences sp = context.getSharedPreferences("MyShoppingCart", 0);
                    SharedPreferences.Editor Ed = sp.edit();
                    JSONObject json = new JSONObject();
                    try {
                        json.put("ID", new JSONArray(CartProductID));
                        json.put("Name", new JSONArray(CartProductName));
                        json.put("Photo", new JSONArray(CartProductPhoto));
                        json.put("Count", new JSONArray(CartProductCount));
                        json.put("Price", new JSONArray(CartProductPrice));
                        String arrayList = json.toString();

                        Ed.putString("Cart", arrayList);
                        Ed.commit();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                myIntent.putExtra("ProductName",MyProduct.get(position).getProductName());
                myIntent.putExtra("ProductPhoto",MyProduct.get(position).getProductImage());
                context.startActivity(myIntent);
            }
        });





        dialog_material_buying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




        return rowView;
    }
}
