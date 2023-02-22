package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertProduct(Product product);
    @Update
    void updateProduct(Product product);
    @Delete
    void deleteProduct(Product product);

    @Query("select * from Product where productId = :id")
    Product getProductById(String id);
//
//    @Query("delete from Product where productId like '%'||:id||'%' ")
//    List<Product> deleteProductById(String id);




}
