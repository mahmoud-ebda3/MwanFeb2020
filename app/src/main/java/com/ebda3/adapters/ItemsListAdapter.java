package com.ebda3.adapters;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebda3.Model.Cart;
import com.ebda3.mwan.CartActivity;
import com.ebda3.mwan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.ebda3.Helpers.Config.cartData;
import static com.ebda3.Helpers.Config.imageupload;

public class ItemsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> ItemName;
    private final ArrayList<Float> ItemPrice;
    private final ArrayList<Float> shippingCost;
    private final ArrayList<String> ItemPhoto;
    private final ArrayList<Integer> ItemID;
    private final ArrayList<Integer> ItemAvailableAmount;
    private final ArrayList<Integer> ItemPartnerID;
    private final ArrayList<String> ItemPartnerName;

    public ItemsListAdapter(Activity context, ArrayList<Integer> ItemID  , ArrayList<String> ItemName,ArrayList<String> ItemPhoto, ArrayList<Float> ItemPrice  , ArrayList<Float> shippingCost  , ArrayList<Integer> ItemAvailableAmount, ArrayList<Integer> ItemPartnerID, ArrayList<String> ItemPartnerName  ) {
        super(context, R.layout.item_item  ,  ItemName);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.ItemName=ItemName;
        this.ItemPhoto=ItemPhoto;
        this.ItemPrice=ItemPrice;
        this.shippingCost=shippingCost;
        this.ItemAvailableAmount=ItemAvailableAmount;
        this.ItemPartnerID=ItemPartnerID;
        this.ItemPartnerName=ItemPartnerName;
        this.ItemID=ItemID;

    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_item, null,true);


        ImageView imageView = (ImageView) rowView.findViewById(R.id.img_my_product);
        TextView name = (TextView) rowView.findViewById(R.id.txt_product_name);
        TextView price = (TextView) rowView.findViewById(R.id.price);


        LinearLayout shopping_cart = (LinearLayout) rowView.findViewById(R.id.shopping_cart_id);





        name.setText(ItemName.get(position));
        price.setText(String.valueOf(ItemPrice.get(position)));

        Picasso.with(this.getContext()).load(imageupload+ItemPhoto.get(position)  )
                .resize(250, 180)
                .centerCrop()

                .error(R.drawable.image_not_found)
                .into(imageView);


        shopping_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cart cart = new Cart();

                Boolean AddToArray = true;

                for(Cart cart1 : cartData) {
                    if(cart1.getID() != 0 && cart1.getID() == ItemID.get(position) ) {
                        cart1.setPrice( ItemPrice.get(position) );
                        cart1.setShippingCost( shippingCost.get(position) );
                        cart1.setAmount(cart1.getAmount()+1);
                        AddToArray = false;
                        //Toast.makeText(context, "Found it! " + String.valueOf(cart1.getAmount())   , Toast.LENGTH_SHORT).show();
                    }
                }

                if ( AddToArray ) {
                    cart.setID(ItemID.get(position));
                    cart.setName(ItemName.get(position));
                    cart.setPhoto(ItemPhoto.get(position));
                    cart.setPrice(ItemPrice.get(position));
                    cart.setShippingCost(shippingCost.get(position));
                    cart.setItemAvailableAmount(ItemAvailableAmount.get(position));
                    cart.setPartner_ID(ItemPartnerID.get(position));
                    cart.setPartnerName(ItemPartnerName.get(position));
                    cart.setAmount(1);
                    cartData.add(cart);
                }
                //Toast.makeText(context, String.valueOf(cartData.size()), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, CartActivity.class);
                context.startActivity (intent);

            }
        });



        return rowView;

    };
}