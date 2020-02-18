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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ebda3.Model.RatingObject;
import com.ebda3.mwan.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.ebda3.Helpers.Config.imageupload;

public class SuppliersCartListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> supplier_name;
    private final ArrayList<String> supplier_photo;
    private final ArrayList<String> supplier_phone;
    private final ArrayList<String> distance;
    private final ArrayList<String> shippingCost;
    private final ArrayList<String> total;
    private final ArrayList<String> TotalItemsCount;
    private final ArrayList<String> itemsCount;
    private ArrayList<String> suppliersDescription;
    private ArrayList<String> rate;
    private ArrayList<String> rateCount;
    private ArrayList<String> rateContent;
    private ArrayList<RatingObject> objectArrayList = new ArrayList<>();
    private RatingAdapter innerAdapter;


    public SuppliersCartListAdapter(Activity context, ArrayList<String> supplier_name, ArrayList<String> supplier_photo, ArrayList<String> supplier_phone, ArrayList<String> distance, ArrayList<String> shippingCost, ArrayList<String> total, ArrayList<String> TotalItemsCount, ArrayList<String> itemsCount, ArrayList<String> suppliersDescription, ArrayList<String> rate, ArrayList<String> rateCount, ArrayList<String> rateContent) {
        super(context, R.layout.worker_item, supplier_name);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.supplier_name = supplier_name;
        this.supplier_photo = supplier_photo;
        this.supplier_phone = supplier_phone;
        this.distance = distance;
        this.shippingCost = shippingCost;
        this.total = total;
        this.TotalItemsCount = TotalItemsCount;
        this.itemsCount = itemsCount;
        this.suppliersDescription = suppliersDescription;
        this.rate = rate;
        this.rateCount = rateCount;
        this.rateContent = rateContent;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.supplier_cart_item, null, true);


        ImageView imageView = (ImageView) rowView.findViewById(R.id.worker_image);
        TextView name = (TextView) rowView.findViewById(R.id.worker_name);
        TextView contact = (TextView) rowView.findViewById(R.id.worker_contact);
        TextView address = (TextView) rowView.findViewById(R.id.worker_address);
        TextView total_price = (TextView) rowView.findViewById(R.id.total_price);
        TextView items_count = (TextView) rowView.findViewById(R.id.items_count);
        TextView description = rowView.findViewById(R.id.suppliers_description_text_view);
        LinearLayout ratingContainer = rowView.findViewById(R.id.rating_container);
        RatingBar ratingBar = rowView.findViewById(R.id.rating_progress);
        TextView ratingNumber = rowView.findViewById(R.id.rating_number);

        final String phone = supplier_phone.get(position);
        ratingContainer.setOnClickListener(click -> {
            BottomSheetDialog dialog = new BottomSheetDialog(context);
            dialog.setContentView(R.layout.rate_bottom_sheet_dialog);
            ListView listView = dialog.findViewById(R.id.rate_list_view);
            try {
                JSONArray array = new JSONArray(rateContent.get(position));
                objectArrayList.clear();
                for (int i = 0; i < array.length(); i++) {
                    Log.d("adsd", String.valueOf(array.length()));
                    JSONObject object = array.getJSONObject(i);
                    objectArrayList.add(new RatingObject(
                            object.getString("rate"),
                            object.getString("rateDate"),
                            object.getString("rateNotes")));
                }
                innerAdapter = new RatingAdapter(context, objectArrayList);
                listView.setAdapter(innerAdapter);
            } catch (Exception e) {
                Log.d("adsd", e.getMessage());

            }
            dialog.show();
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                context.startActivity(intent);
            }
        });

        name.setText(supplier_name.get(position));
        contact.setText(supplier_phone.get(position));
        address.setText(distance.get(position));
        total_price.setText(total.get(position));
        description.setText(suppliersDescription.get(position));
        items_count.setText(itemsCount.get(position) + " من " + TotalItemsCount.get(position) + " وحدة ");
        try {
            if (rate.size() > 0) {
                ratingBar.setRating(Float.parseFloat(rate.get(position)));
            } else {
                ratingBar.setRating(0.0f);
            }
        } catch (Exception e) {
            ratingBar.setRating(0.0f);
            Log.e("ratingbar", e.getMessage());
        }

        ratingNumber.setText(rateCount.get(position));
        Picasso.get().load(imageupload + supplier_photo.get(position))
                .resize(128, 128)
                .transform(new CropCircleTransformation())
                .into(imageView);
        //imageView.setImageResource(map_img[position]);
        return rowView;

    }
}