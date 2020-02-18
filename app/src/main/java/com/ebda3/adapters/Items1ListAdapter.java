package com.ebda3.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebda3.mwan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.ebda3.Helpers.Config.imageupload;

public class Items1ListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> ItemName;
    private final ArrayList<Float> ItemPrice;
    private final ArrayList<Float> shippingCost;
    private final ArrayList<String> ItemPhoto;
    private final ArrayList<Integer> ItemID;
    private final ArrayList<Integer> ItemAvailableAmount;
    private final ArrayList<String> ItemPartnerName;



    public Items1ListAdapter(Activity context, ArrayList<Integer> ItemID  , ArrayList<String> ItemName, ArrayList<String> ItemPhoto, ArrayList<Float> ItemPrice  , ArrayList<Float> shippingCost  , ArrayList<Integer> ItemAvailableAmount, ArrayList<String> ItemPartnerName  ) {
        super(context, R.layout.item1_item  ,  ItemName);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.ItemName=ItemName;
        this.ItemPhoto=ItemPhoto;
        this.ItemPrice=ItemPrice;
        this.shippingCost=shippingCost;
        this.ItemAvailableAmount=ItemAvailableAmount;
        this.ItemPartnerName=ItemPartnerName;
        this.ItemID=ItemID;

    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item1_item, null,true);


        ImageView imageView = (ImageView) rowView.findViewById(R.id.img_my_product);
        TextView name = (TextView) rowView.findViewById(R.id.txt_product_name);
        TextView price = (TextView) rowView.findViewById(R.id.price);


        LinearLayout shopping_cart = (LinearLayout) rowView.findViewById(R.id.add_to_cart);





        name.setText(ItemName.get(position));

        Picasso.get().load(imageupload+ItemPhoto.get(position)  )
                .resize(250, 180)
                .centerCrop()

                .error(R.drawable.image_not_found)
                .into(imageView);





        return rowView;

    };
}