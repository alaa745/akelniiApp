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
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(User user);

    @Delete
    void deleteUser(User user);


    @Query("select * from user_table where mobile = :mobile")
    User getUserByMobile(String mobile);

    @Query("select * from user_table")
    User getUser();

    @Query("UPDATE User_Table SET Name = :userName , email = :userEmail , Password = :userPass WHERE mobile = :userMobile")
    void updateUser(String userName , String userEmail , String userPass , String userMobile);
}
