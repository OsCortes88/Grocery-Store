package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class RemoveItemActivity extends AppCompatActivity {

    public static Intent intentFactory(Context applicationContext) {
        Intent intent = new Intent(applicationContext, RemoveItemActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_item);
    }
}