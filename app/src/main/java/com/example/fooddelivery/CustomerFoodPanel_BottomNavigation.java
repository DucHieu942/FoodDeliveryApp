package com.example.fooddelivery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.fooddelivery.customerFoodPanel.CustomerOrderFragment;
import com.example.fooddelivery.customerFoodPanel.CustomerHomeFragment;
import com.example.fooddelivery.customerFoodPanel.CustomerProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerFoodPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public int id_fragment  ;
    public String tag_fragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_food_panel_bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        String userNameLogin = getIntent().getStringExtra("UserLogin");
//        Fragment fragmentHome = new CustomerHomeFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("UserLogin", userNameLogin);
//        loadcheffragment(fragmentHome);


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        String userNameLogin = getIntent().getStringExtra("UserLogin");
        bundle.putString("UserLogin", userNameLogin);
        Fragment fragmentHome = new CustomerHomeFragment();
        Fragment fragmentCart = new CustomerOrderFragment();
        Fragment fragmentProfile = new CustomerProfileFragment();

        switch (item.getItemId()) {
            case R.id.cus_Home:
                fragment = fragmentHome;
                fragment.setArguments(bundle);
                break;

            case R.id.cus_Order:
                fragment = fragmentCart;
                fragment.setArguments(bundle);
                break;

            case R.id.cus_Profile:
                fragment = fragmentProfile;
                fragment.setArguments(bundle);
                break;
        }
        return loadcheffragment(fragment);
    }

    private boolean loadcheffragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            return true;
        }
        return false;
    }

//    @Override
//    public void sendData(List<Food> foodListCart) {
//        if(foodListCart.size() !=0) {
//            CustomerCartFragment cartFragment = (CustomerCartFragment) getSupportFragmentManager().findFragmentByTag("Cart");
//            cartFragment.receiveDatafromHomeFragment(foodListCart);
//        }
//    }
}
