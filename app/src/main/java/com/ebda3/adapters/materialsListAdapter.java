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

public class materialsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> MaterialsName;
    private final ArrayList<String> MaterialsPhoto;
    private final ArrayList<String> MaterialsID;

    public materialsListAdapter(Activity context, ArrayList<String> MaterialsName, ArrayList<String> MaterialsPhoto , ArrayList<String> MaterialsID ) {
        super(context, R.layout.material_item  ,  MaterialsPhoto);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.MaterialsName=MaterialsName;
        this.MaterialsPhoto=MaterialsPhoto;
        this.MaterialsID=MaterialsID;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.material_item, null,true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.offer_image);
        TextView offer_name = (TextView) rowView.findViewById(R.id.offer_name);

        offer_name.setText(MaterialsName.get(position));
        Picasso.get().load(imageupload+MaterialsPhoto.get(position)  )
                .resize(256, 256)
                .centerCrop()
                .error(R.drawable.image_not_found)
                .into(imageView);
        //imageView.setImageResource(map_img[position]);
        return rowView;

    };
}