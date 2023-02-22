package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
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
import java.util.List;

public class FinalAdapter extends RecyclerView.Adapter<FinalAdapter.ViewHolder> {
    List<Cart> cartList = new ArrayList<>();
    List<GoogleUserCart> googleCartList;
    public static final String fileName = "login";
    public static final String userName = "username";
    public static final String Password = "password";
    SharedPreferences sharedPreferences;
    Context context;
    String junk = "";

//    public FinalAdapter(List<Cart> cartList, Context context) {
//        this.cartList = cartList;
//        this.context = context;
//    }
//
//
//    public FinalAdapter(List<GoogleUserCart> googleCartList, Context context , String junk) {
//        this.googleCartList = googleCartList;
//        this.context = context;
//        this.junk = junk;
//    }


    @NonNull
    @Override
    public FinalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.final_items , parent , false);
        FinalAdapter.ViewHolder viewHolder = new FinalAdapter.ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!junk.equals("Google")) {
            Glide.with(context).load(cartList.get(position).getProductImage()).into(holder.pImg);
            holder.productName.setText(cartList.get(position).getProductName());
            holder.productPrice.setText(String.valueOf(cartList.get(position).getProductPrice()));
        } else {
            Glide.with(context).load(googleCartList.get(position).getProductImage()).into(holder.pImg);
            holder.productName.setText(googleCartList.get(position).getProductName());
            holder.productPrice.setText(String.valueOf(googleCartList.get(position).getProductPrice()));
        }

    }

    @Override
    public int getItemCount() {
        if (!junk.equals("Google")) {
            return cartList.size();
        }
        else
            return googleCartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pImg;
        TextView productName , productPrice , items , DeliveryPrice , TaxPrice;
        CardView cardView;
        String itemsTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            imageView = itemView.findViewById(R.id.imgD);
//            cardView = itemView.findViewById(R.id.cardd);
            productName = itemView.findViewById(R.id.Pname);
            productPrice = itemView.findViewById(R.id.Pprice);
            pImg = itemView.findViewById(R.id.Pimg);

            if (!junk.equals("Google")) {
                for (int i = 0; i < getItemCount(); i++) {
                    itemsTotal = cartList.get(i).getProductPrice() + itemsTotal;
                }
            }else {
                for (int i = 0; i < getItemCount(); i++) {
                    itemsTotal = googleCartList.get(i).getProductPrice() + itemsTotal;
                }
            }

        }
    }

    public void setCartList(List<Cart> cart){
        this.cartList = cart;
        notifyDataSetChanged();
    }

}
