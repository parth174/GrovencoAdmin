package com.grovenco.grovencoadmin;

public class Address {

    private String address_line_1 ;
    private String address_line_2 ;
    private String address_line_3 ;
    private String auto_location ;
    private String type ;
    private String pincode ;
    private String address_uid ;

    public Address(String address_line_1, String address_line_2, String address_line_3, String auto_location, String type, String pincode) {
        this.address_line_1 = address_line_1;
        this.address_line_2 = address_line_2;
        this.address_line_3 = address_line_3;
        this.auto_location = auto_location;
        this.type = type;
        this.pincode = pincode;
    }

    public Address() {
    }

    String getAddress_line_1() {
        return address_line_1;
    }

    String getAddress_line_2() {
        return address_line_2;
    }

    String getAddress_line_3() {
        return address_line_3;
    }

    String getAuto_location() {
        return auto_location;
    }

    public String getType() {
        return type;
    }

    String getPincode() {
        return pincode;
    }

    String getAddress_uid() {
        return address_uid;
    }
}
