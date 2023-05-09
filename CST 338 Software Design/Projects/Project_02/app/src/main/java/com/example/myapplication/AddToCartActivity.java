package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.DB.AppDataBase;
import com.example.myapplication.DB.OzFoodDAO;
import com.example.myapplication.databinding.ActivityAddToCartBinding;
import com.example.myapplication.databinding.ActivityAddToCartBinding;

import java.util.List;

public class AddToCartActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String USER_ID = "com.example.myapplication_user_record";
    ActivityAddToCartBinding binding;
    OzFoodDAO mUserDAO;

    Spinner inventory;

    User mUser;

    List<String> items;
    public static Intent intentFactory(Context packageContext, int userId) {
        Intent intent = new Intent(packageContext, AddToCartActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .OzFoodDAO();

        binding = ActivityAddToCartBinding.inflate((getLayoutInflater()));
        View view = binding.getRoot();
        setContentView(view);

        inventory = findViewById(R.id.spinnerItems);
        inventory.setOnItemSelectedListener(this);

//        https://www.geeksforgeeks.org/spinner-in-android-using-java-with-example/
        items = mUserDAO.getItemNames();
        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                items);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        inventory.setAdapter(ad);

    }

    // Performing action when ItemSelected
    // from spinner, Overriding onItemSelected method
    @Override
    public void onItemSelected(AdapterView<?> arg0,
                               View arg1,
                               int position,
                               long id)
    {

        // make toastof name of course
        // which is selected in spinner
        Toast.makeText(getApplicationContext(),
                        items.get(position),
                        Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0)
    {
        // Auto-generated method stub
    }



}
