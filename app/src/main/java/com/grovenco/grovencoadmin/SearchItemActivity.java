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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchItemActivity extends AppCompatActivity {

    EditText et_itemcode ;
    RecyclerView rv_items ;
    RecyclerView.LayoutManager layoutManagerItems ;
    ItemsAdapter itemsAdapter ;
    ArrayList<Items> itemsList ;
    Spinner spinner ;
    ArrayList<String> filterList ;
    String itemcode ;
    Button btn_search ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        et_itemcode = findViewById(R.id.et_itemcode) ;
        rv_items = findViewById(R.id.rv_items) ;
        btn_search = findViewById(R.id.btn_search) ;

        itemsList = new ArrayList<>() ;


        layoutManagerItems = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManagerItems).setOrientation(RecyclerView.VERTICAL);

        rv_items.setLayoutManager(layoutManagerItems);

        itemsAdapter = new ItemsAdapter(this, itemsList) ;

        rv_items.setAdapter(itemsAdapter);


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utils.getInternetConnectivity(SearchItemActivity.this)){

                    String itemcode = et_itemcode.getText().toString() ;

                    FirebaseUtil.items_ref.document(itemcode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if(task.isSuccessful()){

                                if(task.getResult() != null){

                                    if(task.getResult().exists()){

                                        itemsList.clear() ;

                                        DocumentSnapshot itemDocument = task.getResult() ;

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

                                        itemsAdapter.notifyDataSetChanged();

                                    }else{

                                        Toast.makeText(SearchItemActivity.this, "No item found", Toast.LENGTH_SHORT).show();

                                    }

                                }else{

                                    Toast.makeText(SearchItemActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    if(task.getException() != null){

                                        Log.d("FirebaseItemError", task.getException().getMessage()) ;

                                    }
                                }

                            }else{

                                Toast.makeText(SearchItemActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                if(task.getException() != null){

                                    Log.d("FirebaseItemError", task.getException().getMessage()) ;

                                }

                            }

                        }
                    }) ;

                }else{

                    Toast.makeText(SearchItemActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
}