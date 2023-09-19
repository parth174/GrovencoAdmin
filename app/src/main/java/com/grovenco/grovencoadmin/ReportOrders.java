package com.grovenco.grovencoadmin;

public class ReportOrders {

    private String name ;
    private double cart_value ;
    private String order_date ;
    private String order_time ;
    private double total_savings ;
    private String order_id ;
    private String order_status ;
    private String delivery_date ;
    private long deliveryCharges ;
    private boolean membership ;
    private long exclusiveCount ;
    private String deliveredBy ;


    public ReportOrders(String name, double cart_value, String order_date, String order_time, String order_id, String order_status, String delivery_date, long deliveryCharges, boolean membership, long exclusiveCount, String deliveredBy) {
        this.name = name;
        this.cart_value = cart_value;
        this.order_date = order_date;
        this.order_time = order_time;
        this.order_id = order_id;
        this.order_status = order_status;
        this.delivery_date = delivery_date;
        this.deliveryCharges = deliveryCharges;
        this.membership = membership ;
        this.exclusiveCount = exclusiveCount;
        this.deliveredBy = deliveredBy ;
    }


    public double getCartValue() {
        return cart_value ;
    }

    public String getOrderDate() {
        return order_date ;
    }

    public String getOrderTime() {
        return order_time ;
    }

    public String getOrderId() {
        return order_id ;
    }

    public String getOrderStatus() {
        return order_status ;
    }

    public String getDeliveryDate() { return delivery_date ; }

    public long getDeliveryCharges() {
        return deliveryCharges;
    }

    public String getName() {
        return name;
    }

    public boolean getMembership() {
        return membership;
    }

    public long getExclusiveCount() {
        return exclusiveCount;
    }

    public String getDeliveredBy() {
        return deliveredBy ;
    }


}
