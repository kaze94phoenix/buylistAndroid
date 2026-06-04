package com.example.buylist.models;

import com.google.gson.annotations.SerializedName;

public class Location implements Comparable<Location> {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("address")
    private String address;

    public Location() {
    }

    public Location(String name) {
        this.name = name;
    }

    public Location(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Location(String name, String description, String address) {
        this.name = name;
        this.description = description;
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public int compareTo(Location location) {
        if(location.toString().equals(toString()))
            return 1;
        else
            return -1;
    }
}
