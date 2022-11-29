package com.example.fooddelivery.customerFoodPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fooddelivery.MainLogin;
import com.example.fooddelivery.Model.User;
import com.example.fooddelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerProfileFragment extends Fragment {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");
    User user = new User();
    TextView userName,email,passWordChange,fullName,phoneNumber,address;
    LinearLayout layoutEdit ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_profile, null);
        getActivity().setTitle("Profile");

        Button btnLogout = (Button) v.findViewById(R.id.buttonConFirm);
        userName = (TextView) v.findViewById(R.id.userName);
        email = (TextView) v.findViewById(R.id.email);
        passWordChange = (TextView) v.findViewById(R.id.textChange);
        fullName = (TextView) v.findViewById(R.id.textFullName);
        phoneNumber = (TextView) v.findViewById(R.id.textPhone);
        address = (TextView) v.findViewById(R.id.textAddress);
        String userNameLogin = getArguments().getString("UserLogin");
        layoutEdit = (LinearLayout) v.findViewById(R.id.layoutEdit);


        // Set data from FireBase for Profile
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.child(userNameLogin).getValue(User.class);
                userName.setText(user.getUsername());
                email.setText(user.getEmail());
                phoneNumber.setText(user.getPhonenumber());
                fullName.setText(user.getFullname());
                address.setText(user.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(getActivity(),"user:"+userNameLogin,Toast.LENGTH_SHORT).show();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainLogin.class);
                startActivity(intent);
            }
        });

        passWordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ChangePassWord.class);
                intent.putExtra("UserChange",user.getUsername());
                startActivity(intent);
            }
        });

        layoutEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Có chạy vào này",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),EditProfile.class);
                intent.putExtra("UserChange",user.getUsername());
                startActivity(intent);
            }
        });

        return v;
    }





}
