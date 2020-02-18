package com.ebda3.mwan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.adapters.Items1ListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ebda3.Helpers.Config.cartData;


/**
 * Created by work on 23/10/2017.
 */

public class ItemsActivity extends AppCompatActivity {


    Activity activity = this;

    public Toolbar toolbar;
    public TextView headline;
    public String id, Name, Location, companies, filters, queryWord;

    Spinner filtrationSpinner, companiesSpinner;
    Items1ListAdapter adapter;
    GridView product_grid;
    public LinearLayout no_data, filtrationCompaniesContainer;
    Typeface typeface;
    public String filter = "";
    public String company = "";
    public int StartFrom = 0;
    public int LastStartFrom = 0;
    public int VolleyCurrentConnection = 0;
    public int LimitBerRequest = 20;
    String details;

    View footerView;

    public Activity context = this;
    public ArrayList<String> companyNameArray = new ArrayList<>();
    public ArrayList<String> filterNameArray = new ArrayList<>();
    public ArrayList<String> companyIdArray = new ArrayList<>();
    public ArrayList<String> filterIdArray = new ArrayList<>();
    public ArrayList<String> ItemName = new ArrayList<>();
    public ArrayList<String> ItemPhoto = new ArrayList<>();
    public ArrayList<Float> ItemPrice = new ArrayList<>();
    public ArrayList<Float> shippingCost = new ArrayList<>();
    public ArrayList<Integer> ItemID = new ArrayList<>();
    public ArrayList<String> ItemPartnerName = new ArrayList<>();
    public ArrayList<Integer> ItemAvailableAmount = new ArrayList<>();
    public ArrayList<String> ItemPartnerID = new ArrayList<>();
    public ArrayList<String> ItemUnit = new ArrayList<>();
    public ArrayList<String> ItemDescription = new ArrayList<>();
    public Boolean setAdapterStatus = false;
    ProgressBar loadProgress;
    ArrayAdapter<String> filtrationAdapter;
    ArrayAdapter<String> companiesAdapter;
    CardView filtrationContainer, companiesContainer;
    LinearLayout shopping_cart_image;

