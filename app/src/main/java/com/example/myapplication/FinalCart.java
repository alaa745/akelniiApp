package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.ArrayList;
import java.util.List;

public class FinalCart extends AppCompatActivity {
    DatabaseReference databaseReference;
    ImageView imageView;
    TextView textView , hiTxt , orderTxt;
    ArrayList<Product> images;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    DrawerLayout drawerLayout;
    FinalAdapter adapter;
    popularAdapter adapter2;
    public static final String fileName = "UserData";
    public static final String keyName = "username";
    public static final String keyMobile = "mobile";
    public static final String keyEmail = "email";
    public static final String keyPassword = "password";
    SharedPreferences sharedPreferences;
    CardView cardView;
    String userMobile;
    String user , password;
    Button btn;
    NavigationView navigationView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_cart);

        getDataFromFirebase();
    }

    public void getDataFromFirebase(){

        userMobile = sharedPreferences.getString(keyMobile , "Null");

//                finish();


        RoomDb roomDb1 = RoomDb.getDatabase(getApplicationContext());
        List<Cart> cartList = roomDb1.cartDao().getCartByMobile(userMobile);
        for (int i=0; i<cartList.size(); i++){
            Log.d("CartAd" , "Cart Name: " + cartList.get(i).getProductName());
        }



//                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext() , 2);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext() , RecyclerView.VERTICAL , false);
                recyclerView = findViewById(R.id.finalRv);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);


                adapter = new FinalAdapter();
                recyclerView.setAdapter(adapter);
//                recyclerView.setHasFixedSize(true);
                adapter.setCartList(cartList);
//                adapter = new FinalAdapter(cartList , getApplicationContext());
//                recyclerView.setAdapter(adapter);
//                recyclerView.setHasFixedSize(true);
//                adapter.notifyDataSetChanged();




            }
    }