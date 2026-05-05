package com.example.buylist.models;

import com.google.gson.annotations.SerializedName;

public class ItemType implements Comparable<ItemType> {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    public ItemType() {
    }

    public ItemType(String name) {
        this.name = name;
    }

    public ItemType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TypeItem{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int compareTo(ItemType itemType) {
        if(name.equals(itemType.getName()) & description.equals(itemType.getDescription()))
            return 1;
        else
            return -1;
    }
}
