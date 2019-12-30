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
    public static final String imageupload = "http://adc-company.net/mwan/uploads/";
    public static final String imageupload1 = "http://adc-company.net/mwan/category/";
    public static final String LoginUrl = "http://adc-company.net/mwan/include/pages/login_cust.php?json=true";
    public static final String ServiceRequest = "http://ebda3-eg.com/maintenance/service_provider-edit-1.html?json=true&ajax_page=true";
    public static final String SignUp = "http://adc-company.net/mwan/suppliers-edit-1.html?json=true&ajax_page=true";
    public static final String SignUpSupplier = "http://adc-company.net/mwan/suppliers-edit-1.html?json=true&ajax_page=true";
    public static final String SignUpUser = "http://adc-company.net/mwan/users-edit-1.html?json=true&ajax_page=true";
    public static final String GetSection = "http://adc-company.net/mwan/sections-edit-1.html?json=true&ajax_page=true";
    public static final String PriceUrl = "http://adc-company.net/mwan/items-edit-1.html?json=true&ajax_page=true";
    public static final String GetCategory = "http://adc-company.net/mwan/include/cats_json.php?type=items&sub=0";
    public static final String GetMaterials = "http://adc-company.net/mwan/include/cats_json.php?type=materials&sub=0";
    public static final String webServiceURL = "http://adc-company.net/mwan/include/webService.php?json=true";
    public static boolean isOpened = false;
    public static ArrayList<String> WorkersCatsNames = new ArrayList<String>();
    public static ArrayList<String> WorkersCatsIDS = new ArrayList<String>();
    public static ArrayList<String> PropertyNames = new ArrayList<String>();
    public static ArrayList<String> PropertyIDS = new ArrayList<String>();


    public static ArrayList<Cart> cartData = new ArrayList<Cart>();

    public static String getJsonEmail(Context context) {

        SharedPreferences sp1 = context.getSharedPreferences("Login", 0);
        String email = sp1.getString("email", " ");
        return email;
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
