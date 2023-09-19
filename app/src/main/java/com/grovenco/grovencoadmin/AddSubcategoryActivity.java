package com.grovenco.grovencoadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddSubcategoryActivity extends AppCompatActivity {

    EditText et_subcategoryName, et_categoryName, et_categoryPosition, et_subcategoryPosition,et_imageName ;
    TextView tv_subcategoryName, tv_categoryName, tv_categoryPosition, tv_subcategoryPosition,tv_imageName ;

    ArrayList<EditText> editTextList ;
    ArrayList<TextView> textViewList ;

    Button btn_add ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subcategory);

        btn_add = findViewById(R.id.btn_add) ;

        et_categoryName = findViewById(R.id.et_category_name) ;
        et_subcategoryName = findViewById(R.id.et_subcategory_name) ;
        et_categoryPosition = findViewById(R.id.et_category_position) ;
        et_subcategoryPosition = findViewById(R.id.et_subcategory_position) ;
        et_imageName = findViewById(R.id.et_image_name) ;

        tv_categoryName = findViewById(R.id.tv_category_name) ;
        tv_subcategoryName = findViewById(R.id.tv_subcategory_name) ;
        tv_categoryPosition = findViewById(R.id.tv_category_position) ;
        tv_subcategoryPosition = findViewById(R.id.tv_subcategory_position) ;
        tv_imageName = findViewById(R.id.tv_image_name) ;

        editTextList = new ArrayList<>() ;
        textViewList = new ArrayList<>() ;

        editTextList.add(et_categoryName );
        editTextList.add(et_subcategoryName);
        editTextList.add(et_categoryPosition);
        editTextList.add(et_subcategoryPosition);
        editTextList.add(et_imageName);


        textViewList.add(tv_categoryName );
        textViewList.add(tv_subcategoryName);
        textViewList.add(tv_categoryPosition);
        textViewList.add(tv_subcategoryPosition);
        textViewList.add(tv_imageName);


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Utils.validate(editTextList, textViewList)){

                    final Map<String,Object> hashmap = new HashMap<>() ;
                    hashmap.put("category_name", et_categoryName.getText().toString());
                    hashmap.put("category_position", Long.valueOf(et_categoryPosition.getText().toString()));
                    hashmap.put("subcategory_name",et_subcategoryName.getText().toString());
                    hashmap.put("subcategory_position" ,Long.valueOf(et_subcategoryPosition.getText().toString()));


                    FirebaseUtil.subcategories_storageRef.child(et_imageName.getText().toString()+".png").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if(task.isSuccessful()){

                                Uri image_name = task.getResult() ;

                                hashmap.put("image_name",image_name.toString());

                                FirebaseUtil.subcategories_ref.add(hashmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                        Toast.makeText(AddSubcategoryActivity.this, "Subcategory Added", Toast.LENGTH_SHORT).show();

                                    }
                                }) ;

                            }

                        }
                    }) ;




                }

            }
        });

    }
}
