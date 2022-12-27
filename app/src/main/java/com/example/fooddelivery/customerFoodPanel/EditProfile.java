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

public class EditProfile extends AppCompatActivity {

    Button buttonConfirm,buttonBack;
    EditText phoneEdit,fullnameEdit,addressEdit;
    String phoneEditText,fullnameEditText,addressEditText,userNameChange;
    String currentPhone,currentFullName,currentAddress;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");
    Customer user = new Customer();
    String regex = "/^([^0-9]*)$/";
    TextView userName,email;
    Boolean isvalid = false,isvalidFullName= false,isvalidPhone=false,isvalidAddress=false;
    ImageView avatar,changeAvatar;

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonConfirm = (Button) findViewById(R.id.buttonConFirm);
        userName = (TextView) findViewById(R.id.userName);
        email = (TextView) findViewById(R.id.email);
        phoneEdit = (EditText) findViewById(R.id.phoneNumberEdit);
        fullnameEdit =(EditText) findViewById(R.id.fullnameEdit);
        addressEdit = (EditText) findViewById(R.id.addressEdit);
        userNameChange = getIntent().getStringExtra("UserChange");
        avatar = (ImageView) findViewById(R.id.imageAvatar) ;

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(EditProfile.this, CustomerProfileFragment.class);
//                startActivity(intent);
                finish();
            }
        });
// set value email , user name from firebase
        databaseReference.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                user = snapshot.child(userNameChange).getValue(Customer.class);
                currentPhone = user.getPhonenumber();
                currentFullName = user.getFullname();
                currentAddress = user.getAddress();
                userName.setText(user.getUsername());
                email.setText(user.getEmail());
                phoneEdit.setText(user.getPhonenumber());
                fullnameEdit.setText(user.getFullname());
                addressEdit.setText(user.getAddress());
                Picasso.get().load(user.getImgUrl()).into(avatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneEditText = phoneEdit.getText().toString().trim();
                fullnameEditText = fullnameEdit.getText().toString().trim();
                addressEditText = addressEdit.getText().toString().trim();
        // Nếu có sự chỉnh sửa ở 1 trong 3 thông tin
                if((!phoneEditText.equals(currentPhone))||(!fullnameEditText.equals(currentFullName))||(!addressEditText.equals(currentAddress))){

                    if (isValid()) {
                        final ProgressDialog mDialog = new ProgressDialog(EditProfile.this);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.setCancelable(false);
                        mDialog.setMessage("Changing Please Wait.....");
                        mDialog.show();


                        databaseReference.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //Check số điện thoại đã tồn tại chưa
                                Boolean isCheckPhone =false;

                                if(!phoneEditText.equals(currentPhone)){

                                    for(DataSnapshot snapshotChild :snapshot.getChildren() ){
                                        //Check xem số điện thoại đã tồn tại hay chưa
                                        String phone = snapshotChild.child("phonenumber").getValue(String.class);

                                        if(phone.equals(phoneEditText)){
                                            Toast.makeText(EditProfile.this,"Number have been used",Toast.LENGTH_SHORT).show();
                                            isCheckPhone = true;
                                            mDialog.cancel();
                                            break;
                                        }
                                    }

                                }

                                //Set value for database
                                if(!isCheckPhone) {
                                    user = snapshot.child(userNameChange).getValue(Customer.class);
                                    databaseReference.child("Customer").child(userNameChange).child("phonenumber").setValue(phoneEditText);
                                    databaseReference.child("Customer").child(userNameChange).child("fullname").setValue(fullnameEditText);
                                    databaseReference.child("Customer").child(userNameChange).child("address").setValue(addressEditText);
                                    mDialog.cancel();
                                    Toast.makeText(EditProfile.this, "Change Information Successed", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(EditProfile.this, CustomerProfileFragment.class);
//                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                }

            }
        });

    }

    // Hàm check Valid
    public boolean isValid(){
        // Check valid username and password
        if(TextUtils.isEmpty(phoneEditText) || TextUtils.isEmpty(fullnameEditText) ||TextUtils.isEmpty(addressEditText)){
            Toast.makeText(EditProfile.this,"Please enter infor",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        //Check valid phone number
        if(TextUtils.isEmpty(phoneEditText)){
            Toast.makeText(EditProfile.this,"Please enter phone",Toast.LENGTH_SHORT).show();
        }else{
            isvalidPhone = true;
        }
        //Check valid fullName
        if(TextUtils.isEmpty(fullnameEditText)){
            Toast.makeText(EditProfile.this,"Please enter fullname",Toast.LENGTH_SHORT).show();

        }else{
            if(!fullnameEditText.matches(regex)){
                isvalidFullName = true;
            }
            else {
                Toast.makeText(EditProfile.this,"Invalid fullname",Toast.LENGTH_SHORT).show();
            }
        }
        //Check valid address
        if(TextUtils.isEmpty(addressEditText)){
            Toast.makeText(EditProfile.this,"Please enter address",Toast.LENGTH_SHORT).show();
        }else{
            isvalidAddress = true;
        }

        isvalid = (isvalidFullName && isvalidPhone && isvalidAddress )?true:false;
        return isvalid;
    }
}