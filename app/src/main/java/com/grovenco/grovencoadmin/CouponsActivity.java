package com.grovenco.grovencoadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CouponsActivity extends AppCompatActivity {

    RecyclerView rv_coupons ;
    RecyclerView.LayoutManager layoutManagerCoupons ;
    RelativeLayout rl_coupons ;
    ArrayList<Coupons> couponsList ;
    CouponsAdapter couponsAdapter ;
    ProgressBar progress ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);

        rl_coupons = findViewById(R.id.rl_coupons) ;
        rv_coupons = findViewById(R.id.rv_coupons) ;
        progress = findViewById(R.id.progress) ;

        layoutManagerCoupons = new LinearLayoutManager(this) ;
        ((LinearLayoutManager) layoutManagerCoupons).setOrientation(RecyclerView.VERTICAL) ;

        rv_coupons.setLayoutManager(layoutManagerCoupons) ;

        couponsList = new ArrayList<>() ;

        FirebaseUtil.coupons_ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult() != null){

                        if(!task.getResult().isEmpty()){

                            Utils.setVisibilityVisible(progress) ;

                            int size = task.getResult().size() ;
                            int count = 0 ;

                            for(DocumentSnapshot document : task.getResult().getDocuments()) {

                                String type =  document.get("type").toString() ;
                                String details = document.get("details").toString() ;
                                long minOrder = (long) document.get("min_order") ;
                                String title = document.get("title").toString() ;
                                String code = document.get("code").toString() ;
                                boolean enabled = document.getBoolean("enabled") ;
                                boolean visible = document.getBoolean("visible") ;
                                String coupon_id = document.getString("coupon_id") ;


                                switch (type){

                                    case "1" :

                                        long maxDiscount = (long) document.get("max_discount") ;
                                        long offer = (long) document.get("offer") ;

                                        Coupons coupon_1 = new Coupons(code,details,title,maxDiscount,minOrder,offer,type,enabled,visible, coupon_id) ;
                                        couponsList.add(coupon_1);

                                        break ;

                                    case "2" :

                                        String free_item_code = document.get("item_code").toString() ;

                                        Coupons coupon_2 = new Coupons(code,details,title,minOrder,type,enabled,visible, coupon_id) ;
                                        couponsList.add(coupon_2);

                                        break ;

                                    case "3" :

                                        Coupons coupon_3 = new Coupons(code,details,title,minOrder,type,enabled,visible,coupon_id) ;
                                        couponsList.add(coupon_3);
                                        break ;

                                    case "4" :

                                        double discount = document.getDouble("discount") ;
                                        Coupons coupon_4 = new Coupons(code,details,title,minOrder,type,discount,enabled,visible, coupon_id) ;
                                        couponsList.add(coupon_4);

                                        break ;

                                    case "5" :


                                        long maxDiscount1 = (long) document.get("max_discount") ;
                                        long offer1 = (long) document.get("offer") ;

                                        Coupons coupon_5 = new Coupons(code,details,title,maxDiscount1,minOrder,offer1,type,enabled,visible, coupon_id) ;
                                        couponsList.add(coupon_5);

                                        break ;

                                }


                                count ++ ;

                                if(count == size){

                                    couponsAdapter = new CouponsAdapter(CouponsActivity.this, couponsList) ;
                                    rv_coupons.setAdapter(couponsAdapter);

                                    Utils.setVisibilityGone(progress);

                                }

                            }
                        }else{

                            Utils.setVisibilityGone(rl_coupons);
                            Utils.setVisibilityGone(rv_coupons);
                            Utils.setVisibilityGone(progress);

                        }
                    }else{

                        Utils.setVisibilityGone(progress);

                        if(task.getException() != null){

                            Toast.makeText(CouponsActivity.this, "Oops!! Something went wrong", Toast.LENGTH_SHORT).show();
                            Log.d("FirebaseErrorCoupons", task.getException().getMessage()) ;

                        }


                    }

                }else{

                    Utils.setVisibilityGone(progress) ;

                    if(task.getException() != null){

                        if(task.getException() instanceof FirebaseNetworkException){

                            Toast.makeText(CouponsActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();

                        }else{

                            Toast.makeText(CouponsActivity.this, "Oops!! Something went wrong", Toast.LENGTH_SHORT).show();

                        }


                        Log.d("FirebaseErrorCoupons", task.getException().getMessage()) ;

                    }

                }

            }
        }) ;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_coupon, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.add_coupon){

            startActivity(new Intent(CouponsActivity.this, AddCouponActivity.class));
            return true ;

        }

        return super.onOptionsItemSelected(item);
    }
}