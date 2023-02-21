package com.example.fooddelivery.storeFoodPanel;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.fooddelivery.CustomerFoodPanel_BottomNavigation;
import com.example.fooddelivery.MainLogin;
import com.example.fooddelivery.Model.Food;
import com.example.fooddelivery.Model.Order;
import com.example.fooddelivery.Model.Orderparent;
import com.example.fooddelivery.R;
import com.example.fooddelivery.adapters.OderParentAdapter;
import com.example.fooddelivery.customerFoodPanel.CustomerProfileFragment;
import com.example.fooddelivery.customerFoodPanel.ShoppingCart;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Order_Store_Fragment extends Fragment {


    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");
    private RecyclerView orderParentRec;
    private OderParentAdapter oderParentAdapter;
    private List<Orderparent> orderparentList =new ArrayList<>();
    private Dialog dialog;
    ShapeableImageView logout;
    ProgressDialog mDialog = null;



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_store, container, false);

        orderParentRec = v.findViewById(R.id.store_orders);
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
                intent.putExtra("address",orderparent.getAddress_ship());
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



//    private void initDiagLogCancelOrder(Orderparent item){
//        dialog = new Dialog(getActivity());
//        dialog.setContentView(R.layout.dialog_confirm);
//        Window window = dialog.getWindow();
//        window.setLayout(800, 300);
//        Button buttonCf = dialog.findViewById(R.id.button_cf_order);
//        Button buttonCp = dialog.findViewById(R.id.button_cp_order);
//
//        buttonCf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                databaseReference.child("OrderParent").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        databaseReference.child("OrderParent").child(item.getId().toString()).child("status").setValue("confirmed");
//                        Toast.makeText(getActivity(),"Confirm Order",Toast.LENGTH_SHORT).show();
//                        orderparentList = new ArrayList<>();
//                        dialog.dismiss();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//
//
//        });
//
//        buttonCp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                databaseReference.child("OrderParent").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        databaseReference.child("OrderParent").child(item.getId().toString()).child("status").setValue("complete");
//                        Toast.makeText(getActivity(),"Complete Order",Toast.LENGTH_SHORT).show();
//                        orderparentList = new ArrayList<>();
//                        dialog.dismiss();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });
//
//    };



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