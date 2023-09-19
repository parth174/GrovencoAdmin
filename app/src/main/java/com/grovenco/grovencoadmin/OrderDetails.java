package com.grovenco.grovencoadmin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OrderDetails extends AppCompatActivity {

    String orderId ;
    TextView    tv_grovencoPrice, tv_deliveredBy, tv_orderId, tv_date, tv_time,
                tv_amount, tv_refund_cancelled,
                tv_delivery,
                tv_placed, tv_packed, tv_on_way, tv_delivered,
                tv_addressLabel, tv_addressLine1, tv_addressLine2, tv_addressLine3,
                tv_mrp, tv_grovencoDiscount, tv_memberDiscount, tv_couponDiscount, tv_totalSavings, tv_deliveryCharges, tv_totalAmount, tv_name, tv_call,tv_phoneNumber, tv_grovencoCredits, tv_viewBill  ;

    ImageView img_placed, img_packed, img_on_way, img_delivered ;

    RelativeLayout rl_couponDiscount, rl_parent, rl_grovencoCredits ;
    LinearLayout ll_orderStatus, ll_placed, ll_packed, ll_on_way, ll_delivered, ll_action, ll_actionPlaced, ll_deliveredBy ;
    ArrayList<OrderItems> orderItemsList ;
    RecyclerView rv_orderItems ;
    OrdersItemsAdapter ordersItemsAdapter ;
    ProgressBar progress ;
    RecyclerView.LayoutManager layoutManagerOrderItems ;
    Button btn_accept, btn_decline, btn_declineComplete, btn_declinePlaced ;
    String phoneNumber ;
    String current_uid ;
    double refund_amt, coupon_discount ;
    private File pdfFile;
    double mTotalAmount, mCouponDiscount, mGrovencoCredits ;
    long mDeliveryCharges ;
    Address delivery_address ;
    String mCustomerName ;
    String mCustomerNumber ;
    String mCustomerEmail ;
    String coupon_type ;
    double cashback ;
    int check_membership = 0 ;
    CardView cv_address, cv_vouchersAdded, cv_cashbackAdded ;
    double latitude = 0, longitude = 0 ;
    ListenerRegistration orderDetailsListener ;
    String membership_name ;
    long membership_price = -1 ;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;


    TextView tv_couponTitle, tv_coupon_details, tv_maxDiscount, tv_cashbackAmount ;


    @Override
    protected void onDestroy() {
        super.onDestroy();

        orderDetailsListener.remove() ;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        sharedPreferences = getSharedPreferences("Admin_Pref", 0) ;
        editor = sharedPreferences.edit() ;
        editor.apply();

        tv_orderId = findViewById(R.id.tv_order_id) ;
        tv_date = findViewById(R.id.tv_date) ;
        tv_time = findViewById(R.id.tv_time) ;
        tv_amount = findViewById(R.id.tv_amount) ;
        tv_delivery = findViewById(R.id.tv_delivery) ;
        tv_placed = findViewById(R.id.tv_placed) ;
        tv_packed = findViewById(R.id.tv_packed) ;
        tv_on_way = findViewById(R.id.tv_on_way) ;
        tv_delivered = findViewById(R.id.tv_delivered) ;
        tv_addressLabel = findViewById(R.id.tv_address_label) ;
        tv_addressLine1 = findViewById(R.id.tv_address_line_1) ;
        tv_addressLine2 = findViewById(R.id.tv_address_line_2) ;
        tv_addressLine3 = findViewById(R.id.tv_address_line_3) ;
        tv_mrp = findViewById(R.id.tv_mrp) ;
        tv_grovencoDiscount = findViewById(R.id.tv_grovenco_discount) ;
        tv_memberDiscount = findViewById(R.id.tv_member_discount) ;
        tv_couponDiscount = findViewById(R.id.tv_coupon_discount) ;
        tv_totalSavings = findViewById(R.id.tv_total_savings) ;
        tv_deliveryCharges = findViewById(R.id.tv_delivery_charges) ;
        tv_totalAmount = findViewById(R.id.tv_total_amount) ;
        img_placed = findViewById(R.id.img_placed) ;
        img_packed = findViewById(R.id.img_packed) ;
        img_on_way = findViewById(R.id.img_on_way) ;
        img_delivered = findViewById(R.id.img_delivered) ;
        rl_couponDiscount = findViewById(R.id.rl_coupon_discount) ;
        rv_orderItems = findViewById(R.id.rv_items) ;
        ll_orderStatus = findViewById(R.id.ll_order_status) ;
        tv_refund_cancelled = findViewById(R.id.tv_refund_cancelled) ;
        ll_placed = findViewById(R.id.ll_placed) ;
        ll_packed = findViewById(R.id.ll_packed) ;
        ll_on_way = findViewById(R.id.ll_on_way) ;
        ll_delivered = findViewById(R.id.ll_delivered) ;
        btn_accept = findViewById(R.id.btn_accept) ;
        btn_decline = findViewById(R.id.btn_decline) ;
        ll_action = findViewById(R.id.ll_action) ;
        tv_name = findViewById(R.id.tv_name) ;
        tv_call = findViewById(R.id.tv_call) ;
        tv_phoneNumber = findViewById(R.id.tv_phone_number) ;
        progress = findViewById(R.id.progress) ;
        rl_parent = findViewById(R.id.parent) ;
        rl_grovencoCredits = findViewById(R.id.rl_grovenco_credits) ;
        tv_grovencoCredits = findViewById(R.id.tv_grovenco_credits) ;
        tv_viewBill = findViewById(R.id.tv_view_bill) ;
        btn_declineComplete = findViewById(R.id.btn_decline_complete) ;
        btn_declinePlaced = findViewById(R.id.btn_decline_placed) ;
        ll_actionPlaced = findViewById(R.id.ll_action_placed) ;
        cv_address = findViewById(R.id.cv_address) ;
        tv_coupon_details = findViewById(R.id.tv_coupon_details) ;
        tv_couponTitle = findViewById(R.id.tv_coupon_title) ;
        tv_maxDiscount = findViewById(R.id.tv_max_discount) ;
        cv_vouchersAdded = findViewById(R.id.cv_vouchers_added) ;
        tv_cashbackAmount = findViewById(R.id.tv_cashback_amount) ;
        cv_cashbackAdded = findViewById(R.id.cv_cashback_added) ;
        tv_deliveredBy = findViewById(R.id.tv_delivered_by) ;
        ll_deliveredBy = findViewById(R.id.ll_delivered_by) ;
        tv_grovencoPrice = findViewById(R.id.tv_grovenco_price) ;

        Utils.setVisibilityGone(rl_parent);
        Utils.setVisibilityVisible(progress);

        layoutManagerOrderItems = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManagerOrderItems).setOrientation(RecyclerView.VERTICAL);

        rv_orderItems.setLayoutManager(layoutManagerOrderItems) ;

        orderItemsList = new ArrayList<>() ;

        if(getIntent().getExtras() != null){

            orderId = getIntent().getStringExtra("order_id") ;
            tv_orderId.setText(orderId);

        }

        cv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(latitude != 0 && longitude != 0){

                    Uri gmmIntentUri = Uri.parse("geo:0,0?q="+latitude+","+longitude+"(Google+Sydney)");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");

                    if (mapIntent.resolveActivity(getPackageManager()) != null) {

                        startActivity(mapIntent);

                    }else{

                        Toast.makeText(OrderDetails.this, "Please install Google Maps", Toast.LENGTH_SHORT).show();

                    }

                }else{

                    Toast.makeText(OrderDetails.this, "Location of the customer was not saved, Pease follow the address manually", Toast.LENGTH_LONG).show();

                }

            }
        });


        ll_packed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Utils.setVisibilityGone(ll_actionPlaced);
                FirebaseUtil.getOrdersRef(orderId).update("order_status", "packed", "seen_admin", false).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(OrderDetails.this, "Order packed", Toast.LENGTH_SHORT).show();

                    }
                }) ;

                Utils.setDisabled(ll_packed);

            }
        });

        tv_viewBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Utils.getInternetConnectivity(OrderDetails.this)){

                    try {
                        checkPermissions(mTotalAmount, mGrovencoCredits, mCouponDiscount, mDeliveryCharges);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }

                }else{

                    Toast.makeText(OrderDetails.this, "Please connect to internet", Toast.LENGTH_SHORT).show();

                }

            }
        });


        ll_on_way.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(OrderDetails.this) ;

                View v = LayoutInflater.from(OrderDetails.this).inflate(R.layout.dialog_on_way, null, false) ;

                final EditText et_start = v.findViewById(R.id.et_start_reading) ;

                dialog.setTitle("Start Reading") ;
                dialog.setView(v) ;
                dialog.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(Utils.getInternetConnectivity(OrderDetails.this)){

                            if(!TextUtils.isEmpty(et_start.getText())){

                                //Utils.setVisibilityGone(ll_actionPlaced);

                                FirebaseUtil.getOrdersRef(orderId).update("order_status", "on_way", "seen_admin",false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(OrderDetails.this, "Order on way", Toast.LENGTH_SHORT).show();
                                        float start_reading = Float.parseFloat(et_start.getText().toString()) ;

                                        String key = "start_" + orderId ;

                                        editor.putFloat(key , start_reading) ;
                                        editor.commit() ;

                                        Log.d("SharedPrefOnWay", sharedPreferences.getAll().toString()) ;


                                    }
                                }) ;

                                Utils.setDisabled(ll_on_way);

                            }else{

                                Toast.makeText(OrderDetails.this, "Please enter the start distance reading of your vehicle", Toast.LENGTH_SHORT).show();

                            }

                        }else{

                            Toast.makeText(OrderDetails.this, "Please connect to internet", Toast.LENGTH_SHORT).show();

                        }

                    }
                }) ;

                dialog.setNegativeButton("CANCEl", null) ;
                dialog.show() ;



            }
        });


        ll_delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sharedPreferences.getFloat("start_"+orderId, 0) != 0 ){

                    AlertDialog.Builder dialog = new AlertDialog.Builder(OrderDetails.this) ;

                    View v = LayoutInflater.from(OrderDetails.this).inflate(R.layout.dialog_delivered_by, null, false) ;

                    final Spinner spn_deliveredBy = v.findViewById(R.id.spn_delivered_by) ;
                    final EditText et_endReading = v.findViewById(R.id.et_end_reading) ;

                    ArrayList<String> namesList = new ArrayList<>() ;

                    namesList.add("HARSH") ;
                    namesList.add("RUDRAKSH") ;
                    namesList.add("PARTH") ;

                    ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(OrderDetails.this, android.R.layout.simple_spinner_item, namesList);

                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spn_deliveredBy.setAdapter(categoryAdapter);

                    dialog.setView(v) ;

                    dialog.setTitle("Deliver Order") ;

                    dialog.setNegativeButton("Cancel", null) ;

                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(!TextUtils.isEmpty(et_endReading.getText())){

                                String delivered_by = spn_deliveredBy.getSelectedItem().toString() ;

                                float start_reading = sharedPreferences.getFloat("start_"+ orderId, 0) ;
                                float end_reading = Float.parseFloat(et_endReading.getText().toString()) ;
                                final float distance = end_reading - start_reading ;

                                Log.d("SharedPrefClick", sharedPreferences.getAll().toString()) ;

                                //Utils.setVisibilityGone(ll_actionPlaced);

                                Calendar calendar = Calendar.getInstance() ;

                                final int current_date = calendar.get(Calendar.DATE) ;
                                final int current_month = calendar.get(Calendar.MONTH) ;
                                final int current_year = calendar.get(Calendar.YEAR) ;

                                final int current_hour = calendar.get(Calendar.HOUR) ;
                                final int current_mins = calendar.get(Calendar.MINUTE) ;

                                String am_pm = "" ;

                                if(calendar.get(Calendar.AM_PM)== Calendar.AM){

                                    am_pm = "am" ;

                                }else if(calendar.get(Calendar.AM_PM)== Calendar.PM){

                                    am_pm = "pm" ;

                                }

                                final String current_date_formatted = getDate(current_date)+" "+getMonth(current_month)+" "+current_year ;
                                final String current_time = getHours(current_hour) + ":" + getMinutes(current_mins) + am_pm ;

                                if (check_membership == 1) {

                                    final Map<String, Object> hashmap = new HashMap<>();
                                    hashmap.put("membership_year", Calendar.getInstance().get(Calendar.YEAR));
                                    hashmap.put("membership_date_year", Calendar.getInstance().get(Calendar.DAY_OF_YEAR));

                                    String voucher_code = "MEMBERDEL" ;

                                    Calendar c = Calendar.getInstance() ;
                                    c.set(Calendar.DAY_OF_YEAR, Calendar.getInstance().get(Calendar.DAY_OF_YEAR) +30 );

                                    Map<String,Object> hashmap2 = new HashMap<>() ;
                                    hashmap2.put("type", "3") ;
                                    hashmap2.put("min_order", 0) ;
                                    hashmap2.put("code", voucher_code) ;
                                    hashmap2.put("title", "FREE DELIVERY") ;
                                    hashmap2.put("details", "Get your order at no delivery charges") ;
                                    hashmap2.put("qty", 1) ;
                                    hashmap2.put("expiry", Calendar.getInstance().get(Calendar.DAY_OF_YEAR) +30) ;
                                    hashmap2.put("expiry_date", c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) +1) + "/" + c.get(Calendar.YEAR)) ;

                                    FirebaseUtil.getUserVouchersRef(current_uid).document(voucher_code).set(hashmap2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            Log.d("Voucher added", "Added") ;

                                        }
                                    }) ;

                                    Map<String,Object> hashmap_voucher1 = new HashMap<>() ;
                                    hashmap_voucher1.put("voucher_type", "3") ;
                                    hashmap_voucher1.put("voucher_details", "Get your order at no delivery charges") ;
                                    hashmap_voucher1.put("voucher_title", "FREE DELIVERY") ;
                                    hashmap_voucher1.put("voucher_qty", 1) ;

                                    FirebaseUtil.getOrdersRef(orderId).set(hashmap_voucher1, SetOptions.merge()) ;


                                    FirebaseUtil.getUserRef(current_uid).set(hashmap, SetOptions.merge());

                                }


                                FirebaseUtil.vouchers_ref.whereEqualTo("enabled", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        if(task.isSuccessful()){

                                            if(task.getResult() != null){

                                                if(!task.getResult().isEmpty()){

                                                    for(DocumentSnapshot document : task.getResult().getDocuments()){

                                                        long min_order = (long) document.get("min_order") ;
                                                        long qty = (long) document.get("qty");
                                                        String title = document.get("title").toString() ;
                                                        String details= document.get("details").toString() ;
                                                        String type = document.get("type").toString() ;
                                                        long voucher_min_order = (long) document.get("voucher_min_order") ;

                                                        if(mTotalAmount >= min_order){

                                                            switch (document.getId()){

                                                                case "FREEDELIVERY" :

                                                                    long id = UUID.randomUUID().getLeastSignificantBits() ;

                                                                    if(id<0){

                                                                        id = -(id) ;

                                                                    }

                                                                    String voucher_code = "GRV" + String.valueOf(id).substring(0,5) ;

                                                                    Map<String,Object> hashmap = new HashMap<>() ;
                                                                    hashmap.put("type", type) ;
                                                                    hashmap.put("min_order", voucher_min_order) ;
                                                                    hashmap.put("code", voucher_code) ;
                                                                    hashmap.put("title", title) ;
                                                                    hashmap.put("details", details) ;
                                                                    hashmap.put("qty", qty) ;



                                                                    FirebaseUtil.getUserVouchersRef(current_uid).document(voucher_code).set(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            Log.d("Vouchers added", "Added") ;

                                                                        }
                                                                    }) ;

                                                                    Map<String,Object> hashmap_voucher1 = new HashMap<>() ;
                                                                    hashmap_voucher1.put("voucher_type", type) ;
                                                                    hashmap_voucher1.put("voucher_details", details) ;
                                                                    hashmap_voucher1.put("voucher_title", title) ;
                                                                    hashmap_voucher1.put("voucher_qty", qty) ;

                                                                    FirebaseUtil.getOrdersRef(orderId).set(hashmap_voucher1, SetOptions.merge()) ;


                                                                    break ;

                                                                case "FREEVOUCHERS" :

                                                                    long voucher_amount = (long) document.get("voucher_amount") ;

                                                                    double v_amount = (double) voucher_amount ;

                                                                    long id_coupon = UUID.randomUUID().getLeastSignificantBits() ;

                                                                    if(id_coupon<0){

                                                                        id_coupon = -(id_coupon) ;

                                                                    }

                                                                    String voucher_code1 = "GRV" + String.valueOf(id_coupon).substring(0,5) ;

                                                                    Map<String,Object> hashmap1 = new HashMap<>() ;
                                                                    hashmap1.put("type", type) ;
                                                                    hashmap1.put("min_order", voucher_min_order) ;
                                                                    hashmap1.put("code", voucher_code1 ) ;
                                                                    hashmap1.put("title", title) ;
                                                                    hashmap1.put("details", details) ;
                                                                    hashmap1.put("discount", v_amount) ;
                                                                    hashmap1.put("qty", qty) ;

                                                                    FirebaseUtil.getUserVouchersRef(current_uid).document(voucher_code1).set(hashmap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            Log.d("Vouchers added", "Added") ;

                                                                        }
                                                                    }) ;

                                                                    Map<String,Object> hashmap_voucher2 = new HashMap<>() ;
                                                                    hashmap_voucher2.put("voucher_type", type) ;
                                                                    hashmap_voucher2.put("voucher_details", details) ;
                                                                    hashmap_voucher2.put("voucher_title", title) ;
                                                                    hashmap_voucher2.put("voucher_qty", qty) ;

                                                                    FirebaseUtil.getOrdersRef(orderId).set(hashmap_voucher2, SetOptions.merge()) ;

                                                                    break ;

                                                                case "DISCOUNTVOUCHERS" :

                                                                    long offer = document.getLong("offer") ;
                                                                    long max_discount = document.getLong("max_discount") ;

                                                                    long coupon_id = UUID.randomUUID().getLeastSignificantBits() ;

                                                                    if(coupon_id<0){

                                                                        coupon_id = -(coupon_id) ;

                                                                    }

                                                                    String voucher_code2 = "GRV" + String.valueOf(coupon_id).substring(0,5);

                                                                    Map<String,Object> hashmap2 = new HashMap<>() ;
                                                                    hashmap2.put("type", type) ;
                                                                    hashmap2.put("min_order", voucher_min_order) ;
                                                                    hashmap2.put("code", voucher_code2) ;
                                                                    hashmap2.put("title", title) ;
                                                                    hashmap2.put("offer", offer ) ;
                                                                    hashmap2.put("max_discount", max_discount) ;
                                                                    hashmap2.put("details", details) ;
                                                                    hashmap2.put("qty", qty) ;

                                                                    FirebaseUtil.getUserVouchersRef(current_uid).document(voucher_code2).set(hashmap2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            Log.d("Vouchers added", "Added") ;

                                                                        }
                                                                    }) ;

                                                                    Map<String,Object> hashmap_voucher3 = new HashMap<>() ;
                                                                    hashmap_voucher3.put("voucher_type", type) ;
                                                                    hashmap_voucher3.put("voucher_details", details) ;
                                                                    hashmap_voucher3.put("voucher_title", title) ;
                                                                    hashmap_voucher3.put("voucher_qty", qty) ;

                                                                    FirebaseUtil.getOrdersRef(orderId).set(hashmap_voucher3, SetOptions.merge()) ;

                                                                    break ;

                                                            }

                                                        }

                                                    }

                                                }

                                            }

                                        }

                                    }
                                }) ;



                                if(coupon_type != null && coupon_type.equals("5")){

                                    FirebaseUtil.getOrdersRef(orderId).update("order_status", "delivered","order_status_group","delivered", "delivery_date", current_date_formatted, "delivery_time", current_time, "seen_admin", false, "cashback_unavailed", FieldValue.delete(), "cashback_availed", cashback, "delivered_by", delivered_by, "distance", distance).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            Toast.makeText(OrderDetails.this, "Order delivered, Distance : " + distance, Toast.LENGTH_SHORT).show();
                                            editor.remove("start_" + orderId) ;
                                            editor.commit() ;
                                            Log.d("SharedPrefDelivered", sharedPreferences.getAll().toString()) ;

                                        }
                                    }) ;

                                    FirebaseUtil.getUserRef(current_uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                            if(task.isSuccessful()){

                                                if(task.getResult() != null){

                                                    if(task.getResult().exists()){

                                                        double grovenco_credits = task.getResult().getDouble("grovenco_wallet") ;

                                                        FirebaseUtil.getUserRef(current_uid).update("grovenco_wallet", grovenco_credits + cashback).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if(task.isSuccessful()){

                                                                    Log.d("Cashback", cashback + " added to user wallet" ) ;

                                                                }

                                                            }
                                                        }) ;

                                                    }

                                                }

                                            }

                                        }
                                    }) ;


                                }else{

                                    FirebaseUtil.getOrdersRef(orderId).update("order_status", "delivered","order_status_group","delivered", "delivery_date", current_date_formatted, "delivery_time", current_time, "seen_admin", false, "delivered_by", delivered_by, "distance", distance).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            Toast.makeText(OrderDetails.this, "Order delivered, Distance : " + distance, Toast.LENGTH_SHORT).show();
                                            editor.remove("start_" + orderId) ;
                                            editor.commit() ;
                                            Log.d("SharedPrefDelivered", sharedPreferences.getAll().toString()) ;

                                        }
                                    }) ;

                                }


                                Utils.setDisabled(ll_delivered) ;

                            }else{

                                Toast.makeText(OrderDetails.this, "Please enter end reading distance of your vehicle", Toast.LENGTH_SHORT).show();

                            }



                        }
                    }) ;

                    dialog.show() ;

                }else{

                    Toast.makeText(OrderDetails.this, "Please start your order delivery by clicking OnWay option first", Toast.LENGTH_LONG).show();

                }

            }
        });

        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.setVisibilityGone(ll_action);

                FirebaseUtil.getOrdersRef(orderId).update("order_status", "refund_declined", "seen_admin",false ).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(OrderDetails.this, "Order status changed", Toast.LENGTH_SHORT).show();

                    }
                }) ;


                FirebaseUtil.getOrdersItemsRef(orderId).whereEqualTo("item_status", "processing_refund").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.getResult() != null){

                            if(!task.getResult().isEmpty()){

                                for(DocumentSnapshot document : task.getResult().getDocuments()){

                                    String itemcode = document.get("item_code").toString() ;

                                    FirebaseUtil.getOrdersItemsRef(orderId).document(itemcode).update("item_status", "refund_declined") ;

                                }

                            }

                        }

                    }
                });

            }
        });


        tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });

        FirebaseUtil.getOrdersAddressRef(orderId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.getResult() != null){

                    for(DocumentSnapshot document : task.getResult().getDocuments()){

                        if(document.contains("latitude") && document.contains("longitude")){

                            latitude = document.getDouble("latitude") ;
                            longitude = document.getDouble("longitude") ;

                        }

                        String address_type = document.get("type").toString();
                        String address_line1 = document.get("address_line_1").toString();
                        String address_line2 = document.get("address_line_2").toString();
                        String address_line3 = document.get("address_line_3").toString();
                        String auto_location = document.get("auto_location").toString();
                        String pincode = document.get("pincode").toString();

                        delivery_address = new Address(address_line1,address_line2,address_line3,auto_location,address_type,pincode) ;

                        updateAddressUI(address_type, address_line1, address_line2, address_line3, auto_location, pincode) ;

                    }

                }

            }
        }) ;

        orderDetailsListener =

        FirebaseUtil.getOrdersRef(orderId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException e) {

                if(document != null){

                    if(document.exists()){

                        Utils.setVisibilityGone(rl_parent);
                        Utils.setVisibilityVisible(progress);

                        orderItemsList.clear();

                        final String name =  document.get("name").toString() ;
                        final String phone_number =  document.get("phone_number").toString() ;
                        final String email =  document.get("email").toString() ;

                        mCustomerName = name ;
                        mCustomerNumber = phone_number ;
                        mCustomerEmail = email ;

                        final double total_amount =  document.getDouble("cart_value") ;
                        final String order_date = document.get("order_date").toString() ;
                        final String order_time = document.get("order_time").toString() ;
                        final String delivery_date = document.get("delivery_date").toString() ;
                        final String order_status = document.get("order_status").toString() ;
                        final long order_mrp = (long) document.get("mrp") ;
                        final long total_grovenco_discount = (long) document.get("grovenco_discount") ;
                        final long order_member_discount = (long) document.get("member_discount") ;
                        final double total_savings =  document.getDouble("total_savings") ;
                        final long deliveryCharges = (long) document.get("delivery_charges") ;
                        final double couponDiscount =  document.getDouble("coupon_discount") ;
                        final long delivery_slot = (long) document.get("delivery_slot") ;
                        final String uid = document.get("uid").toString() ;
                        final double grovenco_credits =  document.getDouble("grovenco_credits") ;
                        String refund_date = "" ;

                        String voucher_title = "" ;
                        String voucher_details = "" ;
                        String voucher_type = "" ;
                        long voucher_qty = 1 ;

                        String deliveredBy = "" ;

                        if(document.contains("delivered_by") ){

                            Utils.setVisibilityVisible(ll_deliveredBy);
                            deliveredBy = document.get("delivered_by").toString() ;
                            tv_deliveredBy.setText(deliveredBy);

                        }else{

                            Utils.setVisibilityGone(ll_deliveredBy);

                        }


                        if(document.contains("voucher_title")){

                            voucher_title = document.get("voucher_title").toString() ;

                        }

                        if(document.contains("voucher_details")){

                            voucher_details = document.get("voucher_details").toString() ;

                        }

                        if(document.contains("voucher_qty")){

                            voucher_qty = document.getLong("voucher_qty") ;

                        }

                        if(document.contains("voucher_type")){

                            voucher_type = document.get("voucher_type").toString() ;

                        }


                        if(document.contains("refund_date")){

                            refund_date = document.get("refund_date").toString() ;

                        }

                        if(document.contains("coupon_type")){

                            coupon_type = document.get("coupon_type").toString() ;

                        }


                        if(document.contains("cashback_availed")){

                            Utils.setVisibilityVisible(cv_cashbackAdded);
                            double cashback_availed = document.getDouble("cashback_availed") ;
                            tv_cashbackAmount.setText(Utils.getAmount(cashback_availed));

                        }

                        if(document.contains("cashback_unavailed")){

                            cashback = document.getDouble("cashback_unavailed") ;
                            Utils.setVisibilityVisible(cv_cashbackAdded);
                            double cashback_availed = document.getDouble("cashback_unavailed") ;
                            tv_cashbackAmount.setText("You will get a cashback of " + Utils.getAmount(cashback_availed) + " after your order is delivered.");

                        }


                        current_uid = uid ;
                        coupon_discount = couponDiscount ;

                        ordersItemsAdapter = new OrdersItemsAdapter(OrderDetails.this, orderItemsList,orderId,current_uid) ;
                        rv_orderItems.setAdapter(ordersItemsAdapter);

                        phoneNumber = phone_number ;
                        String delivery_time = "" ;

                        if(document.contains("delivery_time")){

                            delivery_time = document.get("delivery_time").toString() ;

                        }

                        if(order_status.equals("refunded")){

                            Utils.setVisibilityGone(tv_amount);
                            Utils.setVisibilityVisible(tv_refund_cancelled);
                            tv_refund_cancelled.setBackgroundColor(getResources().getColor(R.color.green));
                            tv_refund_cancelled.setText("refunded");

                        }

                        if(order_status.equals("cancelled")){

                            Utils.setVisibilityGone(tv_amount);
                            Utils.setVisibilityVisible(tv_refund_cancelled);
                            tv_refund_cancelled.setBackgroundColor(getResources().getColor(R.color.quantum_googred));
                            tv_refund_cancelled.setText("cancelled");


                        }

                        if(order_status.equals("processing_refund")){

                            Utils.setVisibilityGone(tv_amount);
                            Utils.setVisibilityVisible(ll_action);
                            Utils.setVisibilityVisible(tv_refund_cancelled);
                            tv_refund_cancelled.setBackgroundColor(getResources().getColor(R.color.quantum_googred));
                            tv_refund_cancelled.setText("processing refund");

                        }

                        if(order_status.equals("refund_declined")){

                            Utils.setVisibilityGone(tv_amount) ;
                            Utils.setVisibilityVisible(tv_refund_cancelled) ;
                            tv_refund_cancelled.setBackgroundColor(getResources().getColor(R.color.quantum_googred)) ;
                            tv_refund_cancelled.setText("Refund Declined") ;

                        }

                        if(order_status.equals("cancelled_admin")){

                            Utils.setVisibilityGone(tv_amount);
                            Utils.setVisibilityVisible(tv_refund_cancelled);

                            tv_refund_cancelled.setBackgroundColor(getResources().getColor(R.color.quantum_googred));
                            tv_refund_cancelled.setText("admin cancelled");

                        }

                        final String finalVoucher_title = voucher_title;
                        final String finalVoucher_details = voucher_details;
                        final String finalVoucher_type = voucher_type;
                        final long finalVoucher_qty = voucher_qty;
                        final String finalDelivery_time = delivery_time ;
                        final String finalRefund_date = refund_date ;

                        FirebaseUtil.getOrdersItemsRef(orderId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.getResult() != null){

                                    orderItemsList.clear() ;

                                    final int size = task.getResult().size() ;
                                    final int[] count = {0};
                                    final double[] refund_items_amount = {0} ;

                                    for(DocumentSnapshot document : task.getResult().getDocuments()){

                                        final String item_name = document.get("name").toString() ;
                                        final long item_price = (long) document.get("buying_price") ;
                                        final long item_weight = (long) document.get("weight") ;
                                        final String item_weightUnit = document.get("weight_unit").toString() ;
                                        final long item_qty =(long) document.get("qty") ;
                                        final long mrp = (long) document.get("mrp") ;
                                        final String item_status = document.get("item_status").toString() ;
                                        final String item_code = document.get("item_code").toString() ;
                                        final String image_front = document.get("image_front").toString() ;

                                        Uri uri = Uri.parse(image_front) ;

                                        if(item_code.equals("1")){

                                            check_membership = 1 ;
                                            membership_name = item_name ;
                                            membership_price = item_price ;

                                        }

                                        long refund_qty = 0 ;

                                        if(document.contains("refund_qty")){

                                            refund_qty = (long) document.get("refund_qty") ;

                                        }

                                        long cancel_qty = 0 ;

                                        if(document.contains("cancel_qty")){

                                            cancel_qty = (long) document.get("cancel_qty") ;

                                        }


                                        if(item_status.equals("refunded")){

                                            refund_items_amount[0] = refund_items_amount[0] + (refund_qty * item_price) ;

                                        }

                                        final long finalRefund_qty = refund_qty;

                                        OrderItems orderItems = new OrderItems(item_name,item_weight,item_weightUnit,item_price,item_qty,mrp,uri,item_code,item_status, finalRefund_qty, cancel_qty) ;

                                        orderItemsList.add(orderItems);
                                        count[0] = count[0] +1 ;

                                        if(count[0] == size) {

                                            ordersItemsAdapter.notifyDataSetChanged();

                                            if (refund_items_amount[0] > couponDiscount + 100) {

                                                if(couponDiscount > 0){

                                                    refund_items_amount[0] = refund_items_amount[0] - couponDiscount ;

                                                }

                                            }

                                            refund_amt = refund_items_amount[0] ;

                                            Log.d("RefundAmount", "Amount : " + refund_amt) ;

                                            Utils.setVisibilityVisible(rl_parent);
                                            Utils.setVisibilityGone(progress);

                                        }

                                    }

                                    updateOrderUI(total_amount, order_date, order_time, delivery_date, order_status, order_mrp, total_grovenco_discount, order_member_discount, total_savings, deliveryCharges, couponDiscount, refund_items_amount[0], finalDelivery_time, delivery_slot, name, phone_number,grovenco_credits, finalRefund_date, finalVoucher_title, finalVoucher_details, finalVoucher_type, finalVoucher_qty) ;


                                }


                            }
                        }) ;




                    }

                }

            }
        }) ;


    }



    private void updateOrderUI(final double total_amount, String order_date, String order_time, String delivery_date, String order_status, long order_mrp, long total_grovenco_discount, long order_member_discount, double total_savings, long delivery_charges, final double coupon_discount, double refund_amount, String delivery_time, long delivery_slot, String name, String phone_number, double grovenco_credits, String refund_date, String voucher_title, String voucher_details, String voucher_type, long voucher_qty) {

        tv_amount.setText(Utils.getAmount(total_amount));
        tv_totalAmount.setText(Utils.getAmount(total_amount));
        tv_date.setText(order_date);
        tv_time.setText(order_time);
        tv_mrp.setText(Utils.getAmount(order_mrp));
        tv_grovencoDiscount.setText(Utils.getAmount(total_grovenco_discount));
        tv_grovencoPrice.setText(Utils.getAmount(order_mrp-total_grovenco_discount));
        tv_memberDiscount.setText(Utils.getAmount(order_member_discount));
        tv_totalSavings.setText(Utils.getAmount(total_savings));
        tv_name.setText(name);
        tv_phoneNumber.setText(phone_number);

        mTotalAmount = total_amount ;
        mCouponDiscount = coupon_discount ;
        mGrovencoCredits = grovenco_credits ;
        mDeliveryCharges = delivery_charges ;


        String deliverySlot = "" ;

        if(delivery_slot == 1){

            deliverySlot = "10 am - 12 pm" ;

        }
        if(delivery_slot == 2){

            deliverySlot = "7 pm - 9 pm" ;

        }
        if(delivery_slot == 3){

            deliverySlot = "2 pm - 4 pm" ;

        }

        if(grovenco_credits > 0){

            Utils.setVisibilityVisible(rl_grovencoCredits);
            tv_grovencoCredits.setText(Utils.getAmount(grovenco_credits));

        }else{

            Utils.setVisibilityGone(rl_grovencoCredits);

        }



        if(coupon_discount > 0){

            Utils.setVisibilityVisible(rl_couponDiscount);
            tv_couponDiscount.setText(Utils.getAmount(coupon_discount));

        }else{

            Utils.setVisibilityGone(rl_couponDiscount);

        }

        if(delivery_charges>0){

            tv_deliveryCharges.setText(Utils.getAmount(delivery_charges));

        }else{

            tv_deliveryCharges.setTextColor(getResources().getColor(R.color.green));
            tv_deliveryCharges.setText("FREE");

        }

        if( !voucher_title.equals("") && !voucher_details.equals("")  && !voucher_type.equals("") ){

            Utils.setVisibilityVisible(cv_vouchersAdded);
            tv_couponTitle.setText(voucher_title);
            tv_coupon_details.setText(voucher_details);

            if(voucher_qty>=1){

                Utils.setVisibilityVisible(tv_maxDiscount);
                tv_maxDiscount.setText("X " + voucher_qty) ;

            }else{

                Utils.setVisibilityGone(tv_maxDiscount);

            }


        }else{

            Utils.setVisibilityGone(cv_vouchersAdded);

        }


        switch(order_status){

            case "placed"  :

                Utils.setDisabled(ll_placed);

                Utils.setVisibilityVisible(ll_actionPlaced);

                tv_delivery.setText("Expected Delivery on "+ delivery_date + " between " + deliverySlot);
                img_placed.setBackgroundResource(R.drawable.background_square_green) ;
                img_placed.setImageResource(R.drawable.tick) ;

                tv_placed.setTextColor(getResources().getColor(R.color.black));

                break ;

            case "packed"  :

                Utils.setDisabled(ll_placed);
                Utils.setDisabled(ll_packed);

                Utils.setVisibilityGone(ll_actionPlaced);

                tv_delivery.setText("Expected Delivery on "+ delivery_date + " between " + deliverySlot);

                img_placed.setBackgroundResource(R.drawable.background_square_green) ;
                img_placed.setImageResource(R.drawable.tick) ;

                img_packed.setBackgroundResource(R.drawable.background_square_green) ;
                img_packed.setImageResource(R.drawable.tick) ;

                tv_placed.setTextColor(getResources().getColor(R.color.black));
                tv_packed.setTextColor(getResources().getColor(R.color.black));


                break ;

            case "on_way"  :

                Utils.setDisabled(ll_placed);
                Utils.setDisabled(ll_packed);
                Utils.setDisabled(ll_on_way) ;

                Utils.setVisibilityGone(ll_actionPlaced);

                tv_delivery.setText("Expected Delivery on "+ delivery_date + " between " + deliverySlot);

                img_placed.setBackgroundResource(R.drawable.background_square_green);
                img_placed.setImageResource(R.drawable.tick);
                img_packed.setBackgroundResource(R.drawable.background_square_green) ;
                img_packed.setImageResource(R.drawable.tick) ;
                img_on_way.setBackgroundResource(R.drawable.background_square_green);
                img_on_way.setImageResource(R.drawable.tick);

                tv_placed.setTextColor(getResources().getColor(R.color.black));
                tv_on_way.setTextColor(getResources().getColor(R.color.black));
                tv_packed.setTextColor(getResources().getColor(R.color.black));

                break ;

            case "delivered"  :

                Utils.setDisabled(ll_placed);
                Utils.setDisabled(ll_packed);
                Utils.setDisabled(ll_on_way) ;
                Utils.setDisabled(ll_delivered) ;

                Utils.setVisibilityGone(ll_actionPlaced);

                tv_delivery.setText("Delivered on "+ delivery_date + " " +delivery_time);

                img_placed.setBackgroundResource(R.drawable.background_square_green);
                img_placed.setImageResource(R.drawable.tick);
                img_packed.setBackgroundResource(R.drawable.background_square_green) ;
                img_packed.setImageResource(R.drawable.tick) ;
                img_on_way.setBackgroundResource(R.drawable.background_square_green);
                img_on_way.setImageResource(R.drawable.tick);
                img_delivered.setBackgroundResource(R.drawable.background_square_green);
                img_delivered.setImageResource(R.drawable.tick);

                tv_placed.setTextColor(getResources().getColor(R.color.black));
                tv_on_way.setTextColor(getResources().getColor(R.color.black));
                tv_delivered.setTextColor(getResources().getColor(R.color.black));
                tv_packed.setTextColor(getResources().getColor(R.color.black));

                break ;

            case "processing_refund"  :

                Utils.setVisibilityGone(ll_actionPlaced);

                Utils.setVisibilityGone(tv_amount);
                Utils.setVisibilityVisible(tv_refund_cancelled);
                Utils.setVisibilityGone(ll_orderStatus);
                tv_delivery.setText("The customer has requested refund. Please take pending action") ;

                break ;

            case "refunded"  :

                Utils.setVisibilityGone(ll_actionPlaced);

                Utils.setVisibilityGone(ll_orderStatus);
                tv_delivery.setText("Refunded " + Utils.getAmount(refund_amount) + " On "+ refund_date) ;

                break ;

            case "cancelled"  :

                Utils.setVisibilityGone(ll_actionPlaced);

                Utils.setVisibilityGone(ll_orderStatus);
                tv_delivery.setText("Order Value : " + Utils.getAmount(total_amount)) ;

                break ;

            case "refund_declined" :

                Utils.setVisibilityGone(ll_actionPlaced);

                Utils.setVisibilityGone(ll_orderStatus);
                tv_delivery.setText("Delivered on "+ delivery_date + " " +delivery_time);

                break ;

            case  "cancelled_admin" :

                Utils.setVisibilityGone(cv_cashbackAdded) ;

                Utils.setVisibilityGone(ll_actionPlaced);

                Utils.setVisibilityGone(ll_orderStatus);
                tv_delivery.setText("Order Value : " + Utils.getAmount(total_amount)) ;
                break ;


        }




    }

    private void updateAddressUI(String address_type, String address_line1, String address_line2, String address_line3,String auto_location, String pincode) {

        tv_addressLabel.setText(address_type) ;
        tv_addressLine1.setText(address_line1+ ", "+ address_line2 + ", " + address_line3 ) ;
        tv_addressLine2.setText(auto_location) ;
        tv_addressLine3.setText(pincode) ;

    }

    private String getDate(int date){

        String current_date ;

        switch(date){

            case 1 : current_date = date+"st" ; break ;
            case 2 : current_date = date+"nd" ; break ;
            case 3 : current_date = date+"rd" ; break ;
            case 21 : current_date = date+"st" ; break ;
            case 22 : current_date = date+"nd" ; break ;
            case 23 : current_date = date+"rd" ; break ;
            case 31 : current_date = date+"st" ; break ;
            default: current_date = date+"th" ; break ;
        }

        return current_date ;

    }

    private String getMonth(int current_month){

        String month = "";

        switch (current_month){

            case Calendar.JANUARY : month =  "Jan" ; break ;
            case Calendar.FEBRUARY : month = "Feb" ; break ;
            case Calendar.MARCH : month = "Mar" ; break ;
            case Calendar.APRIL : month = "Apr" ; break ;
            case Calendar.MAY : month = "May" ; break ;
            case Calendar.JUNE : month = "Jun" ; break ;
            case Calendar.JULY : month = "Jul" ; break ;
            case Calendar.AUGUST : month = "Aug" ; break ;
            case Calendar.SEPTEMBER : month = "Sep" ; break ;
            case Calendar.OCTOBER : month = "Oct" ; break ;
            case Calendar.NOVEMBER : month = "Nov" ; break ;
            case Calendar.DECEMBER : month = "Dec" ; break ;

        }
        return month ;
    }

    private String getHours(int current_hour){

        if(current_hour == 0){

            return "12" ;

        }else{

            return String.valueOf(current_hour) ;

        }

    }

    private String getMinutes(int current_minutes){

        String minutes ;

        switch (current_minutes){

            case 0 : minutes =  "01" ; break ;
            case 1 : minutes =  "01" ; break ;
            case 2 : minutes = "02" ; break ;
            case 3: minutes = "03" ; break ;
            case 4 : minutes = "04" ; break ;
            case 5 : minutes = "05" ; break ;
            case 6 : minutes = "06" ; break ;
            case 7 : minutes = "07" ; break ;
            case 8 : minutes = "08" ; break ;
            case 9 : minutes = "09" ; break ;
            default : minutes = String.valueOf(current_minutes) ;

        }
        return minutes ;
    }

    private void createPdf(double total_amount, double grovenco_credits, double coupon_discount, long delivery_charges, String customer_name, String customer_number, String customer_email) throws Exception{

        File docsFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i("New Created", "Created a new directory for PDF");
        }

        String pdfname = "invoice.pdf";

        int columnSize = orderItemsList.size() ;

        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();

        document.setMargins(20,20,60,20) ;
        Rectangle rectangle = new Rectangle(230,1200) ;
        document.setPageSize(rectangle) ;


        PdfPTable table = new PdfPTable(new float[]{columnSize, columnSize, columnSize, columnSize});

        final Font f = new Font(Font.FontFamily.TIMES_ROMAN, 7.0f, Font.BOLD, BaseColor.BLACK);
        final Font k = new Font(Font.FontFamily.TIMES_ROMAN, 6.0f, Font.BOLD, BaseColor.BLACK);
        final Font l = new Font(Font.FontFamily.TIMES_ROMAN, 6.0f, Font.NORMAL, BaseColor.BLACK);
        final Font g = new Font(Font.FontFamily.TIMES_ROMAN, 6.0f, Font.NORMAL, BaseColor.DARK_GRAY);


        final Font o = new Font(Font.FontFamily.TIMES_ROMAN, 8.0f, Font.BOLD, BaseColor.BLACK);


        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(new Paragraph("Name", f));
        table.addCell(new Paragraph("Price", f));
        table.addCell(new Paragraph("Qty", f));
        table.addCell(new Paragraph("Total", f));
        table.setHeaderRows(1);

        table.getRow(0).setWidths(new float[] {1, 0.8f, 0.8f, 0.8f}) ;


        table.setTotalWidth(new float[] {2, 1,1,1});



        for (int i = 0; i < orderItemsList.size(); i++) {

            if(!orderItemsList.get(i).getItemCode().equals("1")){

                String name = orderItemsList.get(i).getItemName() + " " + orderItemsList.get(i).getItemWeight()+ orderItemsList.get(i).getItemWeightUnit();
                String unit_price = String.valueOf(orderItemsList.get(i).getItemPrice()) ;
                String qty = String.valueOf(orderItemsList.get(i).getItemQty()) ;
                String total_price = String.valueOf(orderItemsList.get(i).getItemPrice()*orderItemsList.get(i).getItemQty()) ;

                if(orderItemsList.get(i).getItemQty()>0){

                    table.addCell(new Paragraph(name, g)) ;
                    table.addCell(new Paragraph(unit_price, g)) ;
                    table.addCell(new Paragraph(qty, g)) ;
                    table.addCell(new Paragraph(total_price, g)) ;

                }

            }
        }

        if(membership_name != null && membership_price != -1){

            table.addCell(new Paragraph(membership_name, g)) ;
            table.addCell(new Paragraph(String.valueOf(membership_price), g)) ;
            table.addCell(new Paragraph("1", g)) ;
            table.addCell(new Paragraph(String.valueOf(membership_price), g)) ;

        }

        if(grovenco_credits > 0){

            table.addCell(new Paragraph("Grovenco Credits", g)) ;
            table.addCell("");
            table.addCell("");
            table.addCell(new Paragraph(String.valueOf(Math.round(grovenco_credits)), g));

        }

        if(coupon_discount > 0){

            table.addCell(new Paragraph("Coupon Discount", g)) ;
            table.addCell("");
            table.addCell("");
            table.addCell(new Paragraph(String.valueOf(coupon_discount), g));

        }

        if(delivery_charges > 0){

            table.addCell(new Paragraph("Delivery Charges", g)) ;
            table.addCell("");
            table.addCell("");
            table.addCell(new Paragraph(String.valueOf(delivery_charges), g));

        }else{

            table.addCell(new Paragraph("Delivery Charges", g)) ;
            table.addCell("");
            table.addCell("");
            table.addCell(new Paragraph("FREE", g));

        }


        table.addCell(new Paragraph("TOTAL", f)) ;
        table.addCell("");
        table.addCell("");
        table.addCell(new Paragraph(String.valueOf(Math.round(total_amount)), f));


        PdfWriter.getInstance(document, output);
        document.open();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.grovenco_logo_small);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 , stream);
        Image myImg = Image.getInstance(stream.toByteArray());

        myImg.setAlignment(Image.MIDDLE);

        ByteArrayOutputStream stream_qr = new ByteArrayOutputStream();
        Bitmap bitmap_qr = BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.payment_qr);
        bitmap_qr.compress(Bitmap.CompressFormat.PNG, 100 , stream_qr);
        Image img_qr = Image.getInstance(stream_qr.toByteArray());

        img_qr.setAlignment(Image.MIDDLE);

        document.add(myImg) ;

        Paragraph contact_numbers = new Paragraph("Helpline Numbers :- 9311631869 , 8586920211 " ,o)  ; //k
        contact_numbers.setAlignment(Element.ALIGN_CENTER) ;

        Paragraph contact_address = new Paragraph("Sector-66, Noida" ,o)  ; //k
        contact_address.setAlignment(Element.ALIGN_CENTER) ;

        Paragraph line = new Paragraph("\n---------------------------------------------------------------------------------",k) ;
        line.setAlignment(Element.ALIGN_CENTER) ;




        Paragraph new_line = new Paragraph("\n\n", o) ;
        new_line.setAlignment(Element.ALIGN_CENTER);

        Paragraph p_customer_name = new Paragraph("Customer Name :- "+ customer_name,o) ;
        p_customer_name.setAlignment(Element.ALIGN_LEFT);
        p_customer_name.setIndentationLeft(15);

        Paragraph p_customer_number = new Paragraph("Contact Number :- "+ customer_number,o) ;
        p_customer_number.setAlignment(Element.ALIGN_LEFT);
        p_customer_number.setIndentationLeft(15);

        Paragraph p_customer_email = new Paragraph("Email Id. :- "+ customer_email,o) ;
        p_customer_email.setAlignment(Element.ALIGN_LEFT);
        p_customer_email.setIndentationLeft(15);


        Paragraph address = new Paragraph("ADDRESS :-  \n"+ delivery_address.getAddress_line_1()+"\n"+ delivery_address.getAddress_line_2()+", "+delivery_address.getPincode()+"\n"+delivery_address.getAddress_line_3()+"\n\n\n\n\n\n\n\n", o) ;  //k
        address.setAlignment(Element.ALIGN_LEFT);
        address.setIndentationLeft(15);



        Paragraph order_id = new Paragraph("ORDER ID : " + orderId ,o)  ; //k
        order_id.setAlignment(Element.ALIGN_LEFT) ;
        order_id.setIndentationLeft(15);

        Paragraph paytm_number = new Paragraph("Paytm No. " + "9136322911",o)  ; //k
        paytm_number.setAlignment(Element.ALIGN_CENTER) ;





        document.add(contact_numbers);
        document.add(contact_address);
        document.add(line) ;
        document.add(new_line);
        document.add(new_line);

        document.add(order_id) ;


        document.add(new_line);
        document.add(table);
        document.add(new_line);
        document.add(new_line);
        document.add(new_line);
        document.add(p_customer_name) ;
        document.add(p_customer_number) ;
        document.add(p_customer_email) ;
        document.add(new_line) ;
        document.add(address) ;
        document.add(new_line) ;
        document.add(paytm_number) ;
        document.add(img_qr) ;


        document.close();

        previewPdf() ;

    }

    private void previewPdf() {

        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        Uri apkURI = FileProvider.getUriForFile(
                this,
                this.getApplicationContext()
                        .getPackageName() + ".provider", pdfFile);
        testIntent.setDataAndType(apkURI, "application/pdf");
        testIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);

        if (list.size() > 0) {

            startActivity(testIntent);

        }else {
            Toast.makeText(this, "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
        }

        /*List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            startActivity(intent);
        } else {
            Toast.makeText(this, "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
        }*/


    }

    private void checkPermissions(double total_amount, double grovenco_credits, double coupon_discount, long delivery_charges) throws FileNotFoundException, DocumentException {


        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        0);

            }

        } else {
            try {

                createPdf(total_amount, grovenco_credits, coupon_discount,delivery_charges,mCustomerName, mCustomerNumber, mCustomerEmail);

            } catch (Exception e) {
                Log.d("FileError", e.getMessage()) ;
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if(Utils.getInternetConnectivity(OrderDetails.this)){

                    try {
                        checkPermissions(mTotalAmount, mGrovencoCredits, mCouponDiscount, mDeliveryCharges);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }

                }else{

                    Toast.makeText(OrderDetails.this, "Please connect to internet", Toast.LENGTH_SHORT).show();

                }

            }
        }
    }

}
