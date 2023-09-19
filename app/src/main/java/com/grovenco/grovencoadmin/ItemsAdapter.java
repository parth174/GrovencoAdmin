package com.grovenco.grovencoadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    Context context ;
    ArrayList<Items> itemsList ;
    HomeCartExtraItemsList current_activity ;

    public ItemsAdapter(Context context, ArrayList<Items> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    public ItemsAdapter(Context context, ArrayList<Items> itemsList, HomeCartExtraItemsList current_activity) {
        this.context = context;
        this.itemsList = itemsList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_stock, tv_name, tv_itemcode, tv_extra, tv_category, tv_subcategory, tv_trending, tv_edit ;
        ImageView img_grovenco_exclusive, img_delete ;
        CardView cardView ;
        Button btn_addPosition, btn_removeFromCurrentList ;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_category = itemView.findViewById(R.id.tv_category) ;
            tv_stock = itemView.findViewById(R.id.tv_stock) ;
            tv_name = itemView.findViewById(R.id.tv_name) ;
            tv_itemcode = itemView.findViewById(R.id.tv_itemcode) ;
            tv_extra = itemView.findViewById(R.id.tv_extra) ;
            tv_subcategory = itemView.findViewById(R.id.tv_subcategory) ;
            tv_trending = itemView.findViewById(R.id.tv_trending) ;
            tv_edit = itemView.findViewById(R.id.tv_edit) ;
            img_delete = itemView.findViewById(R.id.img_delete) ;
            img_grovenco_exclusive = itemView.findViewById(R.id.img_grovenco_exclusive) ;
            btn_addPosition = itemView.findViewById(R.id.btn_add_position);
            btn_removeFromCurrentList = itemView.findViewById(R.id.btn_remove) ;

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemsAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item,parent,false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String category = itemsList.get(position).getCategory() ;
        String subcategory = itemsList.get(position).getSubcategory() ;
        long stock = itemsList.get(position).getStock() ;
        final String itemcode = itemsList.get(position).getItemcode() ;
        String name = itemsList.get(position).getName() ;
        Boolean is_extra = itemsList.get(position).getIsExtra() ;
        long trending = itemsList.get(position).getIsTrending() ;
        Boolean grovenco_exclusive = itemsList.get(position).getGrovencoExclusive() ;

        holder.tv_category.setText(category);
        holder.tv_subcategory.setText(subcategory);
        holder.tv_stock.setText(String.valueOf(stock));
        holder.tv_itemcode.setText(itemcode);
        holder.tv_name.setText(String.valueOf(name));

        if(((Activity) context) instanceof HomeCartExtraItemsList){

            Utils.setVisibilityVisible(holder.btn_removeFromCurrentList);

        }else{

            Utils.setVisibilityGone(holder.btn_removeFromCurrentList);

        }

        if(is_extra){

            Utils.setVisibilityVisible(holder.tv_extra);

        }else{

            Utils.setVisibilityGone(holder.tv_extra);

        }

        if(trending == 1){

            Utils.setVisibilityVisible(holder.tv_trending);

        }else{

            Utils.setVisibilityGone(holder.tv_trending);

        }

        if(grovenco_exclusive){

            Utils.setVisibilityVisible(holder.img_grovenco_exclusive);

        }else{

            Utils.setVisibilityGone(holder.img_grovenco_exclusive);

        }


        if(stock <= 5){

            holder.tv_stock.setBackgroundColor(context.getResources().getColor(R.color.quantum_googred));

        }else{

            holder.tv_stock.setBackgroundColor(context.getResources().getColor(R.color.quantum_googgreenA700));


        }

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUtil.items_ref.document(itemcode).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show() ;

                    }
                }) ;

            }
        });

        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, HomeActivity.class) ;

                Log.d("Itemcode", itemcode) ;

                intent.putExtra("itemcode",itemcode) ;
                context.startActivity(intent) ;

            }
        });

        holder.btn_addPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> listType = new ArrayList<>() ;
                listType.add("DHAMAKA") ;
                listType.add("TRENDING") ;
                listType.add("CART") ;

                final ArrayList<Long> positionList = new ArrayList<>() ;


                final ArrayAdapter<Long> positionAdapter = new ArrayAdapter<Long>(context, android.R.layout.simple_spinner_item, positionList);

                ArrayAdapter<String> listTypeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listType);

                positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                listTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                AlertDialog.Builder dialog = new AlertDialog.Builder(context) ;
                dialog.setTitle("ADD TO LIST") ;

                View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_position,null, false) ;

                final Spinner spn_selectList = view.findViewById(R.id.spn_list_type) ;
                final Spinner spn_selectPosition = view.findViewById(R.id.spn_position) ;

                spn_selectList.setAdapter(listTypeAdapter);
                spn_selectPosition.setAdapter(positionAdapter);

                spn_selectList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        switch(position){

                            case 0 :

                                positionList.clear();

                                for(long i=1 ; i<=30 ; i++){

                                    positionList.add(i) ;

                                }

                                spn_selectPosition.setAdapter(positionAdapter);

                                break ;
                            case 1 :

                            positionList.clear();

                            for(long i=1 ; i<=10 ; i++){

                                positionList.add(i) ;

                            }

                            spn_selectPosition.setAdapter(positionAdapter);

                            break ;

                            case 2 :

                                positionList.clear();

                                for(long i=1 ; i<=10 ; i++){

                                    positionList.add(i) ;

                                }

                                spn_selectPosition.setAdapter(positionAdapter);

                                break ;

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                spn_selectList.setSelection(0);
                spn_selectPosition.setSelection(0);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(Utils.getInternetConnectivity(context)){

                            switch (spn_selectList.getSelectedItemPosition()){

                                case 0 :

                                    if(itemsList.get(position).getGrovencoExclusive()){

                                        FirebaseUtil.items_ref.document(itemsList.get(position).getItemcode()).update("position", spn_selectPosition.getSelectedItem()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){

                                                    Toast.makeText(context, "Added to Dhamaka Offers List ", Toast.LENGTH_SHORT).show();

                                                }

                                            }
                                        }) ;


                                    }else{

                                        Toast.makeText(context, "This item is not grovenco exclusive", Toast.LENGTH_SHORT).show();

                                    }

                                    break ;

                                case 1 :

                                    FirebaseUtil.items_ref.document(itemsList.get(position).getItemcode()).update("position", (long)spn_selectPosition.getSelectedItem() + 30, "trending", 1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){

                                                Toast.makeText(context, "Added to Trending Items List ", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    }) ;

                                    break ;

                                case 2 :

                                    FirebaseUtil.items_ref.document(itemsList.get(position).getItemcode()).update("position", (long)spn_selectPosition.getSelectedItem() + 40, "is_extra", true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){

                                                Toast.makeText(context, "Added to Cart Extra Items List ", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    }) ;

                                    break ;

                            }


                        }else{

                            Toast.makeText(context, "Please connect to internet", Toast.LENGTH_SHORT).show();

                        }

                    }
                }) ;

                dialog.setNegativeButton("Cancel", null) ;

                dialog.setView(view) ;

                dialog.show() ;

            }
        });

        holder.btn_removeFromCurrentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Map<String, Object> hashmap = new HashMap<>() ;
                hashmap.put("position", FieldValue.delete()) ;

                AlertDialog.Builder dialog = new AlertDialog.Builder(context) ;
                dialog.setTitle("REMOVE FROM CURRENT LIST") ;

                View view = LayoutInflater.from(context).inflate(R.layout.dialog_remove,null, false) ;

                dialog.setView(view) ;

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(Utils.getInternetConnectivity(context)){

                            if((Activity) context instanceof HomeCartExtraItemsList){

                                final HomeCartExtraItemsList current_activity = (HomeCartExtraItemsList) context ;

                                switch(current_activity.spinner.getSelectedItemPosition()){

                                    case 1 :

                                        hashmap.put("is_extra", false) ;

                                        break ;

                                    case 2 :

                                        hashmap.put("trending", 0) ;

                                        break ;

                                    case 3 :

                                        hashmap.put("grovenco_exclusive", false) ;

                                        break ;

                                }

                                FirebaseUtil.items_ref.document(itemsList.get(position).getItemcode()).update(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){

                                            Toast.makeText(context, "Item removed from corrosponding list", Toast.LENGTH_SHORT).show();
                                            current_activity.itemsList.remove(itemsList.get(position)) ;
                                            current_activity.itemsAdapter.notifyDataSetChanged();


                                        }

                                    }
                                }) ;


                            }


                        }else{

                            Toast.makeText(context, "Please connect to internet", Toast.LENGTH_SHORT).show();

                        }

                    }
                }) ;

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        HomeCartExtraItemsList current_activity = null ;

                        if((Activity) context instanceof HomeCartExtraItemsList) {

                             current_activity = (HomeCartExtraItemsList) context;

                            if(current_activity.spinner.getSelectedItemPosition() == 1){

                                hashmap.put("is_extra", false) ;

                            }

                        }


                        final HomeCartExtraItemsList finalCurrent_activity = current_activity;
                        FirebaseUtil.items_ref.document(itemsList.get(position).getItemcode()).update(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){

                                    Toast.makeText(context, "Item removed from corrosponding list", Toast.LENGTH_SHORT).show();
                                    finalCurrent_activity.itemsList.remove(itemsList.get(position)) ;
                                    finalCurrent_activity.itemsAdapter.notifyDataSetChanged();

                                }

                            }
                        }) ;

                    }
                }) ;

                dialog.setNeutralButton("Cancel", null) ;

                dialog.show() ;
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemsList.size() ;
    }



}
