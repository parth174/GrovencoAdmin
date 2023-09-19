package com.grovenco.grovencoadmin;

import android.net.Uri;

public class SearchOrderItem {

    private String name ;
    private String phoneNumber ;
    private String email ;
    private String orderId ;
    private double orderAmount ;

    public SearchOrderItem(String name, String phoneNumber, String email, String orderId, double orderAmount) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.orderId = orderId;
        this.orderAmount = orderAmount;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getOrderAmount() {
        return orderAmount;
    }
}
