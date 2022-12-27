package com.example.fooddelivery.customerFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddelivery.Model.Customer;
import com.example.fooddelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ChangePassWord extends AppCompatActivity {
    Button buttonConfirm,buttonBack;
    EditText passWordNew,passCf;
    String passWordNewText,passCfText,userNameChange;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");
    Customer user = new Customer();
    TextView userName,email,passWordOld;
    Boolean isvalid = false;
    ImageView avatar,changeAvatar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonConfirm = (Button) findViewById(R.id.buttonConFirm);
        userName = (TextView) findViewById(R.id.userName);
        email = (TextView) findViewById(R.id.email);
        passWordNew =(EditText) findViewById(R.id.fullnameEdit);
        passCf = (EditText) findViewById(R.id.addressEdit);
        passWordOld = (TextView) findViewById(R.id.passwordold);
        userNameChange = getIntent().getStringExtra("UserChange");
        avatar = (ImageView) findViewById(R.id.imageAvatar) ;


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ChangePassWord.this, CustomerProfileFragment.class);
//                startActivity(intent);
                finish();
            }
        });


        // set value email , user name from firebase
        databaseReference.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.child(userNameChange).getValue(Customer.class);
                userName.setText(user.getUsername());
                email.setText(user.getEmail());
                passWordOld.setText(user.getPassword());
                Picasso.get().load(user.getImgUrl()).into(avatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passWordNewText = passWordNew.getText().toString().trim();
                passCfText = passCf.getText().toString().trim();
                if (isValid()) {
                    final ProgressDialog mDialog = new ProgressDialog(ChangePassWord.this);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setCancelable(false);
                    mDialog.setMessage("Changing Please Wait.....");
                    mDialog.show();

                    databaseReference.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            user = snapshot.child(userNameChange).getValue(Customer.class);
                            databaseReference.child("Customer").child(userNameChange).child("password").setValue(passWordNewText);
                            mDialog.cancel();
                            Toast.makeText(ChangePassWord.this,"Change Password Successed",Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(ChangePassWord.this, CustomerProfileFragment.class);
//                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }
        });


    }

    // Hàm check Valid
    public boolean isValid(){
        // Check valid username and password
        if(TextUtils.isEmpty(passWordNewText) || TextUtils.isEmpty(passCfText)){
            Toast.makeText(ChangePassWord.this,"Please enter infor",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        else if(!passWordNewText.equals(user.getPassword())){
            if(passWordNewText.equals(passCfText)){
                isvalid = true;
            }
            else Toast.makeText(ChangePassWord.this,"Confirm Password don't match",Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(ChangePassWord.this,"New password can't be the same as current password",Toast.LENGTH_SHORT).show();
        return isvalid;
    }
}