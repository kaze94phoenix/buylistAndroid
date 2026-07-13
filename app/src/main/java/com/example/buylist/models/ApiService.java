package com.example.buylist.models;

import java.util.ArrayList;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface ApiService {
    //GET REQUESTS
    @GET("api/product-types")
    Call<ArrayList<ItemType>> getProductTypes();
    @GET("api/products")
    Call<ArrayList<Item>> getProducts();
    @GET("api/stores")
    Call<ArrayList<Location>> getLocations();
    @GET("api/store-types")
    Call<ArrayList<LocationType>> getLocationTypes();
    @GET("api/products-stores")
    Call<ArrayList<ItemLocation>> getItemLocations();
    @GET("api/listas/{id}")
    Call<ArrayList<BuyList>> getListas(@Path("id") String userId);
    @GET("api/my-stores/{id}")
    Call<ArrayList<Location>> myLocations(@Path("id") String userId);
    @GET("api/listas/{origin}/{destination}")
    Call<ResponseBody> getDistanceItems(@Path("origin") String origin, @Path("destination") String destination);

    //POST REQUESTS
    @POST("api/login")
    Call<ResponseBody> login(@Body User user);
    @POST("api/listas/{id}")
    Call<ResponseBody> addLista(@Path("id") int userId, @Body BuyList buylist);
    @POST("api/stores/{id}")
    Call<ResponseBody> addStore(@Path("id") int userId, @Body Location location);

    //PUT REQUESTS
    @PUT("api/stores/{id}")
    Call<ResponseBody> updateStore(@Path("id") int storeId, @Body Location location);

    //DELETE REQUESTS
    @DELETE("api/listas/{id}")
    Call<ResponseBody> deleteLista(@Path("id") int listaId);
    @DELETE("api/stores/{id}")
    Call<ResponseBody> deleteStore(@Path("id") int storeId);


}
