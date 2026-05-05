package com.example.buylist.models;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;


public interface ApiService {
    @GET("api/product-types")
    Call<ArrayList<ItemType>> getProductTypes();
}
