package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.DB.AppDataBase;
import com.example.myapplication.DB.OzFoodDAO;
import com.example.myapplication.databinding.ActivitySignupBinding;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    EditText userName;
    EditText password;
    EditText confirm;
    Button signUp;
    OzFoodDAO mUserDAO;

    User mUser;
    public static Intent intentFactory(Context packageContext) {
        Intent intent = new Intent(packageContext, SignUpActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        binding = ActivitySignupBinding.inflate((getLayoutInflater()));
        View view = binding.getRoot();
        setContentView(view);

        userName = binding.mainUsernameEditText;
        password = binding.signupPasswordEditText;
        confirm = binding.mainPasswordEditText2;
        signUp = binding.mainAddUserBtn;

        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .OzFoodDAO();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userName.getText().toString().equals("") && !password.getText().toString().equals("") && !confirm.getText().toString().equals("")) {
                    if (mUserDAO.getUser(userName.getText().toString()) == null) {
                        if (password.getText().toString().equals(confirm.getText().toString())) {
                            User newUser = new User(userName.getText().toString(), password.getText().toString(), false, 0.0);
                            mUserDAO.insert(newUser);
                            // Redirect to log in
                            Intent intent = LogInActivity.intentFactory(getApplicationContext());
                            Toast.makeText(getApplicationContext(), "Account was successfully created", Toast.LENGTH_LONG).show();
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "That username is taken, please enter another", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "All fields must be entered", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
