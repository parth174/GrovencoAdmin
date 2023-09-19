package com.grovenco.grovencoadmin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class PendingOrdersFragment extends Fragment {

    RecyclerView rv_deliveredOrders ;
    RecyclerView.LayoutManager layoutManagerDeliveredOrders ;

    ArrayList<Orders> deliveredOrdersList ;
    MyOrdersAdapter deliveredOrdersAdapter ;
    Context context ;

    public PendingOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_orders, container, false) ;

        rv_deliveredOrders = view.findViewById(R.id.rv_orders) ;




        layoutManagerDeliveredOrders = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManagerDeliveredOrders).setOrientation(RecyclerView.VERTICAL);

        rv_deliveredOrders.setLayoutManager(layoutManagerDeliveredOrders) ;

        deliveredOrdersList = new ArrayList<>() ;

        deliveredOrdersAdapter = new MyOrdersAdapter(context, deliveredOrdersList) ;

        rv_deliveredOrders.setAdapter(deliveredOrdersAdapter);

        getData() ;

        return view ;

    }

    private void getData() {

        FirebaseUtil.orders_ref.whereEqualTo("order_status_group","pending").orderBy("order_date_year", Query.Direction.DESCENDING).orderBy("order_time_formatted", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult() != null){

                        if(!task.getResult().isEmpty()){

                            int size = task.getResult().size() ;
                            int count = 0 ;

                            Log.d("ResultSize", size+"") ;

                         /*   for(DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()){

                                DocumentSnapshot document = documentChange.getDocument() ;

                                double cartValue = (double) document.get("cart_value") ;
                                String orderDate = document.get("order_date").toString() ;
                                String orderTime = document.get("order_time").toString() ;
                                String orderId = document.get("order_id").toString() ;
                                String orderStatus = document.get("order_status").toString() ;
                                String name = document.get("name").toString() ;
                                String phone_number = document.get("phone_number").toString() ;
                                boolean seen_admin = (boolean) document.get("seen_admin") ;


                                if (documentChange.getType() == DocumentChange.Type.ADDED) {

                                    Log.d("insidePending", "InsideAdded") ;

                                    Orders orders = new Orders(cartValue, orderDate, orderTime, orderId, orderStatus, name, phone_number, seen_admin);

                                    deliveredOrdersList.add(orders);

                                    count = count + 1;

                                    checkApiCallEnded(count, size);

                                }else if(documentChange.getType() == DocumentChange.Type.REMOVED){

                                    Log.d("insidePending", "InsideRemoved") ;

                                    for(int i=0 ; i<deliveredOrdersList.size() ; i++){

                                        if(deliveredOrdersList.get(i).getOrder_id().equals(orderId)){

                                            deliveredOrdersList.remove(i) ;
                                            deliveredOrdersAdapter.notifyDataSetChanged();

                                        }


                                    }

                                }else if(documentChange.getType() == DocumentChange.Type.MODIFIED){

                                    Log.d("insidePending", "InsideModified") ;

                                }

                            }
*/
                        for(DocumentSnapshot document : task.getResult().getDocuments()){

                            double cartValue = document.getDouble("cart_value") ;
                            String orderDate = document.get("order_date").toString() ;
                            String orderTime = document.get("order_time").toString() ;
                            String orderId = document.get("order_id").toString() ;
                            String orderStatus = document.get("order_status").toString() ;
                            String name = document.get("name").toString() ;
                            String phone_number = document.get("phone_number").toString() ;
                            boolean seen_admin = (boolean) document.get("seen_admin") ;


                                Orders orders = new Orders(cartValue, orderDate, orderTime, orderId, orderStatus, name, phone_number, seen_admin);

                                deliveredOrdersList.add(orders);

                                count = count + 1;

                                checkApiCallEnded(count, size);



                        }

                        }

                    }

                }

            }
        }) ;

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context ;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    void checkApiCallEnded(int count, int size) {

        if(count == size){

            deliveredOrdersAdapter.notifyDataSetChanged();
        }

    }

}
