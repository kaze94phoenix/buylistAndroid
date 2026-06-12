package com.example.buylist.models;

import com.google.gson.annotations.SerializedName;

public class Location implements Comparable<Location> {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("location_type")
    private LocationType locationType;
    @SerializedName("address")
    private String address;
    @SerializedName("geolocation")
    private String geolocation;

    public Location() {
    }

    public Location(String name) {
        this.name = name;
    }

    public Location(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Location(String name, LocationType locationType, String address) {
        this.name = name;
        this.locationType = locationType;
        this.address = address;
    }

    public Location(int id, String name, LocationType locationType, String address, String geolocation) {
        this.id = id;
        this.name = name;
        this.locationType = locationType;
        this.address = address;
        this.geolocation = geolocation;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
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

    public LocationType getLocationType() {
        return locationType;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", locationType=" + locationType +
                ", address='" + address + '\'' +
                ", geolocation='" + geolocation + '\'' +
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
