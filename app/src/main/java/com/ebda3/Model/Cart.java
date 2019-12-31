package com.ebda3.Model;

import java.util.ArrayList;

/**
 * Created by work on 11/10/2017.
 */

public class Cart {
    public Integer ID;
    public String Name;
    public String Photo;
    public Float Price;
    public Float shippingCost;
    public Integer Amount;
    public Integer ItemAvailableAmount;
    public Integer PartnerID;
    public String PartnerName;
    public String SelectedItems;
    public ArrayList<String> details_name;
    public ArrayList<String> details_value;


    public Cart() {
        ID = 0;
        Name = "";
        Photo = "";
        Price = 0F;
        PartnerID = 0;
        Amount = 0;
        ItemAvailableAmount = 0;
        PartnerName = "";
        details_name = new ArrayList<>();
        details_value = new ArrayList<>();
    }

    public Cart(Integer ID, String Name, String Photo) {
        this.ID = ID;
        this.Name = Name;
        this.Photo = Photo;
        Price = 0F;
        PartnerID = 0;
        Amount = 0;
        ItemAvailableAmount = 0;
        PartnerName = "";
        SelectedItems = "";
        details_name = new ArrayList<>();
        details_value = new ArrayList<>();
    }

    public Cart(Integer ID, String name, String photo, String PartnerName, Float price, Float shippingCost, Integer partner_ID, Integer amount, Integer ItemAvailableAmount, String SelectedItems, ArrayList<String> details_value, ArrayList<String> details_name) {
        this.ID = ID;
        this.Name = name;
        this.Photo = photo;
        this.Price = price;
        this.shippingCost = shippingCost;
        this.PartnerName = PartnerName;
        this.PartnerID = partner_ID;
        this.Amount = amount;
        this.SelectedItems = SelectedItems;
        this.details_name = details_name;
        this.details_value = details_value;
    }



    public Integer getID() {
        return ID;
    }
    public String getSelectedItems() {
        return SelectedItems;
    }

    public String getName() {
        return Name;
    }

    public String getPhoto() {
        return Photo;
    }

    public Float getPrice() {
        return Price;
    }

    public ArrayList<String> getDetailsName() { return details_name; }

    public ArrayList<String> getDetailsValue() { return details_value; }

    public Float getShippingCost() {
        return shippingCost;
    }

    public Integer getAmount() {
        return Amount;
    }

    public Integer getItemAvailableAmount() {
        return ItemAvailableAmount;
    }

    public Integer getPartner_ID() {
        return PartnerID;
    }

    public String getPartnerName() {
        return PartnerName;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setSelectedItems(String SelectedItems) {
        this.SelectedItems = SelectedItems;
    }

    public void setPhoto(String Photo) {
        this.Photo = Photo;
    }

    public void setPrice(Float Price) {
        this.Price = Price;
    }

    public void setShippingCost(Float shippingCost) {
        this.shippingCost = shippingCost;
    }


    public void setAmount(Integer Amount) { this.Amount = Amount; }

    public void setDetailsName(ArrayList<String> details_name) { this.details_name = details_name; }

    public void setDetailsValue(ArrayList<String> details_value) { this.details_value = details_value; }

    public void setItemAvailableAmount(Integer ItemAvailableAmount) { this.ItemAvailableAmount = ItemAvailableAmount; }

    public void setPartner_ID(Integer PartnerID) {
        this.PartnerID = PartnerID;
    }

    public void setPartnerName(String PartnerName) {
        this.PartnerName = PartnerName;
    }


}
