package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.DB.AppDataBase;
import com.example.myapplication.DB.OzFoodDAO;
import com.example.myapplication.databinding.ActivityClientBinding;
import com.example.myapplication.databinding.ActivityMainBinding;

import org.w3c.dom.Text;

import java.util.zip.Inflater;

public class LogInActivity extends AppCompatActivity {
    private static final String USER_ID = "com.example.myapplication_user_record";
    private static final String USER_NAME= "com.example.myapplication_user_name";
    private static final String USER_ADMIN = "com.example.myapplication_user_admin";
    private static final String USER_FUNDS = "com.example.myapplication_user_funds";

    ActivityClientBinding binding;
    int userId;

    private User user;

    TextView header;
    Button addFunds;
    Button viewCart;
    Button purchase;
    Button admin;

    Button logout;

    OzFoodDAO mOzFoodDAO;

    public static Intent intentFactory(Context packageContext, int id) {
        Intent intent = new Intent(packageContext, LogInActivity.class);
        intent.putExtra(USER_ID, id);
        return intent;
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        mOzFoodDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .OzFoodDAO();
        userId = getIntent().getIntExtra(USER_ID, -1);

        user = mOzFoodDAO.getUserById(userId);



        binding = ActivityClientBinding.inflate((getLayoutInflater()));
        View view = binding.getRoot();
        setContentView(view);

        header = binding.textView;
        addFunds = binding.addFunds;
        viewCart = binding.viewCart;
        purchase = binding.purchaseItem;
        admin = binding.adminButton;
//        logout = binding.logOutButton;

        if(!user.getIsAdmin()) {
            admin.setVisibility(View.INVISIBLE);
        } else {
            admin.setVisibility(View.VISIBLE);
        }

        header.setText(getString(R.string.welcome_message) + " " + user.getUserName());

        // Intents to go to next activity when clicked on selected buttons
        addFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddFundsActivity.intentFactory(getApplicationContext(), userId);
                startActivity(intent);
            }
        });

        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CartActivity.intentFactory(getApplicationContext(), userId);
                startActivity(intent);
            }
        });

        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddToCartActivity.intentFactory(getApplicationContext(), userId);
                startActivity(intent);
            }
        });


        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AdminActivity.intentFactory(getApplicationContext(), userId);
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
}
