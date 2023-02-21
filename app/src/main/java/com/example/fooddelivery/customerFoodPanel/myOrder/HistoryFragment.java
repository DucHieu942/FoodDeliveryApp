package com.example.fooddelivery.customerFoodPanel.myOrder;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fooddelivery.Model.Food;
import com.example.fooddelivery.Model.Order;
import com.example.fooddelivery.Model.Orderparent;
import com.example.fooddelivery.R;
import com.example.fooddelivery.adapters.FoodAdapter;
import com.example.fooddelivery.adapters.OderParentAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryFragment extends Fragment {



    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");
    private RecyclerView orderParentRec;
    private OderParentAdapter oderParentAdapter;
    private List<Orderparent> orderparentList =new ArrayList<>();
    private List<Orderparent> test = new ArrayList<>();
    String foodName ="";
    OderParentAdapter.IListenerClickItem cancelOrder;
    ProgressDialog mDialog = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, null);

        orderParentRec = v.findViewById(R.id.listOrderHistory);
        orderParentRec.setHasFixedSize(true);
        orderParentRec.setLayoutManager(new LinearLayoutManager(getContext()));


        cancelOrder = new OderParentAdapter.IListenerClickItem() {
            @Override
            public void onClickItem(Orderparent orderparent) {

            }
        };

        oderParentAdapter = new OderParentAdapter(getActivity(),orderparentList,cancelOrder);
        orderParentRec.setAdapter(oderParentAdapter);


        getDataFromFireBase();


        return v;
    }


    private void getDataFromFireBase(){
        databaseReference.child("OrderParent").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                mDialog = new ProgressDialog(getActivity());
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setCancelable(false);
                mDialog.setMessage("Loading Data.....");
                mDialog.show();


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
                                                        if(orderParent.getFoodName() !=null) {
                                                            foodName = orderParent.getFoodName() + food.getName() + ",";
                                                        }else{
                                                            foodName = food.getName() + ",";

                                                        }
                                                        orderParent.setFoodName(foodName);
                                                        orderParent.setImgUrl(food.getImgUrl());
                                                        orderparentList.add(orderParent);
                                                        oderParentAdapter.notifyDataSetChanged();
                                                        foodName ="";

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

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
                mDialog.cancel();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Loading Data Failed", Toast.LENGTH_SHORT).show();
                mDialog.cancel();
            }
        });
    }
}