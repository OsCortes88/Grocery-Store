package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.DB.AppDataBase;
import com.example.myapplication.DB.OzFoodDAO;
import com.example.myapplication.databinding.ActivityAddToCartBinding;
import com.example.myapplication.databinding.ActivityAddToCartBinding;

import java.text.DecimalFormat;
import java.util.List;

public class AddToCartActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String USER_ID_KEY = "com.example.myapplication.user_record";

    private static final String PREFERENCES_KEY = "com.example.myapplication.PREFERENCES_KEY";
    SharedPreferences mPreferences;
    private int mUserId;

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
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }

    private void wireUp() {
        mOzFoodDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .OzFoodDAO();

        mUser = mOzFoodDAO.getUserById(getIntent().getIntExtra(USER_ID_KEY, -1));

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
//                    Item updatedItem = item;
//                    item.setItemsInStock(item.getItemsInStock()-quantity);

                    CartItem exists = mOzFoodDAO.getItemInCart(mUser.getUserId(), item.getItemId());
                    if(exists != null) {
                        CartItem product = exists;
                        if(quantity != 0) {
                            product.setQuantity(quantity);
                            product.setTotalPriceForItem(totalPrice);
                            mOzFoodDAO.update(product);
                            Toast.makeText(getApplicationContext(),"Updated quantity for " + mOzFoodDAO.getItemById(product.getProductId()).getItemName(), Toast.LENGTH_LONG).show();
                        } else {
                            mOzFoodDAO.delete(product);
                            Toast.makeText(getApplicationContext(), mOzFoodDAO.getItemById(product.getProductId()).getItemName() + " is no longer in your cart", Toast.LENGTH_LONG).show();
                        }

                        } else {
                        CartItem product = new CartItem(mUser.getUserId(), item.getItemId(), item.getItemName(), totalPrice, quantity);
                        mOzFoodDAO.insert(product);
                        Toast.makeText(getApplicationContext(),"Item added to cart", Toast.LENGTH_LONG).show();
                    }

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

    private void reset() {
        quantityField.setText("0");
        itemPrice.setText("Price: $0.00");
        totalPrice = 0;
    }

    private void increaseQuantity() {

        StringBuilder sb = new StringBuilder();
        quantity = Integer.parseInt(quantityField.getText().toString());
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Logout:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mUser != null) {
            MenuItem item = menu.findItem(R.id.Logout);
            item.setTitle(mUser.getUserName());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void logoutUser() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearUserFromIntent();
                        clearUserFromPref();
                        mUserId = -1;
                        checkForUser();

                    }
                });
        alertBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        alertBuilder.create().show();

    }


    private void checkForUser() {
        // Get user id. if no user id default is -1
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        Log.d("Tag", String.valueOf(mUserId));

        if(mUserId != -1) {
            Log.d("Tag", "We are in");
            return;
        }

        // This part maintains the login after closing app
        if(mPreferences == null) {
            getPrefs();
        }

        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        Log.d("Check", String.valueOf(mUserId));

        if(mUserId != -1) {
            return;
        }

        List<User> users = mOzFoodDAO.getAllUsers();

        if(users.size() <= 0) {
            User defClient = new User("testuser1", "testuser1", false, 50.00);
            User defAdmin = new User("admin2", "admin2", true, 100.00);
            mOzFoodDAO.insert(defClient, defAdmin);
        }

        //Take us to login if we get here
        Intent intent = LogInActivity.intentFactory(this);
        startActivity(intent);


    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void clearUserFromIntent() {
        getIntent().putExtra(USER_ID_KEY, -1);
    }

    private void clearUserFromPref() {
        addUserToPreference(-1);
    }

    private void addUserToPreference(int userId) {
        if(mPreferences == null) {
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        editor.apply();

    }


}
