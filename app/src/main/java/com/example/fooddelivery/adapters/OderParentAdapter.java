package com.example.fooddelivery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.MainLogin;
import com.example.fooddelivery.Model.Food;
import com.example.fooddelivery.Model.Orderparent;
import com.example.fooddelivery.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class OderParentAdapter extends RecyclerView.Adapter<OderParentAdapter.ViewHolder>{


    private android.content.Context context;
    private List<Orderparent> list;
    private IListenerClickItem iListenerClickItem;



    public OderParentAdapter(android.content.Context context, List<Orderparent> list,IListenerClickItem iListenerClickItem){
        this.context = context;
        this.list = list;
        this.iListenerClickItem =iListenerClickItem;
    }

    public interface IListenerClickItem{
        void onClickItem(Orderparent orderparent);
    }



    @NonNull
    @Override
    public OderParentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderparent,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull OderParentAdapter.ViewHolder holder, int position) {

        Orderparent orderparent = list.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateString = dateFormat.format(orderparent.getOrder_time());

        Picasso.get().load(orderparent.getImgUrl()).resize(500, 500).into(holder.imageView);
        holder.foodName.setText(orderparent.getFoodName());
        holder.date.setText(dateString);
        holder.address.setText(String.valueOf(orderparent.getAddress_ship()));
        holder.totalPrice.setText(String.valueOf(orderparent.getSum_price())+"$");
        holder.amount.setText(String.valueOf(orderparent.getAmountOrder()));
        holder.status.setText(orderparent.getStatus());
        holder.phone.setText(String.valueOf(orderparent.getPhonenumber()));

        if(orderparent.getStatus().toString().equals("unconfirmed") ||orderparent.getStatus().toString().equals("complete") ){
            System.out.println("chạy vào unconfirmed");
            holder.layout.setBackground(ContextCompat.getDrawable(context,R.drawable.background_shape));
        }else if(orderparent.getStatus().toString().equals("confirmed")){
            System.out.println("chạy vào confirmed");
            holder.layout.setBackground(ContextCompat.getDrawable(context,R.drawable.blue_bg));
        }else{
            System.out.println("chạy vào shipping");
            holder.layout.setBackground(ContextCompat.getDrawable(context,R.drawable.orange_background));
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        iListenerClickItem.onClickItem(orderparent);
                    }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView date,address,amount,totalPrice,foodName,status,phone;
        LinearLayout layout;


        public ViewHolder(@NonNull View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.imageFood);
            layout = itemView.findViewById(R.id.layout_card_orderparent);

            foodName= itemView.findViewById(R.id.foodName);
            date = itemView.findViewById(R.id.dateorder);
            address = itemView.findViewById(R.id.address);
            amount = itemView.findViewById(R.id.amount);
            totalPrice = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.status);
            phone = itemView.findViewById(R.id.phoneCus);
        }
    }
}
