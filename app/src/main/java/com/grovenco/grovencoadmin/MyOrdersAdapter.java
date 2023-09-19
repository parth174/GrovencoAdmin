package com.grovenco.grovencoadmin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {

    private Context context ;
    ArrayList<Orders> ordersList ;

    public MyOrdersAdapter(Context context, ArrayList<Orders> ordersList) {

        this.context = context ;
        this.ordersList = ordersList ;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_orderId, tv_date, tv_time,  tv_amount, tv_name, tv_status, tv_phoneNumber, tv_call, tv_seen_admin;
        CardView cardView ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_orderId = itemView.findViewById(R.id.tv_order_id) ;
            tv_date = itemView.findViewById(R.id.tv_date) ;
            tv_time = itemView.findViewById(R.id.tv_time) ;
            tv_amount = itemView.findViewById(R.id.tv_amount) ;
            cardView = itemView.findViewById(R.id.cardView) ;
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_name = itemView.findViewById(R.id.tv_name) ;
            tv_phoneNumber = itemView.findViewById(R.id.tv_phone_number) ;
            tv_call = itemView.findViewById(R.id.tv_call) ;
            tv_seen_admin = itemView.findViewById(R.id.tv_seen_admin) ;

        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyOrdersAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_my_orders,parent,false)) ;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tv_orderId.setText(ordersList.get(position).getOrder_id()) ;
        holder.tv_date.setText(ordersList.get(position).getOrder_date()) ;
        holder.tv_time.setText(ordersList.get(position).getOrder_time()) ;
        holder.tv_amount.setText(Utils.getAmount(ordersList.get(position).getCart_value())) ;
        holder.tv_name.setText(ordersList.get(position).getName());
        holder.tv_phoneNumber.setText(ordersList.get(position).getPhone_number());


        if(! ordersList.get(position).seen_admin){

            Utils.setVisibilityVisible(holder.tv_seen_admin);

        }else{

            Utils.setVisibilityGone(holder.tv_seen_admin);

        }

        holder.tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + ordersList.get(position).getPhone_number()));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }

            }
        });


        if(ordersList.get(position).getOrder_status().equals("placed")){

            holder.tv_status.setText("placed");

        }

        if(ordersList.get(position).getOrder_status().equals("packed")){

            holder.tv_status.setText("packed");

        }

        if(ordersList.get(position).getOrder_status().equals("on_way")){

            holder.tv_status.setText("on way");

        }

        if(ordersList.get(position).getOrder_status().equals("delivered")){

            holder.tv_status.setText("delivered");

        }

        if(ordersList.get(position).getOrder_status().equals("processing_refund")){

            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.quantum_googred));
            holder.tv_status.setText("refund requested");

        }

        if(ordersList.get(position).getOrder_status().equals("refunded")){

            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.tv_status.setText("refunded");

        }

        if(ordersList.get(position).getOrder_status().equals("cancelled")){

            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.quantum_googred));
            holder.tv_status.setText("cancelled");

        }

        if(ordersList.get(position).getOrder_status().equals("refund_declined")){

            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.quantum_googred));
            holder.tv_status.setText("Refund Declined");

        }

        if(ordersList.get(position).getOrder_status().equals("cancelled_admin")){

            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.quantum_googred));
            holder.tv_status.setText("admin cancelled");

        }




        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(! ordersList.get(position).isSeen_admin()){

                    FirebaseUtil.getOrdersRef(ordersList.get(position).getOrder_id()).update("seen_admin", true) ;

                }


                Intent intent = new Intent(context, OrderDetails.class) ;
                intent.putExtra("order_id", ordersList.get(position).getOrder_id()) ;
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }




}
