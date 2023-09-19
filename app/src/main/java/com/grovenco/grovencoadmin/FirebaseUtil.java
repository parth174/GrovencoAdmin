package com.grovenco.grovencoadmin;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtil {

    static DatabaseReference users_databaseref = FirebaseDatabase.getInstance().getReference("Users");

    static StorageReference offers_storageRef =  FirebaseStorage.getInstance().getReference("sliding_offers_images");
    static DatabaseReference offers_databaseRef = FirebaseDatabase.getInstance().getReference("SlidingOffers");

    static StorageReference subcategories_storageRef = FirebaseStorage.getInstance().getReference("Subcategories") ;
    static DatabaseReference categories_DatabaseRef = FirebaseDatabase.getInstance().getReference("Categories");

    static DatabaseReference trendingItems_databaseRef = FirebaseDatabase.getInstance().getReference("TrendingItems");
    static StorageReference trendingItems_storageRef = FirebaseStorage.getInstance().getReference("Items_images") ;




    static CollectionReference users_ref = FirebaseFirestore.getInstance().collection("Users") ;
    static CollectionReference slidingOffers1_ref = FirebaseFirestore.getInstance().collection("SlidingOffers1") ;
    static CollectionReference items_ref =  FirebaseFirestore.getInstance().collection("Items") ;
    static CollectionReference cart_ref = FirebaseFirestore.getInstance().collection("Cart");
    static CollectionReference orders_ref = FirebaseFirestore.getInstance().collection("Orders") ;
    static CollectionReference vouchers_ref = FirebaseFirestore.getInstance().collection("Vouchers") ;
    static CollectionReference coupons_ref = FirebaseFirestore.getInstance().collection("Coupons") ;
    static CollectionReference subcategories_ref = FirebaseFirestore.getInstance().collection("Subcategories") ;

    public static DatabaseReference getCurrentUserRef(String phoneNumber){
        return users_databaseref.child(phoneNumber) ;
    }


    public static StorageReference getSubCatImageRef(String category_name ){
        return subcategories_storageRef.child(category_name) ;
    }


    public static DocumentReference getUserRef(String current_uid){
        return users_ref.document(current_uid) ;
    }

    public static CollectionReference getUserVouchersRef(String current_uid){
        return users_ref.document(current_uid).collection("Vouchers") ;
    }

    public static CollectionReference getCartDeliveryAddressRef(String current_uid){
        return cart_ref.document(current_uid).collection("DeliveryAddress") ;
    }

    public static CollectionReference getCartDetailsRef(String current_uid){
        return cart_ref.document(current_uid).collection("CartDetails") ;
    }

    public static CollectionReference getUserAddressRef(String current_uid){
        return users_ref.document(current_uid).collection("Addresses") ;
    }

    public static DocumentReference getCartRef(String phone_number){
        return cart_ref.document(phone_number) ;
    }

    public static DocumentReference getOrdersRef(String order_id){
        return orders_ref.document(order_id) ;
    }

    public static CollectionReference getOrdersAddressRef(String order_id){
        return orders_ref.document(order_id).collection("DeliveryAddress") ;
    }

    public static CollectionReference getOrdersItemsRef(String order_id){
        return orders_ref.document(order_id).collection("Items") ;
    }


    public static Boolean validateNode(FirebaseAuth auth){

        Boolean result = false ;

        if(auth!=null){
            result = true ;
        }

        return result ;
    }


}
