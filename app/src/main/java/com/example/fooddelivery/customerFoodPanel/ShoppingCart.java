package com.example.fooddelivery.customerFoodPanel;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddelivery.CustomerFoodPanel_BottomNavigation;
import com.example.fooddelivery.Model.Food;
import com.example.fooddelivery.Model.Order;
import com.example.fooddelivery.Model.Orderparent;
import com.example.fooddelivery.R;
import com.example.fooddelivery.adapters.FoodInCartAdapter;
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

public class ShoppingCart extends AppCompatActivity implements CustomerHomeFragment.OnBackPressedListener {

    RecyclerView foodRec;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");
    public static List<Food> foodListCart = new ArrayList<>();
    Integer amountTotal =0;
    Float priceTotal =0F;
    FoodInCartAdapter foodInCartAdapter;
    ProgressDialog mDialog = null;
    Button btnCancel,btnConfirm;
    TextView amountView, totalView;
    ShapeableImageView homePage;
    private TextView editText;
    private Dialog dialog;
    Integer count=1;

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shopping_cart);
        btnCancel = (Button) findViewById(R.id.buttonCanCel) ;
        btnConfirm = (Button) findViewById(R.id.buttonSave);
        amountView =(TextView) findViewById(R.id.textAmountFood);
        totalView =(TextView) findViewById(R.id.textTotalPrice);
        editText = (TextView) findViewById(R.id.addressEdit);
        homePage =(ShapeableImageView) findViewById(R.id.imagelogo);

//        UUID =getIntent().getStringExtra("UUID");
//        userName= getIntent().getStringExtra("username");


        foodRec = findViewById(R.id.listfoodincart);
        foodRec.setHasFixedSize(true);
        foodRec.setLayoutManager(new LinearLayoutManager(this));
        foodListCart = CustomerHomeFragment.foodListCart;


        initInforOrder();

        foodInCartAdapter = new FoodInCartAdapter(ShoppingCart.this,foodListCart,
                new FoodInCartAdapter.IListenerIncrease(){

                    @Override
                    public void onClickIncrease(Food food, int position) {
                        count = food.getCount();
                        count += 1;
                        food.setCount(count);
//                        System.out.println("Số lượng món ăn: "+food.getCount());
                        foodInCartAdapter.notifyDataSetChanged();
                        amountTotal = amountTotal+1;
                        priceTotal = priceTotal + ((food.getPrice())*1);
                        amountView.setText(amountTotal.toString());
                        totalView.setText(priceTotal+"$");
                    }
                },
                new FoodInCartAdapter.IListenerDecrease(){

                    @Override
                    public void onClickDecrease(Food food, int position) {
                        count = food.getCount();
                        if(count <=0 ){
                            food.setCount(0);
                            foodInCartAdapter.notifyDataSetChanged();
//                            amountTotal = amountTotal-1;
//                            priceTotal = priceTotal + (food.getPrice())*(food.getCount());
//                            amountView.setText(amountTotal.toString());
//                            totalView.setText(priceTotal+"$");
                        }else {
                            count = count-1;
                            food.setCount(count);
                            foodInCartAdapter.notifyDataSetChanged();
                            amountTotal = amountTotal-1;
                            priceTotal = priceTotal - ((food.getPrice())*1);
                            amountView.setText(amountTotal.toString());
                            totalView.setText(priceTotal+"$");
                        }
                    }
                });
        foodRec.setAdapter(foodInCartAdapter);


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edittext);



        Button buttonSave = dialog.findViewById(R.id.button_save);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 EditText editAddress =(EditText)dialog.findViewById(R.id.edit_text_dialog);
                if(editAddress.getText().toString().replaceAll("[^a-zA-Z0-9]", "").length()>0){
                    String address = ((EditText)dialog.findViewById(R.id.edit_text_dialog)).getText().toString();
                    editText.setText(address);
                    dialog.dismiss();
                }else{
                    Toast.makeText(ShoppingCart.this,"Please enter address",Toast.LENGTH_SHORT).show();
                }

            }
        });

// Xét sự kiện click vào logo thì quay trở về trang chủ
        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


// Xét sự kiện click vào nút Cancel thì xóa hết danh sách các món ăn đã chọn
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodListCart = new ArrayList<>();
                CustomerHomeFragment.foodListCart = foodListCart;
                onBackPressed();

            }
        });
// Xét sự kiện click vào Address thì hiện ra cửa sổ để nhập địa chỉ
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

