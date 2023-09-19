package com.grovenco.grovencoadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReportActivity extends AppCompatActivity {

    Spinner spn_type ;
    TextView tv_totalDistance, tv_grossProfit, tv_from, tv_to, tv_fromText, tv_emptyState, tv_count, tv_subTotal, tv_discount, tv_netSale, tv_exclusiveDiscount, tv_memberDiscount, tv_grovencoCredits, tv_couponDiscount, tv_deliveryCharges, tv_membershipCount, tv_exclusiveCount, tv_refundAmount ;
    LinearLayout ll_toDate ;
    int fromDate, toDate ;
    Button btn_generate ;
    ProgressBar progress ;
    ArrayList<String> reportTypeList = new ArrayList<>() ;
    RelativeLayout rl_filter ;
    LinearLayout ll_reportDetails ;
    RecyclerView rv_reportOrders ;
    RecyclerView.LayoutManager layoutManagerReportOrders ;

    ArrayList<ReportOrders> reportOrdersList ;
    ReportOrdersAdapter reportOrdersAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        spn_type = findViewById(R.id.spn_select_type) ;
        tv_from = findViewById(R.id.tv_from_date) ;
        tv_to  = findViewById(R.id.tv_to_date) ;
        ll_toDate = findViewById(R.id.ll_to_date) ;
        tv_fromText = findViewById(R.id.tv_from_text) ;
        btn_generate = findViewById(R.id.btn_generate) ;
        rv_reportOrders = findViewById(R.id.rv_report_orders) ;
        tv_emptyState = findViewById(R.id.tv_empty_state) ;
        progress = findViewById(R.id.progress) ;
        rl_filter = findViewById(R.id.rl_filter) ;
        ll_reportDetails = findViewById(R.id.ll_report_details) ;
        tv_count = findViewById(R.id.tv_count) ;
        tv_subTotal = findViewById(R.id.tv_sub_total) ;
        tv_discount = findViewById(R.id.tv_total_discount) ;
        tv_netSale = findViewById(R.id.tv_net_sale) ;
        tv_exclusiveDiscount = findViewById(R.id.tv_exclusive_discount) ;
        tv_memberDiscount = findViewById(R.id.tv_member_discount) ;
        tv_grovencoCredits = findViewById(R.id.tv_grovenco_credits) ;
        tv_couponDiscount = findViewById(R.id.tv_coupon_discount) ;
        tv_deliveryCharges = findViewById(R.id.tv_delivery_charges) ;
        tv_membershipCount = findViewById(R.id.tv_membership_count) ;
        tv_exclusiveCount = findViewById(R.id.tv_exclusive_count) ;
        tv_refundAmount = findViewById(R.id.tv_refund_amount) ;
        tv_totalDistance = findViewById(R.id.tv_total_distance) ;
        tv_grossProfit = findViewById(R.id.tv_gross_profit) ;


        layoutManagerReportOrders = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManagerReportOrders).setOrientation(RecyclerView.VERTICAL);

        rv_reportOrders.setLayoutManager(layoutManagerReportOrders) ;

        reportOrdersList = new ArrayList<>() ;

        reportOrdersAdapter = new ReportOrdersAdapter(this, reportOrdersList) ;

        reportTypeList.add("1. Daily") ;
        reportTypeList.add("2. Range") ;

        ArrayAdapter<String> couponTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, reportTypeList);

        couponTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spn_type.setAdapter(couponTypeAdapter);

        spn_type.setSelection(0);



        switch (spn_type.getSelectedItemPosition()){

            case 0 :

                Utils.setVisibilityGone(ll_toDate);
                tv_fromText.setText("DATE :");

                break ;

            case 1 :

                Utils.setVisibilityVisible(ll_toDate);
                tv_fromText.setText("FROM :");

                break ;


        }

        spn_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){

                    case 0 :

                        Utils.setVisibilityGone(ll_toDate);
                        tv_fromText.setText("DATE :");

                        break ;

                    case 1 :

                        Utils.setVisibilityVisible(ll_toDate);
                        tv_fromText.setText("FROM :");

                        break ;


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tv_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                tv_from.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                c.set(year, monthOfYear,dayOfMonth);
                                fromDate = c.get(Calendar.DAY_OF_YEAR) ;
                                Log.d("FromDate", ""+fromDate) ;


                            }
                        }, year, month, day);
                datePickerDialog.show();
            }

        });

        tv_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                tv_to.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                c.set(year, monthOfYear,dayOfMonth);
                                toDate = c.get(Calendar.DAY_OF_YEAR) ;
                                Log.d("ToDate", "" + toDate) ;


                            }
                        }, year, month, day);
                datePickerDialog.show();

            }
        });


        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utils.getInternetConnectivity(ReportActivity.this)){



                    if(spn_type.getSelectedItemPosition() == 1){

                        if(!TextUtils.isEmpty(tv_from.getText()) && !TextUtils.isEmpty(tv_to.getText())){

                            if(toDate > fromDate){

                                Utils.setVisibilityVisible(ll_reportDetails); ;
                                Utils.setVisibilityGone(rl_filter) ;

                                final long[] total_count = {0};
                                final double[] total_items_amount = {0};
                                final double[] total_discount = {0};
                                final double[] total_sale = {0};

                                final double[] total_grovencoCredits = {0};
                                final double[] total_couponDiscount = {0};
                                final long[] total_exclusiveDiscount = {0};
                                final long[] total_memberDiscount = {0};

                                final long[] total_deliveryCharges = {0};
                                final long[] total_memberships = {0};

                                final long[] total_exclusiveCount = {0};

                                final double[] total_refund_amount = {0};

                                final double[] total_distance = {0};

                                final double[] grossProfit = {0};


                                FirebaseUtil.orders_ref.whereGreaterThanOrEqualTo("order_date_year", fromDate).whereLessThanOrEqualTo("order_date_year",toDate).orderBy("order_date_year", Query.Direction.DESCENDING).orderBy("order_time_formatted", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        if(task.isSuccessful() && task.getResult() != null){

                                            if(!task.getResult().isEmpty()){

                                                Utils.setVisibilityVisible(progress);

                                                reportOrdersList.clear() ;
                                                reportOrdersAdapter.notifyDataSetChanged() ;

                                                int size = task.getResult().size() ;
                                                int count = 0 ;


                                                for(DocumentSnapshot document : task.getResult().getDocuments()){

                                                    String name = document.get("name").toString() ;
                                                    double cartValue =  document.getDouble("cart_value") ;
                                                    String orderDate = document.get("order_date").toString() ;
                                                    String orderTime = document.get("order_time").toString() ;
                                                    double totalSavings = document.getDouble("total_savings") ;
                                                    String orderId = document.get("order_id").toString() ;
                                                    String orderStatus = document.get("order_status").toString() ;
                                                    String deliveryDate = document.get("delivery_date").toString() ;
                                                    double couponDiscount = document.getDouble("coupon_discount") ;
                                                    long exclusiveDiscount = document.getLong("exclusive_discount") ;
                                                    long exclusiveCount = document.getLong("exclusive_items_count") ;
                                                    double grovencoCredits = document.getDouble("grovenco_credits") ;
                                                    final long mrp = document.getLong("mrp");
                                                    long grovencoDiscount = document.getLong("grovenco_discount");
                                                    long memberDiscount = document.getLong("member_discount");
                                                    long deliveryCharges = document.getLong("delivery_charges") ;
                                                    double purchasing = document.getDouble("total_purchasing") ;
                                                    double distance = 0 ;
                                                    boolean membership = false ;

                                                    String deliveredBy = "" ;

                                                    if(couponDiscount < 0 ){

                                                        couponDiscount = 0 ;

                                                    }

                                                    if(document.contains("distance")){

                                                        distance = document.getDouble("distance") ;

                                                    }

                                                    if(document.contains("delivered_by") ){

                                                        deliveredBy = document.get("delivered_by").toString() ;

                                                    }

                                                    if(document.contains("membership_added")){

                                                        membership = document.getBoolean("membership_added") ;

                                                    }

                                                    if(orderStatus.equals("refunded")){

                                                        total_refund_amount[0] = total_refund_amount[0] + document.getDouble("refund_amount") ;

                                                    }

                                                    if(! orderStatus.equals("cancelled") && ! orderStatus.equals("cancelled_admin")){

                                                        total_items_amount[0] = total_items_amount[0] + (cartValue + grovencoCredits + couponDiscount + memberDiscount +exclusiveDiscount) ;
                                                        total_discount[0] = total_discount[0] + grovencoCredits + couponDiscount + memberDiscount + exclusiveDiscount;
                                                        total_grovencoCredits[0] = total_grovencoCredits[0] + grovencoCredits ;
                                                        total_deliveryCharges[0] = total_deliveryCharges[0] + deliveryCharges ;
                                                        total_exclusiveDiscount[0] = total_exclusiveDiscount[0] + exclusiveDiscount ;
                                                        total_couponDiscount[0] = total_couponDiscount[0] + couponDiscount ;
                                                        total_memberDiscount[0] = total_memberDiscount[0] + memberDiscount ;
                                                        total_exclusiveCount[0] = total_exclusiveCount[0] + exclusiveCount ;
                                                        total_distance[0] = total_distance[0] + distance ;
                                                        grossProfit[0] = grossProfit[0] + (cartValue - purchasing) ;

                                                        if(document.contains("membership_added")){

                                                            if(document.getBoolean("membership_added")){

                                                                total_memberships[0]++ ;

                                                            }

                                                        }


                                                        total_count[0]++ ;

                                                    }


                                                    ReportOrders order = new ReportOrders(name,cartValue,orderDate,orderTime,orderId,orderStatus,deliveryDate, deliveryCharges,membership, exclusiveCount, deliveredBy) ;

                                                    reportOrdersList.add(order);

                                                    count = count + 1 ;

                                                    if(size == count){

                                                        total_sale[0] = total_items_amount[0] - total_discount[0] - total_refund_amount[0] ;

                                                        Log.d("Total", "grossAmount : " + total_items_amount[0] + " total discount : " + total_discount[0] + " grovenco credits : " + total_grovencoCredits[0] + " total coupon :" + total_couponDiscount[0] + " delivery charges : " + total_deliveryCharges[0] + " exclusive discount" + total_exclusiveDiscount[0] + " member discount : " + total_memberDiscount[0] + " exclusive count : " + total_exclusiveCount[0] + " memberships : " + total_memberships[0] + " refund amount : " + total_refund_amount[0]  + " total sale : " + total_sale[0] ) ;

                                                        rv_reportOrders.setAdapter(reportOrdersAdapter);
                                                        Utils.setVisibilityVisible(rv_reportOrders);
                                                        Utils.setVisibilityGone(progress);
                                                        Utils.setVisibilityGone(tv_emptyState);

                                                        updateReportUI(total_count[0], total_items_amount[0], total_discount[0], total_sale[0], total_exclusiveDiscount[0], total_memberDiscount[0], total_grovencoCredits[0], total_couponDiscount[0], total_deliveryCharges[0], total_memberships[0], total_exclusiveCount[0], total_refund_amount[0], total_distance[0], grossProfit[0] )  ;


                                                    }

                                                }

                                            }else{

                                                Utils.setVisibilityVisible(tv_emptyState);
                                                Utils.setVisibilityGone(progress);
                                                Utils.setVisibilityGone(rv_reportOrders);

                                            }



                                        }else{

                                            Utils.setVisibilityVisible(rv_reportOrders);
                                            Utils.setVisibilityGone(progress);

                                            if(task.getException() != null){

                                                if(task.getException() instanceof FirebaseNetworkException){

                                                    Toast.makeText(ReportActivity.this, "Please connect to internet", Toast.LENGTH_LONG).show();

                                                } else{

                                                    Toast.makeText(ReportActivity.this, "Oops!! Something went wrong", Toast.LENGTH_LONG).show();

                                                }

                                                Log.d("FirebaseErrorMyOrders", task.getException().getMessage() ) ;

                                            }

                                        }

                                    }
                                }) ;


                            }else{

                                Toast.makeText(ReportActivity.this, "To cannot be less than From ", Toast.LENGTH_SHORT).show();

                            }

                        }else{

                            Toast.makeText(ReportActivity.this, "Please select the range", Toast.LENGTH_SHORT).show();

                        }

                    }else if(spn_type.getSelectedItemPosition() == 0){

                        if(!TextUtils.isEmpty(tv_from.getText())){

                            Utils.setVisibilityVisible(ll_reportDetails); ;
                            Utils.setVisibilityGone(rl_filter) ;

                            final long[] total_count = {0};
                            final double[] total_items_amount = {0};
                            final double[] total_discount = {0};
                            final double[] total_grovencoCredits = {0};
                            final double[] total_couponDiscount = {0};
                            final long[] total_deliveryCharges = {0};
                            final long[] total_memberships = {0};
                            final long[] total_exclusiveDiscount = {0};
                            final long[] total_memberDiscount = {0};
                            final long[] total_exclusiveCount = {0};
                            final double[] total_sale = {0};
                            final double[] total_refund_amount = {0};
                            final double[] total_distance = {0} ;
                            final double[] grossProfit = {0};


                            FirebaseUtil.orders_ref.whereEqualTo("order_date_year", fromDate).orderBy("order_time_formatted", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if(task.isSuccessful() && task.getResult() != null){

                                        if(!task.getResult().isEmpty()){

                                            reportOrdersList.clear() ;

                                            int size = task.getResult().size() ;
                                            int count = 0 ;

                                            for(DocumentSnapshot document : task.getResult().getDocuments()){

                                                String name = document.get("name").toString() ;
                                                double cartValue =  document.getDouble("cart_value") ;
                                                String orderDate = document.get("order_date").toString() ;
                                                String orderTime = document.get("order_time").toString() ;
                                                double totalSavings = document.getDouble("total_savings") ;
                                                String orderId = document.get("order_id").toString() ;
                                                String orderStatus = document.get("order_status").toString() ;
                                                String deliveryDate = document.get("delivery_date").toString() ;
                                                double couponDiscount = document.getDouble("coupon_discount") ;
                                                long exclusiveDiscount = document.getLong("exclusive_discount") ;
                                                long exclusiveCount = document.getLong("exclusive_items_count") ;
                                                double grovencoCredits = document.getDouble("grovenco_credits") ;
                                                final long mrp = document.getLong("mrp");
                                                long grovencoDiscount = document.getLong("grovenco_discount");
                                                long memberDiscount = document.getLong("member_discount");
                                                long deliveryCharges = document.getLong("delivery_charges") ;
                                                double distance = 0 ;
                                                double purchasing = document.getDouble("total_purchasing") ;
                                                boolean membership = false ;
                                                String deliveredBy = "" ;

                                                if(couponDiscount < 0 ){

                                                    couponDiscount = 0 ;

                                                }

                                                if(document.contains("distance")){

                                                    distance = document.getDouble("distance") ;

                                                }

                                                if(document.contains("delivered_by") ){

                                                    deliveredBy = document.get("delivered_by").toString() ;

                                                }

                                                if(document.contains("membership_added")){

                                                    membership = document.getBoolean("membership_added") ;

                                                }


                                                if(orderStatus.equals("refunded")){

                                                    total_refund_amount[0] = total_refund_amount[0] + document.getDouble("refund_amount") ;

                                                }

                                                if(! orderStatus.equals("cancelled") && ! orderStatus.equals("cancelled_admin")){

                                                    total_items_amount[0] = total_items_amount[0] + (cartValue + grovencoCredits + couponDiscount + memberDiscount +exclusiveDiscount) ;
                                                    total_discount[0] = total_discount[0] + grovencoCredits + couponDiscount + memberDiscount + exclusiveDiscount;
                                                    total_grovencoCredits[0] = total_grovencoCredits[0] + grovencoCredits ;
                                                    total_deliveryCharges[0] = total_deliveryCharges[0] + deliveryCharges ;
                                                    total_exclusiveDiscount[0] = total_exclusiveDiscount[0] + exclusiveDiscount ;
                                                    total_couponDiscount[0] = total_couponDiscount[0] + couponDiscount ;
                                                    total_memberDiscount[0] = total_memberDiscount[0] + memberDiscount ;
                                                    total_exclusiveCount[0] = total_exclusiveCount[0] + exclusiveCount ;
                                                    total_distance[0] = total_distance[0] + distance ;
                                                    grossProfit[0] = grossProfit[0] + (cartValue - purchasing) ;

                                                    if(document.contains("membership_added")){

                                                        if(document.getBoolean("membership_added")){

                                                            total_memberships[0]++ ;

                                                        }

                                                    }


                                                    total_count[0]++ ;

                                                }


                                                ReportOrders order = new ReportOrders(name,cartValue,orderDate,orderTime,orderId,orderStatus,deliveryDate, deliveryCharges,membership, exclusiveCount, deliveredBy) ;

                                                reportOrdersList.add(order);

                                                count = count +1 ;

                                                if(size == count){

                                                    total_sale[0] = total_items_amount[0] - total_discount[0] - total_refund_amount[0] ;

                                                    Log.d("Total", "grossAmount : " + total_items_amount[0] + " total discount : " + total_discount[0] + " grovenco credits : " + total_grovencoCredits[0] + " total coupon :" + total_couponDiscount[0] + " delivery charges : " + total_deliveryCharges[0] + " exclusive discount" + total_exclusiveDiscount[0] + " member discount : " + total_memberDiscount[0] + " exclusive count : " + total_exclusiveCount[0] + " memberships : " + total_memberships[0] + " refund amount : " + total_refund_amount[0]  + " total sale : " + total_sale[0] ) ;

                                                    rv_reportOrders.setAdapter(reportOrdersAdapter);
                                                    Utils.setVisibilityVisible(rv_reportOrders);
                                                    Utils.setVisibilityGone(progress);
                                                    Utils.setVisibilityGone(tv_emptyState);

                                                    updateReportUI(total_count[0], total_items_amount[0], total_discount[0], total_sale[0], total_exclusiveDiscount[0], total_memberDiscount[0], total_grovencoCredits[0], total_couponDiscount[0], total_deliveryCharges[0], total_memberships[0], total_exclusiveCount[0], total_refund_amount[0], total_distance[0], grossProfit[0] )  ;


                                                }

                                            }

                                        }else{

                                            Utils.setVisibilityVisible(tv_emptyState);
                                            Utils.setVisibilityGone(progress);
                                            Utils.setVisibilityGone(rv_reportOrders);

                                        }



                                    }else{

                                        Utils.setVisibilityVisible(rv_reportOrders);
                                        Utils.setVisibilityGone(progress);

                                        if(task.getException() != null){

                                            if(task.getException() instanceof FirebaseNetworkException){

                                                Toast.makeText(ReportActivity.this, "Please connect to internet", Toast.LENGTH_LONG).show();

                                            } else{

                                                Toast.makeText(ReportActivity.this, "Oops!! Something went wrong", Toast.LENGTH_LONG).show();

                                            }

                                            Log.d("FirebaseErrorMyOrders", task.getException().getMessage() ) ;

                                        }

                                    }

                                }
                            }) ;


                        }else{

                            Toast.makeText(ReportActivity.this, "Please select the date", Toast.LENGTH_SHORT).show();

                        }

                    }

                }else{

                    Toast.makeText(ReportActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void updateReportUI(long total_count, double sub_total, double discount, double net_sale, long exclusive_discount, long member_discount, double grovenco_credit, double coupon_discount, long delivery_charges, long membership_count, long exclusive_item_count, double refund_amount, double total_distance, double gross_profit) {

        tv_count.setText(String.valueOf(total_count));
        tv_subTotal.setText(Utils.getAmount(Math.round(sub_total)));
        tv_discount.setText(Utils.getAmount(Math.round(discount)));
        tv_netSale.setText(Utils.getAmount(Math.round(net_sale)));
        tv_exclusiveDiscount.setText(Utils.getAmount(exclusive_discount));
        tv_memberDiscount.setText(Utils.getAmount(member_discount));
        tv_grovencoCredits.setText(Utils.getAmount(Math.round(grovenco_credit)));
        tv_couponDiscount.setText(Utils.getAmount(Math.round(coupon_discount)));
        tv_deliveryCharges.setText(Utils.getAmount(delivery_charges));
        tv_membershipCount.setText(String.valueOf(membership_count));
        tv_exclusiveCount.setText(String.valueOf(exclusive_item_count));
        tv_refundAmount.setText(Utils.getAmount(Math.round(refund_amount)));
        tv_totalDistance.setText(String.format("%.3f", total_distance) + "km");
        tv_grossProfit.setText(Utils.getAmount(Math.round(gross_profit)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.select_date, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.select_date){

            reportOrdersList.clear();
            reportOrdersAdapter.notifyDataSetChanged();

            Utils.setVisibilityGone(ll_reportDetails) ;
            Utils.setVisibilityVisible(rl_filter) ;

        }

        return true;
    }
}