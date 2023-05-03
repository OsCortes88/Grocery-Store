package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.DB.AppDataBase;
import com.example.myapplication.DB.OzFoodDAO;
import com.example.myapplication.databinding.ActivityAddtocartBinding;
import com.example.myapplication.databinding.ActivityMainBinding;

import java.util.List;

public class AddToCartActivity extends AppCompatActivity {
    ActivityAddtocartBinding binding;
    OzFoodDAO mUserDAO;

    Spinner inventory;

    User mUser;
    public static Intent intentFactory(Context packageContext) {
        Intent intent = new Intent(packageContext, AddToCartActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtocart);

        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .OzFoodDAO();

        binding = ActivityAddtocartBinding.inflate((getLayoutInflater()));
        View view = binding.getRoot();
        setContentView(view);

//        inventory = findViewById(R.id.spinnerItems);
//        inventory.setOnItemSelectedListener(this);
//
////        https://www.geeksforgeeks.org/spinner-in-android-using-java-with-example/
//        List<String> items = mUserDAO.getItemNames();
//        // Create the instance of ArrayAdapter
//        // having the list of courses
//        ArrayAdapter ad
//                = new ArrayAdapter(
//                this,
//                android.R.layout.simple_spinner_item,
//                items);
//
//        // set simple layout resource file
//        // for each item of spinner
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        // Set the ArrayAdapter (ad) data on the
//        // Spinner which binds data to spinner
//        inventory.setAdapter(ad);

    }


}
