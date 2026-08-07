package com.example.buylist.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("address")
    private String address;
    @SerializedName("user_type_id")
    private int userTypeId;
    @SerializedName("geolocation")
    private String geolocation;


    public User(){

    }

    public User(int id, String name, String email, String password){
        this.id=id;
        this.name=name;
        this.email=email;
        this.password=password;
    }

    public User(String name, String email, String password, String address,String geolocation, int userTypeId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.geolocation=geolocation;
        this.userTypeId = userTypeId;
    }

    public User(String name, String password){
        this.name=name;
        this.password=password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserType(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
