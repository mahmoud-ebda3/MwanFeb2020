package com.ebda3.Helpers;
/**
 * Created by work on 11/10/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.ebda3.Model.Cart;

import java.util.ArrayList;

/**
 * Created by EBDA3 on 8/26/2017.
 */

public class Config {
    public static final String imageupload = "https://www.mawaneg.com/supplier/uploads/";
    public static final String imageupload1 = "https://www.mawaneg.com/supplier/category/";
    public static final String LoginUrl = "https://www.mawaneg.com/supplier/include/pages/login_cust.php?json=true";
    public static final String SignUp = "https://www.mawaneg.com/supplier/suppliers-edit-1.html?json=true&ajax_page=true";
    public static final String SignUpSupplier = "https://www.mawaneg.com/supplier/suppliers-edit-1.html?json=true&ajax_page=true";
    public static final String SignUpUser = "https://www.mawaneg.com/supplier/users-edit-1.html?json=true&ajax_page=true";
    public static final String GetSection = "https://www.mawaneg.com/supplier/sections-edit-1.html?json=true&ajax_page=true";
    public static final String PriceUrl = "https://www.mawaneg.com/supplier/items-edit-1.html?json=true&ajax_page=true";
    public static final String GetCategory = "https://www.mawaneg.com/supplier/include/cats_json.php?type=items&sub=0";
    public static final String GetMaterials = "https://www.mawaneg.com/supplier/include/cats_json.php?type=materials&sub=0";
    public static final String webServiceURL = "https://www.mawaneg.com/supplier/include/webService.php?json=true";
    public static boolean isOpened = false;
    public static ArrayList<String> WorkersCatsNames = new ArrayList<String>();
    public static ArrayList<String> WorkersCatsIDS = new ArrayList<String>();
    public static ArrayList<String> PropertyNames = new ArrayList<String>();
    public static ArrayList<String> PropertyIDS = new ArrayList<String>();


    public static ArrayList<Cart> cartData = new ArrayList<Cart>();

    public static String getJsonEmail(Context context) {

        SharedPreferences sp1 = context.getSharedPreferences("Login", 0);
        String data = "";
        String username = sp1.getString("username", "");
        if ( username.length() > 2 )
        {
            data = username;
        }
        else
        {
            data = sp1.getString("email", "");
        }
        return data;
    }


    public static String getJsonPassword(Context context) {
        SharedPreferences sp1 = context.getSharedPreferences("Login", 0);
        String u_password = sp1.getString("password", " ");
        return u_password;
    }


    public static String getUserID(Context context) {
        SharedPreferences sp1 = context.getSharedPreferences("Login", 0);
        String ID = sp1.getString("ID", "0");
        return ID;
    }

    public static String getPassword(Context context) {
        SharedPreferences sp1 = context.getSharedPreferences("Login", 0);
        String u_password = sp1.getString("normal_password", " ");
        return u_password;
    }

    public static String getLocation(Context context) {

        SharedPreferences sp1 = context.getSharedPreferences("Login", 0);
        String Location = sp1.getString("Location", " ");
        return Location;
    }

    public static void SetUserLocation(Context context , String Location ){
        SharedPreferences sp = context.getSharedPreferences("Login", 0);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putString("UserLocation", Location );
        Ed.commit();
    }

    public static String GetUserLocation(Context context ){
        SharedPreferences sp1 = context.getSharedPreferences("Login", 0);
        String UserLocation = sp1.getString("UserLocation", " ");
        return  UserLocation;
    }


    public static boolean isOpened() {
        return isOpened;
    }

    public static void setIsOpened(boolean isOpened) {
        Config.isOpened = isOpened;
    }


}
