package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

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
import android.widget.Toast;

import com.example.myapplication.DB.AppDataBase;
import com.example.myapplication.DB.OzFoodDAO;
import com.google.android.material.badge.BadgeUtils;

import java.util.List;

public class UpdateItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String USER_ID_KEY = "com.example.myapplication.user_record";

    private static final String PREFERENCES_KEY = "com.example.myapplication.PREFERENCES_KEY";
    SharedPreferences mPreferences;
    private int mUserId;

    Spinner inventory;
    List<String> items;

    OzFoodDAO mOzFoodDAO;

    EditText nameField;
    EditText priceField;
    EditText quantityField;

    Button update;

    Item item;
    private User mUser;

    public static Intent intentFactory(Context applicationContext, int userId) {
        Intent intent = new Intent(applicationContext, UpdateItemActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        wireUp();

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

        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        mUser = mOzFoodDAO.getUserById(mUserId);

        createDropdown();

        nameField = findViewById(R.id.updateItemName);
        priceField = findViewById(R.id.updateItemPrice);
        quantityField = findViewById(R.id.updateItemQuantity);
        update = findViewById(R.id.updateItem);

        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        mUser = mOzFoodDAO.getUserById(mUserId);

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