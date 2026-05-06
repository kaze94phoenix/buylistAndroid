package com.example.buylist.models;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiService {
    @GET("api/product-types")
    Call<ArrayList<ItemType>> getProductTypes();

    @GET("api/products")
    Call<ArrayList<Item>> getProducts();

    @GET("api/stores")
    Call<ArrayList<Location>> getLocations();

    @GET("api/products-stores")
    Call<ArrayList<ItemLocation>> getItemLocations();
}
