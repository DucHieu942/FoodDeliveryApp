package com.example.fooddelivery.storeFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddelivery.CustomerFoodPanel_BottomNavigation;
import com.example.fooddelivery.Model.Food;
import com.example.fooddelivery.Model.Order;
import com.example.fooddelivery.Model.Orderparent;
import com.example.fooddelivery.R;
import com.example.fooddelivery.adapters.FoodInCartAdapter;
import com.example.fooddelivery.customerFoodPanel.CustomerHomeFragment;
import com.example.fooddelivery.customerFoodPanel.ShoppingCart;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DetailOrder extends AppCompatActivity {

    RecyclerView foodRec;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");
    Integer amountTotal =0;
    Float priceTotal =0F;
    FoodInCartAdapter foodInCartAdapter;
    ProgressDialog mDialog = null;
    Button btnCancel,btnConfirm;
    TextView amountView, totalView;
    private TextView editText;
    public List<Food> foodListOrder = new ArrayList<>();
    Integer amountFood =0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        btnCancel = (Button) findViewById(R.id.buttonCanCel) ;
        btnConfirm = (Button) findViewById(R.id.buttonSave);
        amountView =(TextView) findViewById(R.id.textAmountFood);
        totalView =(TextView) findViewById(R.id.textTotalPrice);
        editText = (TextView) findViewById(R.id.addressEdit);


        foodRec = findViewById(R.id.listfoodinorder);
        foodRec.setHasFixedSize(true);
        foodRec.setLayoutManager(new LinearLayoutManager(this));

        String idOrderparent = getIntent().getStringExtra("idorderparent");
        Long amountorderparent = Long.valueOf(getIntent().getStringExtra("amountorderparent"));
        Float priceorderparent = Float.valueOf(getIntent().getStringExtra("priceorderparent"));
        String address = getIntent().getStringExtra("address");

        amountView.setText(amountorderparent.toString());
        totalView.setText(priceorderparent.toString()+"$");
        editText.setText(address);


        foodInCartAdapter = new FoodInCartAdapter(DetailOrder.this,foodListOrder,
                new FoodInCartAdapter.IListenerIncrease(){

                    @Override
                    public void onClickIncrease(Food food, int position) {
                    }
                },
                new FoodInCartAdapter.IListenerDecrease(){

                    @Override
                    public void onClickDecrease(Food food, int position) {
                    }
                });
        foodRec.setAdapter(foodInCartAdapter);


        getData(idOrderparent);



        // Xét sự kiện click vào nút Cancel thì xóa hết danh sách các món ăn đã chọn
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodListOrder = new ArrayList<>();
               finish();

            }
        });


        // Xét sự kiện ấn Confirm thì sẽ gửi danh sách đơn hàng
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("OrderParent").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child("OrderParent").child(idOrderparent).child("status").setValue("confirmed");
                        Toast.makeText(DetailOrder.this,"Confirm Order",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                finish();
            }
        });







    }

    public  void getData(String idOrderparent){
        databaseReference.child("Order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                mDialog = new ProgressDialog(DetailOrder.this);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setCancelable(false);
                mDialog.setMessage("Loading Data.....");
                mDialog.show();


                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    Order orderFireBase = dataSnapshot.getValue(Order.class);

                    if(orderFireBase.getOrderparent_id().toString().equals(idOrderparent)){

                        databaseReference.child("Foods").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                    Food foodFireBase = dataSnapshot.getValue(Food.class);
                                    if(orderFireBase.getFood_id() ==  foodFireBase.getId()){

                                        foodFireBase.setCount(orderFireBase.getCount());
                                        foodListOrder.add(foodFireBase);
                                        foodInCartAdapter.notifyDataSetChanged();
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

                mDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailOrder.this, "Loading Data Failed", Toast.LENGTH_SHORT).show();
                mDialog.cancel();
            }
        });


    }

}