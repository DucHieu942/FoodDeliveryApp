package com.example.fooddelivery.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fooddelivery.customerFoodPanel.CustomerHomeFragment;
import com.example.fooddelivery.customerFoodPanel.CustomerOrderFragment;
import com.example.fooddelivery.customerFoodPanel.CustomerProfileFragment;

public class BottomNavigationAdapter extends FragmentPagerAdapter {


    private static final int NUM_PAGES = 3;

    public BottomNavigationAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return  new CustomerHomeFragment();
            case 1:
                return new CustomerOrderFragment();
            case 2:
                return new CustomerProfileFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
