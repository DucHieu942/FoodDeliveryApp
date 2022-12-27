package com.example.fooddelivery.customerFoodPanel;

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

import com.example.fooddelivery.Model.Food;
import com.example.fooddelivery.Model.Order;
import com.example.fooddelivery.Model.Orderparent;
import com.example.fooddelivery.R;
import com.example.fooddelivery.adapters.FoodAdapter;
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
    private List<Food> foodListCart = new ArrayList<>();
    private FoodAdapter foodAdapter;
    private SearchView searchView;
    private ImageView shoppingCart;
    String uuidOrder = "None";
    Long idOrder = 0L;
//    private  ISendDataListener mISendDataListener;
//
//    public interface ISendDataListener {
//        void sendData(List<Food> foodListCart);
//    }

    public  CustomerHomeFragment(){

    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        mISendDataListener = (ISendDataListener) getActivity();
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_home, null);
        getActivity().setTitle("Home");

        shoppingCart = v.findViewById(R.id.Cart);
        if(savedInstanceState != null){
            foodListCartId = savedInstanceState.getIntegerArrayList("foodListCartId");
            uuidOrder = savedInstanceState.getString("UUID");
        }
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
                    sendDataToFireBase(food,userNameLogin);
                }
            }
        };


        foodAdapter = new FoodAdapter(getActivity(),foodList,addToCart);
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
// send Data To Firebase
    private void sendDataToFireBase(Food food,String userNameLogin ) {
         Long idFood = food.getId();
         Float priceFood = food.getPrice();
        databaseReference.child("OrderParent").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(uuidOrder.equals("None")) {
                    String status = snapshot.child(uuidOrder).child("status").getValue(String.class) ==null?"None"
                            :snapshot.child(uuidOrder).child("status").getValue(String.class);
                    if(!status.equals("In cart"))
                        uuidOrder = UUID.randomUUID().toString();
                }
                Orderparent orderparent = new Orderparent(uuidOrder,null,null,null,null,null,"In cart",null,null);
                databaseReference.child("OrderParent").child(uuidOrder).setValue(orderparent);
                Order order = new Order(idOrder,uuidOrder,idFood,null);
                databaseReference.child("Order").child(idOrder.toString()).setValue(order);
                databaseReference.child("Order").child(idOrder.toString()).child("price").setValue(priceFood.intValue());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("foodListCartId",(ArrayList<Integer>) foodListCartId);
        outState.putString("UUID", uuidOrder);
    }


}

