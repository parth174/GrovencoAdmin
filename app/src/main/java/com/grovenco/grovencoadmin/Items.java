package com.grovenco.grovencoadmin;

import android.net.Uri;

public class Items {

    private String name ;
    private String description ;
    private String itemcode ;
    private String parent_code ;
    private String category ;
    private String subcategory ;
    private long weight ;
    private String weight_unit ;
    private long mrp ;
    private long grovenco_price ;
    private Boolean grovenco_exclusive ;
    private long grovenco_exclusive_price ;
    private long grovenco_exclusive_qty ;
    private long member_price ;
    private long stock ;
    private long limit ;
    private long purchasing_price ;
    private Uri image_front ;
    private Uri image_back ;
    private Boolean is_extra ;
    private long is_trending ;

    public Items(String name, String itemcode, String category, String subcategory, Boolean grovenco_exclusive, long stock, Boolean is_extra, long is_trending) {
        this.name = name;
        this.itemcode = itemcode;
        this.category = category;
        this.subcategory = subcategory;
        this.grovenco_exclusive = grovenco_exclusive;
        this.stock = stock;
        this.is_extra = is_extra;
        this.is_trending = is_trending;
    }

    public Items(){

    }

    public long getIsTrending() {
        return is_trending;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getItemcode() {
        return itemcode;
    }

    public String getParentcode() {
        return parent_code;
    }

    public String getDescription() {
        return description;
    }

    public Uri getImage_front() {
        return image_front;
    }

    public Uri getImage_back() { return image_back; }


    public long getWeight() {
        return weight;
    }

    public String getWeightUnit() {
        return weight_unit;
    }

    public long getMrp() {
        return mrp;
    }

    public long getGrovencoPrice() {
        return grovenco_price;
    }

    public Boolean getGrovencoExclusive() { return grovenco_exclusive; }

    public long getGrovencoExclusivePrice() {
        return grovenco_exclusive_price;
    }

    public long getGrovencoExclusiveQty() {
        return grovenco_exclusive_qty;
    }

    public long getMemberPrice() {
        return member_price;
    }

    public long getStock() {
        return stock;
    }

    public long getLimit() {
        return limit;
    }

    public long getPurchasingPrice() {
        return purchasing_price;
    }

    public Boolean getIsExtra() { return is_extra ; }


}
