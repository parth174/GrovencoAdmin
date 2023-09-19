package com.grovenco.grovencoadmin;

import android.net.Uri;

public class OrderItems {

    private String item_name ;
    private long item_weight ;
    private String item_weight_unit ;
    private long item_price ;
    private long item_qty ;
    private long mrp ;
    private Uri image_front ;
    private String item_code ;
    private String item_status ;
    private long refund_qty ;
    private long cancel_qty ;


    public OrderItems(String item_name, long item_weight, String item_weight_unit, long item_qty, long mrp, Uri image_front) {
        this.item_name = item_name;
        this.item_weight = item_weight;
        this.item_weight_unit = item_weight_unit;
        this.item_qty = item_qty;
        this.mrp = mrp ;
        this.image_front = image_front ;
    }

    public OrderItems(String item_name, long item_weight, String item_weight_unit, long item_price, long item_qty, long mrp, Uri image_front, String item_code) {
        this.item_name = item_name;
        this.item_weight = item_weight;
        this.item_weight_unit = item_weight_unit;
        this.item_price = item_price;
        this.item_qty = item_qty;
        this.mrp = mrp ;
        this.image_front = image_front ;
        this.item_code = item_code ;
    }

    public OrderItems(String item_name, long item_weight, String item_weight_unit, long item_price, long item_qty, long mrp, Uri image_front, String item_code, String item_status, long refund_qty, long cancel_qty) {
        this.item_name = item_name;
        this.item_weight = item_weight;
        this.item_weight_unit = item_weight_unit;
        this.item_price = item_price;
        this.item_qty = item_qty;
        this.mrp = mrp ;
        this.image_front = image_front ;
        this.item_code = item_code ;
        this.item_status = item_status ;
        this.refund_qty = refund_qty ;
        this.cancel_qty = cancel_qty ;
    }

    public Uri getImageFront() {
        return image_front;
    }

    public long getMrp() {
        return mrp;
    }

    public String getItemName() {
        return item_name;
    }

    public long getItemWeight() {
        return item_weight;
    }

    public String getItemWeightUnit() {
        return item_weight_unit;
    }

    public long getItemPrice() {
        return item_price;
    }

    public long getItemQty() {
        return item_qty;
    }

    public String getItemCode() {
        return item_code;
    }

    public String getItem_status() {
        return item_status;
    }

    public long getRefund_qty() {
        return refund_qty;
    }

    public long getCancel_qty() {
        return cancel_qty;
    }
}