    public Button cart_button;
    ImageView companiesFiltrationIcon;
    ImageView filtrationIcon;
    public static RelativeLayout cart_info;
    public static TextView notificationNum;
    public static CountDownTimer timer = null;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onResume() {
        super.onResume();
        if (cartData.size() > 0) {
            notificationNum.setVisibility(View.VISIBLE);
            notificationNum.setText(String.valueOf(cartData.size()));
        } else {
            notificationNum.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        CartConfirm.BackAction = false;
        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        headline = (TextView) toolbar.findViewById(R.id.app_headline);
        companiesFiltrationIcon = toolbar.findViewById(R.id.companies_icon);
        filtrationIcon = toolbar.findViewById(R.id.filteration_icon);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        filtrationContainer = findViewById(R.id.filtration_container);
        companiesContainer = findViewById(R.id.companies_container);
        filtrationSpinner = findViewById(R.id.filtration_spinner);
        companiesSpinner = findViewById(R.id.companies_spinner);
        filtrationCompaniesContainer = findViewById(R.id.filtration_companies_container);
        filtrationAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text_list_items, filterNameArray);
        companiesAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text_list_items, companyNameArray);
        filtrationAdapter.setDropDownViewResource(R.layout.spinner_text_list_items);
        companiesAdapter.setDropDownViewResource(R.layout.spinner_text_list_items);
        queryWord = "";
        filtrationSpinner.setAdapter(filtrationAdapter);
        companiesSpinner.setAdapter(companiesAdapter);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        cart_info = (RelativeLayout) findViewById(R.id.cart_info);
        cart_button = (Button) findViewById(R.id.cart_button);
        shopping_cart_image = toolbar.findViewById(R.id.notificationB);
        headline.setText("سلة المشتريات");
        shopping_cart_image.setVisibility(View.VISIBLE);
        notificationNum = (TextView) toolbar.findViewById(R.id.notificationNum);
        cart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, NewCart.class);
                startActivity(myIntent);
            }
        });
        shopping_cart_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, NewCart.class);
                startActivity(myIntent);
            }
        });

        id = getIntent().getStringExtra("ID");
        if (id == null) {
            id = "";
        }
        details = getIntent().getStringExtra("Details");
        Name = getIntent().getStringExtra("Name");
        companies = getIntent().getStringExtra("companies");
        filters = getIntent().getStringExtra("filter");
        if (filters.length() <= 2 && companies.length() <= 2) {
            filtrationCompaniesContainer.setVisibility(View.GONE);
        }
        if (filters.length() > 8) {
            try {
                JSONArray jsonArray = new JSONArray(filters);
                filterNameArray.add("أختر نوع");
                filterIdArray.add("");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    filterIdArray.add(jsonObject.getString("ID"));
                    filterNameArray.add(jsonObject.getString("name"));
                }
                filtrationAdapter.notifyDataSetChanged();
            } catch (Exception e) {

            }

            filtrationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    filter = filterIdArray.get(position);
                    swipeRefreshLayout.setRefreshing(true);
                    if (setAdapterStatus) {
                        ItemID.clear();
                        ItemName.clear();
                        ItemPhoto.clear();
                        ItemAvailableAmount.clear();
                        ItemPartnerName.clear();
                        ItemPartnerID.clear();
                        ItemPrice.clear();
                        shippingCost.clear();
                    }
                    VolleyCurrentConnection = 0;
                    StartFrom = 0;
                    loadData();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    filter = "";
                    swipeRefreshLayout.setRefreshing(true);
                    if (setAdapterStatus) {
                        ItemID.clear();
                        ItemName.clear();
                        ItemPhoto.clear();
                        ItemAvailableAmount.clear();
                        ItemPartnerName.clear();
                        ItemPartnerID.clear();
                        ItemPrice.clear();
                        shippingCost.clear();
                    }

                    VolleyCurrentConnection = 0;
                    StartFrom = 0;
                    loadData();
                }
            });
            swipeRefreshLayout.setRefreshing(true);
            if (setAdapterStatus) {
                ItemID.clear();
                ItemName.clear();
                ItemPhoto.clear();
                ItemAvailableAmount.clear();
                ItemPartnerName.clear();
                ItemPartnerID.clear();
                ItemPrice.clear();
                shippingCost.clear();
            }
            VolleyCurrentConnection = 0;
            StartFrom = 0;
            loadData();
        } else {
            filtrationContainer.setVisibility(View.GONE);
        }

        if (companies.length() > 8) {
            try {
                JSONArray jsonArray = new JSONArray(companies);
                companyNameArray.add("أختر شركة");
                companyIdArray.add("");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    companyIdArray.add(jsonObject.getString("ID"));
                    companyNameArray.add(jsonObject.getString("name"));
                }
                companiesAdapter.notifyDataSetChanged();
            } catch (Exception e) {

            }
            companiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    company = companyIdArray.get(position);
                    swipeRefreshLayout.setRefreshing(true);
                    if (setAdapterStatus) {
                        ItemID.clear();
                        ItemName.clear();
                        ItemPhoto.clear();
                        ItemAvailableAmount.clear();
                        ItemPartnerName.clear();
                        ItemPartnerID.clear();
                        ItemPrice.clear();
                        shippingCost.clear();
                        ItemUnit.clear();
                        ItemDescription.clear();
                    }
                    VolleyCurrentConnection = 0;
                    StartFrom = 0;
                    loadData();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    company = "";
                    swipeRefreshLayout.setRefreshing(true);
                    if (setAdapterStatus) {
                        ItemID.clear();
                        ItemName.clear();
                        ItemPhoto.clear();
                        ItemAvailableAmount.clear();
                        ItemPartnerName.clear();
                        ItemPartnerID.clear();
                        ItemPrice.clear();
                        shippingCost.clear();
                    }
                    VolleyCurrentConnection = 0;
                    StartFrom = 0;
                    loadData();
                }

            });
        } else {
            companiesContainer.setVisibility(View.GONE);
        }
        headline.setText(Name);
        if (getIntent().hasExtra("location")) {
            Location = getIntent().getStringExtra("location");
        } else if (getIntent().getStringExtra("extra").equals("one")) {
            headline.setText("بحث");
            queryWord = getIntent().getStringExtra("query_word");
            companies = getIntent().getStringExtra("companies");
            filters = getIntent().getStringExtra("filter");
            filter = "";
            company = "";
            id = "";
            loadData();
        } else {
            Intent intent = new Intent(context, SetLocation.class);
            intent.putExtra("ID", id);
            intent.putExtra("Name", Name);
            startActivity(intent);
        }


        loadProgress = (ProgressBar) findViewById(R.id.loadProgress);

        footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_footer, null, false);

        no_data = (LinearLayout) findViewById(R.id.no_data);

        product_grid = (GridView) findViewById(R.id.grid_prod);
        loadData();

        product_grid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view,
                                 int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
//Algorithm to check if the last item is visible or not
                final int lastItem = firstVisibleItem + visibleItemCount;
                Log.d("lastItem", String.valueOf(visibleItemCount));
                if (lastItem == totalItemCount) {
                    loadData();
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    loadData();
                }
            }
        });

        product_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub


