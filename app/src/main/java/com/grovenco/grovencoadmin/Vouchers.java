package com.grovenco.grovencoadmin;

public class Vouchers {


    private String code ;
    private String details ;
    private String title ;
    private long max_discount ;
    private long min_order ;
    private long voucher_min_order ;
    private long offer ;
    private boolean enabled ;
    private String type ;
    private double discount ;
    private String voucher_id ;


    public Vouchers(String code, String details, String title, long max_discount, long min_order, long voucher_min_order, long offer, String type, boolean enabled, String voucher_id) {
        this.code = code;
        this.details = details;
        this.title = title;
        this.max_discount = max_discount;
        this.min_order = min_order ;
        this.voucher_min_order = voucher_min_order;
        this.offer = offer;
        this.type = type;
        this.enabled = enabled ;
        this.voucher_id = voucher_id ;

    }

    public Vouchers(String code, String details, String title, long min_order, long voucher_min_order, String type, boolean enabled, String voucher_id) {
        this.code = code;
        this.details = details;
        this.title = title;
        this.min_order = min_order ;
        this.voucher_min_order = voucher_min_order;
        this.type = type;
        this.enabled = enabled ;
        this.voucher_id = voucher_id ;

    }

    public Vouchers(String code, String details, String title, long min_order, long voucher_min_order, String type, double discount, boolean enabled, String voucher_id) {
        this.code = code;
        this.details = details;
        this.title = title;
        this.min_order = min_order ;
        this.voucher_min_order = voucher_min_order;
        this.type = type;
        this.discount = discount;
        this.enabled = enabled ;
        this.voucher_id = voucher_id ;

    }

    public Vouchers() {

    }

    public double getDiscount() {
        return discount;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getType() {
        return type;
    }

    public long getMaxDiscount() {
        return max_discount;
    }

    public long getMinOrder() {
        return min_order;
    }

    public long getVoucherMinOrder() {
        return voucher_min_order;
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

    public String getVoucherId() {
        return voucher_id;
    }

}
