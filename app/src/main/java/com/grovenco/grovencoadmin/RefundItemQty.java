package com.grovenco.grovencoadmin;

import java.io.Serializable;

public class RefundItemQty implements Serializable {

    private String itemcode ;
    private long qty ;
    private long item_price ;

    public RefundItemQty(String itemcode, long qty, long item_price) {
        this.itemcode = itemcode;
        this.qty = qty;
        this.item_price = item_price ;
    }

    public long getItem_price() {
        return item_price;
    }

    public String getItemcode() {
        return itemcode;
    }

    public long getQty() {
        return qty;
    }
}
