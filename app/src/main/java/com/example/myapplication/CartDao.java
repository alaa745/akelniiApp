package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecord(Cart cart);

    @Update
    void UpdateCart(Cart cart);

    @Delete
    void DeleteCart(Cart cart);

    @Query("SELECT EXISTS(SELECT * FROM Cart WHERE pId = :productid)")
    Boolean is_Exist(String productid);


    @Query("SELECT * FROM Cart WHERE userMobile = :mobile")
    List<Cart> getCartByMobile(String mobile);

    @Query("SELECT * FROM Cart")
    LiveData<List<Cart>> getCart();

    @Query("UPDATE Cart SET productPrice = :updatedPrice , productQuantity = :productQuantity WHERE pId = :productId")
    void updateProduct(String productId , int updatedPrice , int productQuantity);

    @Query("DELETE FROM Cart WHERE pid = :id")
    void deleteById(String id);



}
