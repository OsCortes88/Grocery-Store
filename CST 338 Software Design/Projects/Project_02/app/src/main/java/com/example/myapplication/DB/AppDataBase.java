package com.example.myapplication.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.myapplication.Item;
import com.example.myapplication.User;

@Database(entities = {User.class, Item.class}, version = 4)
public abstract class AppDataBase extends RoomDatabase {

    public static final String DATABASE_NAME = "OzFood.db";
    public static final String USER_TABLE = "user_table";
    public static final String ITEM_TABLE = "item_table";
    public static final String CART_TABLE = "cart_table";

    private static volatile AppDataBase instance;
    private static final Object LOCK = new Object();

    public abstract OzFoodDAO OzFoodDAO();

    public static AppDataBase getInstance(Context context) {
        if(instance == null) {
            synchronized (LOCK) {
                if(instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class,
                            DATABASE_NAME).build();

                }
            }
        }
        return instance;
    }

}
