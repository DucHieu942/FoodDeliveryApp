package com.example.fooddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.fooddelivery.customerFoodPanel.CustomerHomeFragment;
import com.example.fooddelivery.customerFoodPanel.CustomerOrderFragment;
import com.example.fooddelivery.customerFoodPanel.CustomerProfileFragment;
import com.example.fooddelivery.storeFoodPanel.Complete_Order_Store_Fragment;
import com.example.fooddelivery.storeFoodPanel.Food_List_Store_Fragment;
import com.example.fooddelivery.storeFoodPanel.Infor_Store_Fragment;
import com.example.fooddelivery.storeFoodPanel.Order_Store_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StoreFoodPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    public static String userNameLogin ="";
    Fragment fragment = null;
    Fragment fragmentOrder = new Order_Store_Fragment();
    Fragment fragmentComplete = new Complete_Order_Store_Fragment();
    Fragment fragmentList = new Food_List_Store_Fragment();
//    Fragment fragmentInfo = new Infor_Store_Fragment();
    Bundle bundle = new Bundle();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_food_panel_bottom_navigation);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation_store);
        navigationView.setOnNavigationItemSelectedListener(this);


    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Bundle bundle = new Bundle();
        userNameLogin = getIntent().getStringExtra("UserLogin");
        bundle.putString("UserLogin", userNameLogin);
        switch (item.getItemId()) {
            case R.id.store_orders:
                fragment = fragmentOrder;
                fragment.setArguments(bundle);
                break;

            case R.id.store_clt_orders:
                fragment = fragmentComplete;
                fragment.setArguments(bundle);
                break;

            case R.id.store_food:
                fragment = fragmentList;
                fragment.setArguments(bundle);
                break;
//            case R.id.store_info:
//                fragment = fragmentInfo;
//                fragment.setArguments(bundle);
//                break;
        }
        return loadcheffragment(fragment);
    }

    private boolean loadcheffragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_store,fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        fragment = fragmentOrder;
        fragment.setArguments(bundle);
        userNameLogin = getIntent().getStringExtra("UserLogin");
        bundle.putString("UserLogin", userNameLogin);
        // Load fragment đầu tiên khi mới đăng nhập vào (Fragment1)
        loadcheffragment(fragment);
    }


//    @Override
//    public void onBackPressed() {
//        // xử lý sự kiện nút Back ở đây
//    }
//
//    @Override
//    public void onButtonClicked() {
//
//    }
}