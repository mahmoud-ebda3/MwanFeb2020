package com.ebda3.adapters;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

public class BidOffersListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final  ArrayList<String> ID ;
    private final   ArrayList<String> Notes ;
    private final   ArrayList<String> Date   ;
    private final   ArrayList<String> Amount   ;
    private final   ArrayList<String> SupplierName   ;
    private final   ArrayList<String> SupplierPhoto   ;
    private final   ArrayList<String> SupplierPhone   ;
    private final   ArrayList<String> SupplierAddress   ;


    public BidOffersListAdapter(Activity context, ArrayList<String> ID, ArrayList<String> Notes , ArrayList<String> Date, ArrayList<String> Amount, ArrayList<String> SupplierName , ArrayList<String> SupplierPhoto , ArrayList<String> SupplierPhone , ArrayList<String> SupplierAddress ) {
        super(context, R.layout.bid_offer_item,  ID);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.ID=ID;
        this.Notes=Notes;
        this.Date=Date;
        this.Amount=Amount;
        this.SupplierName=SupplierName;
        this.SupplierPhoto=SupplierPhoto;
        this.SupplierPhone=SupplierPhone;
        this.SupplierAddress=SupplierAddress;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.bid_offer_item, null,true);


        ImageView imageView = (ImageView) rowView.findViewById(R.id.s_image);
        TextView bid_amount = (TextView) rowView.findViewById(R.id.bid_amount);
        TextView date = (TextView) rowView.findViewById(R.id.date);
        TextView notes = (TextView) rowView.findViewById(R.id.notes);
        TextView s_name = (TextView) rowView.findViewById(R.id.s_name);
        TextView contact = (TextView) rowView.findViewById(R.id.s_contact);
        TextView s_address = (TextView) rowView.findViewById(R.id.s_address);


        final String phone = SupplierPhone.get(position);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone  ));
                context.startActivity(intent);
            }
        });



        bid_amount.setText(Amount.get(position));
        date.setText(Date.get(position));
        notes.setText(Notes.get(position));
        s_name.setText(SupplierName.get(position));
        contact.setText(SupplierPhone.get(position));
        s_address.setText(SupplierAddress.get(position));

        Picasso.with(this.getContext()).load(imageupload+SupplierPhoto.get(position)  )
                .resize(128, 128)


                .error(R.drawable.image_not_found)
                .into(imageView);
        //imageView.setImageResource(map_img[position]);
        return rowView;

    };
}