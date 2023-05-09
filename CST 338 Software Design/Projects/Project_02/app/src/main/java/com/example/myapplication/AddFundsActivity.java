package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AddFundsActivity extends AppCompatActivity {
    private static final String USER_ID = "com.example.myapplication_user_record";

    public static Intent intentFactory(Context applicationContext, int userId) {
        Intent intent = new Intent(applicationContext, AddFundsActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_funds);
    }
}