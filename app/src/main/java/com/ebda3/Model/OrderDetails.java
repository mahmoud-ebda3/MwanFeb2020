package com.ebda3.Model;

/**
 * Created by work on 17/10/2017.
 */

public class OrderDetails {
    public String OrderKind;
    public String OrderCount;
    public String OrderCost;

    public OrderDetails() {
        OrderKind = "";
        OrderCount ="";
        OrderCost = "";
    }

    public OrderDetails(String orderKind, String orderCount, String orderCost) {
        OrderKind = orderKind;
        OrderCount = orderCount;
        OrderCost = orderCost;
    }

    public String getOrderKind() {
        return OrderKind;
    }

    public void setOrderKind(String orderKind) {
        OrderKind = orderKind;
    }

    public String getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(String orderCount) {
        OrderCount = orderCount;
    }

    public String getOrderCost() {
        return OrderCost;
    }

    public void setOrderCost(String orderCost) {
        OrderCost = orderCost;
    }
}
