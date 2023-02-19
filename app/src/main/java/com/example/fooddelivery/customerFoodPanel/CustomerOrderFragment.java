package com.example.fooddelivery.customerFoodPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.fooddelivery.Model.Food;
import com.example.fooddelivery.R;
import com.example.fooddelivery.adapters.FoodInCartAdapter;
import com.example.fooddelivery.adapters.ViewPagerAdapter;
import com.example.fooddelivery.customerFoodPanel.myOrder.HistoryFragment;
import com.example.fooddelivery.customerFoodPanel.myOrder.OngoingFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrderFragment extends Fragment  {

    RecyclerView foodRec;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");
    List<Food> foodListCart = new ArrayList<>();
    List<Integer> foodListCartId = new ArrayList<>();
    FoodInCartAdapter foodInCartAdapter;
    String UUID;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_cart, null);
        getActivity().setTitle("My Orders");

        tabLayout = v.findViewById(R.id.tablayoutOrder);
        viewPager = v.findViewById(R.id.view_pager_order);
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        // Add your fragments to the adapter
        adapter.addFragment(new OngoingFragment(), "Ongoing");
        adapter.addFragment(new HistoryFragment(), "History");

        // Set the adapter for the ViewPager
        viewPager.setAdapter(adapter);

        // Set the TabLayout to use the ViewPager
        tabLayout.setupWithViewPager(viewPager);

        // Load the first fragment
        viewPager.setCurrentItem(1, true);



        return v;
    }
}
