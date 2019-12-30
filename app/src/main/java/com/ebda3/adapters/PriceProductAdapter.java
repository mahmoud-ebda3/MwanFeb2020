package com.ebda3.adapters;

import android.app.Activity;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ebda3.Model.Product;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebda3.Helpers.Config;
import com.ebda3.mwan.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.ebda3.Helpers.Config.imageupload;



/**
 * Created by work on 15/10/2017.
 */

public class PriceProductAdapter extends ArrayAdapter<Product> {

    private final Activity context;
    private final ArrayList<Product> MyProduct;

    public LayoutInflater inflater2;
    public View dialoglayout ;
    public AlertDialog.Builder builder;
    public static String my_position;
    public ProgressBar save_progress;
    public boolean my_boolean = false;

    public PriceProductAdapter(Activity context, ArrayList<Product> MyProduct) {
        super(context, R.layout.price_item_activity,MyProduct);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.MyProduct=MyProduct;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.price_item_activity, null,true);


        ImageView dialog_image = (ImageView) rowView.findViewById(R.id.price_img_id);
        TextView dialog_price_name = (TextView) rowView.findViewById(R.id.price_name_id);
        final TextView dialog_price_count = (TextView) rowView.findViewById(R.id.price_name_count);

        Picasso.with(context).load(imageupload+MyProduct.get(position).getProductImage())
                .resize(25,25)
                .centerCrop()
                .transform(new CropCircleTransformation())
                .error(R.drawable.image_not_found)
                .into(dialog_image);


        dialog_price_name.setText(MyProduct.get(position).getProductName());
        dialog_price_count.setText(MyProduct.get(position).getProductPrice());

        dialog_price_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflater2 = context.getLayoutInflater();
                dialoglayout = inflater2.inflate(R.layout.price_dialog, null);
                builder = new AlertDialog.Builder(context);
                builder.setView(dialoglayout);
                my_boolean=false;
                final AlertDialog ad = builder.show();

                final EditText new_prod_count = (EditText) dialoglayout.findViewById(R.id.dialog_new_price) ;
                final EditText new_prod_amount = (EditText) dialoglayout.findViewById(R.id.dialog_new_amount) ;
                final Button done_button = (Button) dialoglayout.findViewById(R.id.dialog_button_done);
                save_progress = (ProgressBar) dialoglayout.findViewById(R.id.save_price_product);

                new_prod_count.setText( MyProduct.get(position).getProductPrice() );
                done_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        done_button.setVisibility(View.GONE);
                        save_progress.setVisibility(View.VISIBLE);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://adc-company.net/mwan/item_prices-edit-1.html?json=true&ajax_page=true",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        response = fixEncoding(response);
                                        Log.d("items22",response);
                                        try
                                        {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.has("ID"))
                                            {
                                                MyProduct.get(position).setProductPrice(jsonObject.getString("price")+ " جنية");
                                                //MyProduct.set(position,jsonObject.getString("price")+ " جنية");
                                                save_progress.setVisibility(View.GONE);
                                                done_button.setVisibility(View.VISIBLE);
                                                //dialog_price_count.setText(new_prod_count.getText().toString() + " جنية");
                                                ad.dismiss();
                                                notifyDataSetChanged();
                                            }
                                            else
                                            {
                                                Toast.makeText(context,"حدث خطأ فى ارسال البيانات",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        catch (JSONException e)
                                        {
                                            Toast.makeText(context,"حدث خطأ فى ارسال البيانات",Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error)
                                    {
                                        Log.d("items22",String.valueOf(error));
                                        save_progress.setVisibility(View.GONE);
                                        done_button.setVisibility(View.VISIBLE);
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("do", "insert");

                                map.put("json_email", Config.getJsonEmail(context) );
                                map.put("json_password", Config.getJsonPassword(context));

                                map.put("item",MyProduct.get(position).getProductID());
                                map.put("price",new_prod_count.getText().toString());
                                map.put("amount",new_prod_amount.getText().toString());
                                map.put("active","yes");
                                return map;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);


                    }
                });
            }
        });
        return rowView;

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
}