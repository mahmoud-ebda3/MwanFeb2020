package com.ebda3.mwan;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Helpers.Config;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddBid extends AppCompatActivity {


    public EditText Details, more_notes;
    public String id, itemID;

    Context context;
    LinearLayout progressBar;
    LinearLayout Add, ChooseDate, Choose_start_date, Choose_Contract_date, Choose_supply, materialContainer;
    TextView ch_time, supply_time, contract_time, start_time;
    Toolbar toolbar;
    TextView headline, materialText;
    private static int mYear, mMonth, mDay, mHour, mMinute;
    private static String Today, supplyToday, contractToday, startToday;
    ArrayAdapter<String> adapter;
    ArrayList<String> itemsName = new ArrayList<>();
    ArrayList<String> itemsID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bid);

        context = this;
        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);
        headline.setText("مناقصة جديدة");
        materialText = findViewById(R.id.material_type);
        Details = (EditText) findViewById(R.id.details);
        more_notes = findViewById(R.id.more_notes);
        materialContainer = findViewById(R.id.material_container);
        Add = (LinearLayout) findViewById(R.id.add);
        progressBar = (LinearLayout) findViewById(R.id.progress1);

        ChooseDate = (LinearLayout) findViewById(R.id.choose_date);
        Choose_Contract_date = (LinearLayout) findViewById(R.id.contract_date);
        Choose_start_date = (LinearLayout) findViewById(R.id.choose_start_date);
        Choose_supply = (LinearLayout) findViewById(R.id.supply_start_date);
        ch_time = (TextView) findViewById(R.id.ch_time);
        start_time = findViewById(R.id.ch_start_time);
        contract_time = findViewById(R.id.contract_time);
        supply_time = findViewById(R.id.supply_start_time);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        Today = String.valueOf(mYear) + "-" + String.valueOf(mMonth + 1) + "-" + String.valueOf(mDay);
        startToday = String.valueOf(mYear) + "-" + String.valueOf(mMonth + 1) + "-" + String.valueOf(mDay);
        contractToday = String.valueOf(mYear) + "-" + String.valueOf(mMonth + 1) + "-" + String.valueOf(mDay);
        supplyToday = String.valueOf(mYear) + "-" + String.valueOf(mMonth + 1) + "-" + String.valueOf(mDay);
        ch_time.setText(Today);
        start_time.setText(startToday);
        contract_time.setText(contractToday);
        supply_time.setText(supplyToday);
        Log.e("time", Today);
        materialContainer.setOnClickListener(click -> {
            BottomSheetDialog dialog = new BottomSheetDialog(context);
            dialog.setContentView(R.layout.bid_dialog_layout);
            ListView listView = dialog.findViewById(R.id.list_view_dialog);
//            ListView searchList = dialog.findViewById(R.id.search_list_view);
            EditText editText = dialog.findViewById(R.id.search_edit_text_bid);
//            editText.setOnClickListener(clickB ->{
//                listView.setVisibility(View.GONE);
//                searchList.setVisibility(View.VISIBLE);
//            });
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(editText.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            listView.setNestedScrollingEnabled(true);
            adapter = new ArrayAdapter<>(context, R.layout.bid_dialog_layout_item, itemsName);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                itemID = itemsID.get(position);
                materialText.setText(itemsName.get(position));
                dialog.dismiss();
            });
            dialog.show();
        });
        ChooseDate.setOnClickListener(click -> {
            DatePickerDialog dpd = new DatePickerDialog(context,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        Today = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        ch_time.setText(Today);
                    }, mYear, mMonth, mDay);
            dpd.show();
        });

        Choose_start_date.setOnClickListener(click -> {
            DatePickerDialog dpd = new DatePickerDialog(context,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        Today = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        start_time.setText(Today);

                    }, mYear, mMonth, mDay);
            dpd.show();
        });

        Choose_Contract_date.setOnClickListener(click -> {
            DatePickerDialog dpd = new DatePickerDialog(context,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        Today = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        contract_time.setText(Today);

                    }, mYear, mMonth, mDay);
            dpd.show();
        });

        Choose_supply.setOnClickListener(click -> {
            DatePickerDialog dpd = new DatePickerDialog(context,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        Today = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        supply_time.setText(Today);

                    }, mYear, mMonth, mDay);
            dpd.show();
        });
        loadDate();
        Add.setOnClickListener(click -> {
            if (Details.getText().toString().length() < 4) {
                Details.setError("من فضلك اكتب تفاصيل المناقصة");
                return;
            } else if (!more_notes.getText().toString().isEmpty()) {
                if (more_notes.getText().toString().length() < 4) {
                    more_notes.setError("من فضلك أدخل ملاحظات أضافية");
                    return;
                }
            } else if (materialText.getText().toString().isEmpty()) {
                Toast.makeText(context, "من فضلك أختر الخامة", Toast.LENGTH_SHORT).show();
                return;
            }
            SendData();
            finish();
        });
    }


    public void SendData() {
        boolean t = isNetworkConnected();
        if (t) {
            progressBar.setVisibility(View.VISIBLE);
            Add.setVisibility(View.GONE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://www.mawaneg.com/supplier/bids-edit-1.html?json=true&ajax_page=true",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("responsessss", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.has("ID")) {
                                    finish();
                                    Toast.makeText(context, "تم ارسال طلب الانضمام بنجاح وجارى مراجعتة", Toast.LENGTH_SHORT).show();
                                } else {
                                    AddAgain();
                                }
                            } catch (Exception e) {
                                AddAgain();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            AddAgain();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    if (!more_notes.getText().toString().isEmpty()) {
                        map.put("more_notes", more_notes.getText().toString());
                    }
                    map.put("material", itemID);
                    map.put("details", Details.getText().toString());
                    map.put("bid_date", Today);
                    map.put("supply_date", supplyToday);
                    map.put("contract_date", contractToday);
                    map.put("start_bid_date", startToday);
                    map.put("do", "insert");
                    map.put("json_email", Config.getJsonEmail(context));
                    map.put("json_password", Config.getJsonPassword(context));
                    Log.d("params", map.toString());
                    return map;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(context, "من فضلك تأكد من اتصالك بالإنترنت", Toast.LENGTH_LONG).show();
        }
    }

    public void AddAgain() {
        progressBar.setVisibility(View.GONE);
        Add.setVisibility(View.VISIBLE);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void loadDate() {
        String url = "https://www.mawaneg.com/supplier/include/webService.php?json=true&do=GetMaterials&end=100000";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Log.e("responsessss", response);
            try {
                JSONArray array = new JSONArray(response);
                if (array.length() > 2) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        itemsID.add(object.getString("ID"));
                        itemsName.add(object.getString("name"));
                    }
                    adapter.notifyDataSetChanged();
                }
            } catch (Exception e) {

            }
        }, error -> {

        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

}
