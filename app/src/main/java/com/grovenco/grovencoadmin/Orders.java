package com.grovenco.grovencoadmin;

public class Orders {

    private double cart_value ;
    private String order_date ;
    private String order_time ;
    private String order_id ;
    private String order_status ;
    private String name ;
    private String phone_number ;
    boolean seen_admin ;

    public Orders(double cart_value, String order_date, String order_time, String order_id, String order_status, String name, String phone_number, boolean seen_admin) {
        this.cart_value = cart_value;
        this.order_date = order_date;
        this.order_time = order_time;
        this.order_id = order_id;
        this.order_status = order_status;
        this.name = name;
        this.phone_number = phone_number;
        this.seen_admin = seen_admin ;
    }

    public Orders(){

    }

    public void setSeen_admin(boolean seen_admin) {
        this.seen_admin = seen_admin;
    }

    public boolean isSeen_admin() {
        return seen_admin;
    }

    public double getCart_value() {
        return cart_value;
    }

    public String getOrder_date() {
        return order_date;
    }

    public String getOrder_time() {
        return order_time;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public String getName() {
        return name;
    }

    public String getPhone_number() {
        return phone_number;
    }
}
