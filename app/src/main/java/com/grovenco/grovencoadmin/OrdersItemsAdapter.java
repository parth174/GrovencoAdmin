package com.grovenco.grovencoadmin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OrdersItemsAdapter extends RecyclerView.Adapter<OrdersItemsAdapter.ViewHolder> {

    Context context ;
    ArrayList<OrderItems> orderItemsList ;
    ArrayList<RefundItemQty> selectedItemCodeList = new ArrayList<>() ;
    String orderId ;
    OrderDetails current_activity ;
    String current_uid ;
    long isMember ;

    public OrdersItemsAdapter(Context context, ArrayList<OrderItems> orderItemsList, String orderId, String current_uid) {
        this.context = context;
        this.orderItemsList = orderItemsList;
        this.orderId = orderId;
        this.current_uid = current_uid ;
        current_activity = (OrderDetails) context ;

        FirebaseUtil.getOrdersRef(orderId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult() != null){

                        if(task.getResult().exists()){

                            FirebaseUtil.getUserRef(task.getResult().get("uid").toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if(task.isSuccessful()){

                                        if(task.getResult() != null){

                                            if(task.getResult().exists()){

                                                isMember = (long) task.getResult().get("membership_plan") ;

                                            }

                                        }

                                    }

                                }
                            }) ;



                        }

                    }

                }

            }
        }) ;

    }

    public OrdersItemsAdapter(Context context, ArrayList<OrderItems> orderItemsList) {

        this.orderItemsList = orderItemsList ;
        this.context = context ;

    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_grovencoPrice, tv_mrp, tv_discount, tv_itemName, tv_itemPrice, tv_itemWeight, btn_plus, btn_minus,tv_qty, tv_cancel_admin ;
        ImageView img_item ;
        RelativeLayout rl_orderItem, rl_changeQty ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_grovencoPrice = itemView.findViewById(R.id.tv_grovenco_price) ;
            tv_mrp = itemView.findViewById(R.id.tv_mrp) ;
            tv_discount = itemView.findViewById(R.id.tv_discount) ;
            tv_itemName = itemView.findViewById(R.id.tv_item_name) ;
            tv_itemPrice = itemView.findViewById(R.id.tv_item_price) ;
            tv_itemWeight = itemView.findViewById(R.id.tv_item_weight) ;
            img_item = itemView.findViewById(R.id.img_item) ;
            rl_orderItem = itemView.findViewById(R.id.rl_order_item) ;
            tv_qty = itemView.findViewById(R.id.tv_qty) ;
            btn_minus = itemView.findViewById(R.id.btn_minus) ;
            btn_plus = itemView.findViewById(R.id.btn_plus) ;
            rl_changeQty = itemView.findViewById(R.id.rl_change_item_qty) ;
            tv_cancel_admin = itemView.findViewById(R.id.tv_cancel_admin) ;

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrdersItemsAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_order_items,parent,false)) ;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        long item_price = orderItemsList.get(position).getItemPrice() ;
        final long item_qty = orderItemsList.get(position).getItemQty() ;
        long item_weight = orderItemsList.get(position).getItemWeight() ;
        long item_mrp = orderItemsList.get(position).getMrp() ;
        String item_weight_unit = orderItemsList.get(position).getItemWeightUnit() ;
        String item_name = orderItemsList.get(position).getItemName() ;
        final Uri item_image_uri = orderItemsList.get(position).getImageFront() ;
        final String item_code = orderItemsList.get(position).getItemCode() ;
        String item_status = orderItemsList.get(position).getItem_status() ;
        long cancel_qty = orderItemsList.get(position).getCancel_qty() ;



        holder.tv_itemWeight.setText(item_weight + item_weight_unit) ;
        holder.tv_itemPrice.setText(Utils.getAmount(item_price)+ " X " + item_qty) ;
        holder.tv_itemName.setText(item_name) ;
        holder.tv_grovencoPrice.setText(Utils.getAmount(item_price*item_qty)) ;


        Glide.with(context).load(item_image_uri).into(holder.img_item) ;

        holder.tv_discount.setTextColor(context.getResources().getColor(R.color.white));
        holder.tv_discount.setBackgroundColor(context.getResources().getColor(R.color.green) );

        if(item_price >= item_mrp){

            Utils.setVisibilityGone(holder.tv_mrp);
            Utils.setVisibilityGone(holder.tv_discount);

        }else{

            holder.tv_mrp.setText(Utils.getAmount(item_mrp * item_qty)) ;
            holder.tv_mrp.setPaintFlags(holder.tv_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_discount.setText(Utils.getAmount((item_mrp*item_qty)-(item_price*item_qty)) + " OFF") ;

        }



        /*if(item_status.equals("processing_refund") || item_status.equals("refunded") || item_status.equals("cancelled") || item_status.equals("refund_declined") || item_status.equals("cancelled_admin")){

            holder.tv_discount.setBackgroundColor(context.getResources().getColor(R.color.quantum_googred));

            switch (item_status){

                case "processing_refund" :

                    holder.tv_discount.setText("refund : " + orderItemsList.get(position).getRefund_qty() );

                    break ;

                case "refunded" :

                    holder.tv_discount.setBackgroundColor(context.getResources().getColor(R.color.green));
                    holder.tv_discount.setText("refunded : " + orderItemsList.get(position).getRefund_qty() );

                    break ;

                case "cancelled" :

                    holder.tv_discount.setText("cancelled" );

                    break ;

                case "refund_declined" :

                    holder.tv_discount.setBackgroundColor(context.getResources().getColor(R.color.quantum_googred)) ;
                    holder.tv_discount.setText("refund declined : " + orderItemsList.get(position).getRefund_qty() ) ;
                    break ;

                case "cancelled_admin" :

                    Utils.setVisibilityVisible(holder.tv_mrp);
                    Utils.setVisibilityVisible(holder.tv_discount);

                    if(item_qty > 0){

                        holder.tv_discount.setTextColor(context.getResources().getColor(R.color.quantum_googred));
                        holder.tv_discount.setBackgroundColor(context.getResources().getColor(R.color.white) );
                        Utils.setVisibilityVisible(holder.tv_cancel_admin);

                        holder.tv_discount.setText("partial cancel : " + cancel_qty );
                        holder.tv_cancel_admin.setText("updated qty : "+ item_qty);

                    }else{

                        holder.tv_discount.setText("stock out" );

                    }



                    break ;


            }


        }
*/

        if(item_status.equals("processing_refund") || item_status.equals("refunded") || item_status.equals("cancelled") || item_status.equals("refund_declined") || item_status.equals("cancelled_admin")){

            switch (item_status){

                case "refund_declined" :

                    Utils.setVisibilityVisible(holder.tv_discount);

                    holder.tv_discount.setBackgroundColor(context.getResources().getColor(R.color.quantum_googred));
                    holder.tv_discount.setText("refund declined : " + orderItemsList.get(position).getRefund_qty() );
                    break ;

                case "processing_refund" :


                    Utils.setVisibilityVisible(holder.tv_discount);
                    holder.tv_discount.setBackgroundColor(context.getResources().getColor(R.color.quantum_googred));
                    holder.tv_discount.setText("refund : " + orderItemsList.get(position).getRefund_qty() );

                    break ;

                case "refunded" :

                    Utils.setVisibilityVisible(holder.tv_discount);

                    holder.tv_discount.setBackgroundColor(context.getResources().getColor(R.color.green));
                    holder.tv_discount.setText("refunded : " + orderItemsList.get(position).getRefund_qty() );

                    break ;

                case "cancelled" :

                    Utils.setVisibilityVisible(holder.tv_discount);
                    holder.tv_discount.setBackgroundColor(context.getResources().getColor(R.color.quantum_googred));
                    holder.tv_discount.setText("cancelled" );

                    break ;

                case "cancelled_admin" :

                    Utils.setVisibilityVisible(holder.tv_discount);
                    holder.tv_discount.setBackgroundColor(context.getResources().getColor(R.color.quantum_googred));

                    if(item_qty  > 0){

                        Utils.setVisibilityVisible(holder.tv_cancel_admin);

                        holder.tv_discount.setTextColor(context.getResources().getColor(R.color.quantum_googred));
                        holder.tv_discount.setBackgroundColor(context.getResources().getColor(R.color.white) );

                        holder.tv_discount.setText("partial cancel : " + cancel_qty );
                        holder.tv_cancel_admin.setText("updated qty : "+ item_qty);

                    }else{

                        if(item_price == item_mrp){

                            Utils.setVisibilityGone(holder.tv_mrp);

                        }

                        holder.tv_grovencoPrice.setText(Utils.getAmount(orderItemsList.get(position).getItemPrice()));
                        holder.tv_mrp.setText(Utils.getAmount(orderItemsList.get(position).getMrp()));
                        holder.tv_discount.setText("stock out" );

                    }



                    break ;

            }


        }


        holder.rl_orderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( ((ColorDrawable) holder.rl_orderItem.getBackground()).getColor() == context.getResources().getColor(R.color.quantum_grey200)){

                    holder.rl_orderItem.setBackgroundColor(context.getResources().getColor(R.color.white));
                    Glide.with(context).load(item_image_uri).into(holder.img_item) ;

                    for(int i=0 ; i<selectedItemCodeList.size() ; i++){

                        if(selectedItemCodeList.get(i).getItemcode().equals(orderItemsList.get(position).getItemCode())){

                            selectedItemCodeList.remove(selectedItemCodeList.get(i)) ;

                            Utils.setVisibilityGone(holder.rl_changeQty) ;

                            holder.tv_qty.setText("0") ;

                        }

                    }

                }else{

                    holder.img_item.setImageResource(R.drawable.tick);
                    holder.rl_orderItem.setBackgroundColor(context.getResources().getColor(R.color.quantum_grey200)) ;

                    RefundItemQty object = new RefundItemQty(orderItemsList.get(position).getItemCode(), 1, orderItemsList.get(position).getItemPrice()) ;

                    selectedItemCodeList.add(object) ;

                    Utils.setVisibilityVisible(holder.rl_changeQty) ;

                    for(int i=0 ; i<selectedItemCodeList.size() ; i++){

                        Log.d("selectedItems", selectedItemCodeList.get(i).getItemcode() + "  " + selectedItemCodeList.get(i).getQty() ) ;

                    }

                    holder.tv_qty.setText("1") ;

                }



            }
        });


        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long current_qty = Long.valueOf(holder.tv_qty.getText().toString()) ;

                if(current_qty < item_qty){

                    holder.tv_qty.setText(String.valueOf(current_qty+1)) ;

                    for(int i=0 ; i<selectedItemCodeList.size() ; i++){

                        if(selectedItemCodeList.get(i).getItemcode().equals(orderItemsList.get(position).getItemCode())){

                            selectedItemCodeList.set(i,new RefundItemQty(orderItemsList.get(position).getItemCode(),current_qty+1, orderItemsList.get(position).getItemPrice())) ;

                            holder.tv_qty.setText(String.valueOf(current_qty+1)) ;

                            Log.d("selectedItems", selectedItemCodeList.get(i).getItemcode() + "  " + selectedItemCodeList.get(i).getQty() ) ;

                        }

                    }


                }else{

                    Toast.makeText(context, "Can't go above this", Toast.LENGTH_SHORT).show();

                }

            }
        });

        holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long current_qty = Long.valueOf(holder.tv_qty.getText().toString()) ;

                if(current_qty > 1 ){

                    holder.tv_qty.setText(String.valueOf(current_qty-1)) ;

                    for(int i=0 ; i<selectedItemCodeList.size() ; i++){

                        if(selectedItemCodeList.get(i).getItemcode().equals(orderItemsList.get(position).getItemCode())){

                            selectedItemCodeList.set(i,new RefundItemQty(orderItemsList.get(position).getItemCode(),current_qty-1, orderItemsList.get(position).getItemPrice())) ;

                            holder.tv_qty.setText(String.valueOf(current_qty-1)) ;

                            Log.d("selectedItems", selectedItemCodeList.get(i).getItemcode() + "  " + selectedItemCodeList.get(i).getQty() ) ;

                        }

                    }

                }else if(current_qty == 1){

                    holder.rl_orderItem.setBackgroundColor(context.getResources().getColor(R.color.white));
                    Glide.with(context).load(item_image_uri).into(holder.img_item) ;

                    for(int i=0 ; i<selectedItemCodeList.size() ; i++){

                        if(selectedItemCodeList.get(i).getItemcode().equals(orderItemsList.get(position).getItemCode())){

                            selectedItemCodeList.remove(selectedItemCodeList.get(i)) ;

                            Utils.setVisibilityGone(holder.rl_changeQty) ;

                            holder.tv_qty.setText("0") ;

                        }

                    }

                }else{

                    Toast.makeText(context, "Can't go below!!", Toast.LENGTH_SHORT).show();

                }

            }
        });

        current_activity.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedItemCodeList.size()>0){

                    Utils.setVisibilityGone(current_activity.ll_action);

                    Utils.setVisibilityGone(current_activity.rl_parent);
                    Utils.setVisibilityVisible(current_activity.progress);

                    FirebaseUtil.getOrdersItemsRef(orderId).whereEqualTo("item_status", "processing_refund").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.getResult() != null) {

                                if (!task.getResult().isEmpty()) {

                                    final double[] refund_items_amount = {0};
                                    final int[] count = {0} ;

                                    for (DocumentSnapshot document : task.getResult().getDocuments()) {

                                        final int size = task.getResult().size();
                                        final int[] inner_count = {0};

                                        final String itemcode = document.get("item_code").toString();

                                        Log.d("SelectedSize", ""+selectedItemCodeList.size()) ;

                                        for (int i = 0; i < selectedItemCodeList.size(); i++) {


                                            Log.d("selectedList", selectedItemCodeList.get(i).getItemcode() + " " + selectedItemCodeList.get(i).getQty());

                                            if (itemcode.equals(selectedItemCodeList.get(i).getItemcode())) {


                                                refund_items_amount[0] = refund_items_amount[0] + (selectedItemCodeList.get(i).getItem_price() * selectedItemCodeList.get(i).getQty());

                                                FirebaseUtil.items_ref.document(itemcode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                        if(task.getResult() != null){

                                                            if(task.getResult().exists()){

                                                                long stock = (long) task.getResult().get("stock") ;

                                                                FirebaseUtil.items_ref.document(itemcode).update("stock", stock+1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                    }
                                                                }) ;
                                                            }
                                                        }
                                                    }
                                                }) ;

                                                FirebaseUtil.getOrdersItemsRef(orderId).document(itemcode).update("item_status", "refunded", "refund_qty", selectedItemCodeList.get(i).getQty()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        count[0]++ ;

                                                        if (count[0] == size) {

                                                            if (refund_items_amount[0] > current_activity.coupon_discount + 100) {

                                                                if (current_activity.coupon_discount > 0) {

                                                                    refund_items_amount[0] = refund_items_amount[0] - current_activity.coupon_discount;

                                                                }

                                                            }

                                                            FirebaseUtil.getUserRef(current_uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                    if (task.getResult() != null) {

                                                                        if (task.getResult().exists()) {

                                                                            double current_refund_amount =  task.getResult().getDouble("grovenco_wallet");
                                                                            double total_spent =  task.getResult().getDouble("total_spent") ;

                                                                            FirebaseUtil.getUserRef(current_uid).update("grovenco_wallet", refund_items_amount[0] + current_refund_amount, "total_spent", total_spent - refund_items_amount[0]).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    Log.d("Refunded amount", "Refunded " + refund_items_amount[0]);

                                                                                }
                                                                            });

                                                                        }

                                                                    }

                                                                }
                                                            });



                                                            Map<String,Object> hashmap = new HashMap<>() ;
                                                            hashmap.put("refund_amount", refund_items_amount[0]) ;
                                                            hashmap.put("seen_admin",false);
                                                            hashmap.put("refund_date", getDate(Calendar.getInstance().get(Calendar.DATE))+" " + getMonth(Calendar.getInstance().get(Calendar.MONTH)) + " " + Calendar.getInstance().get(Calendar.YEAR) ) ;
                                                            hashmap.put("order_status", "refunded") ;
                                                            hashmap.put("order_status_group", "refund") ;

                                                            FirebaseUtil.getOrdersRef(orderId).set(hashmap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                }
                                                            }) ;


                                                        }

                                                    }
                                                });

                                                break;

                                            } else {

                                                inner_count[0]++;

                                                if (inner_count[0] == selectedItemCodeList.size()) {

                                                    count[0]++ ;

                                                    FirebaseUtil.getOrdersItemsRef(orderId).document(itemcode).update("item_status", "refund_declined").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {



                                                        }
                                                    }) ;


                                                }

                                                if (count[0] == size) {


                                                    if (refund_items_amount[0] > current_activity.coupon_discount + 100) {

                                                        if (current_activity.coupon_discount > 0) {

                                                            refund_items_amount[0] = refund_items_amount[0] - current_activity.coupon_discount;

                                                        }

                                                    }

                                                    FirebaseUtil.getUserRef(current_uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                            if (task.getResult() != null) {

                                                                if (task.getResult().exists()) {

                                                                    double current_refund_amount =  task.getResult().getDouble("grovenco_wallet");
                                                                    double total_spent =  task.getResult().getDouble("total_spent") ;

                                                                    FirebaseUtil.getUserRef(current_uid).update("grovenco_wallet", refund_items_amount[0] + current_refund_amount, "total_spent", total_spent - refund_items_amount[0]).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            Log.d("Refunded amount", "Refunded " + refund_items_amount[0]);

                                                                        }
                                                                    });

                                                                }

                                                            }

                                                        }
                                                    });

                                                    Map<String,Object> hashmap = new HashMap<>() ;
                                                    hashmap.put("refund_amount", refund_items_amount[0]) ;
                                                    hashmap.put("seen_admin",false);
                                                    hashmap.put("refund_date", getDate(Calendar.getInstance().get(Calendar.DATE))+" " + getMonth(Calendar.getInstance().get(Calendar.MONTH)) + " " + Calendar.getInstance().get(Calendar.YEAR) ) ;
                                                    hashmap.put("order_status", "refunded") ;
                                                    hashmap.put("order_status_group", "refund") ;

                                                    FirebaseUtil.getOrdersRef(orderId).set(hashmap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                        }
                                                    }) ;
                                                }
                                            }

                                        }

                                    }

                                }

                            }

                        }



                    });
                }else{

                    Toast.makeText(context, "Please select the items from the list", Toast.LENGTH_SHORT).show();

                }
            }
        });

        current_activity.btn_declineComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Utils.getInternetConnectivity(context)){

                    AlertDialog.Builder dialog = new AlertDialog.Builder(context) ;
                    dialog.setTitle("Cancel Order") ;
                    dialog.setMessage("Are you sure you want to cancel the order?") ;
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Utils.setVisibilityGone(current_activity.ll_action);
                            Utils.setVisibilityGone(current_activity.rl_parent);
                            Utils.setVisibilityVisible(current_activity.progress);

                            FirebaseUtil.getOrdersRef(orderId).update("order_status", "cancelled_admin","order_status_group", "cancelled", "seen_admin", false, "coupon_type", FieldValue.delete(), "cashback", FieldValue.delete() ) ;

                            FirebaseUtil.getOrdersRef(orderId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if(task.isSuccessful()){

                                        if(task.getResult() != null){

                                            if(task.getResult().exists()){

                                                final DocumentSnapshot documentSnapshot = task.getResult() ;

                                                final double total_amount = documentSnapshot.getDouble("cart_value") ;

                                                FirebaseUtil.getUserRef(current_uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                        if(task.isSuccessful()){

                                                            if(task.getResult() != null){

                                                                if(task.getResult().exists()){

                                                                    double total_spent = task.getResult().getDouble("total_spent") ;

                                                                    Map<String,Object>  hashmap = new HashMap<>() ;
                                                                    hashmap.put("total_spent", total_spent - total_amount) ;

                                                                    FirebaseUtil.getUserRef(current_uid).update(hashmap) ;

                                                                }

                                                            }

                                                        }

                                                    }
                                                }) ;


                                            }

                                        }

                                    }



                                }
                            }) ;



                            FirebaseUtil.getOrdersItemsRef(orderId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if(task.isSuccessful()){

                                        if(task.getResult() != null){

                                            if(!task.getResult().isEmpty()){

                                                final int[] count = {0};
                                                final int size = task.getResult().size() ;

                                                for(final DocumentSnapshot document : task.getResult().getDocuments()){


                                                    final String itemcode = document.get("item_code").toString() ;


                                                    FirebaseUtil.items_ref.document(itemcode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                            if(task.isSuccessful()){

                                                                if(task.getResult() != null){

                                                                    if(task.getResult().exists()){

                                                                        long stock = (long) task.getResult().get("stock") ;
                                                                        String itemcode = task.getResult().get("item_code").toString() ;

                                                                        if(itemcode.equals("1")){

                                                                            FirebaseUtil.getUserRef(current_uid).update("membership_plan",0) ;

                                                                            Map<String,Object> hashmap = new HashMap<>() ;
                                                                            hashmap.put("membership_year", FieldValue.delete()) ;
                                                                            hashmap.put("membership_date_year", FieldValue.delete()) ;

                                                                            FirebaseUtil.getUserRef(current_uid).update(hashmap) ;

                                                                            FirebaseUtil.getUserVouchersRef(current_uid).document("MEMBERDEL").delete() ;

                                                                        }

                                                                        FirebaseUtil.items_ref.document(itemcode).update("stock", stock+1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                count[0] = count[0] +1 ;

                                                                                if(count[0] == size){

                                                                                    Utils.setVisibilityVisible(current_activity.rl_parent);
                                                                                    Utils.setVisibilityGone(current_activity.progress);

                                                                                    Toast.makeText(context, "The order has been cancelled", Toast.LENGTH_SHORT).show();


                                                                                }

                                                                            }
                                                                        }) ;
                                                                    }
                                                                }

                                                            }
                                                        }
                                                    }) ;
                                                }
                                            }
                                        }

                                    }

                                }
                            }) ;

                        }
                    }) ;
                    dialog.setNegativeButton("No", null) ;
                    dialog.show() ;

                }else{

                    Toast.makeText(context, "Please connect to internet", Toast.LENGTH_SHORT).show();

                }



            }
        });

        current_activity.btn_declinePlaced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedItemCodeList.size()>0){

                    Utils.setVisibilityGone(current_activity.ll_action);
                    Utils.setVisibilityGone(current_activity.rl_parent);
                    Utils.setVisibilityVisible(current_activity.progress);

                    FirebaseUtil.getOrdersItemsRef(orderId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.getResult() != null) {

                                if (!task.getResult().isEmpty()) {

                                    final double[] cancel_items_amount = {0};
                                    final int[] count = {0} ;
                                    final long[] total_mrp = {0};
                                    final long[] total_grovencoDiscount = {0};
                                    final long[] total_memberDiscount = {0};
                                    final double[] total_savings = {0};


                                    for (final DocumentSnapshot itemDocument : task.getResult().getDocuments()) {

                                        final int size = task.getResult().size();
                                        final int[] inner_count = {0} ;
                                        final String itemcode = itemDocument.get("item_code").toString();
                                        final long qty = (long) itemDocument.get("qty") ;

                                        Log.d("SelectedSize", ""+selectedItemCodeList.size()) ;


                                        for (int i = 0; i < selectedItemCodeList.size(); i++) {

                                            Log.d("selectedList", selectedItemCodeList.get(i).getItemcode() + " " + selectedItemCodeList.get(i).getQty());


                                            if (itemcode.equals(selectedItemCodeList.get(i).getItemcode())) {


                                                cancel_items_amount[0] = cancel_items_amount[0] + (selectedItemCodeList.get(i).getItem_price() * selectedItemCodeList.get(i).getQty());

                                                final int finalI = i;


                                                final int finalI1 = i;
                                                FirebaseUtil.getOrdersItemsRef(orderId).document(itemcode).update("item_status", "cancelled_admin", "cancel_qty", selectedItemCodeList.get(i).getQty()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if(task.isSuccessful()){

                                                            FirebaseUtil.items_ref.document(itemcode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                    if(task.isSuccessful()){

                                                                        if(task.getResult() != null){

                                                                            if(task.getResult().exists()){

                                                                                DocumentSnapshot itemDocument = task.getResult() ;

                                                                                final long grovenco_price =(long) itemDocument.get("grovenco_price") ;
                                                                                final Boolean grovenco_exclusive = (Boolean) itemDocument.get("grovenco_exclusive") ;

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

                                                                                long updated_price ;

                                                                                if (grovenco_exclusive && grovenco_exclusive_price != 0 && grovenco_exclusive_qty != 0 && (qty - selectedItemCodeList.get(finalI).getQty()) >= grovenco_exclusive_qty) {

                                                                                    updated_price = grovenco_exclusive_price ;

                                                                                } else {

                                                                                    if (isMember == 1) {

                                                                                        updated_price = member_price ;

                                                                                    } else {

                                                                                        updated_price = grovenco_price ;

                                                                                    }

                                                                                }

                                                                                if(qty - selectedItemCodeList.get(finalI).getQty() > 0){

                                                                                    FirebaseUtil.getOrdersItemsRef(orderId).document(itemcode).update("buying_price", updated_price, "qty", qty - selectedItemCodeList.get(finalI).getQty()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                            if(task.isSuccessful()){

                                                                                                total_mrp[0] = total_mrp[0] + ((qty - selectedItemCodeList.get(finalI).getQty()) * mrp);

                                                                                                if (grovenco_exclusive && grovenco_exclusive_price != 0 && grovenco_exclusive_qty != 0 && (qty - selectedItemCodeList.get(finalI).getQty()) >= grovenco_exclusive_qty) {
                                                                                                    total_grovencoDiscount[0] = total_grovencoDiscount[0] + (qty - selectedItemCodeList.get(finalI).getQty()) * (mrp - grovenco_exclusive_price);

                                                                                                } else {

                                                                                                    if (isMember == 1) {
                                                                                                        total_memberDiscount[0] = total_memberDiscount[0] + (qty - selectedItemCodeList.get(finalI).getQty()) * (grovenco_price - member_price);
                                                                                                    }

                                                                                                    total_grovencoDiscount[0] = total_grovencoDiscount[0] + (qty - selectedItemCodeList.get(finalI).getQty()) * (mrp - grovenco_price);

                                                                                                }

                                                                                                Log.d("Values", total_grovencoDiscount[0]+" "+ total_memberDiscount[0]+" "+ total_mrp[0]) ;


                                                                                                count[0]++ ;

                                                                                                if (count[0] == size) {


                                                                                                    FirebaseUtil.getOrdersRef(orderId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                                            if(task.isSuccessful()){

                                                                                                                if(task.getResult() != null){

                                                                                                                    if(task.getResult().exists()){

                                                                                                                        DocumentSnapshot document = task.getResult() ;

                                                                                                                        final long deliveryCharges = (long) document.get("delivery_charges") ;
                                                                                                                        double couponDiscount = document.getDouble("coupon_discount") ;
                                                                                                                        double grovenco_credits = document.getDouble("grovenco_credits") ;
                                                                                                                        final double previous_totalAmount = document.getDouble("cart_value") ;
                                                                                                                        double previous_grovenco_credits = grovenco_credits ;

                                                                                                                        if(couponDiscount == -1){

                                                                                                                            couponDiscount = 0 ;

                                                                                                                        }




                                                                                                                        if(grovenco_credits == 0) {

                                                                                                                            if( total_mrp[0]-total_grovencoDiscount[0]-total_memberDiscount[0]-couponDiscount < 0){

                                                                                                                                couponDiscount = total_mrp[0]-total_grovencoDiscount[0]-total_memberDiscount[0] ;

                                                                                                                            }

                                                                                                                        }

                                                                                                                        if(couponDiscount == 0){

                                                                                                                            Log.d("Case1", "1") ;

                                                                                                                            if( total_mrp[0]-total_grovencoDiscount[0]-total_memberDiscount[0]-grovenco_credits < 0){

                                                                                                                                grovenco_credits = total_mrp[0]-total_grovencoDiscount[0]-total_memberDiscount[0] ;
                                                                                                                                /*extra_amount = grovenco_credits - (total_mrp[0]-total_grovencoDiscount[0]-total_memberDiscount[0]) ;*/

                                                                                                                            }

                                                                                                                        }

                                                                                                                        if(couponDiscount > 0 && grovenco_credits > 0) {

                                                                                                                            if (total_mrp[0] - total_grovencoDiscount[0] - total_memberDiscount[0] - grovenco_credits < 0) {

                                                                                                                                grovenco_credits = total_mrp[0] - total_grovencoDiscount[0] - total_memberDiscount[0];
                                                                                                                                couponDiscount = 0;
                                                                                                                                /*extra_amount = grovenco_credits - (total_mrp[0] - total_grovencoDiscount[0] - total_memberDiscount[0]) ;*/

                                                                                                                            } else {

                                                                                                                                if (total_mrp[0] - total_grovencoDiscount[0] - total_memberDiscount[0] - grovenco_credits - couponDiscount < 0) {

                                                                                                                                    couponDiscount = total_mrp[0] - total_grovencoDiscount[0] - total_memberDiscount[0] - grovenco_credits;

                                                                                                                                }

                                                                                                                            }

                                                                                                                        }

                                                                                                                        final double extra_amount = previous_grovenco_credits - grovenco_credits ;

                                                                                                                        final double finalGrovenco_credits = grovenco_credits;


                                                                                                                        Log.d("extra_amt","" + extra_amount ) ;

                                                                                                                        if(extra_amount > 0){

                                                                                                                            FirebaseUtil.getUserRef(current_uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                @Override
                                                                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                                                                    if(task.isSuccessful()){

                                                                                                                                        if(task.getResult() != null){

                                                                                                                                            if(task.getResult().exists()){

                                                                                                                                                final double grovenco_wallet = task.getResult().getDouble("grovenco_wallet") ;

                                                                                                                                                FirebaseUtil.getUserRef(current_uid).update("grovenco_wallet", extra_amount + grovenco_wallet ).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                                                                                                        if(task.isSuccessful()){

                                                                                                                                                            Log.d("Wallet Updated" , finalGrovenco_credits + " : Added to wallet") ;

                                                                                                                                                        }

                                                                                                                                                    }
                                                                                                                                                }) ;

                                                                                                                                            }

                                                                                                                                        }

                                                                                                                                    }

                                                                                                                                }
                                                                                                                            }) ;

                                                                                                                        }


                                                                                                                        total_savings[0] = total_grovencoDiscount[0] + total_memberDiscount[0] +couponDiscount + grovenco_credits ;

                                                                                                                        Log.d("ValuesFinal", total_savings[0] + " " + total_mrp[0]) ;

                                                                                                                        if(grovenco_credits >= 0){

                                                                                                                            FirebaseUtil.getOrdersRef(orderId).update("coupon_discount",couponDiscount, "grovenco_credits",grovenco_credits,"member_discount", total_memberDiscount[0],"grovenco_discount",total_grovencoDiscount[0], "total_savings", total_savings[0], "cart_value", total_mrp[0] - total_savings[0] + deliveryCharges, "mrp", total_mrp[0]) ;

                                                                                                                        }else{

                                                                                                                            FirebaseUtil.getOrdersRef(orderId).update("coupon_discount",couponDiscount,"member_discount", total_memberDiscount[0],"grovenco_discount",total_grovencoDiscount[0], "total_savings", total_savings[0], "cart_value", total_mrp[0] - total_savings[0] + deliveryCharges, "mrp", total_mrp[0]) ;


                                                                                                                        }

                                                                                                                        final double updated_totalAmount = total_mrp[0] - total_savings[0] + deliveryCharges ;

                                                                                                                        FirebaseUtil.getUserRef(current_uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                            @Override
                                                                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                                                                if (task.getResult() != null) {

                                                                                                                                    if (task.getResult().exists()) {

                                                                                                                                        double total_spent = task.getResult().getDouble("total_spent") ;

                                                                                                                                        FirebaseUtil.getUserRef(current_uid).update( "total_spent", total_spent - (previous_totalAmount - updated_totalAmount)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                            @Override
                                                                                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                                                                                Log.d("Refunded amount", "Refunded " + cancel_items_amount[0]);

                                                                                                                                            }
                                                                                                                                        });

                                                                                                                                    }

                                                                                                                                }

                                                                                                                            }
                                                                                                                        });





                                                                                                                    }

                                                                                                                }

                                                                                                            }

                                                                                                        }
                                                                                                    }) ;
                                                                                                }

                                                                                            }

                                                                                        }
                                                                                    }) ;



                                                                                }else{

                                                                                    FirebaseUtil.getOrdersItemsRef(orderId).document(itemcode).update("item_status", "cancelled_admin", "cancel_qty", selectedItemCodeList.get(finalI1).getQty(), "qty", 0) ;

                                                                                    count[0]++ ;

                                                                                    if (count[0] == size) {

                                                                                        FirebaseUtil.getOrdersRef(orderId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                                if(task.isSuccessful()){

                                                                                                    if(task.getResult() != null){

                                                                                                        if(task.getResult().exists()){

                                                                                                            DocumentSnapshot document = task.getResult() ;

                                                                                                            final long deliveryCharges = (long) document.get("delivery_charges") ;
                                                                                                            double couponDiscount =  document.getDouble("coupon_discount") ;
                                                                                                            double grovenco_credits =  document.getDouble("grovenco_credits") ;
                                                                                                            final double previous_totalAmount = document.getDouble("cart_value") ;
                                                                                                            double previous_grovenco_credits = grovenco_credits ;

                                                                                                            if(couponDiscount == -1){

                                                                                                                couponDiscount = 0 ;

                                                                                                            }



                                                                                                            if(grovenco_credits == 0) {

                                                                                                                if( total_mrp[0]-total_grovencoDiscount[0]-total_memberDiscount[0]-couponDiscount < 0){

                                                                                                                    couponDiscount = total_mrp[0]-total_grovencoDiscount[0]-total_memberDiscount[0] ;

                                                                                                                }

                                                                                                            }

                                                                                                            if(couponDiscount == 0){

                                                                                                                Log.d("Case1", "1") ;

                                                                                                                if( total_mrp[0]-total_grovencoDiscount[0]-total_memberDiscount[0]-grovenco_credits < 0){

                                                                                                                    grovenco_credits = total_mrp[0]-total_grovencoDiscount[0]-total_memberDiscount[0] ;
                                                                                                                    /*extra_amount = grovenco_credits - (total_mrp[0]-total_grovencoDiscount[0]-total_memberDiscount[0]) ;
*/
                                                                                                                }

                                                                                                            }

                                                                                                            if(couponDiscount > 0 && grovenco_credits > 0) {

                                                                                                                if (total_mrp[0] - total_grovencoDiscount[0] - total_memberDiscount[0] - grovenco_credits < 0) {

                                                                                                                    grovenco_credits = total_mrp[0] - total_grovencoDiscount[0] - total_memberDiscount[0];
                                                                                                                    couponDiscount = 0;
                                                                                                                    /*extra_amount = grovenco_credits - (total_mrp[0] - total_grovencoDiscount[0] - total_memberDiscount[0]) ;
*/
                                                                                                                } else {

                                                                                                                    if (total_mrp[0] - total_grovencoDiscount[0] - total_memberDiscount[0] - grovenco_credits - couponDiscount < 0) {

                                                                                                                        couponDiscount = total_mrp[0] - total_grovencoDiscount[0] - total_memberDiscount[0] - grovenco_credits;

                                                                                                                    }

                                                                                                                }

                                                                                                            }


                                                                                                            final double finalGrovenco_credits = grovenco_credits;
                                                                                                            final double extra_amount = previous_grovenco_credits - grovenco_credits ;

                                                                                                            Log.d("extra_amt","" + extra_amount ) ;

                                                                                                            if(extra_amount > 0){

                                                                                                                FirebaseUtil.getUserRef(current_uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                    @Override
                                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                                                        if(task.isSuccessful()){

                                                                                                                            if(task.getResult() != null){

                                                                                                                                if(task.getResult().exists()){

                                                                                                                                    final double grovenco_wallet = task.getResult().getDouble("grovenco_wallet") ;

                                                                                                                                    FirebaseUtil.getUserRef(current_uid).update("grovenco_wallet", extra_amount + grovenco_wallet ).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                        @Override
                                                                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                                                                            if(task.isSuccessful()){

                                                                                                                                                Log.d("Wallet Updated" , finalGrovenco_credits + " : Added to wallet") ;

                                                                                                                                            }

                                                                                                                                        }
                                                                                                                                    }) ;

                                                                                                                                }

                                                                                                                            }

                                                                                                                        }

                                                                                                                    }
                                                                                                                }) ;

                                                                                                            }


                                                                                                            total_savings[0] = total_grovencoDiscount[0] + total_memberDiscount[0] +couponDiscount + grovenco_credits ;

                                                                                                            Log.d("ValuesFinal", total_savings[0] + " " + total_mrp[0]) ;

                                                                                                            if(grovenco_credits >= 0){

                                                                                                                FirebaseUtil.getOrdersRef(orderId).update("coupon_discount",couponDiscount, "grovenco_credits",grovenco_credits,"member_discount", total_memberDiscount[0],"grovenco_discount",total_grovencoDiscount[0], "total_savings", total_savings[0], "cart_value", total_mrp[0] - total_savings[0] + deliveryCharges, "mrp", total_mrp[0]) ;

                                                                                                            }else{

                                                                                                                FirebaseUtil.getOrdersRef(orderId).update("coupon_discount",couponDiscount,"member_discount", total_memberDiscount[0],"grovenco_discount",total_grovencoDiscount[0], "total_savings", total_savings[0], "cart_value", total_mrp[0] - total_savings[0] + deliveryCharges, "mrp", total_mrp[0]) ;


                                                                                                            }

                                                                                                            final double updated_totalAmount = total_mrp[0] - total_savings[0] + deliveryCharges ;

                                                                                                            FirebaseUtil.getUserRef(current_uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                                                    if (task.getResult() != null) {

                                                                                                                        if (task.getResult().exists()) {

                                                                                                                            double total_spent = task.getResult().getDouble("total_spent") ;

                                                                                                                            FirebaseUtil.getUserRef(current_uid).update( "total_spent", total_spent - (previous_totalAmount - updated_totalAmount)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                                                                    Log.d("Refunded amount", "Refunded " + cancel_items_amount[0]);

                                                                                                                                }
                                                                                                                            });

                                                                                                                        }

                                                                                                                    }

                                                                                                                }
                                                                                                            });


                                                                                                        }

                                                                                                    }

                                                                                                }

                                                                                            }
                                                                                        }) ;
                                                                                    }


                                                                                }

                                                                            }

                                                                        }

                                                                    }

                                                                }
                                                            }) ;

                                                        }


                                                    }
                                                });

                                                break;

                                            } else {

                                                inner_count[0]++;

                                                if (inner_count[0] == selectedItemCodeList.size()) {

                                                    FirebaseUtil.items_ref.document(itemcode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                            if(task.isSuccessful()){

                                                                if(task.getResult() != null){

                                                                    if(task.getResult().exists()){

                                                                        DocumentSnapshot itemDocument = task.getResult() ;

                                                                        final long grovenco_price =(long) itemDocument.get("grovenco_price") ;
                                                                        final Boolean grovenco_exclusive = (Boolean) itemDocument.get("grovenco_exclusive") ;

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

                                                                        total_mrp[0] = total_mrp[0] + (qty * mrp);

                                                                        if (grovenco_exclusive && grovenco_exclusive_price != 0 && grovenco_exclusive_qty != 0 && qty >= grovenco_exclusive_qty) {
                                                                            total_grovencoDiscount[0] = total_grovencoDiscount[0] + qty * (mrp - grovenco_exclusive_price);

                                                                        } else {

                                                                            if (isMember == 1) {
                                                                                total_memberDiscount[0] = total_memberDiscount[0] + qty * (grovenco_price - member_price);
                                                                            }

                                                                            total_grovencoDiscount[0] = total_grovencoDiscount[0] + qty * (mrp - grovenco_price);

                                                                        }

                                                                        Log.d("Values", total_grovencoDiscount[0]+" "+ total_memberDiscount[0]) ;

                                                                        count[0]++ ;

                                                                        if (count[0] == size) {

                                                                            FirebaseUtil.getOrdersRef(orderId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                    if(task.isSuccessful()){

                                                                                        if(task.getResult() != null){

                                                                                            if(task.getResult().exists()){

                                                                                                DocumentSnapshot document = task.getResult() ;

                                                                                                final long deliveryCharges = (long) document.get("delivery_charges") ;
                                                                                                double couponDiscount = document.getDouble("coupon_discount") ;
                                                                                                double grovenco_credits = document.getDouble("grovenco_credits") ;
                                                                                                final double previous_totalAmount = document.getDouble("cart_value") ;
                                                                                                double previous_grovenco_credits = grovenco_credits ;

                                                                                                if(couponDiscount == -1){

                                                                                                    couponDiscount = 0 ;

                                                                                                }



                                                                                                if(grovenco_credits == 0) {

                                                                                                    if( total_mrp[0]-total_grovencoDiscount[0]-total_memberDiscount[0]-couponDiscount < 0){

                                                                                                        couponDiscount = total_mrp[0]-total_grovencoDiscount[0]-total_memberDiscount[0] ;

                                                                                                    }

                                                                                                }

                                                                                                if(couponDiscount == 0){

                                                                                                    Log.d("Case1", "1") ;

                                                                                                    if( total_mrp[0]-total_grovencoDiscount[0]-total_memberDiscount[0]-grovenco_credits < 0){

                                                                                                        grovenco_credits = total_mrp[0]-total_grovencoDiscount[0]-total_memberDiscount[0] ;
                                                                                                        /*extra_amount = grovenco_credits - (total_mrp[0]-total_grovencoDiscount[0]-total_memberDiscount[0]) ;
*/
                                                                                                    }

                                                                                                }

                                                                                                if(couponDiscount > 0 && grovenco_credits > 0) {

                                                                                                    if (total_mrp[0] - total_grovencoDiscount[0] - total_memberDiscount[0] - grovenco_credits < 0) {

                                                                                                        grovenco_credits = total_mrp[0] - total_grovencoDiscount[0] - total_memberDiscount[0];
                                                                                                        couponDiscount = 0;
                                                                                                        /*extra_amount = grovenco_credits - (total_mrp[0] - total_grovencoDiscount[0] - total_memberDiscount[0]) ;
*/
                                                                                                    } else {

                                                                                                        if (total_mrp[0] - total_grovencoDiscount[0] - total_memberDiscount[0] - grovenco_credits - couponDiscount < 0) {

                                                                                                            couponDiscount = total_mrp[0] - total_grovencoDiscount[0] - total_memberDiscount[0] - grovenco_credits;

                                                                                                        }

                                                                                                    }

                                                                                                }


                                                                                                final double finalGrovenco_credits = grovenco_credits;
                                                                                                final double extra_amount = previous_grovenco_credits - grovenco_credits ;

                                                                                                Log.d("extra_amt","" + extra_amount ) ;

                                                                                                if(extra_amount > 0){

                                                                                                    FirebaseUtil.getUserRef(current_uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                                            if(task.isSuccessful()){

                                                                                                                if(task.getResult() != null){

                                                                                                                    if(task.getResult().exists()){

                                                                                                                        double grovenco_wallet = task.getResult().getDouble("grovenco_wallet") ;

                                                                                                                        FirebaseUtil.getUserRef(current_uid).update("grovenco_wallet", extra_amount + grovenco_wallet ).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                                                                if(task.isSuccessful()){

                                                                                                                                    Log.d("Wallet Updated" , extra_amount + " : Added to wallet") ;

                                                                                                                                }

                                                                                                                            }
                                                                                                                        }) ;

                                                                                                                    }

                                                                                                                }

                                                                                                            }

                                                                                                        }
                                                                                                    }) ;

                                                                                                }


                                                                                                total_savings[0] = total_grovencoDiscount[0] + total_memberDiscount[0] +couponDiscount + grovenco_credits ;

                                                                                                Log.d("ValuesFinal", total_savings[0] + " " + total_mrp[0]) ;

                                                                                                if(grovenco_credits >= 0){

                                                                                                    FirebaseUtil.getOrdersRef(orderId).update("coupon_discount",couponDiscount, "grovenco_credits",grovenco_credits,"member_discount", total_memberDiscount[0],"grovenco_discount",total_grovencoDiscount[0], "total_savings", total_savings[0], "cart_value", total_mrp[0] - total_savings[0] + deliveryCharges, "mrp", total_mrp[0]) ;

                                                                                                }else{

                                                                                                    FirebaseUtil.getOrdersRef(orderId).update("coupon_discount",couponDiscount,"member_discount", total_memberDiscount[0],"grovenco_discount",total_grovencoDiscount[0], "total_savings", total_savings[0], "cart_value", total_mrp[0] - total_savings[0] + deliveryCharges, "mrp", total_mrp[0]) ;


                                                                                                }

                                                                                                final double updated_totalAmount = total_mrp[0] - total_savings[0] + deliveryCharges ;

                                                                                                FirebaseUtil.getUserRef(current_uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                                        if (task.getResult() != null) {

                                                                                                            if (task.getResult().exists()) {

                                                                                                                double total_spent = task.getResult().getDouble("total_spent") ;

                                                                                                                FirebaseUtil.getUserRef(current_uid).update( "total_spent", total_spent - (previous_totalAmount - updated_totalAmount)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                                                                        Log.d("Refunded amount", "Refunded " + cancel_items_amount[0]);

                                                                                                                    }
                                                                                                                });

                                                                                                            }

                                                                                                        }

                                                                                                    }
                                                                                                });



                                                                                            }

                                                                                        }

                                                                                    }

                                                                                }
                                                                            }) ;


                                                                        }

                                                                    }

                                                                }

                                                            }

                                                        }
                                                    }) ;




                                                }


                                            }

                                        }



                                    }

                                }

                            }

                        }



                    });
                }else{

                    Toast.makeText(context, "Please select the items from the list", Toast.LENGTH_SHORT).show();

                }
            }
        });


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

    @Override
    public int getItemCount() {
        return orderItemsList.size() ;
    }



}
