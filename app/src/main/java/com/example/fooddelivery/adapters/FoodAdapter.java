package com.example.fooddelivery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.MainActivity;
import com.example.fooddelivery.Model.Food;
import com.example.fooddelivery.R;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {


    private android.content.Context context;
    private List<Food> list;
    private IAddFoodListener mIAddFoodListener;



    public FoodAdapter(android.content.Context context, List<Food> list,IAddFoodListener mIAddFoodListener){
        this.context = context;
        this.list = list;
        this.mIAddFoodListener = mIAddFoodListener;
    }

    public void setFoodFilter(List<Food> foodFilter){
        this.list = foodFilter;
        notifyDataSetChanged();
    }


    public interface IAddFoodListener{
        void onAddFood(Food food);
    }


    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, int position) {

        Food food = list.get(position);

        Picasso.get().load(food.getImgUrl()).resize(1500, 844).into(holder.imageView);
        holder.name.setText(food.getName());
        holder.rating.setText(String.valueOf(food.getRate()));
        holder.price.setText("Price:"+String.valueOf(food.getPrice())+"$");

        if(MainActivity.role == 0){
            holder.addtocart.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIAddFoodListener.onAddFood(food);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView name,rating,price,addtocart;


        public ViewHolder(@NonNull View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.img_food);
            name = itemView.findViewById(R.id.name);
            rating = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
            addtocart = itemView.findViewById(R.id.addtoCart);

        }
    }


}
