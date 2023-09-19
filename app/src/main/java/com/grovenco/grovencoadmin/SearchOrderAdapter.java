package com.grovenco.grovencoadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchOrderAdapter extends RecyclerView.Adapter<SearchOrderAdapter.ViewHolder> {

  private Context context ;
  private ArrayList<SearchOrderItem> searchOrderItemsList;

    SearchOrderAdapter(Context context, ArrayList<SearchOrderItem> searchOrderItemsList) {
        this.context = context;
        this.searchOrderItemsList = searchOrderItemsList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name, tv_order_id, tv_phone_number, tv_email, tv_order_amount ;
        RelativeLayout rl_parent ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rl_parent = itemView.findViewById(R.id.rl_parent) ;
            tv_name = itemView.findViewById(R.id.tv_name) ;
            tv_order_id = itemView.findViewById(R.id.tv_order_id) ;
            tv_phone_number = itemView.findViewById(R.id.tv_phone_number) ;
            tv_email = itemView.findViewById(R.id.tv_email) ;
            tv_order_amount = itemView.findViewById(R.id.tv_order_amount) ;

        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchOrderAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_search_order,parent,false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tv_email.setText(searchOrderItemsList.get(position).getEmail());
        holder.tv_name.setText(searchOrderItemsList.get(position).getName());
        holder.tv_order_id.setText(searchOrderItemsList.get(position).getOrderId());
        holder.tv_order_amount.setText(Utils.getAmount(searchOrderItemsList.get(position).getOrderAmount()));
        holder.tv_phone_number.setText(searchOrderItemsList.get(position).getPhoneNumber());

        holder.rl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Utils.getInternetConnectivity(context)){

                    Intent intent = new Intent(context, OrderDetails.class) ;
                    intent.putExtra("order_id", searchOrderItemsList.get(position).getOrderId() ) ;
                    context.startActivity(intent);

                }else{

                    Toast.makeText(context, "Please connect to internet", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return searchOrderItemsList.size() ;
    }



}
