package com.grovenco.grovencoadmin;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.firebase.firestore.ListenerRegistration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchOrderActivity extends AppCompatActivity {

    static String ALGOLIA_ADMIN_API_KEY = "6406a173b3f00ffcf95d74f50c1b4936" ;
    static String ALGOLIA_APPLICATION_ID = "6SP23N3YQ8" ;

    ImageView img_back ;

    EditText et_search ;
    TextView tv_cartCount ;
    RecyclerView rv_searchList ;
    RecyclerView.LayoutManager layoutManagerSearchItems;
    SearchOrderAdapter searchOrderAdapter;
    ArrayList<SearchOrderItem> ordersList ;
    ProgressBar progress ;
    TextView tv_emptyState ;
    ListenerRegistration cartSizeListener ;

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_search_order) ;




        if (! Utils.getInternetConnectivity(this)) {

            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();

        }

        if(getSupportActionBar() != null){

            getSupportActionBar().hide() ;

        }

        rv_searchList = findViewById(R.id.rv_search_list) ;
        progress = findViewById(R.id.progress) ;
        img_back = findViewById(R.id.img_back) ;
        tv_emptyState = findViewById(R.id.tv_empty_state) ;

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Utils.getInternetConnectivity(SearchOrderActivity.this)){

                    finish();

                }else{

                    Toast.makeText(SearchOrderActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();

                }

            }
        });


        layoutManagerSearchItems = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManagerSearchItems).setOrientation(RecyclerView.VERTICAL);

        rv_searchList.setLayoutManager(layoutManagerSearchItems) ;

        ordersList = new ArrayList<>() ;

        Client client = new Client(ALGOLIA_APPLICATION_ID,ALGOLIA_ADMIN_API_KEY );
        final Index index = client.getIndex("Orders");


        et_search = findViewById(R.id.et_search) ;

        et_search.requestFocus() ;

        et_search.addTextChangedListener(new TextWatcher() {

            CountDownTimer timer = null;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (timer != null) {
                    timer.cancel();
                }

                timer = new CountDownTimer(900, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {

                        Utils.setVisibilityVisible(progress);
                        Utils.setVisibilityGone(rv_searchList);

                        Query query = new Query(et_search.getText().toString())
                                .setAttributesToRetrieve("order_id","phone_number", "email", "name","order_amount")
                                .setHitsPerPage(50);
                        index.searchAsync(query, new CompletionHandler() {
                            @Override
                            public void requestCompleted(JSONObject content, AlgoliaException error) {

                                if(error != null){

                                    Log.d("AlgoliaError", error.getMessage()) ;

                                }

                                if(ordersList.size() > 0 ){

                                    ordersList.clear();
                                    searchOrderAdapter.notifyDataSetChanged();
                                }

                                try{

                                    Log.d("Result", content.toString()) ;
                                    JSONArray jsonArray = content.getJSONArray("hits") ;

                                    for(int i=0 ; i<jsonArray.length() ; i++){

                                        JSONObject order = jsonArray.getJSONObject(i) ;

                                        String order_id = order.getString("order_id") ;
                                        String email = order.getString("email") ;
                                        String name = order.getString("name") ;
                                        String phone_number = order.getString("phone_number") ;
                                        double order_amount = order.getDouble("order_amount") ;

                                        SearchOrderItem orderItem = new SearchOrderItem(name,phone_number,email,order_id,order_amount) ;
                                        ordersList.add(orderItem) ;

                                    }

                                    searchOrderAdapter = new SearchOrderAdapter(SearchOrderActivity.this, ordersList) ;
                                    rv_searchList.setAdapter(searchOrderAdapter);

                                    Utils.setVisibilityGone(progress);

                                    if(!ordersList.isEmpty()){

                                        Utils.setVisibilityVisible(rv_searchList);
                                        Utils.setVisibilityGone(tv_emptyState);

                                    }else{

                                        Utils.setVisibilityVisible(tv_emptyState);
                                        Utils.setVisibilityGone(rv_searchList) ;
                                    }




                                }


                                catch (Exception e){

                                    Log.d("JSONERROR", e.getMessage()) ;

                                }

                            }
                        });

                    }

                }.start();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }

}
