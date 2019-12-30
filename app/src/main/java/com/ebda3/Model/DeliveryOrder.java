package com.ebda3.Model;

/**
 * Created by work on 17/10/2017.
 */

public class DeliveryOrder {
    public String DeliveryAddress;
    public String DeliveryDate;

    public DeliveryOrder() {
        DeliveryAddress = "";
        DeliveryDate = "";
    }

    public DeliveryOrder(String deliveryAddress, String deliveryDate) {
        DeliveryAddress = deliveryAddress;
        DeliveryDate = deliveryDate;
    }

    public String getDeliveryAddress() {
        return DeliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        DeliveryAddress = deliveryAddress;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }
}
