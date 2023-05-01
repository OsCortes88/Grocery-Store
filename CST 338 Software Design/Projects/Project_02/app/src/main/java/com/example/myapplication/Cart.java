package com.example.myapplication;

import androidx.room.Entity;

import com.example.myapplication.DB.AppDataBase;

@Entity(tableName = AppDataBase.USER_TABLE)
public class Cart {

    int id;
    String name;
    int price;
    int quantity;
}
