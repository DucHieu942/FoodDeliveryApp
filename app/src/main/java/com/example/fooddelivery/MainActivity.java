package com.example.fooddelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fooddelivery.customerFoodPanel.CustomerProfileFragment;

public class MainActivity extends AppCompatActivity implements CustomerProfileFragment.OnButtonClickListener {

    ImageView imageView;
    TextView textView;
    public static Integer role ;
    public static String[] listRole = {"Stores","Customer","Shipper"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView)findViewById(R.id.textViewAppName);
        onButtonClicked();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, MainSelectRole.class);
                startActivity(intent);
                finish();
            }
        },1);
    }

    @Override
    public void onButtonClicked() {
        Intent intent = new Intent(this, MainSelectRole.class);
        startActivity(intent);
    }
}