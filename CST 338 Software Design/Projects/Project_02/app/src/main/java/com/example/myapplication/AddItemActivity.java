package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.DB.AppDataBase;
import com.example.myapplication.DB.OzFoodDAO;

import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    OzFoodDAO mOzFoodDAO;

    EditText nameField;
    EditText priceField;
    EditText quantityField;

    Button submit;

    Item item;

    public static Intent intentFactory(Context applicationContext) {
        Intent intent = new Intent(applicationContext, AddItemActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        wireUp();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameField.getText().toString().equals("") || priceField.getText().toString().equals("") || quantityField.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "All fields must be filled out", Toast.LENGTH_LONG).show();
                } else {
                    Item item = new Item(nameField.getText().toString(), Integer.parseInt(quantityField.getText().toString()), Double.parseDouble(priceField.getText().toString()));
                    mOzFoodDAO.insert(item);
                    Toast.makeText(getApplicationContext(), "Item has been successfully added", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void wireUp() {
        mOzFoodDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .OzFoodDAO();

        nameField = findViewById(R.id.updateItemName);
        priceField = findViewById(R.id.updateItemPrice);
        quantityField = findViewById(R.id.updateItemQuantity);
        submit = findViewById(R.id.updateItem);

    }

}