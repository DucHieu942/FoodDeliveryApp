package com.example.fooddelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fooddelivery.customerFoodPanel.CustomerProfileFragment;
import com.example.fooddelivery.storeFoodPanel.DetailOrder;
import com.example.fooddelivery.storeFoodPanel.Food_List_Store_Fragment;
import com.example.fooddelivery.storeFoodPanel.Order_Store_Fragment;

public class MainActivity extends AppCompatActivity implements CustomerProfileFragment.OnButtonClickListener,
        Food_List_Store_Fragment.OnButtonLogoutFoodListListener {

    ImageView imageView;
    TextView textView;
    public static String imgUrlDefault = "https://firebasestorage.googleapis.com/v0/b/food-delivery-21dff.appspot.com/o/Food1.png?alt=media&token=2ed68ed6-96f1-4d94-b075-e22d4afd3192";
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


    @Override
    public void onButtonLogoutFoodListClicked() {
        Intent intent = new Intent(this, MainSelectRole.class);
        startActivity(intent);
    }
}