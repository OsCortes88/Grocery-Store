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
    String userName;
    String password;
    boolean isAdmin;
    double funds;

    TextView header;
    Button addFunds;
    Button viewCart;
    Button purchase;
    Button admin;
    Button logout;

    public static Intent intentFactory(Context packageContext, int id, String userName, boolean isAdmin, double funds) {
        Intent intent = new Intent(packageContext, LogInActivity.class);
        intent.putExtra(USER_ID, id);
        intent.putExtra(USER_NAME, userName);
        intent.putExtra(USER_ADMIN, isAdmin);
        intent.putExtra(USER_FUNDS, funds);
        return intent;
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        userId = getIntent().getIntExtra(USER_ID, 0);
        userName = getIntent().getStringExtra(USER_NAME);
        isAdmin = getIntent().getBooleanExtra(USER_ADMIN, false);
        funds = getIntent().getDoubleExtra(USER_FUNDS, 0.0);

        binding = ActivityClientBinding.inflate((getLayoutInflater()));
        View view = binding.getRoot();
        setContentView(view);

        header = binding.textView;
        addFunds = binding.addFunds;
        viewCart = binding.viewCart;
        purchase = binding.purchaseItem;
        admin = binding.adminButton;
        logout = binding.logOutButton;

        if(!isAdmin) {
            admin.setVisibility(View.INVISIBLE);
        } else {
            admin.setVisibility(View.VISIBLE);
        }

        header.setText(getString(R.string.welcome_message) + " " + userName);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId = 0;
                userName = "";
                isAdmin = false;
                funds = 0.0;
                Intent intent = MainActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

    }
}
