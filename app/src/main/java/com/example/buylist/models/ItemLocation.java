package com.example.buylist.models;

import com.google.gson.annotations.SerializedName;

public class ItemLocation implements Comparable<ItemLocation> {
    @SerializedName("id")
    private int id;
    @SerializedName("location")
    private Location location;
    @SerializedName("item")
    private Item item;
    @SerializedName("price")
    private double price;

    public ItemLocation(Location location, Item item, double price) {
        this.location = location;
        this.item = item;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
