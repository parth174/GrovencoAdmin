package com.grovenco.grovencoadmin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Utils {

    public static String getAmount (long price){
        return "₹"+price ;
    }
    public static String getAmount (double price){

        DecimalFormat df = new DecimalFormat("####0.00");
        return "₹"+df.format(price) ;

    }



    public static Boolean validate(ArrayList<EditText> editTextList ,ArrayList <TextView> tverrorList){

        boolean flag = true ;

        for(int i = 0 ; i<editTextList.size() ; i++){

            if(TextUtils.isEmpty(editTextList.get(i).getText().toString().trim())){

                Utils.setVisibilityVisible(tverrorList.get(i));
                tverrorList.get(i).setText("This field cannot be empty");
                flag = false ;

            }

        }


        return flag ;
    }




    public static void setVisibilityGone(View view){
        if(view.getVisibility()==View.VISIBLE){
            view.setVisibility(View.GONE);
        }
    }

    public static void setVisibilityVisible(View view){
        if(view.getVisibility()==View.GONE || view.getVisibility()==View.INVISIBLE  ){
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void setVisibilityHide(View view){
        if(view.getVisibility()==View.VISIBLE){
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void setDisabled(View view){
        if(view.isEnabled()){
            view.setEnabled(false);
        }
    }

    public static void setEnabled(View view){
        if(!view.isEnabled()){
            view.setEnabled(true);
        }
    }

    public static void setRefreshing(SwipeRefreshLayout swipe){

        if(!swipe.isRefreshing()){

            swipe.setRefreshing(true);

        }

    }


    public static void endRefrishing(SwipeRefreshLayout swipe){

        if(swipe.isRefreshing()){

            swipe.setRefreshing(false);

        }

    }

    public static boolean getInternetConnectivity(Context context){

        boolean isConnected = true ;

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm != null){

            NetworkInfo activeNetwork =   cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

        }


        return isConnected ;
    }

}
