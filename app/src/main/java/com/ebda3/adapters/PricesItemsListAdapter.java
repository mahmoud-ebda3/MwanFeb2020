package com.ebda3.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebda3.mwan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.ebda3.Helpers.Config.imageupload;

public class PricesItemsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> ItemName;
    private final ArrayList<Float> ItemPrice;
    private final ArrayList<Float> shippingCost;
    private final ArrayList<String> ItemPhoto;
    private final ArrayList<Integer> ItemID;
    private final ArrayList<Integer> ItemAvailableAmount;
    private final ArrayList<Integer> ItemPartnerID;
    private final ArrayList<String> ItemPartnerName;

    public PricesItemsListAdapter(Activity context, ArrayList<Integer> ItemID  , ArrayList<String> ItemName, ArrayList<String> ItemPhoto, ArrayList<Float> ItemPrice  , ArrayList<Float> shippingCost  , ArrayList<Integer> ItemAvailableAmount, ArrayList<Integer> ItemPartnerID, ArrayList<String> ItemPartnerName  ) {
        super(context, R.layout.prices_item_item  ,  ItemName);
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
        View rowView=inflater.inflate(R.layout.prices_item_item, null,true);


        ImageView imageView = (ImageView) rowView.findViewById(R.id.img_my_product);
        TextView name = (TextView) rowView.findViewById(R.id.txt_product_name);
        TextView price = (TextView) rowView.findViewById(R.id.price);







        name.setText(ItemName.get(position));
        price.setText(String.valueOf(ItemPrice.get(position)));

        Picasso.with(this.getContext()).load(imageupload+ItemPhoto.get(position)  )
                .resize(250, 180)
                .centerCrop()

                .error(R.drawable.image_not_found)
                .into(imageView);






        return rowView;

    };
}