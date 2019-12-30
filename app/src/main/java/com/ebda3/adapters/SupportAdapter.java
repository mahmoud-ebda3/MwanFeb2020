package com.ebda3.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ebda3.mwan.R;

import java.util.ArrayList;


public class SupportAdapter extends ArrayAdapter<String> {


    private final Activity context;
    private final ArrayList<String> Status;
    private final ArrayList<String> StatusText;
    private final ArrayList<String> AddDate;
    private final ArrayList<String> Complaint;
    private final ArrayList<String> fininshedComment;

    public SupportAdapter(Activity context, ArrayList<String> Status, ArrayList<String> StatusText, ArrayList<String> AddDate, ArrayList<String> Complaint , ArrayList<String> fininshedComment ) {
        super(context, R.layout.support_item,Status);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.Status=Status;
        this.StatusText=StatusText;
        this.AddDate=AddDate;
        this.Complaint=Complaint;
        this.fininshedComment=fininshedComment;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.support_item, null,true);

        TextView status = (TextView) rowView.findViewById(R.id.status);
        TextView msg_time = (TextView) rowView.findViewById(R.id.cus_msg_time);
        TextView msg = (TextView) rowView.findViewById(R.id.msg_content);



        status.setText(StatusText.get(position));
        msg_time.setText(AddDate.get(position));
        msg.setText(Complaint.get(position));
        return rowView;

    }
}