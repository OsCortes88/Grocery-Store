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

        userName = binding.mainUsernameEditText;
        password = binding.mainPasswordEditText;
        logIn = binding.mainLogInButton;
        signUp = binding.mainSignUpButton;

        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .OzFoodDAO();


        User defClient = new User("testuser1", "testuser1", false, 50.00);
        User defAdmin = new User("admin2", "admin2", true, 100.00);
        if(mUserDAO.getUser(defClient.getUserName()) == null && mUserDAO.getUser(defAdmin.getUserName()) == null)  {
            mUserDAO.insert(defClient);
            mUserDAO.insert(defAdmin);
        }


        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUser = mUserDAO.getUser(userName.getText().toString());
//                Log.d("TAG", mUser.getUserId() + " " + mUser.getUserName()+ " " + mUser.getIsAdmin() + " " + mUser.getAccountFunds());
                if(mUser != null && mUser.getUserName().equals(password.getText().toString())) {
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