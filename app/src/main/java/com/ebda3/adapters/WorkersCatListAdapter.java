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

import static com.ebda3.Helpers.Config.imageupload1;

public class WorkersCatListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> CatName;
    private final ArrayList<String> CatPhoto;
    private final ArrayList<String> CatNotes;

    public WorkersCatListAdapter(Activity context, ArrayList<String> CatName, ArrayList<String> CatPhoto , ArrayList<String> CatNotes ) {
        super(context, R.layout.cat_item,  CatPhoto);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.CatName=CatName;
        this.CatPhoto=CatPhoto;
        this.CatNotes=CatNotes;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.cat_item, null,true);


        ImageView imageView = (ImageView) rowView.findViewById(R.id.Cat_image);
        TextView Cat_name = (TextView) rowView.findViewById(R.id.Cat_name);







        Cat_name.setText(CatName.get(position));
        Picasso.with(this.getContext()).load(imageupload1+CatPhoto.get(position)  )
                .resize(128, 128)


                .error(R.drawable.image_not_found)
                .into(imageView);
        //imageView.setImageResource(map_img[position]);
        return rowView;

    };
}