package com.example.myapplication.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Cart;
import com.example.myapplication.Item;
import com.example.myapplication.User;

import java.util.List;

@Dao
public interface OzFoodDAO {
    @Insert
    void insert(User...users);

    @Update
    void update(User... users);

    @Delete
    void delete(User users);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE userName = :userName")
    User getUser(String userName);


    // ===========================  Item Table  ===============================================
    @Insert
    void insert(Item...items);

    @Update
    void update(Item... items);

    @Delete
    void delete(Item items);

    @Query("SELECT itemName FROM " + AppDataBase.ITEM_TABLE)
    List<String> getItemNames();
    @Query("SELECT * FROM " + AppDataBase.ITEM_TABLE)
    List<Item> getInventory();


    // ==========================  Cart Table =========================================
//    @Insert
//    void insert(Cart...carts);
//
//    @Update
//    void update(Cart... carts);
//
//    @Delete
//    void delete(Cart carts);


}
