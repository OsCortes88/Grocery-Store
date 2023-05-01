package com.example.myapplication.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.User;

import java.util.List;

@Dao
public interface OzFoodDAO {
    @Insert
    void insert(User...gymLogs);

    @Update
    void update(User... gymLogs);

    @Delete
    void delete(User gymLog);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE userName = :userName")
    User getUser(String userName);

}
