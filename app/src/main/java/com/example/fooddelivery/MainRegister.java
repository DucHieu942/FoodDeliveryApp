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

import com.example.fooddelivery.Model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainRegister extends AppCompatActivity {
    TextInputEditText userName,fullName, passWord,email,phone,cfPass;
    FirebaseAuth fAuth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");
    FirebaseDatabase firebaseDatabase;
    String userNameText ,fullNameText, passWordText,emailText,phoneText,cfPassText;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]";
    String regex = "/^([^0-9]*)$/";
    Boolean isvalid=false,isvalidUserName=false,isvalidFullName=false,isvalidPassWord=false,ischeckLogin=false,isvalidEmail=false,isvalidPhone=false,isvalidCfPass=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);
        try{

            userName = (TextInputEditText) findViewById(R.id.username);
            fullName = (TextInputEditText) findViewById(R.id.fullName);
            passWord = (TextInputEditText) findViewById(R.id.password);
            email = (TextInputEditText) findViewById(R.id.email);
            phone = (TextInputEditText) findViewById(R.id.phone);
            cfPass = (TextInputEditText) findViewById(R.id.fullName);
            Button buttonSignIn = (Button) findViewById(R.id.buttonToLoginView);
            Button buttonSignUp =  (Button) findViewById(R.id.buttonSignUp);

            buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainRegister.this,MainLogin.class);
                startActivity(intent);
                finish();
            }
            });

            buttonSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userNameText = userName.getText().toString().trim();
                    fullNameText = fullName.getText().toString().trim();
                    passWordText = passWord.getText().toString().trim();
                    emailText = email.getText().toString().trim();
                    phoneText = phone.getText().toString().trim();
                    cfPassText = cfPass.getText().toString().trim();
//                    Toast.makeText(MainRegister.this,"Email:"+emailText,Toast.LENGTH_LONG).show();

                    if(isvalid()){
                        final ProgressDialog mDialog = new ProgressDialog(MainRegister.this);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.setCancelable(false);
                        mDialog.setMessage("Sign Up Please Wait.....");
                        mDialog.show();

                        //Gửi thông tin đăng nhập
                        isCheckRegister(mDialog);

                    }
                }
            });





        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
        }

    }

    public  boolean isvalid(){

        if(TextUtils.isEmpty(userNameText) && TextUtils.isEmpty(passWordText) && TextUtils.isEmpty(fullNameText)
                && TextUtils.isEmpty(phoneText)  && TextUtils.isEmpty(emailText)
                && TextUtils.isEmpty(cfPassText)){
            Toast.makeText(MainRegister.this,"Please enter infor",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        // Check valid username
        if(TextUtils.isEmpty(userNameText)){
            Toast.makeText(MainRegister.this,"Please enter username",Toast.LENGTH_SHORT).show();

        }else{
            isvalidUserName = true;
        }
        //Check valid fullName
        if(TextUtils.isEmpty(fullNameText)){
            Toast.makeText(MainRegister.this,"Please enter fullname",Toast.LENGTH_SHORT).show();

        }else{
            if(!fullNameText.matches(regex)){
                isvalidFullName = true;
            }
            else {
                Toast.makeText(MainRegister.this,"Invalid fullname",Toast.LENGTH_SHORT).show();
            }
        }
        // Check valid password
        if(TextUtils.isEmpty(passWordText)){
            Toast.makeText(MainRegister.this,"Please enter password",Toast.LENGTH_SHORT).show();
        }else{
            isvalidPassWord = true;
        }
        // Check valid Email
        if(TextUtils.isEmpty(emailText)){
            Toast.makeText(MainRegister.this,"Please enter email",Toast.LENGTH_SHORT).show();
        }else{
            if(emailText.contains("@")){
                isvalidEmail = true;
            }
            else {
                Toast.makeText(MainRegister.this,"Invalid email",Toast.LENGTH_SHORT).show();
            }
        }
        // Check valid Phone
        if(TextUtils.isEmpty(emailText)){
            Toast.makeText(MainRegister.this,"Please enter phone",Toast.LENGTH_SHORT).show();
        }else{
            isvalidPhone = true;
        }
        // Check valid ConfirmPass
        if(TextUtils.isEmpty(cfPassText)){
            Toast.makeText(MainRegister.this,"Please enter Confirm Password",Toast.LENGTH_SHORT).show();
        }else{
            if(!TextUtils.isEmpty(passWordText)){
                if(passWordText.equals(cfPassText)){
                    isvalidCfPass = true;
                }else{
                    Toast.makeText(MainRegister.this,"Confirm Password don't match",Toast.LENGTH_SHORT).show();
                }
            }
        }

        isvalid = (isvalidUserName && isvalidPassWord &&isvalidEmail &&isvalidCfPass &&isvalidPhone &&isvalidFullName)?true:false;
        return isvalid;
    }

    public void isCheckRegister(ProgressDialog mDialog ){
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                    Boolean isCheckPhone = false;
                    Boolean isCheckUserName = false;
                    mDialog.cancel();

                    for(DataSnapshot snapshot :dataSnapShot.getChildren() ){
                        //Check xem số điện thoại đã tồn tại hay chưa
//                        System.out.println("Key"+snapshot.getKey());
                        String phone = snapshot.child("phone").getValue(String.class).toString().trim();
                        String username =snapshot.getKey().toString();

                        if(phone.equals(phoneText)){
                            Toast.makeText(MainRegister.this,"Number have been used",Toast.LENGTH_SHORT).show();
                            isCheckPhone = true;
                            mDialog.cancel();
                            break;
                        }
                        if(username.equals(userNameText)){
                            Toast.makeText(MainRegister.this,"UserName already exists",Toast.LENGTH_SHORT).show();
                            isCheckUserName = true;
                            mDialog.cancel();
                            break;
                        }

                    }
                    // Nếu chưa tồn tại số điện thoại thì cho phép đăng ký
                    if(!isCheckUserName && !isCheckPhone){
                        User user = new User(userNameText,phoneText,emailText,fullNameText,passWordText,null);
                        databaseReference.child("Users").child(userNameText).setValue(user);
//                        databaseReference.child("Users").child(userNameText).child("phone").setValue(phoneText);
//                        databaseReference.child("Users").child(userNameText).child("email").setValue(emailText);
//                        databaseReference.child("Users").child(userNameText).child("fullname").setValue(fullNameText);
//                        databaseReference.child("Users").child(userNameText).child("password").setValue(passWordText);
                        Toast.makeText(MainRegister.this,"Successful Register",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainRegister.this,MainLogin.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainRegister.this,error.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
    }

}