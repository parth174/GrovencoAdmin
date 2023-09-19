package com.grovenco.grovencoadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReportOrdersAdapter extends RecyclerView.Adapter<ReportOrdersAdapter.ViewHolder> {

    private Context context ;
    ArrayList<ReportOrders> ordersList ;

    public ReportOrdersAdapter(Context context, ArrayList<ReportOrders> ordersList) {

        this.context = context ;
        this.ordersList = ordersList ;
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_deliveredBy, tv_membership, tv_exclusiveCount, tv_name, tv_orderId, tv_date, tv_time, tv_DeliveryCharges, tv_details, tv_amount, tv_deliveryDate, tv_status;
        ImageView img_placed,img_packed, img_onWay, img_delivered ;
        RelativeLayout rl_membership, rl_deliveredBy ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name) ;
            tv_orderId = itemView.findViewById(R.id.tv_order_id) ;
            tv_date = itemView.findViewById(R.id.tv_date) ;
            tv_time = itemView.findViewById(R.id.tv_time) ;
            tv_amount = itemView.findViewById(R.id.tv_amount) ;
            tv_DeliveryCharges = itemView.findViewById(R.id.tv_delivery_charges) ;
            tv_status = itemView.findViewById(R.id.tv_status) ;
            tv_membership = itemView.findViewById(R.id.tv_membership) ;
            tv_exclusiveCount = itemView.findViewById(R.id.tv_exclusive_count) ;
            tv_details = itemView.findViewById(R.id.tv_details) ;
            rl_membership = itemView.findViewById(R.id.rl_membership) ;
            tv_deliveredBy = itemView.findViewById(R.id.tv_delivered_by) ;
            rl_deliveredBy = itemView.findViewById(R.id.rl_delivered_by) ;

        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReportOrdersAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_report_orders,parent,false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tv_name.setText(ordersList.get(position).getName());
        holder.tv_orderId.setText(ordersList.get(position).getOrderId()) ;
        holder.tv_date.setText(ordersList.get(position).getOrderDate()) ;
        holder.tv_time.setText(ordersList.get(position).getOrderTime()) ;
        holder.tv_amount.setText(Utils.getAmount(ordersList.get(position).getCartValue())) ;
        holder.tv_exclusiveCount.setText(String.valueOf(ordersList.get(position).getExclusiveCount()));

        holder.tv_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Utils.getInternetConnectivity(context)){

                    Intent intent = new Intent(context, OrderDetails.class) ;
                    intent.putExtra("order_id",ordersList.get(position).getOrderId()) ;
                    context.startActivity(intent);

                }else{

                    Toast.makeText(context, "Please connect to internet", Toast.LENGTH_SHORT).show();

                }

            }
        });

        if(ordersList.get(position).getDeliveryCharges() > 0 ){

            holder.tv_DeliveryCharges.setText( Utils.getAmount(ordersList.get(position).getDeliveryCharges())) ;

        }else{

            holder.tv_DeliveryCharges.setText("FREE") ;

        }

        if(ordersList.get(position).getMembership()){

            Utils.setVisibilityVisible(holder.rl_membership);
            holder.tv_membership.setText("ADDED");

        }else{

            Utils.setVisibilityGone(holder.rl_membership);
            holder.tv_membership.setText("");

        }

        if(!ordersList.get(position).getDeliveredBy().equals("") && ordersList.get(position).getDeliveredBy() != null){

            Utils.setVisibilityVisible(holder.rl_deliveredBy);
            holder.tv_deliveredBy.setText(ordersList.get(position).getDeliveredBy());

        }else{

            Utils.setVisibilityGone(holder.rl_deliveredBy);
            holder.tv_deliveredBy.setText("");

        }

        if(ordersList.get(position).getOrderStatus().equals("placed")){

           holder.tv_status.setText("placed");
           holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.green));

        }else if(ordersList.get(position).getOrderStatus().equals("packed")){

            holder.tv_status.setText("packed");
            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.green));


        } else if(ordersList.get(position).getOrderStatus().equals("on_way")){

            holder.tv_status.setText("on way");
            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.green));

        }

        else if(ordersList.get(position).getOrderStatus().equals("delivered")){

            holder.tv_status.setText("delivered");
            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.green));


        }

        else if(ordersList.get(position).getOrderStatus().equals("processing_refund")){

            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.quantum_googred));
            holder.tv_status.setText("processing refund");

        }

        else if(ordersList.get(position).getOrderStatus().equals("refunded")){

            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.tv_status.setText("refunded");

        }

        else if(ordersList.get(position).getOrderStatus().equals("cancelled")){

            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.quantum_googred));
            holder.tv_status.setText("cancelled");
        }

        else if(ordersList.get(position).getOrderStatus().equals("refund_declined")){

            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.quantum_googred));
            holder.tv_status.setText("Refund Declined");

        }

        else if(ordersList.get(position).getOrderStatus().equals("cancelled_admin")){

            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.quantum_googred));
            holder.tv_status.setText("admin cancelled");

        }


    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

}
