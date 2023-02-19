package com.example.fooddelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainSelectRole extends AppCompatActivity {

    Button btnStore,btnCustomer,btnShipper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);

        btnStore = (Button) findViewById(R.id.btnStore);
        btnCustomer = (Button) findViewById(R.id.btnCustomer);
//        btnShipper = (Button) findViewById(R.id.btnShipper);



        btnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.role = 0;
                Intent intent = new Intent(MainSelectRole.this, MainLogin.class);
                startActivity(intent);
                finish();
            }
        });

        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.role = 1;
                Intent intent = new Intent(MainSelectRole.this, MainLogin.class);
                startActivity(intent);
                finish();
            }
        });

//        btnShipper.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.role = 2;
//                Intent intent = new Intent(MainSelectRole.this, MainLogin.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }
}