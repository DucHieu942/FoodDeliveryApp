package com.example.fooddelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);
        //Khai báo biến cho nút SignIn
        Button buttonSignIn = (Button) findViewById(R.id.buttonToLoginView);
        //Khai báo biến cho nút SigUp
        Button buttonSignUp =  (Button) findViewById(R.id.buttonSignUp);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainRegister.this,MainLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }
}