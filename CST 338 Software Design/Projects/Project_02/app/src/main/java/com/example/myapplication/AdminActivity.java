package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.DB.AppDataBase;
import com.example.myapplication.DB.OzFoodDAO;
import com.example.myapplication.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

    private static final String USER_ID = "com.example.myapplication_user_record";

    ActivityAdminBinding binding;

    Button update;

    Button add;

    Button remove;

    Button shop;

    int userId;
    User mUser;

    OzFoodDAO mOzFoodDAO;

    public static Intent intentFactory(Context applicationContext, int userId) {
        Intent intent = new Intent(applicationContext, AdminActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getDataBase();

        binding = ActivityAdminBinding.inflate((getLayoutInflater()));
        View view = binding.getRoot();
        setContentView(view);

        update = binding.mainUpdateButton;
        add = binding.mainAddItemButton;
        remove = binding.mainRemoveButton;
        shop = binding.mainShopButton;


        userId = getIntent().getIntExtra(USER_ID, -1);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = UpdateItemActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddItemActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });


        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = RemoveItemActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });


        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MainActivity.intentFactory(getApplicationContext(), userId);
                startActivity(intent);
            }
        });

    }

    private void getDataBase() {
        mOzFoodDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .OzFoodDAO();
    }
}