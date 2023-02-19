package com.example.fooddelivery.storeFoodPanel;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fooddelivery.Model.Food;
import com.example.fooddelivery.Model.Order;
import com.example.fooddelivery.Model.Orderparent;
import com.example.fooddelivery.R;
import com.example.fooddelivery.adapters.OderParentAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Complete_Order_Store_Fragment extends Fragment {



    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");
    private RecyclerView orderParentRec;
    private OderParentAdapter oderParentAdapter;
    private List<Orderparent> orderparentList =new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_complete_order_store, container, false);

        orderParentRec = v.findViewById(R.id.store_orders_complete);
        orderParentRec.setHasFixedSize(true);
        orderParentRec.setLayoutManager(new LinearLayoutManager(getContext()));
        orderparentList =new ArrayList<>();

        OderParentAdapter.IListenerClickItem detail;
        detail = new OderParentAdapter.IListenerClickItem() {
            @Override
            public void onClickItem(Orderparent orderparent) {


                Intent intent = new Intent(getActivity(), DetailOrder.class);
                intent.putExtra("idorderparent", orderparent.getId());
                intent.putExtra("amountorderparent", orderparent.getAmountOrder().toString());
                intent.putExtra("priceorderparent", orderparent.getSum_price().toString());
                System.out.println("1: "+orderparent.getAmountOrder());
                System.out.println("2: "+orderparent.getSum_price());
                startActivity(intent);

//                    initDiagLogCancelOrder(orderparent);
//                    dialog.show();


            }
        };
        oderParentAdapter = new OderParentAdapter(getActivity(),orderparentList,detail);
        orderParentRec.setAdapter(oderParentAdapter);

        getDataFromFireBase();





        return v;
    }


    private void getDataFromFireBase(){
        databaseReference.child("OrderParent").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Orderparent orderParent = dataSnapshot.getValue(Orderparent.class);


                    if(orderParent.getStatus().equals("complete")){
                        databaseReference.child("Order").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                    Order order = dataSnapshot.getValue(Order.class);
                                    if(order.getOrderparent_id().equals(orderParent.getId())){

                                        databaseReference.child("Foods").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                                    Food food = dataSnapshot.getValue(Food.class);
                                                    if(food.getId()== order.getFood_id()){
                                                        orderParent.setFoodName(food.getName());
                                                        orderParent.setImgUrl(food.getImgUrl());
                                                        orderparentList.add(orderParent);
                                                        oderParentAdapter.notifyDataSetChanged();
                                                        break;


                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }


                }
                oderParentAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}