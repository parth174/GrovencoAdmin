package com.grovenco.grovencoadmin;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText  et_description, et_grovencoExclusive, et_grovencoExclusivePrice, et_grovencoExclusiveQty, et_grovencoPrice,et_itemCode, et_limit, et_memberPrice, et_mrp, et_name, et_parentCode, et_purchasingPrice, et_stock, et_weight, et_weightUnit, et_image_front, et_image_back ;
    TextView tv_category, tv_description, tv_grovencoExclusive, tv_grovencoExclusivePrice, tv_grovencoExclusiveQty, tv_grovencoPrice, tv_isExtra,tv_itemCode, tv_limit, tv_memberPrice, tv_mrp, tv_name, tv_parentCode, tv_purchasingPrice, tv_stock, tv_subcategory,tv_trending, tv_weight, tv_weightUnit, tv_image_front, tv_image_back ;
    Button btn_add ;
    Spinner spn_grovencoExclusive, spn_isExtra, spn_trending ;
    ArrayList<EditText> editTextList ;
    ArrayList<TextView> textViewList ;
    ArrayList<Boolean> booleanList ;
    ArrayList<String> categoryList ;
    ArrayList<String> subcategoryList ;
    ArrayList<Long> binaryList ;
    long one = 1 ;
    long zero = 0 ;
    Boolean is_extra ;
    Boolean grovenco_exclusive ;
    Long trending ;
    LinearLayout ll_exclusivePrice, ll_exclusiveQty, ll_addItem ;
    ProgressBar progress ;
    Spinner auto_category, auto_subcategory ;

    NotificationService notificationService ;
    Intent serviceIntent ;
    FirebaseAuth auth ;
    String mSubcategory ;

    static String ALGOLIA_ADMIN_API_KEY = "04e2ad6958ffc8e8155e7d89e5350af0" ;
    static String ALGOLIA_APPLICATION_ID = "0OZPXWMR23" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Client client = new Client(ALGOLIA_APPLICATION_ID,ALGOLIA_ADMIN_API_KEY );
        final Index index = client.getIndex("Items");

        auth = FirebaseAuth.getInstance() ;

        booleanList = new ArrayList<>() ;
        binaryList = new ArrayList<>() ;
        categoryList = new ArrayList<>() ;
        subcategoryList = new ArrayList<>() ;

        categoryList.add("Foodgrains & Oil") ;
        categoryList.add("Beverages") ;
        categoryList.add("Snacks & Instant Food") ;
        categoryList.add("Personal Care") ;
        categoryList.add("Cleaning & Household") ;

        booleanList.add(true);
        booleanList.add(false);

        binaryList.add(one);
        binaryList.add(zero);

        auto_category = findViewById(R.id.auto_category) ;
        et_description = findViewById(R.id.et_description) ;
        et_grovencoExclusivePrice = findViewById(R.id.et_grovenco_exclusive_price) ;
        et_grovencoExclusiveQty = findViewById(R.id.et_grovenco_exclusive_qty) ;
        et_grovencoPrice = findViewById(R.id.et_grovenco_price) ;
        et_itemCode = findViewById(R.id.et_item_code) ;
        et_limit = findViewById(R.id.et_limit) ;
        et_memberPrice = findViewById(R.id.et_member_price) ;
        et_mrp = findViewById(R.id.et_mrp) ;
        et_name = findViewById(R.id.et_name) ;
        et_parentCode = findViewById(R.id.et_parent_code) ;
        et_purchasingPrice = findViewById(R.id.et_purchasing_price) ;
        et_stock = findViewById(R.id.et_stock) ;
        auto_subcategory = findViewById(R.id.auto_subcategory) ;
        et_weight = findViewById(R.id.et_weight) ;
        et_weightUnit = findViewById(R.id.et_weight_unit) ;
        et_image_front = findViewById(R.id.et_image_front) ;
        et_image_back = findViewById(R.id.et_image_back) ;
        ll_addItem = findViewById(R.id.ll_addItem) ;
        progress = findViewById(R.id.progress) ;

        spn_grovencoExclusive = findViewById(R.id.spn_grovenco_exclusive) ;
        spn_isExtra = findViewById(R.id.spn_is_extra) ;
        spn_trending = findViewById(R.id.spn_trending) ;

        tv_category = findViewById(R.id.tv_category) ;
        tv_description = findViewById(R.id.tv_description) ;
        tv_grovencoExclusive = findViewById(R.id.tv_grovenco_exclusive) ;
        tv_grovencoExclusivePrice = findViewById(R.id.tv_grovenco_exclusive_price) ;
        tv_grovencoExclusiveQty = findViewById(R.id.tv_grovenco_exclusive_qty) ;
        tv_grovencoPrice = findViewById(R.id.tv_grovenco_price) ;
        tv_isExtra = findViewById(R.id.tv_is_extra) ;
        tv_itemCode = findViewById(R.id.tv_item_code) ;
        tv_limit = findViewById(R.id.tv_limit) ;
        tv_memberPrice = findViewById(R.id.tv_member_price) ;
        tv_mrp = findViewById(R.id.tv_mrp) ;
        tv_name = findViewById(R.id.tv_name) ;
        tv_parentCode = findViewById(R.id.tv_parent_code) ;
        tv_purchasingPrice = findViewById(R.id.tv_purchasing_price) ;
        tv_stock = findViewById(R.id.tv_stock) ;
        tv_subcategory = findViewById(R.id.tv_subcategory) ;
        tv_trending = findViewById(R.id.tv_trending) ;
        tv_weight = findViewById(R.id.tv_weight) ;
        tv_weightUnit = findViewById(R.id.tv_weight_unit) ;
        tv_image_back = findViewById(R.id.tv_image_back) ;
        tv_image_front = findViewById(R.id.tv_image_front) ;

        btn_add = findViewById(R.id.btn_add) ;
        ll_exclusivePrice = findViewById(R.id.ll_exclusive_price) ;
        ll_exclusiveQty = findViewById(R.id.ll_exclusive_qty) ;


        editTextList = new ArrayList<>() ;
        textViewList = new ArrayList<>() ;

        /*editTextList.add(et_category );*/
        editTextList.add(et_description);
       /* editTextList.add(et_grovencoExclusivePrice);
        editTextList.add(et_grovencoExclusiveQty);*/
        editTextList.add(et_grovencoPrice);
        /*editTextList.add(et_image_front);
        editTextList.add(et_image_back);*/
        editTextList.add(et_itemCode);
        editTextList.add(et_limit);
        editTextList.add(et_memberPrice);
        editTextList.add(et_mrp);
        editTextList.add(et_name);
        editTextList.add(et_parentCode);
        editTextList.add(et_purchasingPrice);
        editTextList.add(et_stock);
        /*editTextList.add(et_subcategory);*/
        editTextList.add(et_weight);
        editTextList.add(et_weightUnit);

        /*textViewList.add(tv_category );*/
        textViewList.add(tv_description);
        /*textViewList.add(tv_grovencoExclusivePrice);
        textViewList.add(tv_grovencoExclusiveQty);*/
        textViewList.add(tv_grovencoPrice);
        /*textViewList.add(tv_image_front);
        textViewList.add(tv_image_back);*/
        textViewList.add(tv_itemCode);
        textViewList.add(tv_limit);
        textViewList.add(tv_memberPrice);
        textViewList.add(tv_mrp);
        textViewList.add(tv_name);
        textViewList.add(tv_parentCode);
        textViewList.add(tv_purchasingPrice);
        textViewList.add(tv_stock);
        /*textViewList.add(tv_subcategory);*/
        textViewList.add(tv_weight);
        textViewList.add(tv_weightUnit);



        notificationService = new NotificationService() ;
        serviceIntent = new Intent(this, notificationService.getClass()) ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            startForegroundService(serviceIntent) ;

        }else{

            startService(serviceIntent) ;

        }


        et_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_description) ;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_grovencoExclusivePrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_grovencoExclusivePrice) ;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_grovencoExclusiveQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_grovencoExclusiveQty) ;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_grovencoPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_grovencoPrice) ;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_itemCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_itemCode) ;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_limit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_limit) ;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_memberPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_memberPrice) ;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_mrp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_mrp) ;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_mrp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_mrp) ;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_name) ;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_parentCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_parentCode) ;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_purchasingPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_purchasingPrice) ;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_stock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_stock) ;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        et_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_weight) ;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_weightUnit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_weightUnit) ;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

      /*  et_image_front.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_image_front) ;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_image_back.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_image_back) ;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
*/

        ArrayAdapter<Boolean> booleanAdapter = new ArrayAdapter<Boolean>(this, android.R.layout.simple_spinner_item, booleanList);

        ArrayAdapter<Long> binaryAdapter = new ArrayAdapter<Long>(this, android.R.layout.simple_spinner_item, binaryList);

        final ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subcategoryList);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);

        booleanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binaryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subcategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spn_grovencoExclusive.setAdapter(booleanAdapter);
        spn_isExtra.setAdapter(booleanAdapter);
        spn_trending.setAdapter(binaryAdapter);

        spn_grovencoExclusive.setSelection(1);
        spn_trending.setSelection(1);
        spn_isExtra.setSelection(1);

        auto_category.setAdapter(categoryAdapter);

        auto_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                subcategoryList.clear() ;
                Utils.setVisibilityGone(tv_category);

                FirebaseUtil.subcategories_ref.whereEqualTo("category_name", adapterView.getSelectedItem().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){

                            if(task.getResult() != null){

                                if(!task.getResult().isEmpty()){

                                    int size = task.getResult().size() ;
                                    int count = 0 ;

                                    for(DocumentSnapshot document : task.getResult().getDocuments()){

                                        String name = document.get("subcategory_name").toString() ;

                                        subcategoryList.add(name);

                                        count++ ;

                                        if(count == size){

                                            auto_subcategory.setAdapter(subcategoryAdapter) ;

                                            if(getIntent().getStringExtra("itemcode") != null){

                                                for(int i=0 ; i<subcategoryList.size() ; i++){

                                                    if(subcategoryList.get(i).equals(mSubcategory)){

                                                        auto_subcategory.setSelection(i);

                                                    }

                                                }

                                            }

                                        }

                                    }

                                }

                            }
                        }

                    }
                }) ;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if(getIntent().getStringExtra("itemcode") != null){

            String itemcode = getIntent().getStringExtra("itemcode") ;

            Log.d("ItemcodeReceived", "Received itemcode" + itemcode ) ;

            FirebaseUtil.items_ref.document(itemcode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.getResult() != null){

                        DocumentSnapshot itemDocument = task.getResult() ;

                        final String name = itemDocument.get("name").toString() ;
                        final String description = itemDocument.get("description").toString() ;
                        final String item_code =itemDocument.get("item_code").toString() ;
                        final String category = itemDocument.get("category").toString() ;
                        final String subcategory = itemDocument.get("subcategory").toString() ;
                        final String parent_code = itemDocument.get("parent_code").toString() ;
                        final long weight =(long) itemDocument.get("weight") ;
                        final String weight_unit = itemDocument.get("weight_unit").toString() ;
                        final long grovenco_price =(long) itemDocument.get("grovenco_price") ;
                        final Boolean grovenco_exclusive = (Boolean) itemDocument.get("grovenco_exclusive") ;
                        final String image_front = itemDocument.get("image_front").toString() ;
                        final String image_back = itemDocument.get("image_back").toString() ;
                        long tempGrovenco_exclusive_qty = 0;
                        long tempGrovenco_exclusive_price = 0;

                        if(grovenco_exclusive){
                            tempGrovenco_exclusive_qty =(long) itemDocument.get("grovenco_exclusive_qty") ;
                            tempGrovenco_exclusive_price =(long) itemDocument.get("grovenco_exclusive_price") ;
                        }

                        final long grovenco_exclusive_qty = tempGrovenco_exclusive_qty ;
                        final long grovenco_exclusive_price = tempGrovenco_exclusive_price ;

                        final long mrp =(long) itemDocument.get("mrp") ;
                        final long member_price =(long) itemDocument.get("member_price") ;
                        final long stock =(long) itemDocument.get("stock") ;
                        final long limit =(long) itemDocument.get("limit") ;
                        final double purchasing_price = itemDocument.getDouble("purchasing_price") ;
                        final Boolean is_extra = (Boolean) itemDocument.get("is_extra") ;
                        final long is_trending  = (long) itemDocument.get("trending") ;

                        mSubcategory = subcategory ;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                for(int i=0 ; i<categoryList.size() ; i++){

                                    if(categoryList.get(i).equals(category)){

                                        auto_category.setSelection(i);

                                    }

                                }
                                for(int i=0 ; i<subcategoryList.size() ; i++){

                                    if(subcategoryList.get(i).equals(subcategory)){

                                        auto_subcategory.setSelection(i);

                                    }

                                }

                            }
                        }, 1000) ;


                        et_description.setText(description);
                        et_name.setText(name);
                        et_itemCode.setText(item_code);
                        et_mrp.setText(String.valueOf(mrp));
                        et_memberPrice.setText(String.valueOf(member_price));
                        et_stock.setText(String.valueOf(stock));
                        et_limit.setText(String.valueOf(limit));
                        et_grovencoExclusivePrice.setText(String.valueOf(grovenco_exclusive_price));
                        et_grovencoExclusiveQty.setText(String.valueOf(grovenco_exclusive_qty));
                        et_parentCode.setText(parent_code);
                        et_weight.setText(String.valueOf(weight));
                        et_weightUnit.setText(weight_unit);
                        et_grovencoPrice.setText(String.valueOf(grovenco_price));
                        et_purchasingPrice.setText(String.valueOf(purchasing_price));
                        et_image_front.setText(image_front);
                        et_image_back.setText(image_back);



                        if(grovenco_exclusive){

                            spn_grovencoExclusive.setSelection(0);

                        }else{

                            spn_grovencoExclusive.setSelection(1);

                        }

                        if(is_extra != null && is_extra){

                            spn_isExtra.setSelection(0);

                        }else{

                            spn_isExtra.setSelection(1);

                        }


                        if(is_trending == 1){

                            Log.d("trending1", "one") ;
                            spn_trending.setSelection(0);

                        }else{

                            Log.d("trending0", "zero") ;
                            spn_trending.setSelection(1);

                        }

                    }



                }
            }) ;

        }

        spn_trending.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                trending = (Long) adapterView.getItemAtPosition(i) ;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_grovencoExclusive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                grovenco_exclusive = (Boolean) adapterView.getItemAtPosition(i) ;

                if(!grovenco_exclusive){

                    Utils.setVisibilityGone(ll_exclusivePrice);
                    Utils.setVisibilityGone(ll_exclusiveQty);

                    editTextList.remove(et_grovencoExclusivePrice);
                    editTextList.remove(et_grovencoExclusiveQty);

                    textViewList.remove(tv_grovencoExclusivePrice);
                    textViewList.remove(tv_grovencoExclusiveQty);

                }else{

                    Utils.setVisibilityVisible(ll_exclusivePrice);
                    Utils.setVisibilityVisible(ll_exclusiveQty);

                    editTextList.add(et_grovencoExclusivePrice);
                    editTextList.add(et_grovencoExclusiveQty);

                    textViewList.add(tv_grovencoExclusivePrice);
                    textViewList.add(tv_grovencoExclusiveQty);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_isExtra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                is_extra = (Boolean) adapterView.getItemAtPosition(i) ;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Utils.validate(editTextList, textViewList)){

                    Utils.setVisibilityVisible(progress);
                    Utils.setVisibilityGone(ll_addItem);

                            final Map<String,Object> hashmap = new HashMap<>() ;
                            hashmap.put("category", auto_category.getSelectedItem().toString());
                            hashmap.put("description", et_description.getText().toString());
                            hashmap.put("grovenco_exclusive",grovenco_exclusive);

                            if((boolean)spn_grovencoExclusive.getSelectedItem()){

                                hashmap.put("grovenco_exclusive_price" ,Long.valueOf(et_grovencoExclusivePrice.getText().toString()));
                                hashmap.put("grovenco_exclusive_qty",Long.valueOf(et_grovencoExclusiveQty.getText().toString()) );

                            }else{

                                hashmap.put("grovenco_exclusive_price" , (long) 0 );
                                hashmap.put("grovenco_exclusive_qty", (long) 0 );

                            }


                            hashmap.put("grovenco_price", Long.valueOf(et_grovencoPrice.getText().toString()));
                            hashmap.put("is_extra", is_extra);
                            hashmap.put("item_code", et_itemCode.getText().toString());
                            hashmap.put("limit", Long.valueOf(et_limit.getText().toString()));
                            hashmap.put("member_price", Long.valueOf(et_memberPrice.getText().toString()));
                            hashmap.put("mrp", Long.valueOf(et_mrp.getText().toString()));
                            hashmap.put("name", et_name.getText().toString());
                            hashmap.put("parent_code", et_parentCode.getText().toString());
                            hashmap.put("purchasing_price", Double.valueOf(et_purchasingPrice.getText().toString()));
                            hashmap.put("stock", Long.valueOf(et_stock.getText().toString()));
                            hashmap.put("subcategory", auto_subcategory.getSelectedItem().toString());
                            hashmap.put("trending", trending) ;
                            hashmap.put("weight", Long.valueOf(et_weight.getText().toString()));
                            hashmap.put("weight_unit", et_weightUnit.getText().toString());

                            final Map<String,Object> algoliaHashmap = new HashMap<>() ;
                            algoliaHashmap.put("name", et_name.getText().toString()) ;
                            algoliaHashmap.put("item_code", et_itemCode.getText().toString()) ;
                            algoliaHashmap.put("parent_code",et_parentCode.getText().toString()) ;
                            algoliaHashmap.put("subcategory", auto_subcategory.getSelectedItem().toString());
                            algoliaHashmap.put("category", auto_category.getSelectedItem().toString());

                            String image_front = et_itemCode.getText().toString() + "F" ;
                            final String image_back = et_itemCode.getText().toString() + "B" ;



                            FirebaseUtil.trendingItems_storageRef.child(image_front+".webp").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    if(task.isSuccessful()){

                                        final Uri image_front = task.getResult() ;

                                        FirebaseUtil.trendingItems_storageRef.child(image_back+".webp").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {

                                                if(task.isSuccessful()){

                                                    Uri image_back = task.getResult() ;

                                                    algoliaHashmap.put("image_front",image_front.toString() ) ;

                                                    hashmap.put("image_front", image_front.toString()) ;
                                                    hashmap.put("image_back", image_back.toString()) ;

                                                    if(! BuildConfig.BUILD_TYPE.equals("development") && getIntent().getStringExtra("itemcode") == null ){

                                                        JSONObject algoliaObject = new JSONObject( algoliaHashmap ) ;
                                                        index.addObjectAsync(algoliaObject, new CompletionHandler() {
                                                            @Override
                                                            public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {

                                                                if(e != null){

                                                                    Log.d("AlgoliaException", e.getMessage()) ;
                                                                    Toast.makeText(HomeActivity.this, "Something went wrong !! Please contact backend team.", Toast.LENGTH_LONG).show();

                                                                }

                                                            }
                                                        }) ;

                                                    }

                                                    FirebaseUtil.items_ref.document(et_itemCode.getText().toString()).set(hashmap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            Utils.setVisibilityVisible(ll_addItem);
                                                            Utils.setVisibilityGone(progress);

                                                            if(task.isSuccessful()){

                                                                Toast.makeText(HomeActivity.this, "Item Added", Toast.LENGTH_LONG).show();

                                                            }else{

                                                                Toast.makeText(HomeActivity.this, "Something went wrong ! File not uploaded ", Toast.LENGTH_LONG).show();

                                                            }

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                            Log.d("FirebaseError", e.getMessage()) ;

                                                        }
                                                    }) ;

                                                }

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Utils.setVisibilityVisible(ll_addItem);
                                                Utils.setVisibilityGone(progress);

                                                Log.d("FirebaseStorageError", e.getMessage()) ;
                                                Toast.makeText(HomeActivity.this, "Item back image not found ! Item not uploaded", Toast.LENGTH_LONG).show();

                                            }
                                        }) ;

                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Utils.setVisibilityVisible(ll_addItem);
                                    Utils.setVisibilityGone(progress);

                                    Log.d("FirebaseStorageError", e.getMessage()) ;

                                    Toast.makeText(HomeActivity.this, "Item front image not found ! Item not uploaded", Toast.LENGTH_LONG).show();

                                }
                            }) ;



                }

            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.add_item) {
            // Handle the camera action
        } else if (id == R.id.filter_item) {

            startActivity(new Intent(this, HomeCartExtraItemsList.class));

        } else if (id == R.id.orders) {

            startActivity(new Intent(this, OrdersActivity.class));

        } else if (id == R.id.add_subcategory) {

            startActivity(new Intent(this, AddSubcategoryActivity.class));

        } else if (id == R.id.grovenco_wallet) {

            startActivity(new Intent(this, UpdateGrovencoWallet.class));

        }else if (id == R.id.sign_out){

            auth.signOut() ;
            Intent intent = new Intent(this, LoginActivity.class) ;
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }else if (id == R.id.coupons){

            startActivity(new Intent(this, CouponsActivity.class));

        }else if (id == R.id.search_order){

            startActivity(new Intent(this, SearchOrderActivity.class));

        }else if (id == R.id.stock_alert){

            startActivity(new Intent(this, StockAlertActivity.class));

        }else if (id == R.id.report){

            startActivity(new Intent(this, ReportActivity.class));

        }else if (id == R.id.vouchers){

            startActivity(new Intent(this, VouchersActivity.class));

        }else if (id == R.id.search_item){

            startActivity(new Intent(this, SearchItemActivity.class));

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
