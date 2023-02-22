package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class popularAdapter extends RecyclerView.Adapter<popularAdapter.ViewHolder> {
    ArrayList<Product> images;
    public static final String fileName = "login";
    public static final String userName = "username";
    public static final String Password = "password";
    SharedPreferences sharedPreferences;
    Context context;

    public popularAdapter(ArrayList<Product> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public popularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular , parent , false);
        popularAdapter.ViewHolder viewHolder = new popularAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull popularAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(images.get(position).getLink()).into(holder.imageView);
        holder.textView.setText(images.get(position).getProductName());
        holder.productPrice.setText(String.valueOf(images.get(position).getProductPrice()) + " EGP");

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView , productPrice;
        CardView cardView;
        Button addBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.productImg);
            cardView = itemView.findViewById(R.id.cardd);
            textView = itemView.findViewById(R.id.productName);
            addBtn = itemView.findViewById(R.id.addBtn);
            productPrice = itemView.findViewById(R.id.productPrice);


            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < getItemCount(); i++) {
                        if (textView.getText() == images.get(i).getProductName()){
                            Bundle bundle = new Bundle();
                            Fragment fragment = new ProductDetailsFragment();
                            bundle.putString("name" , textView.getText().toString());
                            bundle.putString("image" , images.get(i).getLink());
                            bundle.putInt("price" , Integer.parseInt(productPrice.getText().toString().replaceAll("[^0-9]", "")));

                            fragment.setArguments(bundle);
                            
                            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_productDetailsFragment , bundle);

                        }
                    }
                }
            });
        }
    }


}
