package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DB.AppDataBase;
import com.example.myapplication.DB.OzFoodDAO;

public class AddFundsActivity extends AppCompatActivity {
    private static final String USER_ID = "com.example.myapplication.user_record";

    private TextView mTotalFundsField;
    private EditText mNewFundsField;
    private Button mButton;

    private double mTotalFunds;
    private double mNewFunds;
    private User mUser;
    private int mUserId;
    private OzFoodDAO mOzFoodDAO;

    SharedPreferences mPreferences;

    public static Intent intentFactory(Context applicationContext, int userId) {
        Intent intent = new Intent(applicationContext, AddFundsActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_funds);

        wiredUpDisplay();



    }

    private void wiredUpDisplay() {
        mTotalFundsField = findViewById(R.id.accountTotal);
        mNewFundsField = findViewById(R.id.editTextNumberDecimal);

        mButton = findViewById(R.id.submitFunds);

        getDataBase();
        mUserId = getIntent().getIntExtra(USER_ID, -1);
        mUser = mOzFoodDAO.getUserById(mUserId);

        currentFunds();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewFunds = Double.parseDouble(mNewFundsField.getText().toString());
                if(mNewFunds > 0) {
                    mTotalFunds += mNewFunds;
                    mOzFoodDAO.updateFunds(mTotalFunds, mUser.getUserId());
                    mTotalFundsField.setText("$" + mTotalFunds);
                }
            }
        });
    }

    private void currentFunds() {
        mTotalFunds = mUser.getAccountFunds();
        mTotalFundsField.setText("$" + mTotalFunds);
    }

    private void getDataBase() {
        mOzFoodDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .OzFoodDAO();
    }

    private void getValuesFromDisplay() {

    }
}