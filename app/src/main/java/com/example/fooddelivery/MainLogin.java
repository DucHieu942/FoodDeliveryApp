package com.example.fooddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fooddelivery.Model.Customer;
import com.example.fooddelivery.customerFoodPanel.CustomerProfileFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainLogin extends AppCompatActivity {

    TextInputEditText userName, passWord;
    TextInputLayout userNameLayout, passWordLayout;
    FirebaseAuth fAuth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");
    FirebaseDatabase firebaseDatabase;
    String userNameText , passWordText;
    String userNamePattern = "[a-z]";
    Boolean isvalid=false,isvalidUserName=false,isvalidPassWord=false,ischeckLogin=false;
    private MainLogin.OnButtonClickListener listener;
    public static Customer customer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(MainActivity.role ==0){
            setContentView(R.layout.activity_main_login_store);

        }
        else{
            setContentView(R.layout.activity_main_login);
            Button buttonSignUp = (Button) findViewById(R.id.buttonToLoginView);
            buttonSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainLogin.this,MainRegister.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        try{
            userName = (TextInputEditText) findViewById(R.id.userNameInput);
            passWord = (TextInputEditText) findViewById(R.id.password);
            Button buttonLogin =  (Button) findViewById(R.id.btnShipper);
            Button buttonRole = (Button) findViewById(R.id.selectRole);



            buttonRole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainLogin.this, MainSelectRole.class);
                    startActivity(intent);
                }
            });



            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userNameText = userName.getText().toString().trim();
                    passWordText = passWord.getText().toString().trim();
                    System.out.println("Role là: "+ MainActivity.role);
                    if (isValid()) {
                        final ProgressDialog mDialog = new ProgressDialog(MainLogin.this);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.setCancelable(false);
                        mDialog.setMessage("Sign In Please Wait.....");
                        mDialog.show();
                        //Check thông tin xem đúng không
                        isCheckLogin(mDialog);
                    }
                }
            });
        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
        }

    }

// Hàm check Valid userName and Password
    public boolean isValid(){
        // Check valid username and password
        if(TextUtils.isEmpty(userNameText) && TextUtils.isEmpty(passWordText)){
            Toast.makeText(MainLogin.this,"Please enter infor",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        // Check valid username
        if(TextUtils.isEmpty(userNameText)){
            Toast.makeText(MainLogin.this,"Please enter username",Toast.LENGTH_SHORT).show();

        }else{
            isvalidUserName = true;
        }
        // Check valid password
        if(TextUtils.isEmpty(passWordText)){
            Toast.makeText(MainLogin.this,"Please enter password",Toast.LENGTH_SHORT).show();
        }else{
            isvalidPassWord = true;
        }
        isvalid = (isvalidUserName && isvalidPassWord)?true:false;
        return isvalid;
    }


    public void isCheckLogin(ProgressDialog mDialog ){
        //check data to firebase Realtime Database,
        //Check theo userName
        if(MainActivity.role == 0){
            databaseReference.child("Stores").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Check username in firebase
                    if(snapshot.hasChild(userNameText)){
                        final String getPassWord = snapshot.child(userNameText).child("password").getValue(String.class);
                        if(!getPassWord.equals(passWordText)){
                            Toast.makeText(MainLogin.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                            mDialog.cancel();
                        }else{
                            ischeckLogin = true;
                            Toast.makeText(MainLogin.this,"Successful Login",Toast.LENGTH_SHORT).show();
                            mDialog.cancel();
                            Intent intent = new Intent(MainLogin.this,StoreFoodPanel_BottomNavigation.class);
                            intent.putExtra("UserLogin", userNameText);
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        Toast.makeText(MainLogin.this,"Wrong UserName",Toast.LENGTH_SHORT).show();
                        mDialog.cancel();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainLogin.this,error.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
            );
        }

        if(MainActivity.role == 1){
            databaseReference.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Check username in firebase
                    if(snapshot.hasChild(userNameText)){
                        final String getPassWord = snapshot.child(userNameText).child("password").getValue(String.class);
                        final String phonenumber= snapshot.child(userNameText).child("phonenumber").getValue(String.class);
                        customer = snapshot.child(userNameText).getValue(Customer.class);
                        if(!getPassWord.equals(passWordText)){
                            Toast.makeText(MainLogin.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                            mDialog.cancel();
                        }else{
                            ischeckLogin = true;
                            Toast.makeText(MainLogin.this,"Successful Login",Toast.LENGTH_SHORT).show();
                            mDialog.cancel();
                            Intent intent = new Intent(MainLogin.this,CustomerFoodPanel_BottomNavigation.class);
                            intent.putExtra("UserLogin", userNameText);
                            intent.putExtra("PhoneNumber",phonenumber);
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        Toast.makeText(MainLogin.this,"Wrong UserName",Toast.LENGTH_SHORT).show();
                        mDialog.cancel();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainLogin.this,error.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
            );
        }

        if(MainActivity.role == 2){
            databaseReference.child("Shipper").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Check username in firebase
                    if(snapshot.hasChild(userNameText)){
                        final String getPassWord = snapshot.child(userNameText).child("password").getValue(String.class);
                        if(!getPassWord.equals(passWordText)){
                            Toast.makeText(MainLogin.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                            mDialog.cancel();
                        }else{
                            ischeckLogin = true;
                            Toast.makeText(MainLogin.this,"Successful Login",Toast.LENGTH_SHORT).show();
                            mDialog.cancel();
                            Intent intent = new Intent(MainLogin.this,CustomerFoodPanel_BottomNavigation.class);
                            intent.putExtra("UserLogin", userNameText);
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        Toast.makeText(MainLogin.this,"Wrong UserName",Toast.LENGTH_SHORT).show();
                        mDialog.cancel();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainLogin.this,error.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
            );
        }




//        databaseReference.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        // Check username in firebase
//                        if(snapshot.hasChild(userNameText)){
//                            final String getPassWord = snapshot.child(userNameText).child("password").getValue(String.class);
//                            if(!getPassWord.equals(passWordText)){
//                                Toast.makeText(MainLogin.this,"Wrong Password",Toast.LENGTH_SHORT).show();
//                                mDialog.cancel();
//                            }else{
//                                ischeckLogin = true;
//                                Toast.makeText(MainLogin.this,"Successful Login",Toast.LENGTH_SHORT).show();
//                                mDialog.cancel();
//                                Intent intent = new Intent(MainLogin.this,CustomerFoodPanel_BottomNavigation.class);
//                                intent.putExtra("UserLogin", userNameText);
//                                startActivity(intent);
//                                finish();
//                            }
//                        }else{
//                            Toast.makeText(MainLogin.this,"Wrong UserName",Toast.LENGTH_SHORT).show();
//                            mDialog.cancel();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(MainLogin.this,error.getMessage(),Toast.LENGTH_LONG).show();
//                    }
//                }
//        );
    }

    public interface OnButtonClickListener {
        void onButtonClicked();
    }


}