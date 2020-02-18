package com.ebda3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ebda3.Model.RatingObject;
import com.ebda3.mwan.R;

import java.util.List;

public class RatingAdapter extends ArrayAdapter<RatingObject> {
    private Context context;
    private List<RatingObject> objects;

    public RatingAdapter(Context context, List<RatingObject> objects) {
        super(context, 0, objects);
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_content_items_layout, parent, false);
        }
        RatingObject obj = objects.get(position);
        RatingBar ratingBar = convertView.findViewById(R.id.content_rating_progress);
        TextView noteText = convertView.findViewById(R.id.content_rating_note);
        TextView dateText = convertView.findViewById(R.id.content_rating_date);

        ratingBar.setRating(Float.parseFloat(obj.getRateNumber()));
        noteText.setText(obj.getRateNote());
        dateText.setText(obj.getRateDate());
        return convertView;
    }
}
