package com.ebda3.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebda3.Model.Supplier;
import com.ebda3.mwan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.ebda3.Helpers.Config.imageupload;

/**
 * Created by work on 29/10/2017.
 */

public class SuppliersAdapter extends ArrayAdapter<Supplier> {

    private final Activity context;
    private final ArrayList<Supplier> MySuppliers;

    public SuppliersAdapter(Activity context, ArrayList<Supplier> MySuppliers)
    {
        super(context, R.layout.supplier_activity_item,MySuppliers);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.MySuppliers=MySuppliers;

    }

    public View getView(int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.supplier_activity_item, null,true);

        TextView suppler_near_loc = (TextView) rowView.findViewById(R.id.supplier_distance_card);
        TextView supplier_name = (TextView) rowView.findViewById(R.id.supplier_name_card);
        ImageView supplier_photo = (ImageView) rowView.findViewById(R.id.supplier_image_card);

        supplier_name.setText(MySuppliers.get(position).getSuppliername());
        suppler_near_loc.setText(MySuppliers.get(position).getNear_dist());

        Picasso.with(context).load(imageupload + MySuppliers.get(position).getPhoto())
                .resize(60,60)
                .centerCrop()
                .transform(new CropCircleTransformation())
                .error(R.drawable.ic_account_circle_white_48dp)
                .into(supplier_photo);


        return rowView;
    }
}
