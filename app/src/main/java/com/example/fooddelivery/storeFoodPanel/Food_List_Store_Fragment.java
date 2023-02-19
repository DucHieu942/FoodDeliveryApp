package com.example.fooddelivery.storeFoodPanel;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fooddelivery.Model.Food;
import com.example.fooddelivery.R;
import com.example.fooddelivery.adapters.FoodAdapter;
import com.example.fooddelivery.customerFoodPanel.CustomerHomeFragment;
import com.example.fooddelivery.customerFoodPanel.ShoppingCart;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Food_List_Store_Fragment extends Fragment {


    private RecyclerView foodRec;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");
    private List<Food> foodList;
    private List<Integer> foodListCartId = new ArrayList<>();
    public static List<Food> foodListCart = new ArrayList<>();
    private FoodAdapter foodAdapter;
    private SearchView searchView;
    private ImageView addFoodMenu;
    private CustomerHomeFragment.OnBackPressedListener onBackPressedListener;
    ShapeableImageView logout;

    public static Food foodEdit = new Food();
    private OnButtonLogoutFoodListListener listener;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_food_list_store, container, false);



        addFoodMenu = v.findViewById(R.id.add_food);
        logout = v.findViewById(R.id.imagelogout);
        String userNameLogin = getArguments().getString("UserLogin");
        searchView = v.findViewById(R.id.searchView);
        searchView.clearFocus();
        foodRec = v.findViewById(R.id.store_foodlist);
        foodRec.setHasFixedSize(true);
        foodRec.setLayoutManager(new LinearLayoutManager(getContext()));


        foodList = new ArrayList<>();
        FoodAdapter.IAddFoodListener editFood;

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Có vào hàm này đấyyyyyy");
                listener.onButtonLogoutFoodListClicked();
            }
        });




        editFood = new FoodAdapter.IAddFoodListener() {
            @Override
            public void onAddFood(Food food) {






                foodEdit.setId(food.getId());
                System.out.println("Mã: "+food.getId());
                foodEdit.setName(food.getName());
                foodEdit.setImgUrl(food.getImgUrl());
                foodEdit.setPrice(food.getPrice());
                Intent intent = new Intent(getActivity(), Edit_add_food.class);
                startActivity(intent);
            }
        };

        foodAdapter = new FoodAdapter(getActivity(),foodList,editFood);
        foodRec.setAdapter(foodAdapter);



        databaseReference.child("Foods").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    Food food = dataSnapshot.getValue(Food.class);
                    foodList.add(food);
                }
                foodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });


        addFoodMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent intent = new Intent(getActivity(), Edit_add_food.class);
                    startActivity(intent);
            }
        });





        return v;
    }



    private void filterList(String newText) {
        List<Food> filteredList = new ArrayList<>();

        //Kiểm tra kí tự nhập vào ô tìm kiếm
        if(newText == null || newText.length() ==0){
            filteredList.addAll(foodList);
        }else {
            String filterPattern = newText.toString().toLowerCase().trim();

            for(Food item: foodList){
                if(item.getName().toLowerCase().contains(filterPattern)){
                    filteredList.add(item);
                }
            }
        }


        if(filteredList.isEmpty()){
            Toast.makeText(getActivity(),"No food found",Toast.LENGTH_SHORT).show();
        }else {
            foodAdapter.setFoodFilter(filteredList);
        }
    }

    public interface OnButtonLogoutFoodListListener {
        void onButtonLogoutFoodListClicked();
    }

}