package com.example.myapplication.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.myapplication.Cart;
import com.example.myapplication.CartDao;
import com.example.myapplication.RoomDb;

import java.util.List;

public class Repository {
    CartDao cartDao;
    private LiveData<List<Cart>> carts;


    public Repository(Application application){
        RoomDb roomDb = RoomDb.getDatabase(application);
        cartDao = roomDb.cartDao();
        carts = cartDao.getCart();

    }

    public void insertCart(Cart cart){
        RoomDb.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                cartDao.insertRecord(cart);
            }
        });
    }

    public void updateCart(String productId , int updatedPrice , int productQuantity){
        RoomDb.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                cartDao.updateProduct(productId , updatedPrice , productQuantity);
            }
        });
    }


    public LiveData<List<Cart>> getCart(){
        return cartDao.getCart();
    }

}
