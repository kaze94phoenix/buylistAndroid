package com.example.buylist.models;

import androidx.annotation.IdRes;

public class ItemLocation implements Comparable<ItemLocation> {
    @IdRes
    long id;
    private Location location;
    private Item item;
    private double price;

    public ItemLocation(Location location, Item item, double price) {
        this.location = location;
        this.item = item;
        this.price = price;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Location getLocation() {
        return location;
    }

    public Item getItem() {
        return item;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "ItemLocation{" +
                "location=" + location.toString() +
                ", item=" + item.toString() +
                ", price=" + price +
                '}';
    }

    public ItemLocation() {
    }

    @Override
    public int compareTo(ItemLocation itemLocation) {
        if(itemLocation.toString().equals(toString()))
            return 1;
        else
            return -1;
    }
}
