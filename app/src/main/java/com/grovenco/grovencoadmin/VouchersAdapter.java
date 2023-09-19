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

public class VouchersAdapter extends RecyclerView.Adapter<VouchersAdapter.ViewHolder> {

    private Context context ;
    private ArrayList<Vouchers> vouchersList ;
    private ArrayList<Long> qtyList ;


    VouchersAdapter(Context context, ArrayList<Vouchers> couponsList) {

        this.context = context;
        this.vouchersList = couponsList;

    }

    VouchersAdapter(Context context, ArrayList<Vouchers> couponsList, ArrayList<Long> qtyList) {

        this.context = context;
        this.vouchersList = couponsList;
        this.qtyList = qtyList ;

    }



    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_voucherTitle, tv_voucherDetails, tv_maxDiscount, tv_qty, tv_voucherCode, tv_minOrderDetails ;
        ImageView img_delete ;
        RelativeLayout rl_content ;
        Switch switch_enabled, switch_visible ;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_voucherTitle = itemView.findViewById(R.id.tv_voucher_title) ;
            tv_voucherDetails = itemView.findViewById(R.id.tv_voucher_details) ;
            tv_maxDiscount = itemView.findViewById(R.id.tv_max_discount) ;
            rl_content = itemView.findViewById(R.id.rl_content) ;
            tv_qty = itemView.findViewById(R.id.tv_qty) ;
            tv_voucherCode = itemView.findViewById(R.id.tv_voucher_code) ;
            switch_enabled = itemView.findViewById(R.id.switch_enabled) ;
            switch_visible = itemView.findViewById(R.id.switch_visible) ;
            img_delete = itemView.findViewById(R.id.img_delete) ;
            tv_minOrderDetails = itemView.findViewById(R.id.tv_min_order_details) ;

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VouchersAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_vouchers,parent,false)) ;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final String code = vouchersList.get(position).getCode() ;
        final String details = vouchersList.get(position).getDetails() ;
        final String title = vouchersList.get(position).getTitle() ;
        final long max_discount = vouchersList.get(position).getMaxDiscount() ;
        final long min_order = vouchersList.get(position).getMinOrder() ;
        final long voucher_min_order = vouchersList.get(position).getVoucherMinOrder() ;
        final long offer = vouchersList.get(position).getOffer() ;
        final double discount = vouchersList.get(position).getDiscount() ;
        final String type = vouchersList.get(position).getType() ;
        boolean enabled = vouchersList.get(position).isEnabled() ;
        final String voucher_id = vouchersList.get(position).getVoucherId() ;

        holder.tv_voucherTitle.setText(title) ;
        holder.tv_voucherDetails.setText(details) ;

        if(code != null && !code.equals("")){

            Utils.setVisibilityVisible(holder.tv_voucherCode);
            holder.tv_voucherCode.setText(code);

        }else{

            Utils.setVisibilityGone(holder.tv_voucherCode);

        }



        if(enabled){

            holder.switch_enabled.setChecked(true);

        }else{

            holder.switch_enabled.setChecked(false);

        }


        holder.switch_enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d("CheckChanged", "Enabled check changed") ;
                FirebaseUtil.vouchers_ref.document(voucher_id).update("enabled", isChecked) ;

            }
        });


        if(min_order > 0 ){

            holder.tv_minOrderDetails.setText("** This voucher can only be availed for orders having value greater that " + Utils.getAmount(min_order) );

        }else{

            holder.tv_minOrderDetails.setText("** This voucher will be added only once to new users' account" );

        }

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

                             FirebaseUtil.vouchers_ref.document(voucher_id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {

                                     if(task.isSuccessful()){

                                         Toast.makeText(context, "Voucher Deleted", Toast.LENGTH_SHORT).show();
                                         ((VouchersActivity)context).vouchersList.remove(vouchersList.get(position)) ;
                                         ((VouchersActivity)context).vouchersAdapter.notifyDataSetChanged();

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

         /*holder.rl_content.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if(Utils.getInternetConnectivity(context)){

                     Intent intent = new Intent(context, AddvoucherActivity.class) ;
                     intent.putExtra("title", title) ;
                     intent.putExtra("details", details) ;
                     intent.putExtra("code", code) ;
                     intent.putExtra("voucher_id", voucher_id) ;
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
         });*/

    }

    @Override
    public int getItemCount() {
        return vouchersList.size() ;
    }



}
