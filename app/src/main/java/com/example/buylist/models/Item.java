package com.example.buylist.models;

import androidx.annotation.IdRes;

public class Item implements Comparable<Item>{
    @IdRes
    long id;
    private String name,description;
    private ItemType itemType;

    public Item() {
    }

    public Item(String name, ItemType itemType) {
        this.name = name;
        this.itemType = itemType;
    }

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Item(String name, String description, ItemType itemType) {
        this.name = name;
        this.description = description;
        this.itemType = itemType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTypeItem(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ItemType getItemType() {
        return itemType;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", typeItem=" + itemType.getName() +
                '}';
    }

    @Override
    public int compareTo(Item item) {
        if(item.toString().equals(toString()))
            return 1;
        else
            return -1;
    }
}
