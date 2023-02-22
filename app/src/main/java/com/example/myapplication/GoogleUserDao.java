package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface GoogleUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(GoogleUser user);

    @Delete
    void deleteUser(GoogleUser user);



//
//    @Query("UPDATE User_Table SET Name = :userName , email = :userEmail , Password = :userPass WHERE mobile = :userMobile")
//    void updateUser(String userName , String userEmail , String userPass , String userMobile);
}