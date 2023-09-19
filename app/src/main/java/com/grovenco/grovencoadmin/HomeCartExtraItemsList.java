package com.grovenco.grovencoadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeCartExtraItemsList extends AppCompatActivity {

    RecyclerView rv_items ;
    RecyclerView.LayoutManager layoutManagerItems ;
    ItemsAdapter itemsAdapter ;
    ArrayList<Items> itemsList ;
    Spinner spinner ;
    ArrayList<String> filterList ;
    String itemcode ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        rv_items = findViewById(R.id.rv_items) ;
        spinner = findViewById(R.id.spinner) ;

        itemsList = new ArrayList<>() ;

        layoutManagerItems = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManagerItems).setOrientation(RecyclerView.VERTICAL);

        rv_items.setLayoutManager(layoutManagerItems);

        itemsAdapter = new ItemsAdapter(this, itemsList) ;

        rv_items.setAdapter(itemsAdapter);


        filterList = new ArrayList<>() ;

        filterList.add("SELECT FILTER") ;
        filterList.add("Cart");
        filterList.add("Trending") ;
        filterList.add("GrovencoExclusive");

        final ArrayAdapter<String> filterAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, filterList);

        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(filterAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, View view, int i, long l) {

                switch(adapterView.getItemAtPosition(i).toString()){

                    case "SELECT FILTER" :

                        itemsList.clear();
                        itemsAdapter.notifyDataSetChanged();

                        break ;

                    case "Cart" :

                        FirebaseUtil.items_ref.whereEqualTo("is_extra", true).orderBy("position", Query.Direction.ASCENDING).limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.getResult() != null){

                                    itemsList.clear() ;
                                    itemsAdapter.notifyDataSetChanged();

                                    int size = task.getResult().size() ;
                                    int count = 0 ;

                                    for(DocumentSnapshot itemDocument : task.getResult().getDocuments()){

                                        final String name = itemDocument.get("name").toString() ;
                                        final String description = itemDocument.get("description").toString() ;
                                        final String item_code =itemDocument.get("item_code").toString() ;
                                        final String category = itemDocument.get("category").toString() ;
                                        final String subcategory = itemDocument.get("subcategory").toString() ;
                                        final String parent_code = itemDocument.get("parent_code").toString() ;
                                        final long weight =(long) itemDocument.get("weight") ;
                                        final String weight_unit = itemDocument.get("weight_unit").toString() ;
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
                                        final long stock =(long) itemDocument.get("stock") ;
                                        final long limit =(long) itemDocument.get("limit") ;
                                        final double purchasing_price = itemDocument.getDouble("purchasing_price") ;
                                        final Boolean is_extra = (Boolean) itemDocument.get("is_extra") ;
                                        final long is_trending  = (long) itemDocument.get("trending") ;

                                        Items item = new Items(name,item_code,category,subcategory,grovenco_exclusive,stock,is_extra,is_trending) ;

                                        itemsList.add(item);


                                        count = count+1 ;

                                        if(count == size){

                                            itemsAdapter.notifyDataSetChanged();

                                        }



                                    }

                                }


                            }
                        }) ;

                        break ;
                    case "Trending" :

                        FirebaseUtil.items_ref.whereEqualTo("trending", 1).orderBy("position", Query.Direction.ASCENDING).limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.getResult() != null){

                                    itemsList.clear() ;
                                    itemsAdapter.notifyDataSetChanged();

                                    int size = task.getResult().size() ;
                                    int count = 0 ;

                                    for(DocumentSnapshot itemDocument : task.getResult().getDocuments()){

                                        final String name = itemDocument.get("name").toString() ;
                                        final String description = itemDocument.get("description").toString() ;
                                        final String item_code =itemDocument.get("item_code").toString() ;
                                        final String category = itemDocument.get("category").toString() ;
                                        final String subcategory = itemDocument.get("subcategory").toString() ;
                                        final String parent_code = itemDocument.get("parent_code").toString() ;
                                        final long weight =(long) itemDocument.get("weight") ;
                                        final String weight_unit = itemDocument.get("weight_unit").toString() ;
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
                                        final long stock =(long) itemDocument.get("stock") ;
                                        final long limit =(long) itemDocument.get("limit") ;
                                        final double purchasing_price = itemDocument.getDouble("purchasing_price") ;
                                        final Boolean is_extra = (Boolean) itemDocument.get("is_extra") ;
                                        final long is_trending  = (long) itemDocument.get("trending") ;

                                        Items item = new Items(name,item_code,category,subcategory,grovenco_exclusive,stock,is_extra,is_trending) ;

                                        itemsList.add(item);


                                        count = count+1 ;

                                        if(count == size){

                                            itemsAdapter.notifyDataSetChanged();

                                        }



                                    }

                                }


                            }
                        }) ;

                        break ;
                    case "GrovencoExclusive" :

                        FirebaseUtil.items_ref.whereEqualTo("grovenco_exclusive", true).orderBy("position", Query.Direction.ASCENDING).limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.getResult() != null){

                                    itemsList.clear() ;
                                    itemsAdapter.notifyDataSetChanged();

                                    int size = task.getResult().size() ;
                                    int count = 0 ;

                                    for(DocumentSnapshot itemDocument : task.getResult().getDocuments()){

                                        final String name = itemDocument.get("name").toString() ;
                                        final String description = itemDocument.get("description").toString() ;
                                        final String item_code =itemDocument.get("item_code").toString() ;
                                        final String category = itemDocument.get("category").toString() ;
                                        final String subcategory = itemDocument.get("subcategory").toString() ;
                                        final String parent_code = itemDocument.get("parent_code").toString() ;
                                        final long weight =(long) itemDocument.get("weight") ;
                                        final String weight_unit = itemDocument.get("weight_unit").toString() ;
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
                                        final long stock =(long) itemDocument.get("stock") ;
                                        final long limit =(long) itemDocument.get("limit") ;
                                        final double purchasing_price = itemDocument.getDouble("purchasing_price") ;
                                        final Boolean is_extra = (Boolean) itemDocument.get("is_extra") ;
                                        final long is_trending  = (long) itemDocument.get("trending") ;

                                        Items item = new Items(name,item_code,category,subcategory,grovenco_exclusive,stock,is_extra,is_trending) ;

                                        itemsList.add(item);


                                        count = count+1 ;

                                        if(count == size){

                                            itemsAdapter.notifyDataSetChanged();

                                        }



                                    }

                                }


                            }
                        }) ;


                        break ;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
}
