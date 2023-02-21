package com.example.fooddelivery.storeFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fooddelivery.MainRegister;
import com.example.fooddelivery.Model.Food;
import com.example.fooddelivery.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Add_Food extends AppCompatActivity {


    EditText foodName,price;
    ShapeableImageView imageView;
    AppCompatButton btnCancel,btnSave;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");
    ProgressDialog mDialog =null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        foodName = (EditText) findViewById(R.id.food_Name);
        price = (EditText) findViewById(R.id.price_food);

        imageView =(ShapeableImageView) findViewById(R.id.img_food_edit);
        btnCancel = (AppCompatButton) findViewById(R.id.buttonCanCel);
        btnSave = (AppCompatButton) findViewById(R.id.buttonSave);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food_List_Store_Fragment.foodEdit = new Food();
                finish();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long idFood = Food_List_Store_Fragment.foodEdit.getId();


                if(price.getText().toString().contains(".")){
                    if((foodName.getText().toString().length() > 0) && (price.getText().toString().length()> 0)
                    ){



                        databaseReference.child("Foods").addListenerForSingleValueEvent(new ValueEventListener() {




                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {




                                mDialog = new ProgressDialog(Add_Food.this);
                                mDialog.setCanceledOnTouchOutside(false);
                                mDialog.setCancelable(false);
                                mDialog.setMessage("Adding Food.....");
                                mDialog.show();


                                long id =   snapshot.getChildrenCount() +1;
                                String imgUrl = "https://firebasestorage.googleapis.com/v0/b/food-delivery-21dff.appspot.com/o/Food1.png?alt=media&token=2ed68ed6-96f1-4d94-b075-e22d4afd3192";
                                Food newFood = new Food("",id,imgUrl,foodName.getText().toString(),Float.valueOf(price.getText().toString()),5L);
                                databaseReference.child("Foods").child(String.valueOf(id)).setValue(newFood);
                                Toast.makeText(Add_Food.this,"Adding Success",Toast.LENGTH_SHORT).show();

                                mDialog.cancel();

                                }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Add_Food.this, "Adding Failed", Toast.LENGTH_SHORT).show();
                                mDialog.cancel();
                            }
                        });

                        finish();
                    }
                    else{
                        Toast.makeText(Add_Food.this, "Please Enter Infor", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Add_Food.this, "Please Enter Float In Price", Toast.LENGTH_SHORT).show();
                }






            }
        });




    }
}