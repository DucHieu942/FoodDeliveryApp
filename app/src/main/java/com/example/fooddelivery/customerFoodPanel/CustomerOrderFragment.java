package com.example.fooddelivery.customerFoodPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class CustomerOrderFragment extends Fragment {

    RecyclerView foodRec;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");
    List<Food> foodListCart = new ArrayList<>();
    List<Integer> foodListCartId = new ArrayList<>();
    FoodInCartAdapter foodInCartAdapter;
    String UUID;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_cart, null);
        getActivity().setTitle("Cart");
//        UUID =getArguments().getString("UUID") == null ?"Déo có gì":getArguments().getString("UUID");
//        foodListCartId = getArguments().getIntegerArrayList("foodListCartId") == null ?new ArrayList<>():getArguments().getIntegerArrayList("foodListCartId");
//
//        Toast.makeText(getActivity(),"UUID: "+UUID,Toast.LENGTH_SHORT).show();
//
//
//        if(foodListCartId.size() !=0) {
//            databaseReference.child("Foods").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                    for (Integer idItem : foodListCartId) {
//
//                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                            Food food = dataSnapshot.getValue(Food.class);
//                            Integer id = food.getId().intValue();
//                            if (idItem.equals(id))
//                                foodListCart.add(food);
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//
//
//        foodRec = v.findViewById(R.id.listfoodincart);
//        foodRec.setHasFixedSize(true);
//        foodRec.setLayoutManager(new LinearLayoutManager(getContext()));
////        if(!foodListCartId.isEmpty()){
//
//        Toast.makeText(getActivity(),"UUID: "+UUID,Toast.LENGTH_SHORT).show();
//
//            foodInCartAdapter = new FoodInCartAdapter(getActivity(),foodListCart);
//            foodRec.setAdapter(foodInCartAdapter);
////        }
        return v;
    }
}
