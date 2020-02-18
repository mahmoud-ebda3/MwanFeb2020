package com.ebda3.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ebda3.Model.DetailsObject;
import com.ebda3.mwan.MaterialsDetailsActivity;
import com.ebda3.mwan.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.ebda3.mwan.MaterialsDetailsActivity.selectedItemsdetails;

public class MaterialsDetailsListAdapter extends BaseAdapter {

    private Context context;
    private List<DetailsObject> list;
    private SpinnerListTextAdapter adapter;
    List<String> color = new ArrayList<>();

    public static ArrayList<String> details_name = new ArrayList<>();


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
                spinnerList.add("اختر " + object.getSectionName());
                String type = "s";
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = array.getJSONObject(i);
                    if (row.getString("Name").contains("***")) {
                        type = "v";
                        String name = row.getString("Name");
                        String[] arr = name.split("\\*\\*\\*");
                        spinnerList.add(arr[0]);
                        if (i > 0) {
                            color.add(arr[1].toString());
                        }
                        Log.e("asterisk", name + " - " + arr[0] + " - " + "\"" + arr[1].toString() + "\"");
                    } else {
                        spinnerList.add(row.getString("Name"));
                    }
                }
                if ( type.equals("s") ) {
                    Log.e("asterisk", String.valueOf(color.size()));
                    if (color.size() > 0) {
                        Log.e("asterisk", String.valueOf(color.size()));
                        adapter = new SpinnerListTextAdapter(context, spinnerList, color);
                    } else {
                        adapter = new SpinnerListTextAdapter(context, spinnerList, null);
                    }
                    adapter.setDropDownViewResource(R.layout.spinner_text_list_items);
                    sectionSpinner.setAdapter(adapter);
                    sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position > 0) {
                                Log.e("adapter_selected", String.valueOf(sectionSpinner.getSelectedItem()) + "-" + object.getID());
                                MaterialsDetailsActivity.selectedItems.put(object.getID(), String.valueOf(sectionSpinner.getSelectedItem()));
                                selectedItemsdetails.put(object.getSectionName(), String.valueOf(sectionSpinner.getSelectedItem()));
                                Log.d("adapter_selected_item", String.valueOf(MaterialsDetailsActivity.selectedItems));
                                Log.d("adapter_selecteddetails", String.valueOf(selectedItemsdetails));
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else
                {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT
                            ,80);
                    params.setMargins(5, 10, 5, 10);

                    ArrayList<LinearLayout> arrayList = new ArrayList<>();
                    sectionSpinner.setVisibility(View.GONE);
                    final LinearLayout LinearLayout_details = convertView.findViewById(R.id.LinearLayout_details);
                    LinearLayout_details.setVisibility(View.VISIBLE);
                    LinearLayout_details.removeAllViews();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        String name = row.getString("Name");
                        String[] arr = name.split("\\*\\*\\*");
                        LinearLayout layoutDetails = new LinearLayout(context);
                        TextView textView = new TextView(context);
                        textView.setTextColor(context.getResources().getColor(R.color.black));
                        textView.setText(arr[0]);
                        textView.setLayoutParams(params);
                        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        layoutDetails.addView(textView);
                        layoutDetails.setLayoutParams(params);
                        layoutDetails.setBackgroundColor(Color.parseColor(arr[1]));
                        layoutDetails.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MaterialsDetailsActivity.selectedItems.put(object.getID(),arr[0] );
                                selectedItemsdetails.put(object.getSectionName(),arr[0] );
                                for ( int i = 0 ; i < arrayList.size() ; i++  )
                                {
                                    arrayList.get(i).getBackground().setAlpha(30);
                                }
                                view.getBackground().setAlpha(255);
                            }
                        });
                        arrayList.add(layoutDetails);
                        LinearLayout_details.addView(layoutDetails);
                    }


                }
            }

        } catch (Exception e) {

        }
        return convertView;
    }
}
