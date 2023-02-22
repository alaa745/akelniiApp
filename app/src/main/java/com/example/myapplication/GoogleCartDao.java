package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GoogleCartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecord(GoogleUserCart cart);

    @Update
    void UpdateCart(GoogleUserCart cart);

    @Delete
    void DeleteCart(GoogleUserCart cart);

    @Query("SELECT EXISTS(SELECT * FROM GoogleCart WHERE pId = :productid)")
    Boolean is_Exist(String productid);


    @Query("SELECT * FROM GoogleCart WHERE userEmail = :email")
    List<GoogleUserCart> getCartByEmail(String email);



    @Query("UPDATE GoogleCart SET productPrice = :updatedPrice , productQuantity = :productQuantity WHERE pId = :productId")
    void updateProduct(String productId , int updatedPrice , int productQuantity);

    @Query("DELETE FROM GoogleCart WHERE pid = :id")
    void deleteById(String id);
}
