package com.example.myapplication;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.myapplication.DB.AppDataBase;

import java.text.DecimalFormat;

@Entity(tableName = AppDataBase.CART_TABLE)
public class CartItem {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @PrimaryKey(autoGenerate = true)
    int cartItemId;

    int userId;

    int productId;

    String itemName;
    double totalPriceForItem;
    int quantity;

    public CartItem(int userId, int productId, String itemName, double totalPriceForItem, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.itemName = itemName;
        this.totalPriceForItem = totalPriceForItem;
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getTotalPriceForItem() {
        return totalPriceForItem;
    }

    public void setTotalPriceForItem(double totalPriceForItem) {
        this.totalPriceForItem = totalPriceForItem;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return getItemName() +
                "  x " + getQuantity() +
                "   $" + df.format(getTotalPriceForItem()) + "\n" +
                "---------------------------------------------------\n";
    }
}
