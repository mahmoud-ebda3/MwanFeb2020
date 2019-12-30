package com.ebda3.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ebda3.Model.DetailsObject;
import com.ebda3.mwan.MaterialsDetailsActivity;
import com.ebda3.mwan.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MaterialsDetailsListAdapter extends BaseAdapter {

    private Context context;
    private List<DetailsObject> list;
    private SpinnerListTextAdapter adapter;



    public MaterialsDetailsListAdapter(Context context, List<DetailsObject> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_list_item, parent, false);
        }
        TextView sectionName = convertView.findViewById(R.id.section_details_name);
        final Spinner sectionSpinner = convertView.findViewById(R.id.details_spinner_options);
        final DetailsObject object = list.get(position);
        sectionName.setText(object.getSectionName());
        ArrayList<String> spinnerList = new ArrayList<>();
        try {
            if (!object.getDetailsResponse().isEmpty()) {
                Log.e("9898", object.getDetailsResponse());
                JSONArray array = new JSONArray(object.getDetailsResponse());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = array.getJSONObject(i);
                    spinnerList.add(row.getString("Name"));
                }
                adapter = new SpinnerListTextAdapter(context, spinnerList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sectionSpinner.setAdapter(adapter);
                sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.e("490", String.valueOf(sectionSpinner.getSelectedItem()) + "-" + object.getID());
                        MaterialsDetailsActivity.selectedItems.put(object.getID(),String.valueOf(sectionSpinner.getSelectedItem()));
                        Log.d("adapter_selected_item",String.valueOf( MaterialsDetailsActivity.selectedItems));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

        } catch (Exception e) {

        }
        return convertView;
    }
}
