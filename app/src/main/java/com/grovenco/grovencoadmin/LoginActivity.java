package com.grovenco.grovencoadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText email, pass;
    TextView login;
    String emailid, password;
    int flag = 1;
    int signed_in = 0 ;
    ProgressDialog p ;


    @Override
    protected void onStart() {
        super.onStart();

        if(auth.getCurrentUser()!=null)
        {
            signed_in=1 ;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        p = new ProgressDialog(LoginActivity.this);
        p.setMessage("Loading...");
        p.setCancelable(false);


        link();


        if((signed_in==1)||(auth.getCurrentUser()!=null))
        {
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));

        }


               login.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       p.show();

                       flag = 1;

                       check(email, pass);


                       if (flag == 0) {
                           if(p.isShowing()){
                               p.dismiss();
                           }

                           Toast.makeText(LoginActivity.this, "Please Enter necessary fields", Toast.LENGTH_SHORT).show();

                       } else {


                           auth.signInWithEmailAndPassword(emailid, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if (task.isSuccessful()) {

                                       if(p.isShowing()){
                                           p.dismiss();
                                       }

                                       Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                       startActivity(i);

                                   } else {

                                       if(p.isShowing()){
                                           p.dismiss();
                                       }
                                       Exception e = task.getException();
                                       Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });


                       }
                   }


               });



           }







    private void link() {

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.editname);
        pass = findViewById(R.id.editpass);
        login = findViewById(R.id.loginbutton);
    }

    private int check(EditText email, EditText pass) {

        emailid = email.getText().toString().trim();
        password = pass.getText().toString().trim();

        if (TextUtils.isEmpty(emailid)) {
            email.setError("Please Enter Email Id");
            email.requestFocus();
            flag = 0;
        }
        if (TextUtils.isEmpty(password)) {
            pass.setError("Please Enter Password");
            pass.requestFocus();
            flag = 0;
        }

        return flag;

    }
}
