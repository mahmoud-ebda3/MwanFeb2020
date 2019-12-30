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

public class offersListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> OfferName;
    private final ArrayList<String> OfferPhoto;
    private final ArrayList<String> offerItems;

    public offersListAdapter(Activity context, ArrayList<String> OfferName, ArrayList<String> OfferPhoto , ArrayList<String> offerItems ) {
        super(context, R.layout.offer_item  ,  OfferPhoto);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.OfferName=OfferName;
        this.OfferPhoto=OfferPhoto;
        this.offerItems=offerItems;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.offer_item, null,true);


        ImageView imageView = (ImageView) rowView.findViewById(R.id.offer_image);
        TextView offer_name = (TextView) rowView.findViewById(R.id.offer_name);







        offer_name.setText(OfferName.get(position));
        Picasso.with(this.getContext()).load(imageupload+OfferPhoto.get(position)  )
                .resize(626, 250)
                .centerCrop()

                .error(R.drawable.image_not_found)
                .into(imageView);
        //imageView.setImageResource(map_img[position]);
        return rowView;

    };
}