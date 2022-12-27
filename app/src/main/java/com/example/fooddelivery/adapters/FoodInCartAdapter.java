package com.example.fooddelivery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.Model.Food;
import com.example.fooddelivery.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodInCartAdapter extends RecyclerView.Adapter<FoodInCartAdapter.ViewHolder>{

    private android.content.Context context;
    private List<Food> listFoodCart;
    Integer count;


    public FoodInCartAdapter(android.content.Context context, List<Food> list){
        this.context = context;
        this.listFoodCart = list;
    }
    public List<Food> getListFood(){
        return this.listFoodCart;
    }

    @NonNull
    @Override
    public FoodInCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodInCartAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodInCartAdapter.ViewHolder holder, int position) {

        Food food = listFoodCart.get(position);
        count = food.getCount();

        Picasso.get().load(food.getImgUrl()).resize(1500, 844).into(holder.imageView);
        holder.name.setText(food.getName());
        holder.rating.setText(String.valueOf(food.getRate()));
        holder.price.setText("Price:"+String.valueOf(food.getPrice())+"$");
        holder.amountfood.setText(String.valueOf(food.getCount()));
        // Nút giảm số lượng
        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count < 0){
                    count = 0;
                    food.setCount(count);
                }else {
                    count = count -1;
                    food.setCount(count);
                }
            }
        });
        // Nút tăng số lượng
        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = count + 1;
                food.setCount(count);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFoodCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,rating,price,amountfood;
        Button btnIncrease,btnDecrease;


        public ViewHolder(@NonNull View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.img_food);
            name = itemView.findViewById(R.id.name);
            rating = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
            amountfood = itemView.findViewById(R.id.amountFood);
            btnIncrease = itemView.findViewById(R.id.buttonincrease);
            btnDecrease = itemView.findViewById(R.id.buttondecrease);

        }
    }
}
