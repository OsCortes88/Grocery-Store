package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.DB.AppDataBase;
import com.example.myapplication.DB.OzFoodDAO;
import com.google.android.material.badge.BadgeUtils;

import java.util.List;

public class UpdateItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner inventory;
    List<String> items;

    OzFoodDAO mOzFoodDAO;

    EditText nameField;
    EditText priceField;
    EditText quantityField;

    Button update;

    Item item;

    public static Intent intentFactory(Context applicationContext) {
        Intent intent = new Intent(applicationContext, UpdateItemActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        wireUp();

        nameField.setText("Bruce Dickinson");
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameField.getText().toString().equals("") || priceField.getText().toString().equals("") || quantityField.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "All fields must be filled out", Toast.LENGTH_LONG).show();
                } else {
                    item.setItemName(nameField.getText().toString());
                    item.setPrice(Double.parseDouble(priceField.getText().toString()));
                    item.setItemsInStock(Integer.parseInt(quantityField.getText().toString()));
                    Item updatedItem = item;
                    mOzFoodDAO.update(updatedItem);
                    Toast.makeText(getApplicationContext(), "Item has been successfully updated", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void putDefaultValues() {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getItemName());
        nameField.setText(sb);

        sb.delete(0, sb.length());
        sb.append(item.getPrice());
        priceField.setText(sb);

        sb.delete(0, sb.length());
        sb.append(item.getItemsInStock());
        quantityField.setText(sb);
    }


    private void wireUp() {
        mOzFoodDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .OzFoodDAO();

        createDropdown();

        nameField = findViewById(R.id.updateItemName);
        priceField = findViewById(R.id.updateItemPrice);
        quantityField = findViewById(R.id.updateItemQuantity);
        update = findViewById(R.id.updateItem);

    }


    private void createDropdown() {

        inventory = findViewById(R.id.currentInventory);
        inventory.setOnItemSelectedListener(this);

//        https://www.geeksforgeeks.org/spinner-in-android-using-java-with-example/
        items = mOzFoodDAO.getItemNames();
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

        item = mOzFoodDAO.getItemByName(items.get(position));

        putDefaultValues();

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0)
    {
        // Auto-generated method stub
    }
}