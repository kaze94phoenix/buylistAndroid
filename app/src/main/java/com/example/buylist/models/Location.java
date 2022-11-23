package com.example.buylist.models;

import androidx.annotation.IdRes;

public class Location implements Comparable<Location> {
    @IdRes
    long id;
    private String name, description, address;

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
