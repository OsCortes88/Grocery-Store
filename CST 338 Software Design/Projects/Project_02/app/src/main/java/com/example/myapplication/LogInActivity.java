package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.DB.AppDataBase;
import com.example.myapplication.DB.OzFoodDAO;
import com.example.myapplication.databinding.ActivityLogInBinding;
import com.example.myapplication.databinding.ActivityMainBinding;

public class LogInActivity extends AppCompatActivity {
    private static boolean firstTimeSetUp = true;
    ActivityLogInBinding binding;
    private EditText userName;
    private EditText password;
    private Button signUp, logIn;
    private OzFoodDAO mOzFoodDAO;

    private User mUser;

    public static Intent intentFactory(Context packageContext) {
        Intent intent = new Intent(packageContext, LogInActivity.class);;
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        binding = ActivityLogInBinding.inflate((getLayoutInflater()));
        View view = binding.getRoot();
        setContentView(view);

        userName = binding.UserNameEditTest;
        password = binding.mainPasswordEditText3;
        logIn = binding.mainLogInButton;
        signUp = binding.mainSignUpButton;

        mOzFoodDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .OzFoodDAO();
        
//         Set default users
        User defClient = new User("testuser1", "testuser1", false, 50.00);
        User defAdmin = new User("admin2", "admin2", true, 100.00);
        if(mOzFoodDAO.getUser(defClient.getUserName()) == null && mOzFoodDAO.getUser(defAdmin.getUserName()) == null)  {
            mOzFoodDAO.insert(defClient);
            mOzFoodDAO.insert(defAdmin);
        }

//         Set default items in the items table
        if(firstTimeSetUp) {
            // TODO: 5/2/2023 Make firstTimeSetUp persistant
            firstTimeSetUp = false;
            Item milk = new Item("Milk (1 GAL)", 20, 2.99);
            Item eggs = new Item("Eggs (12 PK)", 15, 2.50);
            Item redApple = new Item("Red Apple", 100, 0.5);
            Item brownies = new Item("Brownies (6 PK)", 30, 5);
            Item doritos = new Item("Doritos (Salsa Verde)", 25, 2.99);

            mOzFoodDAO.insert(milk, eggs, redApple, brownies, doritos);
        }


        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUser = mOzFoodDAO.getUser(userName.getText().toString());
//                Log.d("TAG", mUser.getPassword() + " " + password.getText().toString());
                if(mUser != null && mUser.getPassword().equals(password.getText().toString())) {
                    Intent intent = MainActivity.intentFactory(getApplicationContext(), mUser.getUserId());
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