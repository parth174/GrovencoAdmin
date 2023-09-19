package com.grovenco.grovencoadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class VouchersActivity extends AppCompatActivity {

    RecyclerView rv_vouchers ;
    RecyclerView.LayoutManager layoutManagerVouchers ;
    RelativeLayout rl_vouchers ;
    ArrayList<Vouchers> vouchersList;
    ArrayList<Long> qtyList ;
    VouchersAdapter vouchersAdapter ;
    ProgressBar progress ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchers);

        rl_vouchers = findViewById(R.id.rl_vouchers) ;
        rv_vouchers = findViewById(R.id.rv_vouchers) ;
        progress = findViewById(R.id.progress) ;

        layoutManagerVouchers = new LinearLayoutManager(this) ;
        ((LinearLayoutManager) layoutManagerVouchers).setOrientation(RecyclerView.VERTICAL) ;

        rv_vouchers.setLayoutManager(layoutManagerVouchers) ;

        vouchersList = new ArrayList<>() ;
        qtyList = new ArrayList<>() ;

        FirebaseUtil.vouchers_ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                long voucher_minOrder = (long) document.get("voucher_min_order") ;
                                String title = document.get("title").toString() ;
                                String voucher_id = document.get("voucher_id").toString() ;
                                long qty =  document.getLong("qty") ;

                                long minOrder = -1 ;

                                boolean enabled = false ;

                                String code = "" ;

                                if(document.contains("code")){

                                    code = document.get("code").toString() ;

                                }

                                if(document.contains("enabled")){

                                    enabled = document.getBoolean("enabled") ;

                                }

                                if(document.contains("enable")){

                                   enabled = document.getBoolean("enable") ;

                                }

                                if(document.contains("min_order")){

                                    minOrder = document.getLong("min_order") ;

                                }

                                qtyList.add(qty) ;

                                switch (type){

                                    case "1" :

                                        long maxDiscount = (long) document.get("max_discount") ;
                                        long offer = (long) document.get("offer") ;

                                        Vouchers voucher1 = new Vouchers(code,details,title,maxDiscount, minOrder, voucher_minOrder,offer,type,enabled, voucher_id) ;
                                        vouchersList.add(voucher1);

                                        break ;

                                    case "2" :

                                        String free_item_code = document.get("item_code").toString() ;

                                        Vouchers voucher2 = new Vouchers(code,details,title, minOrder,voucher_minOrder,type,enabled, voucher_id) ;
                                        vouchersList.add(voucher2);

                                        break ;

                                    case "3" :

                                        Vouchers voucher3 = new Vouchers(code,details,title, minOrder,voucher_minOrder,type,enabled, voucher_id) ;
                                        vouchersList.add(voucher3);
                                        break ;

                                    case "4" :

                                        double discount = 0 ;

                                        if(document.contains("discount")){

                                            discount = document.getDouble("discount") ;

                                        }else if(document.contains("voucher_amount")){

                                            discount = document.getDouble("voucher_amount") ;

                                        }

                                        Vouchers voucher4 = new Vouchers(code,details,title, minOrder,voucher_minOrder,type,discount,enabled, voucher_id) ;
                                        vouchersList.add(voucher4);

                                        break ;

                                    case "5" :


                                        long maxDiscount1 = (long) document.get("max_discount") ;
                                        long offer1 = (long) document.get("offer") ;

                                        Vouchers voucher5 = new Vouchers(code,details,title,maxDiscount1, minOrder,voucher_minOrder,offer1,type,enabled, voucher_id) ;
                                        vouchersList.add(voucher5);

                                        break ;

                                }


                                count ++ ;

                                if(count == size){

                                    vouchersAdapter = new VouchersAdapter(VouchersActivity.this, vouchersList,qtyList) ;
                                    rv_vouchers.setAdapter(vouchersAdapter);

                                    Utils.setVisibilityGone(progress);

                                }

                            }
                        }else{

                            Utils.setVisibilityGone(rl_vouchers);
                            Utils.setVisibilityGone(rv_vouchers);
                            Utils.setVisibilityGone(progress);

                        }
                    }else{

                        Utils.setVisibilityGone(progress);

                        if(task.getException() != null){

                            Toast.makeText(VouchersActivity.this, "Oops!! Something went wrong", Toast.LENGTH_SHORT).show();
                            Log.d("FirebaseErrorCoupons", task.getException().getMessage()) ;

                        }


                    }

                }else{

                    Utils.setVisibilityGone(progress) ;

                    if(task.getException() != null){

                        if(task.getException() instanceof FirebaseNetworkException){

                            Toast.makeText(VouchersActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();

                        }else{

                            Toast.makeText(VouchersActivity.this, "Oops!! Something went wrong", Toast.LENGTH_SHORT).show();

                        }


                        Log.d("FirebaseErrorCoupons", task.getException().getMessage()) ;

                    }

                }

            }
        }) ;

    }
}