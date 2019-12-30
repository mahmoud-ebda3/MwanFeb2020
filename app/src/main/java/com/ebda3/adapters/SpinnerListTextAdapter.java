package com.ebda3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ebda3.mwan.R;

import java.util.ArrayList;

public class SpinnerListTextAdapter extends ArrayAdapter<String> {

    public SpinnerListTextAdapter(Context context, ArrayList<String> stringList) {
        super(context, 0, stringList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_text_list_items, parent, false);
        }

        String object = getItem(position);
        TextView textView = convertView.findViewById(R.id.spinner_list_item_text);
        textView.setText(object);
        return convertView;
    }
}
