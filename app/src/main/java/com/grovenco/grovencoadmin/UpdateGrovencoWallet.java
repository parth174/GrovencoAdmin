package com.grovenco.grovencoadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UpdateGrovencoWallet extends AppCompatActivity {

    TextView tv_phoneNumber, tv_wallet ;
    EditText et_phoneNumber, et_wallet ;
    Button btn_update ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_grovenco_wallet);

        tv_phoneNumber = findViewById(R.id.tv_phone_number) ;
        tv_wallet = findViewById(R.id.tv_wallet) ;

        et_phoneNumber = findViewById(R.id.et_phone_number) ;
        et_wallet = findViewById(R.id.et_wallet) ;

        btn_update = findViewById(R.id.btn_update) ;

        et_wallet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_wallet);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Utils.setVisibilityGone(tv_phoneNumber);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(et_phoneNumber.getText()) && !TextUtils.isEmpty(et_wallet.getText())){

                    String phone_number = et_phoneNumber.getText().toString() ;
                    final double wallet_amount = Double.valueOf(et_wallet.getText().toString()) ;

                    FirebaseUtil.users_ref.whereEqualTo("phone_number", phone_number).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if(task.getResult() != null){

                                if(!task.getResult().isEmpty()){

                                    for(DocumentSnapshot document : task.getResult().getDocuments()){

                                        String uid =  document.get("uid").toString() ;

                                        FirebaseUtil.getUserRef(uid).update("grovenco_wallet", wallet_amount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                Toast.makeText(UpdateGrovencoWallet.this, "Wallet amount updated", Toast.LENGTH_SHORT).show();

                                            }
                                        }) ;

                                    }

                                }else{

                                    Toast.makeText(UpdateGrovencoWallet.this, "Doc not found", Toast.LENGTH_SHORT).show();

                                }

                            }else{

                                Toast.makeText(UpdateGrovencoWallet.this, "Doc not found empty", Toast.LENGTH_SHORT).show();

                                if(task.getException() != null){

                                    Log.d("FirebaseErrorWallet", task.getException().toString()) ;

                                }


                            }



                        }
                    }) ;

                }else{

                    if(TextUtils.isEmpty(et_phoneNumber.getText())){

                        Utils.setVisibilityVisible(tv_phoneNumber);

                    }

                    if(TextUtils.isEmpty(et_wallet.getText())){

                        Utils.setVisibilityVisible(tv_wallet);

                    }


                }

            }
        });

    }
}
