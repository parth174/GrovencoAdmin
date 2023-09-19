package com.grovenco.grovencoadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.ViewHolder> {

    private Context context ;
    private ArrayList<Coupons> couponsList ;
    private ArrayList<Long> qtyList ;


    CouponsAdapter(Context context, ArrayList<Coupons> couponsList) {

        this.context = context;
        this.couponsList = couponsList;

    }

    CouponsAdapter(Context context, ArrayList<Coupons> couponsList, ArrayList<Long> qtyList) {

        this.context = context;
        this.couponsList = couponsList;
        this.qtyList = qtyList ;

    }



    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_couponTitle, tv_couponDetails, tv_maxDiscount, tv_qty, tv_couponCode ;
        ImageView img_delete ;
        RelativeLayout rl_content ;
        Switch switch_enabled, switch_visible ;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_couponTitle = itemView.findViewById(R.id.tv_coupon_title) ;
            tv_couponDetails = itemView.findViewById(R.id.tv_coupon_details) ;
            tv_maxDiscount = itemView.findViewById(R.id.tv_max_discount) ;
            rl_content = itemView.findViewById(R.id.rl_content) ;
            tv_qty = itemView.findViewById(R.id.tv_qty) ;
            tv_couponCode = itemView.findViewById(R.id.tv_coupon_code) ;
            switch_enabled = itemView.findViewById(R.id.switch_enabled) ;
            switch_visible = itemView.findViewById(R.id.switch_visible) ;
            img_delete = itemView.findViewById(R.id.img_delete) ;

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CouponsAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_coupons,parent,false)) ;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final String code = couponsList.get(position).getCode() ;
        final String details = couponsList.get(position).getDetails() ;
        final String title = couponsList.get(position).getTitle() ;
        final long max_discount = couponsList.get(position).getMax_discount() ;
        final long min_order = couponsList.get(position).getMin_order() ;
        final long offer = couponsList.get(position).getOffer() ;
        final double discount = couponsList.get(position).getDiscount() ;
        final String type = couponsList.get(position).getType() ;
        boolean enabled = couponsList.get(position).isEnabled() ;
        boolean visible = couponsList.get(position).isVisible() ;
        final String coupon_id = couponsList.get(position).getCouponId() ;

        Log.d("CouponId", coupon_id) ;

        holder.tv_couponTitle.setText(title) ;
        holder.tv_couponDetails.setText(details) ;
        holder.tv_couponCode.setText(code);

        if(enabled){

            holder.switch_enabled.setChecked(true);

        }else{

            holder.switch_enabled.setChecked(false);

        }

        if(visible){

            holder.switch_visible.setChecked(true);

        }else{

            holder.switch_visible.setChecked(false);

        }

        holder.switch_enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d("CheckChanged", "Enabled check changed") ;
                FirebaseUtil.coupons_ref.document(coupon_id).update("enabled", isChecked) ;

            }
        });

        holder.switch_visible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d("CheckChanged", "Enabled check changed") ;

                FirebaseUtil.coupons_ref.document(coupon_id).update("visible", isChecked) ;

            }
        });



        if(qtyList != null){

            if(qtyList.get(position) > 0){

                Utils.setVisibilityVisible(holder.tv_qty);
                holder.tv_qty.setText("X "+ qtyList.get(position));

            }else{

                Utils.setVisibilityGone(holder.tv_qty);

            }

        }


         if((type.equals("1") || type.equals("5")) && max_discount > 0){

             Utils.setVisibilityVisible(holder.tv_maxDiscount) ;
             holder.tv_maxDiscount.setText("Upto " + Utils.getAmount(max_discount));

         }else{

             Utils.setVisibilityGone(holder.tv_maxDiscount) ;

         }

         holder.img_delete.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if(Utils.getInternetConnectivity(context)){

                     AlertDialog.Builder dialog = new AlertDialog.Builder(context) ;
                     dialog.setTitle("Attention") ;
                     dialog.setMessage("Are you sure you want to delete this coupon ?") ;
                     dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {

                             FirebaseUtil.coupons_ref.document(coupon_id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {

                                     if(task.isSuccessful()){

                                         Toast.makeText(context, "Coupon Deleted", Toast.LENGTH_SHORT).show();
                                         ((CouponsActivity)context).couponsList.remove(couponsList.get(position)) ;
                                         ((CouponsActivity)context).couponsAdapter.notifyDataSetChanged();

                                     }else{

                                         Toast.makeText(context, "Something went wrong Try again", Toast.LENGTH_SHORT).show();

                                         if(task.getException() != null)
                                            Log.d("FirebaseErrorCoupon", task.getException().getMessage()) ;

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

         holder.rl_content.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if(Utils.getInternetConnectivity(context)){

                     Intent intent = new Intent(context, AddCouponActivity.class) ;
                     intent.putExtra("title", title) ;
                     intent.putExtra("details", details) ;
                     intent.putExtra("code", code) ;
                     intent.putExtra("coupon_id", coupon_id) ;
                     intent.putExtra("max_discount", max_discount) ;
                     intent.putExtra("offer", offer) ;
                     intent.putExtra("min_order", min_order) ;
                     intent.putExtra("discount", discount) ;
                     intent.putExtra("type", type) ;
                     context.startActivity(intent);


                 }else{

                     Toast.makeText(context, "Please connect to internet", Toast.LENGTH_SHORT).show();

                 }

             }
         });

    }

    @Override
    public int getItemCount() {
        return couponsList.size() ;
    }



}
