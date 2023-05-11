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

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE userId = :userId")
    User getUserById(int userId);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE)
    List<User> getAllUsers();

    @Query("UPDATE " + AppDataBase.USER_TABLE + " SET accountFunds = :total WHERE userId =:userId")
    void updateFunds(double total, int userId);

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

    @Query("SELECT * FROM " + AppDataBase.ITEM_TABLE + " where itemName = :name")
    Item getItemByName(String name);





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
