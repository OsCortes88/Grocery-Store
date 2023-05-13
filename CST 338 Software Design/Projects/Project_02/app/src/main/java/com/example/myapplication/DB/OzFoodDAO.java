package com.example.myapplication.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.CartItem;
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

    @Query("SELECT * FROM " + AppDataBase.ITEM_TABLE + " where itemName = :name ")
    Item getItemByName(String name);

    @Query("SELECT * FROM " + AppDataBase.ITEM_TABLE + " where itemId = :id")
    Item getItemById(int id);




    // ==========================  Cart Table =========================================
    @Insert
    void insert(CartItem... cartItems);

    @Update
    void update(CartItem... cartItems);

    @Delete
    void delete(CartItem cartItem);

//    @Query("SELECT * FROM " + AppDataBase.CART_TABLE + " NATURAL JOIN " + AppDataBase.USER_TABLE + " " + AppDataBase.ITEM_TABLE + " WHERE userId = :userId")
//    List<CartItem> getItemsInUsersCart(int userId);

    @Query("SELECT * FROM " + AppDataBase.CART_TABLE + " WHERE userId = :userId")
    List<CartItem> getAllItemsInUsersCart(int userId);

    @Query("SELECT * FROM " + AppDataBase.CART_TABLE + " WHERE userid = :userid AND productId = :productId")
    CartItem getItemInCart(int userid, int productId);



}
