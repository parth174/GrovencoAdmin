package com.grovenco.grovencoadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

public class OrdersActivity extends AppCompatActivity {

    TabLayout tabLayout ;
    ViewPager viewPager ;
    private String[] tabTitles = new String[]{"PENDING ", "DELIVERED ", "REFUND ", "CANCELLED "};
    MyFragmentAdapter adapter ;
    ListenerRegistration ordersListener ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);

        tabLayout.setTabTextColors(R.color.black,R.color.grey );

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        adapter = new MyFragmentAdapter(OrdersActivity.this,getSupportFragmentManager(), tabLayout.getTabCount(),tabTitles);

        tabLayout.setupWithViewPager(viewPager);


        setUpTabs() ;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ordersListener.remove();

    }

    private void setUpTabs() {

        ordersListener =

        FirebaseUtil.orders_ref.whereEqualTo("seen_admin", false).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(queryDocumentSnapshots != null){

                    if(!queryDocumentSnapshots.isEmpty()){

                        int size = queryDocumentSnapshots.size() ;
                        int count = 0 ;
                        int count_delivered = 0 ;
                        int count_pending = 0 ;
                        int count_cancelled = 0 ;
                        int count_refund = 0 ;

                        for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){

                            Log.d("data", document.toString()) ;

                            switch(document.get("order_status_group").toString()){

                                case "delivered" :

                                    count_delivered++ ;

                                    break ;

                                case "refund" :

                                    count_refund++ ;

                                    break ;

                                case "pending" :

                                    count_pending++ ;

                                    break ;

                                case "cancelled" :

                                    count_cancelled++ ;

                                    break ;

                            }

                            count++ ;

                            if(count == size){

                                Log.d("Count+Size"," c"+ count + " s"+ size +" change size" + queryDocumentSnapshots.getDocumentChanges().size()) ;

                                //Utils.endRefrishing(swipe);

                                if(count_pending != 0){

                                    tabTitles[0] = "PENDING\n"+ count_pending ;

                                }else{

                                    tabTitles[0] = "PENDING " ;

                                }

                                if(count_delivered != 0){

                                    tabTitles[1] = "DELIVERED\n"+ count_delivered ;

                                }else{

                                    tabTitles[1] = "DELIVERED" ;

                                }

                                if(count_refund != 0){

                                    tabTitles[2] = "REFUND\n"+ count_refund;

                                }else{

                                    tabTitles[2] = "REFUND " ;

                                }
                                if(count_cancelled != 0){

                                    tabTitles[3] = "CANCELLED\n"+ count_cancelled;

                                }else{

                                    tabTitles[3] = "CANCELLED" ;

                                }

                                int position = tabLayout.getSelectedTabPosition() ;

                                viewPager.setAdapter(adapter) ;
                                viewPager.setCurrentItem(position);

                            }

                        }

                    }else{

                        viewPager.setAdapter(adapter) ;

                    }

                }

            }

        }) ;

    }

}
