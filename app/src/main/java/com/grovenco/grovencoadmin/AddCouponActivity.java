package com.grovenco.grovencoadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.CompletionHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddCouponActivity extends AppCompatActivity {

    LinearLayout ll_type, ll_code, ll_title, ll_details, ll_minOrder, ll_discount, ll_offerPercent, ll_maxDiscount ;
    EditText  et_code, et_title, et_details, et_minOrder, et_discount, et_offerPercent, et_maxDiscount ;
    TextView  tv_code, tv_title, tv_details, tv_minOrder, tv_discount, tv_offerPercent, tv_maxDiscount ;
    Spinner spn_type ;
    ArrayList<String> couponTypesList ;
    ArrayList<EditText> editTextList ;
    ArrayList<TextView>  textViewList ;
    Button btn_add ;
    ProgressBar progress ;
    String couponId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);

        btn_add = findViewById(R.id.btn_add) ;
        progress = findViewById(R.id.progress) ;

        ll_type = findViewById(R.id.ll_type) ;
        ll_code = findViewById(R.id.ll_code) ;
        ll_title = findViewById(R.id.ll_title) ;
        ll_details = findViewById(R.id.ll_details) ;
        ll_minOrder = findViewById(R.id.ll_min_order) ;
        ll_discount = findViewById(R.id.ll_discount) ;
        ll_offerPercent = findViewById(R.id.ll_offer_percent) ;
        ll_maxDiscount = findViewById(R.id.ll_max_discount) ;

        spn_type = findViewById(R.id.spn_coupon_type) ;
        et_code = findViewById(R.id.et_code) ;
        et_title = findViewById(R.id.et_title) ;
        et_details = findViewById(R.id.et_details) ;
        et_minOrder = findViewById(R.id.et_min_order) ;
        et_discount = findViewById(R.id.et_discount) ;
        et_offerPercent = findViewById(R.id.et_offer_percent) ;
        et_maxDiscount = findViewById(R.id.et_max_discount) ;

        tv_code = findViewById(R.id.tv_code) ;
        tv_title = findViewById(R.id.tv_title) ;
        tv_details = findViewById(R.id.tv_details) ;
        tv_minOrder = findViewById(R.id.tv_min_order) ;
        tv_discount = findViewById(R.id.tv_discount) ;
        tv_offerPercent = findViewById(R.id.tv_offer_percent) ;
        tv_maxDiscount = findViewById(R.id.tv_max_discount) ;

        couponTypesList = new ArrayList<>() ;
        editTextList = new ArrayList<>() ;
        textViewList = new ArrayList<>() ;

        editTextList.add(et_code) ;
        editTextList.add(et_title) ;
        editTextList.add(et_details) ;
        editTextList.add(et_minOrder) ;
        editTextList.add(et_offerPercent) ;
        editTextList.add(et_maxDiscount) ;

        textViewList.add(tv_code) ;
        textViewList.add(tv_title) ;
        textViewList.add(tv_details) ;
        textViewList.add(tv_minOrder) ;
        textViewList.add(tv_offerPercent) ;
        textViewList.add(tv_maxDiscount) ;


        couponTypesList.add("1. Percentage Offer") ;
        couponTypesList.add("2. Free Delivery") ;
        couponTypesList.add("3. Flat Discount") ;
        couponTypesList.add("4. Cashback Percentage Offer") ;


        ArrayAdapter<String> couponTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, couponTypesList);

        couponTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spn_type.setAdapter(couponTypeAdapter);

        spn_type.setSelection(0);

        switch (spn_type.getSelectedItemPosition()){

            case 0 :

                Utils.setVisibilityGone(ll_discount);
                Utils.setVisibilityVisible(ll_maxDiscount);
                Utils.setVisibilityVisible(ll_offerPercent);

                break ;

            case 1 :

                Utils.setVisibilityGone(ll_discount);
                Utils.setVisibilityGone(ll_maxDiscount);
                Utils.setVisibilityGone(ll_offerPercent);

                break ;

            case 2 :

                Utils.setVisibilityVisible(ll_discount);
                Utils.setVisibilityGone(ll_maxDiscount);
                Utils.setVisibilityGone(ll_offerPercent);

                break ;

            case 3 :

                Utils.setVisibilityGone(ll_discount);
                Utils.setVisibilityVisible(ll_maxDiscount);
                Utils.setVisibilityVisible(ll_offerPercent);

                break ;

        }

        if(getIntent().getStringExtra("coupon_id") != null){

            String type  = getIntent().getStringExtra("type") ;

            String details = getIntent().getStringExtra("details") ;
            long minOrder = getIntent().getLongExtra("min_order", 0) ;
            String title = getIntent().getStringExtra("title") ;
            String code = getIntent().getStringExtra("code") ;

            et_details.setText(details);
            et_minOrder.setText(String.valueOf(minOrder));
            et_title.setText(title);
            et_code.setText(code);


            switch (type){

                case "1" :

                    long maxDiscount = getIntent().getLongExtra("max_discount",0) ;
                    long offer = getIntent().getLongExtra("offer",0) ;

                    spn_type.setSelection(0) ;
                    et_maxDiscount.setText(String.valueOf(maxDiscount));
                    et_offerPercent.setText(String.valueOf(offer));

                    break ;

                case "3" :

                    spn_type.setSelection(1) ;

                    break ;

                case "4" :

                    double discount = getIntent().getDoubleExtra("discount", 0) ;

                    spn_type.setSelection(2) ;
                    et_discount.setText(String.valueOf(discount));

                    break ;

                case "5" :

                    long maxDiscount1 = getIntent().getLongExtra("max_discount",0) ;
                    long offer1 = getIntent().getLongExtra("offer",0) ;

                    spn_type.setSelection(3) ;
                    et_maxDiscount.setText(String.valueOf(maxDiscount1));
                    et_offerPercent.setText(String.valueOf(offer1));

                    break ;

            }

        }

        spn_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){

                    case 0 :

                        Utils.setVisibilityGone(ll_discount);
                        Utils.setVisibilityVisible(ll_maxDiscount);
                        Utils.setVisibilityVisible(ll_offerPercent);

                        editTextList.clear();
                        textViewList.clear();

                        editTextList.add(et_code) ;
                        editTextList.add(et_title) ;
                        editTextList.add(et_details) ;
                        editTextList.add(et_minOrder) ;
                        editTextList.add(et_offerPercent) ;
                        editTextList.add(et_maxDiscount) ;

                        textViewList.add(tv_code) ;
                        textViewList.add(tv_title) ;
                        textViewList.add(tv_details) ;
                        textViewList.add(tv_minOrder) ;
                        textViewList.add(tv_offerPercent) ;
                        textViewList.add(tv_maxDiscount) ;

                        break ;

                    case 1 :

                        Utils.setVisibilityGone(ll_discount);
                        Utils.setVisibilityGone(ll_maxDiscount);
                        Utils.setVisibilityGone(ll_offerPercent);

                        editTextList.clear();
                        textViewList.clear();

                        editTextList.add(et_code) ;
                        editTextList.add(et_title) ;
                        editTextList.add(et_details) ;
                        editTextList.add(et_minOrder) ;

                        textViewList.add(tv_code) ;
                        textViewList.add(tv_title) ;
                        textViewList.add(tv_details) ;
                        textViewList.add(tv_minOrder) ;


                        break ;

                    case 2 :

                        Utils.setVisibilityVisible(ll_discount);
                        Utils.setVisibilityGone(ll_maxDiscount);
                        Utils.setVisibilityGone(ll_offerPercent);

                        editTextList.clear();
                        textViewList.clear();

                        editTextList.add(et_code) ;
                        editTextList.add(et_title) ;
                        editTextList.add(et_details) ;
                        editTextList.add(et_minOrder) ;
                        editTextList.add(et_discount) ;

                        textViewList.add(tv_code) ;
                        textViewList.add(tv_title) ;
                        textViewList.add(tv_details) ;
                        textViewList.add(tv_minOrder) ;
                        textViewList.add(tv_discount) ;


                        break ;

                    case 3 :

                        Utils.setVisibilityGone(ll_discount);
                        Utils.setVisibilityVisible(ll_maxDiscount);
                        Utils.setVisibilityVisible(ll_offerPercent);

                        editTextList.clear();
                        textViewList.clear();

                        editTextList.add(et_code) ;
                        editTextList.add(et_title) ;
                        editTextList.add(et_details) ;
                        editTextList.add(et_minOrder) ;
                        editTextList.add(et_offerPercent) ;
                        editTextList.add(et_maxDiscount) ;

                        textViewList.add(tv_code) ;
                        textViewList.add(tv_title) ;
                        textViewList.add(tv_details) ;
                        textViewList.add(tv_minOrder) ;
                        textViewList.add(tv_offerPercent) ;
                        textViewList.add(tv_maxDiscount) ;

                        break ;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utils.getInternetConnectivity(AddCouponActivity.this)){

                    if(Utils.validate(editTextList, textViewList)){

                        Utils.setVisibilityVisible(progress);

                        long id = UUID.randomUUID().getMostSignificantBits() ;

                        Log.d("Uid",String.valueOf(id).substring(0,7)) ;

                        if(id<0){

                            id = -(id) ;

                        }

                        if(getIntent().getStringExtra("coupon_id") != null){

                            couponId = getIntent().getStringExtra("coupon_id") ;

                        }else{

                            couponId = String.valueOf(id) ;

                        }

                        Map<String, Object> hashMap = new HashMap<>() ;
                        hashMap.put("enabled", false) ;
                        hashMap.put("visible", false) ;
                        hashMap.put("coupon_id", couponId) ;
                        hashMap.put("title",et_title.getText().toString()) ;
                        hashMap.put("code",et_code.getText().toString()) ;
                        hashMap.put("details",et_details.getText().toString()) ;
                        hashMap.put("min_order",Long.valueOf(et_minOrder.getText().toString())) ;

                        switch(spn_type.getSelectedItemPosition()){

                            case 0 :

                                hashMap.put("type", "1") ;
                                hashMap.put("offer", Long.valueOf(et_offerPercent.getText().toString())) ;
                                hashMap.put("max_discount", Long.valueOf(et_maxDiscount.getText().toString())) ;

                                break ;

                            case 1 :

                                hashMap.put("type", "3") ;

                                break ;

                            case 2 :

                                hashMap.put("type", "4") ;
                                hashMap.put("discount", Double.valueOf(et_discount.getText().toString())) ;

                                break ;

                            case 3 :

                                hashMap.put("type", "5") ;
                                hashMap.put("offer",  Long.valueOf(et_offerPercent.getText().toString())) ;
                                hashMap.put("max_discount", Long.valueOf(et_maxDiscount.getText().toString())) ;

                                break ;

                        }

                        FirebaseUtil.coupons_ref.document(couponId).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Utils.setVisibilityGone(progress);

                                if(task.isSuccessful()){

                                    Toast.makeText(AddCouponActivity.this, "Coupon Added/Updated", Toast.LENGTH_SHORT).show();

                                }else{

                                    if(task.getException() != null){

                                        Log.d("FirebaseCouponsError", task.getException().getMessage()) ;

                                    }

                                    Toast.makeText(AddCouponActivity.this, "Something went wrong ! Try again", Toast.LENGTH_LONG).show();

                                }



                            }
                        }) ;

                    }

                }else{

                    Toast.makeText(AddCouponActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

}