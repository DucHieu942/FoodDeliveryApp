package com.example.fooddelivery.customerFoodPanel.myOrder;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.fooddelivery.Model.Food;
import com.example.fooddelivery.Model.Order;
import com.example.fooddelivery.Model.Orderparent;
import com.example.fooddelivery.R;
import com.example.fooddelivery.adapters.OderParentAdapter;
import com.example.fooddelivery.customerFoodPanel.ShoppingCart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class OngoingFragment extends Fragment {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");
    private RecyclerView orderParentRec;
    private OderParentAdapter oderParentAdapter;
    private List<Orderparent> orderparentList =new ArrayList<>();
//    private List<Orderparent> test = new ArrayList<>();
    String foodName ="";
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ongoing, null);
        orderParentRec = v.findViewById(R.id.listOrderOngoing);
        orderParentRec.setHasFixedSize(true);
        orderParentRec.setLayoutManager(new LinearLayoutManager(getContext()));



        OderParentAdapter.IListenerClickItem cancelOrder;
        cancelOrder = new OderParentAdapter.IListenerClickItem() {
            @Override
            public void onClickItem(Orderparent orderparent) {

                if (orderparent.getStatus().toString().equals("unconfirmed")) {
                    initDiagLogCancelOrder(orderparent);
                    dialog.show();
                }else{
                    initDiagLogCompleteOrder(orderparent);
                    dialog.show();
                }
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

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Orderparent orderParent = dataSnapshot.getValue(Orderparent.class);


                    if(!orderParent.getStatus().equals("complete")){
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
//                                                        if(orderParent.getFoodName() !=null) {
//                                                            foodName = orderParent.getFoodName() + food.getName() + ",";
//                                                        }else{
//                                                            foodName = food.getName() + ",";
//
//                                                        }
                                                        orderParent.setFoodName(food.getName());
                                                        orderParent.setImgUrl(food.getImgUrl());


                                                        orderparentList.add(orderParent);
                                                        oderParentAdapter.notifyDataSetChanged();
                                                        break;

//                                                        if(orderparentList.size()>0) {
//
//                                                            for (Orderparent orderparentItem : orderparentList) {
//                                                                if (orderparentItem.getId().toString().equals(
//                                                                        orderParent.getId().toString()
//                                                                )) {
//                                                                    orderparentItem.setFoodName(foodName);
//                                                                    orderparentList.remove(orderParent);
//                                                                    oderParentAdapter.notifyDataSetChanged();
//                                                                }
//                                                            }
//                                                        }
//                                                        oderParentAdapter.notifyDataSetChanged();
//
//                                                        foodName ="";

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

    private void initDiagLogCancelOrder(Orderparent cancel){
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_cancel_order);
        Button buttonYes = dialog.findViewById(R.id.button_yes);
        Button buttonNo = dialog.findViewById(R.id.button_no);

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("OrderParent").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child("OrderParent").child(cancel.getId().toString()).setValue(null);
                        Toast.makeText(getActivity(),"Canceled Order",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    };

    private  void initDiagLogCompleteOrder(Orderparent complete) {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_complete_order);
        Window window = dialog.getWindow();
        window.setLayout(800, 300);

    Button buttonYes = dialog.findViewById(R.id.button_yes);
    Button buttonNo = dialog.findViewById(R.id.button_no);

        buttonYes.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            databaseReference.child("OrderParent").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    databaseReference.child("OrderParent").child(complete.getId().toString()).child("status").setValue("complete");
                    Toast.makeText(getActivity(),"Complete Order",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    });

        buttonNo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    });

};
}