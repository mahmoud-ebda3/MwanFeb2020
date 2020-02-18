package com.ebda3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ebda3.mwan.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SpinnerListTextAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> strings;
    private ArrayList<String> stringList;

    public SpinnerListTextAdapter(Context context, ArrayList<String> stringList, List<String> strings) {
        super(context, 0, stringList);
        this.context = context;
        this.strings = strings;
        this.stringList = stringList;
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
