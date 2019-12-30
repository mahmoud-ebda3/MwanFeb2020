package com.ebda3.Model;

/**
 * Created by work on 23/10/2017.
 */

public class UserOrder
{
    public String OrderID;
    public String productName;
    public String productCount;
    public String Price;

    public UserOrder()
    {
        OrderID="";
        productName="";
        productCount="";
        Price="";
    }

    public UserOrder(String orderID, String productName, String productCount, String price) {
        OrderID = orderID;
        this.productName = productName;
        this.productCount = productCount;
        Price = price;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
