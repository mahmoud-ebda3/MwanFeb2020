package com.ebda3.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ebda3.mwan.R;

import java.util.ArrayList;

public class BidsListAdapter extends ArrayAdapter<String> {

    private final Activity context;


    private final ArrayList<String> ID ;
    private final  ArrayList<String> Details ;
    private final  ArrayList<String> BidDate  ;
    private final  ArrayList<String> BidsCount  ;
    private final  ArrayList<String> Bids  ;


    public BidsListAdapter(Activity context, ArrayList<String> ID, ArrayList<String> Details , ArrayList<String> BidDate, ArrayList<String> BidsCount, ArrayList<String> Bids ) {
        super(context, R.layout.bid_item,  Details);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.ID=ID;
        this.Details=Details;
        this.BidDate=BidDate;
        this.BidsCount=BidsCount;
        this.Bids=Bids;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.bid_item, null,true);


        TextView details = (TextView) rowView.findViewById(R.id.details);
        TextView bID = (TextView) rowView.findViewById(R.id.bID);
        TextView replies = (TextView) rowView.findViewById(R.id.replies);
        TextView add_date = (TextView) rowView.findViewById(R.id.add_date);
        details.setText(Details.get(position));
        bID.setText( "رقم :  " + ID.get(position));
        replies.setText( "الردود :  " + BidsCount.get(position));
        add_date.setText(   BidDate.get(position));


        return rowView;

    };
}