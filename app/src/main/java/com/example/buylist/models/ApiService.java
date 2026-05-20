package com.example.buylist.models;

import org.json.JSONObject;

import java.util.ArrayList;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiService {
    //GET REQUESTS
    @GET("api/product-types")
    Call<ArrayList<ItemType>> getProductTypes();

    @GET("api/products")
    Call<ArrayList<Item>> getProducts();

    @GET("api/stores")
    Call<ArrayList<Location>> getLocations();

    @GET("api/products-stores")
    Call<ArrayList<ItemLocation>> getItemLocations();

    @GET("api/listas/{id}")
    Call<ArrayList<BuyList>> getListas(@Path("id") String userId);

    @GET("api/my-stores/{id}")
    Call<ArrayList<Location>> myLocations(@Path("id") String userId);

    //POST REQUESTS
    @POST("api/login")
    Call<ResponseBody> login(@Body User user);
    @FormUrlEncoded
    @POST("api/listas")
    Call<ResponseBody> addLista(@Field("json_request") String buylist);


}
