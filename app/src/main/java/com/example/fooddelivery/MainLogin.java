package com.example.fooddelivery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        try{
            userName = (TextInputEditText) findViewById(R.id.userNameInput);
            passWord = (TextInputEditText) findViewById(R.id.password);
            Button buttonSignUp = (Button) findViewById(R.id.buttonToLoginView);
            Button buttonLogin =  (Button) findViewById(R.id.buttonLogin);
            buttonSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainLogin.this,MainRegister.class);
                    startActivity(intent);
                    finish();
                }
            });
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userNameText = userName.getText().toString().trim();
                    passWordText = passWord.getText().toString().trim();
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
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
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
                                startActivity(new Intent(MainLogin.this,CustomerFoodPanel_BottomNavigation.class));
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


}