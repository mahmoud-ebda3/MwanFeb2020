package com.ebda3.adapters;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.ebda3.mwan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SupplierConfirmItemsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> Name;
    private final ArrayList<String> Photo;
    private final ArrayList<String> Amount;
    private final ArrayList<String> ItemAvailableAmount;
    private final ArrayList<String> totalPrice;
    private final ArrayList<String> Info;
    ArrayList<String> priceBeforeDiscount;


    public SupplierConfirmItemsListAdapter(Activity context, ArrayList<String> Name, ArrayList<String> Photo, ArrayList<String> Amount, ArrayList<String> ItemAvailableAmount, ArrayList<String> totalPrice, ArrayList<String> Info, ArrayList<String> priceBeforeDiscount) {
        super(context, R.layout.confrim_item, Name);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.Name = Name;
        this.Photo = Photo;
        this.Amount = Amount;
        this.ItemAvailableAmount = ItemAvailableAmount;
        this.totalPrice = totalPrice;
        this.Info = Info;
        this.priceBeforeDiscount = priceBeforeDiscount;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confrim_item, parent, false);
        }

        TextView item_name = (TextView) view.findViewById(R.id.item_name);
        TextView item_amount = (TextView) view.findViewById(R.id.item_amount);
        TextView item_available = (TextView) view.findViewById(R.id.item_available);
        TextView item_total = (TextView) view.findViewById(R.id.item_total);
        TextView discount_text = view.findViewById(R.id.item_total_before_discount);
        LinearLayout information_linaer = (LinearLayout) view.findViewById(R.id.info_linear);


        item_name.setText(Name.get(position));
        item_amount.setText(Amount.get(position));
        item_available.setText(ItemAvailableAmount.get(position));
        item_total.setText(totalPrice.get(position));
        if (Integer.valueOf(priceBeforeDiscount.get(position)) > 0) {
            discount_text.setVisibility(View.VISIBLE);
            discount_text.setText(priceBeforeDiscount.get(position));
            discount_text.setPaintFlags(discount_text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            discount_text.setVisibility(View.GONE);
        }
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(Info.get(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {

                try {

                    JSONObject row = jsonArray.getJSONObject(i);
                    Log.d("tttess", row.toString());
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


        return view;

    }

    ;
}