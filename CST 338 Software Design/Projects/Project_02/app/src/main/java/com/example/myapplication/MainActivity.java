package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.DB.AppDataBase;
import com.example.myapplication.DB.OzFoodDAO;
import com.example.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static boolean firstTimeSetUp = true;
    ActivityMainBinding binding;
    EditText userName;
    EditText password;
    Button signUp, logIn;
    OzFoodDAO mUserDAO;

    User mUser;

    public static Intent intentFactory(Context packageContext) {
        Intent intent = new Intent(packageContext, MainActivity.class);;
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate((getLayoutInflater()));
        View view = binding.getRoot();
        setContentView(view);

        userName = binding.UserNameEditTest;
        password = binding.mainPasswordEditText3;
        logIn = binding.mainLogInButton;
        signUp = binding.mainSignUpButton;

        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .OzFoodDAO();
        
        // Set default users
        User defClient = new User("testuser1", "testuser1", false, 50.00);
        User defAdmin = new User("admin2", "admin2", true, 100.00);
        if(mUserDAO.getUser(defClient.getUserName()) == null && mUserDAO.getUser(defAdmin.getUserName()) == null)  {
            mUserDAO.insert(defClient);
            mUserDAO.insert(defAdmin);
        }

        // Set default items in the items table
        if(firstTimeSetUp) {
            // TODO: 5/2/2023 Make firstTimeSetUp persistant 
            firstTimeSetUp = false;
            Item milk = new Item("Milk (1 GAL)", 20, 2.99);
            Item eggs = new Item("Eggs (12 PK)", 15, 2.50);
            Item redApple = new Item("Red Apple", 100, 0.5);
            Item brownies = new Item("Brownies (6 PK)", 30, 5);
            Item doritos = new Item("Doritos (Salsa Verde)", 25, 2.99);

            mUserDAO.insert(milk, eggs, redApple, brownies, doritos);
        }


        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUser = mUserDAO.getUser(userName.getText().toString());
                Log.d("TAG", mUser.getPassword() + " " + password.getText().toString());
                if(mUser != null && mUser.getPassword().equals(password.getText().toString())) {
                    Intent intent = LogInActivity.intentFactory(getApplicationContext(), mUser.getUserId(), mUser.getUserName(), mUser.getIsAdmin(), mUser.getAccountFunds());
                    startActivity(intent);
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SignUpActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

}