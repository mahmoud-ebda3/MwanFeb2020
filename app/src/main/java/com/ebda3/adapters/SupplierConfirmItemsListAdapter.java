package com.ebda3.adapters;


import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebda3.mwan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;

public class SupplierConfirmItemsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> Name;
    private final ArrayList<String> Photo;
    private final ArrayList<String> Amount;
    private final ArrayList<String> ItemAvailableAmount;
    private final ArrayList<String> totalPrice;
    private final ArrayList<String> Info;



    public SupplierConfirmItemsListAdapter(Activity context,  ArrayList<String> Name ,  ArrayList<String> Photo ,  ArrayList<String> Amount ,  ArrayList<String> ItemAvailableAmount ,  ArrayList<String> totalPrice,  ArrayList<String> Info  ) {
        super(context, R.layout.confrim_item  ,  Name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.Name=Name;
        this.Photo=Photo;
        this.Amount=Amount;
        this.ItemAvailableAmount=ItemAvailableAmount;
        this.totalPrice=totalPrice;
        this.Info=Info;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.confrim_item, null,true);


        TextView item_name = (TextView) rowView.findViewById(R.id.item_name);
        TextView item_amount = (TextView) rowView.findViewById(R.id.item_amount);
        TextView item_available = (TextView) rowView.findViewById(R.id.item_available);
        TextView item_total = (TextView) rowView.findViewById(R.id.item_total);
        LinearLayout information_linaer = (LinearLayout) rowView.findViewById(R.id.info_linear);



        item_name.setText(Name.get(position));
        item_amount.setText(Amount.get(position));
        item_available.setText(ItemAvailableAmount.get(position));
        item_total.setText(totalPrice.get(position));

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(Info.get(position));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }


        if ( jsonArray.length() > 0 ) {
            for (int i = 0; i < jsonArray.length(); i++)
            {

                    try {



                        JSONObject row = jsonArray.getJSONObject(i);
                        Log.d("tttess",row.toString());
                        String name = row.getString("Name").toString();
                        String value = row.getString("Value").toString();


                    TextView valueTV = new TextView(context);
                    valueTV.setText(name + " : " + value);
                    valueTV.setTextSize(11);
                    valueTV.setGravity(Gravity.CENTER);
                    valueTV.setTextColor(Color.parseColor("#0087C2"));
                    valueTV.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));

                    information_linaer.addView(valueTV);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }




            }


        return rowView;

    };
}