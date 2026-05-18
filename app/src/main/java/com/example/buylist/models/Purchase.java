package com.example.buylist.models;

import androidx.annotation.IdRes;

import com.google.gson.annotations.SerializedName;

public class Purchase {
    @SerializedName("id")
    private long id;
    @SerializedName("item_location")
    private ItemLocation itemLocation;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("purchased")
    private boolean purchased;

    public Purchase(ItemLocation itemLocation, int quantity) {
        this.itemLocation = itemLocation;
        setQuantity(quantity);
        purchased=false;
    }

    public Purchase(){

    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if(quantity<1)
            this.quantity=1;
        else
            this.quantity = quantity;
    }

    public void setItemLocation(ItemLocation itemLocation) {
        this.itemLocation = itemLocation;
    }

    public ItemLocation getItemLocation() {
        return itemLocation;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "itemLocation=" + itemLocation +
                ", quantity=" + quantity +
                ", purchased=" + purchased +
                '}';
    }
}
