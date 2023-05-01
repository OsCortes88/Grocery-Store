package com.example.myapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.myapplication.DB.AppDataBase;

@Entity(tableName = AppDataBase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int userId;

    private String userName;
    private String password;
    private boolean isAdmin;
    private double accountFunds;

    public User(String userName, String password, boolean isAdmin, double accountFunds) {
        this.userName = userName;
        this.password = password;
        this.isAdmin = isAdmin;
        this.accountFunds = accountFunds;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                ", accountFunds=" + accountFunds +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public double getAccountFunds() {
        return accountFunds;
    }

    public void setAccountFunds(double accountFunds) {
        this.accountFunds = accountFunds;
    }
}
