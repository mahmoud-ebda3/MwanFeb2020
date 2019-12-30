package com.ebda3.adapters;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Helpers.Config;
import com.ebda3.mwan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ebda3.Helpers.Config.imageupload;

public class WorkersListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> WorkerID;
    private final ArrayList<String> WorkersName;
    private final ArrayList<String> WorkersPhoto;
    private final ArrayList<String> WorkerContact;
    private final ArrayList<String> WorkerAddress;
    private final ArrayList<String> WorkersNotes;



    public WorkersListAdapter(Activity context, ArrayList<String> WorkerID,  ArrayList<String> WorkersName, ArrayList<String> WorkersPhoto , ArrayList<String> WorkersNotes, ArrayList<String> WorkerContact, ArrayList<String> WorkerAddress ) {
        super(context, R.layout.worker_item,  WorkersPhoto);
        // TODO Auto-generated constructor stub


        this.context=context;
        this.WorkerID=WorkerID;
        this.WorkersName=WorkersName;
        this.WorkersPhoto=WorkersPhoto;
        this.WorkerContact=WorkerContact;
        this.WorkerAddress=WorkerAddress;
        this.WorkersNotes=WorkersNotes;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.worker_item, null,true);


        ImageView imageView = (ImageView) rowView.findViewById(R.id.worker_image);
        TextView Worker_name = (TextView) rowView.findViewById(R.id.worker_name);
        TextView Worker_contact = (TextView) rowView.findViewById(R.id.worker_contact);
        TextView Worker_address = (TextView) rowView.findViewById(R.id.worker_address);


        final int pos = position;
        final String phone = WorkerContact.get(position);

        Worker_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://adc-company.net/mwan/workers_call-edit-1.html?json=true&ajax_page=true",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();


                        map.put("do", "insert" );
                        map.put("worker_id", WorkerID.get(pos) );
                        map.put("json_email", Config.getJsonEmail(context) );
                        map.put("json_password", Config.getJsonPassword(context));
                        Log.d("params",map.toString());
                        return map;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone  ));
                context.startActivity(intent);
            }
        });



        Worker_name.setText(WorkersName.get(position));
        Worker_contact.setText(WorkerContact.get(position));
        Worker_address.setText(WorkerAddress.get(position));

        Picasso.with(this.getContext()).load(imageupload+WorkersPhoto.get(position)  )
                .resize(128, 128)


                .error(R.drawable.image_not_found)
                .into(imageView);
        //imageView.setImageResource(map_img[position]);
        return rowView;

    };
}