//                ID = getIntent().getStringExtra("ID");
//                Name = getIntent().getStringExtra("Name");
//                Location = getIntent().getStringExtra("ID");
//                Photo = getIntent().getStringExtra("ID");
//                Details = getIntent().getStringExtra("Details");

                Intent intent = new Intent(activity, MaterialsDetailsActivity.class);
                intent.putExtra("ID", String.valueOf(ItemID.get(position)));
                intent.putExtra("Name", ItemName.get(position));
                intent.putExtra("Photo", ItemPhoto.get(position));
                intent.putExtra("Details", ItemPartnerID.get(position));
                intent.putExtra("Unit", ItemUnit.get(position));
                intent.putExtra("Description", ItemDescription.get(position));


                //  Log.d("itemphoto", String.valueOf(ItemID.get(position)));
                startActivity(intent);

            }
        });

    }

    public void refreshData() {
        if (setAdapterStatus) {
            ItemID.clear();
            ItemName.clear();
            ItemPhoto.clear();
            ItemAvailableAmount.clear();
            ItemPartnerName.clear();
            ItemPartnerID.clear();
            ItemPrice.clear();
            shippingCost.clear();
        }
        VolleyCurrentConnection = 0;
        StartFrom = 0;
        company = "";
        filter = "";
        queryWord = "";
        companiesSpinner.setSelection(0);
        filtrationSpinner.setSelection(0);
        loadData();
    }


    public void loadData() {
        if (VolleyCurrentConnection == 0) {
            VolleyCurrentConnection = 1;
            String VolleyUrl = "https://www.mawaneg.com/supplier/include/webService.php?json=true&do=GetMaterials&materials=" + id + "&word=" + queryWord + "&company=" + company + "&filter=" + filter + "&start=" + String.valueOf(StartFrom) + "&end=" + String.valueOf(LimitBerRequest);
            Log.d("fdfdf", VolleyUrl);
            try {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, VolleyUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadProgress.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("res", response);
                        //response = fixEncoding(response);
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {
                                product_grid.setVisibility(View.VISIBLE);
                                no_data.setVisibility(View.GONE);
                                VolleyCurrentConnection = 0;
                                StartFrom += LimitBerRequest;
                                LastStartFrom = StartFrom;
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject row = array.getJSONObject(i);
                                    ItemID.add(row.getInt("ID"));
                                    ItemName.add(row.getString("name").toString());
                                    ItemPhoto.add(row.getString("photo").toString());
                                    ItemAvailableAmount.add(row.getInt("ID"));
                                    ItemPartnerName.add(row.getString("ID").toString());
                                    ItemUnit.add(row.getString("unit"));
                                    ItemDescription.add(row.getString("description").toString());
                                    ItemPartnerID.add(row.getString("itemInfo"));
                                    ItemPrice.add(0F);
                                    shippingCost.add(Float.parseFloat(row.getString("ID").toString()));
                                }

                            }
                        } catch (JSONException e) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        if (ItemID.size() == 0) {
                            no_data.setVisibility(View.VISIBLE);
                            product_grid.setVisibility(View.GONE);
                        }

                        if (!setAdapterStatus) {
                            adapter = new Items1ListAdapter(context, ItemID, ItemName, ItemPhoto, ItemPrice, shippingCost, ItemAvailableAmount, ItemPartnerName);
                            product_grid.setAdapter(adapter);
                            setAdapterStatus = true;
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new CountDownTimer(3000, 1000) {
                            public void onFinish() {
                                VolleyCurrentConnection = 0;
                                loadData();
                            }

                            public void onTick(long millisUntilFinished) {
                                // millisUntilFinished    The amount of time until finished.
                            }
                        }.start();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap();
                        //paramas.put("type", "1");
                        return params;
                    }
                };

                int socketTimeout = 10000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(stringRequest);
            } catch (Exception e) {
                swipeRefreshLayout.setRefreshing(false);
                VolleyCurrentConnection = 0;
                loadData();
            }
        }
    }

    public static String fixEncoding(String response) {
        try {
            byte[] u = response.toString().getBytes(
                    "ISO-8859-1");
            response = new String(u, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    public static void expand(final View v) {
        v.measure(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? Toolbar.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);

        try {
            timer.cancel();
        } catch (Exception e) {

        }

        timer = new CountDownTimer(3000, 1000) {
            public void onFinish() {
                Log.d("123", "123");
                ItemsActivity.collapse(v);
                //ItemsActivity.cart_info.setVisibility(View.GONE);
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }

    public static void collapse(View v) {
        int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
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
}
