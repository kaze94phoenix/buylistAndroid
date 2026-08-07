package com.example.buylist.models;

import com.google.gson.annotations.SerializedName;

public class UserType {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;

    public UserType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public UserType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return "UserType{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
