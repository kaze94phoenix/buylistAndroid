package com.example.buylist.models;

import androidx.annotation.IdRes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class BuyList {

    @SerializedName("id")
    private int id;
    @SerializedName("purchases")
    private ArrayList<Purchase> purchases;
    @SerializedName("date")
    private Date date;
    @SerializedName("name")
    private String name;

    public BuyList(){

    }

    public BuyList(Date date, ArrayList<Purchase> purchases){
        this.date=date;
        if(purchases!=null)
            this.purchases=purchases;
        else
            purchases = new ArrayList<Purchase>();
    }

    public BuyList(String name, Date date, ArrayList<Purchase> purchases) {
        this.purchases = purchases;
        this.date = date;
        this.name = name;
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

    public ArrayList<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(ArrayList<Purchase> purchases) {
        this.purchases = purchases;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void addToBuyList(Purchase purchase){
        purchases.add(purchase);
    }

    public void addToBuyList(int position, Purchase purchase){
        purchases.add(position,purchase);
    }



}
