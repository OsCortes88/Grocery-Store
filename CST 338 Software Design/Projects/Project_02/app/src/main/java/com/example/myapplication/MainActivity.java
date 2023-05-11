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
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.DB.AppDataBase;
import com.example.myapplication.DB.OzFoodDAO;
import com.example.myapplication.databinding.ActivityMainBinding;

import java.util.List;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.example.myapplication.userIdKey" ;
    private static final String PREFERENCES_KEY = "com.example.myapplication.PREFERENCES_KEY";
    private OzFoodDAO mOzFoodDAO;
    private int mUserId = -1;
    private SharedPreferences mPreferences;
    private User mUser;
    @NonNull ActivityMainBinding binding;
    private TextView header;
    private Button addFunds;
    private Button viewCart;
    private Button purchase;
    private Button admin;



    public static Intent intentFactory(Context packageContext, int id) {
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(USER_ID_KEY, id);
        return intent;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getDataBase();

        checkForUser();

       // addUserToPreference(mUserId); //do this after logInUser

        logInUser(mUserId);
        Log.d("PREFERENCE_INT", String.valueOf(mPreferences.getInt(USER_ID_KEY, -1)));
        binding = ActivityMainBinding.inflate((getLayoutInflater()));
        setContentView(binding.getRoot());

        header = binding.textView;
        addFunds = binding.addFunds;
        viewCart = binding.viewCart;
        purchase = binding.purchaseItem;
        admin = binding.adminButton;

        if(!mUser.getIsAdmin()) {
            admin.setVisibility(View.INVISIBLE);
        } else {
            admin.setVisibility(View.VISIBLE);
        }

        header.setText(getString(R.string.welcome_message) + " " + mUser.getUserName());

        // Intents to go to next activity when clicked on selected buttons
        addFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddFundsActivity.intentFactory(getApplicationContext(), mUserId);
                startActivity(intent);
            }
        });

        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CartActivity.intentFactory(getApplicationContext(), mUserId);
                startActivity(intent);
            }
        });

        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddToCartActivity.intentFactory(getApplicationContext(), mUserId);
                startActivity(intent);
            }
        });


        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AdminActivity.intentFactory(getApplicationContext(), mUserId);
                startActivity(intent);
            }
        });

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                userId = 0;
//                userName = "";
//                isAdmin = false;
//                funds = 0.0;
//                Intent intent = MainActivity.intentFactory(getApplicationContext());
//                startActivity(intent);
//            }
//        });

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mUser != null) {
            MenuItem item = menu.findItem(R.id.Logout);
            item.setTitle(mUser.getUserName());
        }
        return super.onPrepareOptionsMenu(menu);
    }


    private void logInUser(int userId) {
        mUser = mOzFoodDAO.getUserById(userId);
        //make sure user exists.
        if(mUser != null) {
            addUserToPreference(userId);
        } else {
            Log.d("USER", "Don't work");
        }
        invalidateOptionsMenu();
    }

    private void addUserToPreference(int userId) {
        if(mPreferences == null) {
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        editor.apply();

    }

    private void getDataBase() {
        mOzFoodDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .OzFoodDAO();
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

}
