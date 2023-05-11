package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.DB.AppDataBase;
import com.example.myapplication.DB.OzFoodDAO;
import com.example.myapplication.databinding.ActivityAddToCartBinding;
import com.example.myapplication.databinding.ActivityAddToCartBinding;

import java.text.DecimalFormat;
import java.util.List;

public class AddToCartActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String USER_ID = "com.example.myapplication_user_record";
    private static final DecimalFormat df = new DecimalFormat("0.00");
    ActivityAddToCartBinding binding;

    Spinner inventory;

    Button minusBtn;

    Button plusBtn;

    Button addToCart;

    TextView quantityField;

    TextView itemPrice;

    int quantity;

    double totalPrice;

    User mUser;

    Item item;

    double priceForOne;

    OzFoodDAO mOzFoodDAO;

    List<String> items;
    public static Intent intentFactory(Context packageContext, int userId) {
        Intent intent = new Intent(packageContext, AddToCartActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    private void wireUp() {
        mOzFoodDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .OzFoodDAO();

        createDropdown();

        minusBtn = findViewById(R.id.minusBtn);
        plusBtn = findViewById(R.id.plusBtn);
        quantityField = findViewById(R.id.quantity);
        itemPrice = findViewById(R.id.itemPrice);
        addToCart = findViewById(R.id.addToCart);

    }

    private void createDropdown() {

        inventory = findViewById(R.id.spinnerItems);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        wireUp();

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseQuantity();
            }
        });

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseQuantity();
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.getItemsInStock() > 0) {
                    Item updatedItem = item;
                    item.setItemsInStock(item.getItemsInStock()-quantity);
                    mOzFoodDAO.update(updatedItem);
                    Toast.makeText(getApplicationContext(),"Item added to cart", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Sorry, we are currently out of stock", Toast.LENGTH_LONG).show();
                }

            }
        });





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

        item = mOzFoodDAO.getItemByName(items.get(position));
        priceForOne = item.getPrice();

        reset();

    }

//    private void displayPrice() {
//        if(quantity > 0) {
//            StringBuilder sb = new StringBuilder();
//            sb.append("Price: $");
//            // Get price fro item
//            double price = item.getPrice();
//            totalPrice = price;
//            sb.append(df.format(price));
//
//            itemPrice.setText(sb);
//        }
//    }

    private void reset() {
        quantityField.setText("0");
        itemPrice.setText("Price: $0.00");
    }

    private void increaseQuantity() {

        StringBuilder sb = new StringBuilder();
        quantity = Integer.parseInt(quantityField.getText().toString());
        // TODO: 5/10/2023 Max capacity
        if(quantity+1 > item.getItemsInStock()) {
            Toast.makeText(getApplicationContext(),"Sorry, we don't have enough", Toast.LENGTH_LONG).show();
            return;
        }
        quantity++;
        sb.append(quantity);
        quantityField.setText(sb);

        sb.delete(0, sb.length());
        totalPrice += priceForOne;
        sb.append("Price: $");
        sb.append(df.format(totalPrice));
        itemPrice.setText(sb);
    }

    private void decreaseQuantity() {
        StringBuilder sb = new StringBuilder();
        quantity = Integer.parseInt(quantityField.getText().toString());
        // TODO: 5/10/2023 Max capacity
        if(quantity-1 < 0) {
            Toast.makeText(getApplicationContext(),"That is impossible", Toast.LENGTH_LONG).show();
            return;
        }
        quantity--;
        sb.append(quantity);
        quantityField.setText(sb);

        sb.delete(0, sb.length());
        totalPrice -= priceForOne;
        sb.append("Price: $");
        sb.append(df.format(totalPrice));
        itemPrice.setText(sb);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0)
    {
        // Auto-generated method stub
    }



}
