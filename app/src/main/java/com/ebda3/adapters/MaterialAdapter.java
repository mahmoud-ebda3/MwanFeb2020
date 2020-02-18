package com.ebda3.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebda3.Model.Material;
import com.ebda3.mwan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.ebda3.Helpers.Config.imageupload;

/**
 * Created by work on 23/10/2017.
 */

public class MaterialAdapter extends ArrayAdapter<Material> {

    private final Activity context;
    private final ArrayList<Material> MyMaterial;

    public MaterialAdapter(Activity context, ArrayList<Material> MyMaterial) {
        super(context, R.layout.material_activity_item,MyMaterial);

        this.context=context;
        this.MyMaterial=MyMaterial;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.material_activity_item, null, true);


        ImageView dialog_material_image = (ImageView) rowView.findViewById(R.id.material_img_id);
        TextView dialog_material_name = (TextView) rowView.findViewById(R.id.material_name_id);

        Picasso.get().load(imageupload + MyMaterial.get(position).getMaterialImage())
                .resize(25, 25)
                .centerCrop()
                .transform(new CropCircleTransformation())
                .error(R.drawable.image_not_found)
                .into(dialog_material_image);

        dialog_material_name.setText(MyMaterial.get(position).getMaterialName());



        return rowView;

    }
}
