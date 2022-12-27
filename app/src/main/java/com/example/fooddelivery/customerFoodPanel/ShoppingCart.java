package com.example.fooddelivery.customerFoodPanel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.fooddelivery.Model.Food;
import com.example.fooddelivery.R;
import com.example.fooddelivery.adapters.FoodInCartAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart extends AppCompatActivity {

    RecyclerView foodRec;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");
    List<Food> foodListCart = new ArrayList<>();
    List<Integer> foodListCartId = new ArrayList<>();
    FoodInCartAdapter foodInCartAdapter;
    String UUID;
    String userName;
    Button btnBack,btnConfirm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_customer_cart);
        btnBack = (Button) findViewById(R.id.buttonBack) ;
        btnConfirm = (Button) findViewById(R.id.buttonConFirm);


        UUID =getIntent().getStringExtra("UUID");
        userName= getIntent().getStringExtra("username");

        foodRec = findViewById(R.id.listfoodincart);
        foodRec.setHasFixedSize(true);
        foodRec.setLayoutManager(new LinearLayoutManager(this));
        foodInCartAdapter = new FoodInCartAdapter(ShoppingCart.this,foodListCart);
        foodRec.setAdapter(foodInCartAdapter);



        Toast.makeText(this,"UUID đây này: "+UUID,Toast.LENGTH_SHORT).show();

        databaseReference.child("Order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String uuid = dataSnapshot.child("orderparent_id").getValue(String.class).trim();
                    if(uuid.equals(UUID)) {
                        Integer food_id = dataSnapshot.child("food_id").getValue(Integer.class);
                        foodListCartId.add(food_id);
                        databaseReference.child("Foods").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                               Food food = snapshot.child(food_id.toString()).getValue(Food.class);
                               if(!foodListCart.contains(food)) {

                                   food.setCount(1);
                                   foodListCart.add(food);
                               }
                                    foodInCartAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    };
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}