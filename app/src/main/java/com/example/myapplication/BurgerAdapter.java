package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BurgerAdapter extends RecyclerView.Adapter<BurgerAdapter.ViewHolder>{
    ArrayList<Product> images;
    public static final String fileName = "login";
    public static final String userName = "username";
    public static final String Password = "password";
    SharedPreferences sharedPreferences;
    Context context;

    public BurgerAdapter(ArrayList<Product> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public BurgerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.burger_items , parent , false);
        BurgerAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BurgerAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(images.get(position).getLink()).into(holder.imageView);
        holder.productName.setText(images.get(position).getProductName());
        holder.productPrice.setText(String.valueOf(images.get(position).getProductPrice()) + " EGP");
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView productName , productPrice;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.prdImg);
            cardView = itemView.findViewById(R.id.card);
            productName = itemView.findViewById(R.id.prdName);
            productPrice = itemView.findViewById(R.id.prdPrice);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < getItemCount(); i++) {
                        if (productName.getText() == images.get(i).getProductName()){
                            if (productName.getText().equals("car")){
//                                Intent intent = new Intent(v.getContext(), ProductCart.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                v.getContext().startActivity(intent);

                            }
//                            else if (textView.getText().equals("park")){
//                                Intent intent = new Intent(v.getContext(), MapsActivity2.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                v.getContext().startActivity(intent);
//                            }
                        }
                    }
                }
            });
        }
    }
}
