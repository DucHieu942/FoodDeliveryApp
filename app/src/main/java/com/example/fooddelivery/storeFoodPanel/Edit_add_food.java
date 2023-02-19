package com.example.fooddelivery.storeFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fooddelivery.Model.Food;
import com.example.fooddelivery.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

public class Edit_add_food extends AppCompatActivity {


    EditText foodName,price;
    ShapeableImageView imageView;
    AppCompatButton btnCancel,btnSave;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_add_food);

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


                                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                                    Food foodFireBase = dataSnapshot.getValue(Food.class);
                                    if(foodFireBase.getId().intValue() == idFood.intValue() ){
                                        System.out.println("Đang thay đổi");
                                        System.out.println("Tên: "+ foodName.getText().toString());
                                        databaseReference.child("Foods").child(foodFireBase.getId().toString())
                                                .child("name").setValue(foodName.getText().toString());
                                        databaseReference.child("Foods").child(foodFireBase.getId().toString())
                                                .child("price").setValue(Float.valueOf(price.getText().toString()));
                                        break;
                                    }
                                }

                                Toast.makeText(Edit_add_food.this,"Update Success",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Edit_add_food.this, "Update Failed", Toast.LENGTH_SHORT).show();
                            }
                        });


                        Food_List_Store_Fragment.foodEdit = new Food();
                        finish();
                    }
                    else{
                        Toast.makeText(Edit_add_food.this, "Please Enter Infor", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Edit_add_food.this, "Please Enter Float In Price", Toast.LENGTH_SHORT).show();
                }






            }
        });

        setData();
    }





    public void setData() {
        if(Food_List_Store_Fragment.foodEdit.getName() != null){
            foodName.setText(Food_List_Store_Fragment.foodEdit.getName());
        }
        if(Food_List_Store_Fragment.foodEdit.getPrice() != null){
            price.setText(Food_List_Store_Fragment.foodEdit.getPrice().toString());
        }
        if(Food_List_Store_Fragment.foodEdit.getImgUrl()!= null){
            String uri = Food_List_Store_Fragment.foodEdit.getImgUrl();
            Picasso.get().load(uri).resize(1500, 844).into(imageView);
        }
    }
}