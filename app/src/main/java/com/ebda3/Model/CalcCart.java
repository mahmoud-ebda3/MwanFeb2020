package com.ebda3.Model;

import android.util.Log;

import com.ebda3.mwan.NewCart;

import java.util.ArrayList;

import static com.ebda3.Helpers.Config.cartData;

/**
 * Created by work on 11/10/2017.
 */

public class CalcCart {



    public Float GetTotalPrice() {
        NewCart.total_price.setText("1500 جنية");
        return 1500F;

    }
    public void GetAll() {

        com.ebda3.Model.Cart cart = new com.ebda3.Model.Cart();
        Float totalPrice = 0F;
        Float totalShipping = 0F;
        ArrayList<Integer> Partners  = new  ArrayList<>()  ;

        for(com.ebda3.Model.Cart cart1 : cartData) {
            totalPrice+= ( cart1.getPrice() * cart1.getAmount() );
            if( Partners.indexOf( cart1.getPartner_ID() )   == -1 ) {
                Partners.add(cart1.getPartner_ID());
                Log.d("rrrrrrrrrrr" , String.valueOf( Partners.indexOf( cart1.getPartner_ID() )  ) );
//                totalShipping += cart1.getShippingCost();
            }
        }




        /*
        CartActivity.total_price.setText( String.valueOf(totalPrice) +" جنية");
        CartActivity.shiping_price.setText(String.valueOf(totalShipping) +" جنية");
        CartActivity.net_price.setText(String.valueOf(totalPrice+totalShipping) +" جنية");
        */
    }



}
