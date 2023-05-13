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
import android.widget.Button;

import com.example.myapplication.DB.AppDataBase;
import com.example.myapplication.DB.OzFoodDAO;
import com.example.myapplication.databinding.ActivityAdminBinding;

import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.example.myapplication.user_record";

    private static final String PREFERENCES_KEY = "com.example.myapplication.PREFERENCES_KEY";
    SharedPreferences mPreferences;
    private int mUserId;

    ActivityAdminBinding binding;

    Button update;

    Button add;

    Button remove;

    Button profit;

    User mUser;

    OzFoodDAO mOzFoodDAO;

    public static Intent intentFactory(Context applicationContext, int userId) {
        Intent intent = new Intent(applicationContext, AdminActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
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
        profit = binding.mainWithdrawButton;


        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        mUser = mOzFoodDAO.getUserById(mUserId);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = UpdateItemActivity.intentFactory(getApplicationContext(),  mUserId);
                startActivity(intent);
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddItemActivity.intentFactory(getApplicationContext(),  mUserId);
                startActivity(intent);
            }
        });


        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = RemoveItemActivity.intentFactory(getApplicationContext(), mUserId);
                startActivity(intent);
            }
        });

        profit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = WithdrawActivity.intentFactory(getApplicationContext(), mUserId);
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