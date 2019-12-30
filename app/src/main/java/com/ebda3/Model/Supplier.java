package com.ebda3.Model;

/**
 * Created by work on 15/10/2017.
 */

public class Supplier {
    public String suppliername;
    public String supplierID;
    public String photo;
    public String location;
    public String address;
    public String near_dist;

    public Supplier()
    {
        this.suppliername = "";
        this.photo = "";
        this.location = "";
        this.address = "";
        this.near_dist = "";
        this.supplierID = "";
    }

    public Supplier(String suppliername, String supplierID, String photo, String location, String address, String near_dist) {
        this.suppliername = suppliername;
        this.supplierID = supplierID;
        this.photo = photo;
        this.location = location;
        this.address = address;
        this.near_dist = near_dist;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getSuppliername() {
        return suppliername;
    }

    public void setSuppliername(String suppliername) {
        this.suppliername = suppliername;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNear_dist() {
        return near_dist;
    }

    public void setNear_dist(String near_dist) {
        this.near_dist = near_dist;
    }
}
