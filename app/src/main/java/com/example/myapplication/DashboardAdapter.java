package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    ArrayList<Product> images;
    public static final String fileName = "login";
    public static final String userName = "username";
    public static final String Password = "password";
    SharedPreferences sharedPreferences;
    Context context;

    public DashboardAdapter(ArrayList<Product> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_dashboard , parent , false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(images.get(position).getLink()).into(holder.imageView);
        holder.textView.setText(images.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgD);
            cardView = itemView.findViewById(R.id.cardd);
            textView = itemView.findViewById(R.id.textD);


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < getItemCount(); i++) {
                        if (textView.getText() == images.get(i).getProductName()){
                            if (textView.getText().equals("Cheese Burger")){
                                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_burgerFragment);

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
