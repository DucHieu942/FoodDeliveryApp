package com.example.fooddelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        //Khai báo biến cho nút SignUp
        Button buttonSignUp = (Button) findViewById(R.id.buttonToLoginView);
        //Khai báo biến cho nút Login
        Button buttonLogin =  (Button) findViewById(R.id.buttonLogin);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainLogin.this,MainRegister.class);
                startActivity(intent);
                finish();
            }
        });
    }
}