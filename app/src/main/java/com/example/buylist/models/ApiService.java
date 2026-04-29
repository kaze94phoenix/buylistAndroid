package com.example.buylist.models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiService {
    @GET("api/product-types")
    Call<List<ItemType>> getProductTypes();
}