// Xét sự kiện ấn Confirm thì sẽ gửi danh sách đơn hàng
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               if(editText.getText().toString().length() > 0) {
                   List<Food> foodListOrder = foodInCartAdapter.listFoodCart;



                   Calendar calendar = Calendar.getInstance();
                   Date now = calendar.getTime();
                   SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                   formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                   String order_time = formatter.format(now);

                   Calendar calendar_2 = Calendar.getInstance();
                   calendar.add(calendar_2.HOUR_OF_DAY, 1);
                   Date now_plus1h = calendar_2.getTime();
                   String require_time = formatter.format(now_plus1h);

                   Float sum_price = priceTotal;
                   Long amountOrder = Long.valueOf(amountTotal);
                   String status ="unconfirmed";
                   String address_ship = String.valueOf(editText.getText());
                   String customer_name = CustomerFoodPanel_BottomNavigation.userNameLogin;
                   String phonenumber = CustomerFoodPanel_BottomNavigation.phoneNumber;


                   System.out.println("Số điện thoại: "+CustomerFoodPanel_BottomNavigation.phoneNumber);

                   String id = (customer_name+"-"+order_time).replaceAll("[^a-zA-Z0-9\\s+]", "");


                   Orderparent orderparent = new Orderparent(id,customer_name,now,
                           now_plus1h,address_ship,null,status,sum_price,amountOrder,phonenumber);
                   mDialog =new ProgressDialog(ShoppingCart.this);
                   mDialog.setCanceledOnTouchOutside(false);
                   mDialog.setCancelable(false);
                   mDialog.setMessage("Sign In Please Wait.....");
                   mDialog.show();

                   onCreateOrderParent(orderparent);

                   for(Food foodOrder:foodListOrder){
                       System.out.println("Id món: "+foodOrder.getId());
                       System.out.println("Số lượng món: "+foodOrder.getCount());
                       String idOder =foodOrder.getId()+id;
                       Long food_id= foodOrder.getId();
                       Float price = foodOrder.getPrice()*foodOrder.getCount() ;
                       Integer count = foodOrder.getCount();
                       Order order = new Order(idOder,id,food_id,price,count);
                       onCreateOrder(order);
                   }
                   foodListCart = new ArrayList<>();
                   CustomerHomeFragment.foodListCart = foodListCart;
                   onBackPressed();
               }
               else{
                   Toast.makeText(ShoppingCart.this,"Please enter address",Toast.LENGTH_SHORT).show();
               }

            }
        });


// Hàm khởi tạo các giá trị hiển thị ban đầu cho đơn
    }
    public void initInforOrder (){
        for(Food food:foodListCart){
            if(food.getCount()==null){
                food.setCount(count);
            }
            amountTotal = amountTotal+food.getCount();
            priceTotal = priceTotal + (food.getPrice())*(food.getCount());

        }
        amountView.setText(amountTotal.toString());
        totalView.setText(priceTotal+"$");
    }

//Hàm ấn xử lí việc khởi tạo lại CustomerHomeFragment
    @Override
    public void onBackPressed() {
        // Khởi tạo lại CustomerHomeFragment
//        CustomerHomeFragment customerHomeFragment = new CustomerHomeFragment();
//        CustomerOrderFragment customerOrderFragment = new CustomerOrderFragment();
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container_customer, customerHomeFragment, "CustomerHomeFragment")
//                .commit();

        finish();

    }

//Hàm tạo đơn hàng tổng trên FireBase
private void onCreateOrderParent(Orderparent orderparent) {


    databaseReference.child("OrderParent").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
             String id = orderparent.getId();
            databaseReference.child("OrderParent").child(id).setValue(orderparent);
            Toast.makeText(ShoppingCart.this,"Successful Order",Toast.LENGTH_SHORT).show();
            System.out.println("Có đẩy lên FireBase");
            mDialog.cancel();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            System.out.println("Không đẩy lên được FireBase");
            Toast.makeText(ShoppingCart.this,"Wrong Order",Toast.LENGTH_SHORT).show();
            mDialog.cancel();
        }
    });

}

//Hàm tạo chi tiết đơn hàng tổng trên FireBase
private void onCreateOrder(Order order){


    databaseReference.child("Order").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            String id = order.getId();
            databaseReference.child("Order").child(id).setValue(order);
            Toast.makeText(ShoppingCart.this,"Successful Order",Toast.LENGTH_SHORT).show();
            mDialog.cancel();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(ShoppingCart.this,"Wrong Order",Toast.LENGTH_SHORT).show();
            mDialog.cancel();
        }
    });
}

}