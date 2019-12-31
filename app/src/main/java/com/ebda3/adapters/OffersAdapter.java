package com.ebda3.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ebda3.Model.OfferObject;
import com.ebda3.mwan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.ebda3.Helpers.Config.imageupload;

public class OffersAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<OfferObject> objectList;


    public OffersAdapter(Context context, ArrayList<OfferObject> object) {
        this.context = context;
        this.objectList = object;
    }

    @Override
    public int getCount() {
        return objectList.size();
    }

    @Override
    public Object getItem(int position) {
        return objectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item_layout, parent, false);
        }
        OfferObject object = objectList.get(position);
        TextView itemName = convertView.findViewById(R.id.offer_name);
        ImageView imageView = convertView.findViewById(R.id.offer_image);

        Log.d("ttteessseet",object.getName());
        itemName.setText(object.getName());
        Log.d("ttteessseet",object.getName());
        Picasso.with(context).load(imageupload + object.getPhoto())
                .resize(626, 250)
                .centerCrop()
                .error(R.drawable.image_not_found)
                .into(imageView);
        Log.d("ttteessseet",object.getName());
        return convertView;
    }
}
