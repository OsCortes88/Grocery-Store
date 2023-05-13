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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DB.AppDataBase;
import com.example.myapplication.DB.OzFoodDAO;
import com.google.android.material.badge.BadgeUtils;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.myapplication.user_record";

    private static final String PREFERENCES_KEY = "com.example.myapplication.PREFERENCES_KEY";
    SharedPreferences mPreferences;
    private int mUserId;

    private static final DecimalFormat df = new DecimalFormat("0.00");
    TextView cart;

    double totalPrice;

    TextView subtotal;

    Button addItem;

    Button buy;

    OzFoodDAO mOzFoodDAO;

    User mUser;

    int userId;

    List<CartItem> itemsInCart;



    public static Intent intentFactory(Context applicationContext, int userId) {
        Intent intent = new Intent(applicationContext, CartActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        wireUp();

        displayUserCart(userId);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddToCartActivity.intentFactory(getApplicationContext(), userId);
                startActivity(intent);
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemsInCart.size() > 0) {
                    if (totalPrice > mUser.getAccountFunds()) {
                        insufficientFunds();
                    } else {
                        mUser.setAccountFunds(mUser.getAccountFunds() - totalPrice);
                        User updatedFunds = mUser;
                        mOzFoodDAO.update(updatedFunds);

                        WithdrawActivity.profits += totalPrice;

                        removeFromInventory();

                        resetDisplay();

                        Toast.makeText(getApplicationContext(),"Purchase was made successfully, Enjoy ;)", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(),"You can't buy nothing. PICK SOMETHING!!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void removeFromInventory() {
        for (CartItem item : itemsInCart) {
            Item storeItem = mOzFoodDAO.getItemById(item.getProductId());
            storeItem.setItemsInStock(storeItem.getItemsInStock() - item.getQuantity());
            mOzFoodDAO.update(storeItem);

            mOzFoodDAO.delete(item);
        }
    }

    private void insufficientFunds() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("INSUFFICIENT FUNDS\n\nWould you like to add more funds to your account?");

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = AddFundsActivity.intentFactory(getApplicationContext(), userId);
                        startActivity(intent);
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

    private void wireUp() {

        mOzFoodDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .OzFoodDAO();

        cart = findViewById(R.id.itemsInCart);
        subtotal = findViewById(R.id.total);
        addItem = findViewById(R.id.addItems);
        buy = findViewById(R.id.buyItemsInCart);

        userId = getIntent().getIntExtra(USER_ID_KEY, -1);

        mUser = mOzFoodDAO.getUserById(userId);


        cart.setMovementMethod(new ScrollingMovementMethod());

    }


    private void displayUserCart(int userId) {
        StringBuilder text = new StringBuilder();
        itemsInCart = mOzFoodDAO.getAllItemsInUsersCart(userId);
        if(itemsInCart.size() > 0) {
            if(itemsInCart.size() == 1 ) {
                cart.setText(itemsInCart.get(0).toString());
                totalPrice += itemsInCart.get(0).getTotalPriceForItem();
            } else {
                totalPrice = 0;
                for(CartItem item : itemsInCart) {
                    text.append(item.toString());
                    totalPrice += item.getTotalPriceForItem();
                }
                cart.setText(text.toString());
            }
            text.delete(0, text.length());
            text.append("Total: $");
            text.append(df.format(totalPrice));
            subtotal.setText(text.toString());
        }
    }

    private void resetDisplay() {
        subtotal.setText("Total: $0.00");
        cart.setText("Your cart is empty");
        totalPrice = 0;
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
