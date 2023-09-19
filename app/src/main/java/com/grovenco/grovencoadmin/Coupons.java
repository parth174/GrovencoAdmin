package com.grovenco.grovencoadmin;

public class Coupons {


    private String code ;
    private String details ;
    private String title ;
    private long max_discount ;
    private long min_order ;
    private long offer ;
    private boolean enabled ;
    private boolean visible ;
    private String type ;
    private double discount ;
    private long qty ;
    String coupon_id ;


    public Coupons(String code, String details, String title, long max_discount, long min_order, long offer, String type, boolean enabled, boolean visible, long qty, String coupon_id) {
        this.code = code;
        this.details = details;
        this.title = title;
        this.max_discount = max_discount;
        this.min_order = min_order;
        this.offer = offer;
        this.type = type;
        this.enabled = enabled ;
        this.visible = visible ;
        this.qty = qty ;
        this.coupon_id = coupon_id ;
    }

    public Coupons(String code, String details, String title, long min_order, String type, boolean enabled, boolean visible, long qty, String coupon_id) {
        this.code = code;
        this.details = details;
        this.title = title;
        this.min_order = min_order;
        this.type = type;
        this.enabled = enabled ;
        this.visible = visible ;
        this.qty = qty ;
        this.coupon_id = coupon_id ;
    }

    public Coupons(String code, String details, String title, long min_order, String type, double discount, boolean enabled, boolean visible, long qty, String coupon_id) {
        this.code = code;
        this.details = details;
        this.title = title;
        this.min_order = min_order;
        this.type = type;
        this.discount = discount;
        this.enabled = enabled ;
        this.visible = visible ;
        this.qty = qty ;
        this.coupon_id = coupon_id ;
    }

    public Coupons(String code, String details, String title, long max_discount, long min_order, long offer, String type, boolean enabled, boolean visible, String coupon_id) {
        this.code = code;
        this.details = details;
        this.title = title;
        this.max_discount = max_discount;
        this.min_order = min_order;
        this.offer = offer;
        this.type = type;
        this.enabled = enabled ;
        this.visible = visible ;
        this.coupon_id = coupon_id ;
    }

    public Coupons(String code, String details, String title, long min_order, String type, boolean enabled, boolean visible,String coupon_id) {
        this.code = code;
        this.details = details;
        this.title = title;
        this.min_order = min_order;
        this.type = type;
        this.enabled = enabled ;
        this.visible = visible ;
        this.coupon_id = coupon_id ;
    }

    public Coupons(String code, String details, String title, long min_order, String type, double discount, boolean enabled, boolean visible, String coupon_id) {
        this.code = code;
        this.details = details;
        this.title = title;
        this.min_order = min_order;
        this.type = type;
        this.discount = discount;
        this.enabled = enabled ;
        this.visible = visible ;
        this.coupon_id = coupon_id ;
    }

    public Coupons() {

    }

    public String getCouponId() {
        return coupon_id;
    }

    public double getDiscount() {
        return discount;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isVisible() {
        return visible;
    }

    public long getQty() {
        return qty;
    }

    public String getType() {
        return type;
    }

    public long getMax_discount() {
        return max_discount;
    }

    public long getMin_order() {
        return min_order;
    }

    public String getCode() {
        return code;
    }

    public String getDetails() {
        return details;
    }

    public String getTitle() {
        return title;
    }

    public long getOffer() {
        return offer;
    }
}
