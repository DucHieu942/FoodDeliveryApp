package com.example.fooddelivery.customerFoodPanel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.MainLogin;
import com.example.fooddelivery.Model.Food;
import com.example.fooddelivery.Model.Order;
import com.example.fooddelivery.Model.Orderparent;
import com.example.fooddelivery.R;
import com.example.fooddelivery.adapters.FoodAdapter;
import com.example.fooddelivery.storeFoodPanel.Add_Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class CustomerHomeFragment extends Fragment  {

    private RecyclerView  foodRec;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://food-delivery-21dff-default-rtdb.firebaseio.com/");
    private List<Food> foodList;
    private List<Integer> foodListCartId = new ArrayList<>();
    public static List<Food> foodListCart = new ArrayList<>();
    private FoodAdapter foodAdapter;
    private SearchView searchView;
    private ImageView shoppingCart;
    private OnBackPressedListener onBackPressedListener;
    ProgressDialog mDialog = null;
    String uuidOrder = "None";
    Long idOrder = 0L;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_home, null);
        getActivity().setTitle("Home");

        shoppingCart = v.findViewById(R.id.Cart);
//        if(savedInstanceState != null){
//            foodListCartId = savedInstanceState.getIntegerArrayList("foodListCartId");
//            uuidOrder = savedInstanceState.getString("UUID");
//        }
        String userNameLogin = getArguments().getString("UserLogin");
        searchView = v.findViewById(R.id.searchView);
        searchView.clearFocus();
        foodRec = v.findViewById(R.id.home_foodlist);
        foodRec.setHasFixedSize(true);
        foodRec.setLayoutManager(new LinearLayoutManager(getContext()));

        foodList = new ArrayList<>();
        FoodAdapter.IAddFoodListener addToCart;




        // Thêm món ăn vào giỏ hàng
        addToCart = new FoodAdapter.IAddFoodListener() {
            @Override
            public void onAddFood(Food food) {
                if(!foodListCart.contains(food) && !foodListCartId.contains(food.getId().intValue())) {
                    foodListCart.add(food);
//                    Long id = food.getId();
                    foodListCartId.add(food.getId().intValue());
                    idOrder = idOrder +1L;
                    Toast.makeText(getActivity(), "Size: " + foodListCart.size(), Toast.LENGTH_SHORT).show();
//                    sendDataToFireBase(food,userNameLogin);
                }
            }
        };


        foodAdapter = new FoodAdapter(getActivity(),foodList,addToCart);
        foodRec.setAdapter(foodAdapter);

        databaseReference.child("Foods").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                mDialog = new ProgressDialog(getActivity());
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setCancelable(false);
                mDialog.setMessage("Sign In Please Wait.....");
                mDialog.show();

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    Food food = dataSnapshot.getValue(Food.class);
                    foodList.add(food);
                }
                foodAdapter.notifyDataSetChanged();
                mDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Load Data Failed", Toast.LENGTH_SHORT).show();
                mDialog.cancel();

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


        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(foodListCart.size() != 0) {
                    Intent intent = new Intent(getActivity(), ShoppingCart.class);
                    intent.putExtra("UUID", uuidOrder);
                    intent.putExtra("username", userNameLogin);
                    startActivity(intent);
                }
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

//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putIntegerArrayList("foodListCartId",(ArrayList<Integer>) foodListCartId);
//        outState.putString("UUID", uuidOrder);
//    }


    public interface OnBackPressedListener {
        void onBackPressed();
    }

}

