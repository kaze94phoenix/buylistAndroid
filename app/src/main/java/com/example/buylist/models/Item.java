package com.example.buylist.models;

import com.google.gson.annotations.SerializedName;

public class Item implements Comparable<Item>{
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("item_type")
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setItemType(ItemType itemType) {
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
