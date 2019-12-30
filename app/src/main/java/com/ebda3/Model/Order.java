package com.ebda3.Model;

/**
 * Created by work on 17/10/2017.
 */

public class Order {
    public String ID;
    public String Address;
    public String Price;

    public Order()
    {
        this.ID = "";
        this.Address = "";
        this.Price = "";
    }

    public Order(String ID, String address, String price) {
        this.ID = ID;
        Address = address;
        Price = price;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
