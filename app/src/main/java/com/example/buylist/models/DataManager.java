package com.example.buylist.models;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.buylist.LoginActivity;
import com.example.buylist.MainActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataManager {

    private Context context;

    private static final String ITEMS_TYPE = "items type";
    private static final String ITEMS = "items";
    private static final String LOCATIONS = "locations";
    private static final String MY_LOCATIONS = "my_locations";
    private static final String ITEM_LOCATIONS = "item_locations";
    private static final String BUYLISTS = "buylists";
    private static final String PURCHASES = "purchases";
    private static final String LOCATIONS_TYPE = "locations type";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Paper paper;


    private ArrayList<ItemType> itemTypes;
    private ArrayList<Item> items;
    private ArrayList<Location> locations;
    private ArrayList<LocationType> locationTypes;
    private ArrayList<Location> myLocations;
    private ArrayList<ItemLocation> itemLocations;
    private ArrayList<BuyList> buylists;
    private ArrayList<Purchase> purchases;
    private String userId;

    private ApiService api;

    public DataManager(Context context) {

        preferences = context.getSharedPreferences("UserPreferences", MODE_PRIVATE);
        editor = preferences.edit();
        userId = preferences.getString("userId", "");

        api = RetrofitInstance.getApiInterface();

        Paper.init(context);

        this.context=context;

        //ITEM TYPE
        fetchItemTypes();

        //ITEM
        fetchItems();

        //LOCATION
        fetchLocations();

        //LOCATION TYPE
        fetchLocationTypes();

        //MY LOCATIONS
        fetchMyLocations();

        //ITEM LOCATION
        fetchItemLocations();

        //BUYLIST
        fetchBuylists();

        //PURCHASES
        fetchPurchases();

    }

    public SharedPreferences getPreferences() {
        return this.preferences;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }


    //Authorization
    public void login(User user, Activity activity) {
        api.login(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String res = response.body().string();
                        JSONObject jsonResponse = new JSONObject(res);
                        JSONObject user = jsonResponse.getJSONObject("user");
                        String username = user.getString("name");
                        String userId = user.getString("id");
                        String userType = jsonResponse.getString("user_type");
                        String token = jsonResponse.getString("token");
                        editor.putString("username", username);
                        editor.putString("userId", userId);
                        editor.putString("userType", userType);
                        editor.putString("token", token);
                        editor.putString("loginStatus", "true");
                        editor.apply();
                        activity.startActivity(new Intent(activity.getApplicationContext(), MainActivity.class));
                        activity.finish();
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    System.out.println("Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }
        });
    }

    public void logout(Activity activity) {
        editor.putString("username", "Guest");
        editor.putString("userId", "");
        editor.putString("userType", "CONS");
        editor.putString("token", "");
        editor.putString("loginStatus", "false");
        editor.apply();
        activity.startActivity(new Intent(activity.getApplicationContext(), LoginActivity.class));
        activity.finish();
    }


    //Item Type Manipulation
    public void fetchItemTypes() {
        api.getProductTypes().enqueue(new Callback<ArrayList<ItemType>>() {
            @Override
            public void onResponse(Call<ArrayList<ItemType>> call, Response<ArrayList<ItemType>> response) {
                ArrayList<ItemType> itemTypeApi = response.body();
                ArrayList<ItemType> itemTypeHD = Paper.book().read(ITEMS_TYPE);
                if (itemTypeApi == null || itemTypeApi.isEmpty())
                    itemTypes = itemTypeHD;
                else
                    itemTypes = itemTypeApi;

                if (itemTypes == null || itemTypes.isEmpty())
                    itemTypes = new ArrayList<ItemType>();

                Paper.book().write(ITEMS_TYPE, itemTypes);
            }

            @Override
            public void onFailure(Call<ArrayList<ItemType>> call, Throwable t) {

                System.out.println("Error: " + t.getMessage());
            }
        });
        itemTypes = Paper.book().read(ITEMS_TYPE);
    }

    public ArrayList<ItemType> getItemTypes() {
        return itemTypes;
    }


    public void addItemType(ItemType itemType) {
        itemTypes.add(itemType);
        Paper.book().write(ITEMS_TYPE, itemTypes);
    }

    public void deleteItemType(int position) {
        ItemType itemType = itemTypes.get(position);
        itemTypes.remove(position);
        Paper.book().write(ITEMS_TYPE, itemTypes);

        for (int i = 0; i < items.size(); i++)
            if (items.get(i).getItemType().compareTo(itemType) > 0)
                deleteItem(i);
    }


    //Item Manipulation
    public void fetchItems() {
        api.getProducts().enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                ArrayList<Item> itemApi = response.body();
                ArrayList<Item> itemHD = Paper.book().read(ITEMS);
                if (itemApi == null || itemApi.isEmpty())
                    items = itemHD;
                else
                    items = itemApi;

                if (items == null || items.isEmpty())
                    items = new ArrayList<Item>();

                Paper.book().write(ITEMS, items);
            }

            @Override
            public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }
        });
        items = Paper.book().read(ITEMS);
    }

    public void editItem(int position, Item item) {
        Item itemA = items.get(position);
        items.set(position, item);
        Paper.book().write(ITEMS, items);
        for (int i = 0; i < itemLocations.size(); i++)
            if (itemLocations.get(i).getItem().compareTo(itemA) > 0) {
                ItemLocation itemLocation = itemLocations.get(i);
                itemLocation.setItem(items.get(position));
                editItemLocation(i, itemLocation);
            }

    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void addItems(Item item) {
        items.add(item);
        Paper.book().write(ITEMS, items);
    }

    public void deleteItem(int position) {
        Item item = items.get(position);
        items.remove(position);
        Paper.book().write(ITEMS, items);
        for (int i = 0; i < itemLocations.size(); i++)
            if (item.compareTo(itemLocations.get(i).getItem()) > 0)
                deleteItemLocation(i);
    }

    //Location Manipulation
    public void fetchLocations(){
        api.getLocations().enqueue(new Callback<ArrayList<Location>>() {
            @Override
            public void onResponse(Call<ArrayList<Location>> call, Response<ArrayList<Location>> response) {
                ArrayList<Location> locationApi = response.body();
                ArrayList<Location> locationHD = Paper.book().read(LOCATIONS);
                if (locationApi == null || locationApi.isEmpty())
                    locations = locationHD;
                else
                    locations = locationApi;

                if (locations == null || locations.isEmpty())
                    locations = new ArrayList<Location>();

                Paper.book().write(LOCATIONS, locations);
            }

            @Override
            public void onFailure(Call<ArrayList<Location>> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }
        });
        locations = Paper.book().read(LOCATIONS);
    }
    public void fetchMyLocations(){
        if (!userId.equalsIgnoreCase("")) {
            api.myLocations(userId).enqueue(new Callback<ArrayList<Location>>() {
                @Override
                public void onResponse(Call<ArrayList<Location>> call, Response<ArrayList<Location>> response) {
                    ArrayList<Location> myLocationApi = response.body();
                    ArrayList<Location> myLocationHD = Paper.book().read(LOCATIONS);
                    if (myLocationApi == null || myLocationApi.isEmpty())
                        myLocations = myLocationHD;
                    else
                        myLocations = myLocationApi;

                    if (myLocations == null || myLocations.isEmpty())
                        myLocations = new ArrayList<Location>();

                    Paper.book().write(MY_LOCATIONS, myLocations);
                }

                @Override
                public void onFailure(Call<ArrayList<Location>> call, Throwable t) {
                    System.out.println("Error: " + t.getMessage());
                }
            });
        } else {
            Paper.book().write(MY_LOCATIONS, new ArrayList<Location>());
        }
        myLocations = Paper.book().read(MY_LOCATIONS);
    }
    public ArrayList<Location> getLocations() {
        return locations;
    }

    public ArrayList<Location> getMyLocations() {
        return myLocations;
    }


    public void addLocation(Location location) {
        JSONObject locationObj = null;
        try {
            locationObj = new JSONObject(new Gson().toJson(location));
            locationObj.put("user_id", preferences.getString("userId", ""));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Toast.makeText(context, "Adding Location. Please Wait!", Toast.LENGTH_LONG).show();
        api.addStore(locationObj.toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {

                    try {
                        String res = response.body().string();
                        JSONObject jsonResponse = new JSONObject(res);
                        System.out.println("Response: " + jsonResponse);
                        fetchMyLocations();
                        fetchLocations();
                        Toast.makeText(context, "Location Added. Refresh the List!", Toast.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void editLocation(int position, Location location) {
        JSONObject locationObj = null;
        try {
            locationObj = new JSONObject(new Gson().toJson(location));
            locationObj.put("user_id", preferences.getString("userId", ""));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Toast.makeText(context, "Updating Location. Please Wait!", Toast.LENGTH_LONG).show();
        api.updateStore(position,location).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {

                    try {
                        String res = response.body().string();
                        JSONObject jsonResponse = new JSONObject(res);
                        System.out.println("Response: " + jsonResponse);
                        fetchMyLocations();
                        fetchLocations();
                        Toast.makeText(context, "Location Updated. Refresh the List!", Toast.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteLocation(int position) {
        Toast.makeText(context, "Deleting Location. Please Wait!", Toast.LENGTH_LONG).show();
        api.deleteStore(myLocations.get(position).getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String res = null;
                    try {
                        res = response.body().string();
                        JSONObject jsonResponse = new JSONObject(res);
                        System.out.println("Response: " + jsonResponse);
                        fetchMyLocations();
                        fetchLocations();
                        Toast.makeText(context, "Location Deleted. Refresh the List!", Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Location Type Manipulation
    public void fetchLocationTypes(){
        api.getLocationTypes().enqueue(new Callback<ArrayList<LocationType>>() {
            @Override
            public void onResponse(Call<ArrayList<LocationType>> call, Response<ArrayList<LocationType>> response) {
                ArrayList<LocationType> locationTypeApi = response.body();
                ArrayList<LocationType> locationTypeHD = Paper.book().read(LOCATIONS_TYPE);
                if (locationTypeApi == null || locationTypeApi.isEmpty())
                    locationTypes = locationTypeHD;
                else
                    locationTypes = locationTypeApi;

                if (locationTypes == null || locations.isEmpty())
                    locationTypes = new ArrayList<LocationType>();

                Paper.book().write(LOCATIONS_TYPE, locationTypes);
            }

            @Override
            public void onFailure(Call<ArrayList<LocationType>> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }
        });
        locationTypes = Paper.book().read(LOCATIONS_TYPE);
    }
    public ArrayList<LocationType> getLocationTypes() {
        return locationTypes;
    }

    public ArrayList<String> getLocationTypeNames() {
        ArrayList<String> names = new ArrayList<>();
        for (LocationType lT : locationTypes)
            names.add(lT.getName());
        return names;
    }

    //Item Location Manipulation
    public void fetchItemLocations(){
        api.getItemLocations().enqueue(new Callback<ArrayList<ItemLocation>>() {
            @Override
            public void onResponse(Call<ArrayList<ItemLocation>> call, Response<ArrayList<ItemLocation>> response) {
                ArrayList<ItemLocation> itemLocationApi = response.body();
                ArrayList<ItemLocation> itemLocationHD = Paper.book().read(ITEM_LOCATIONS);
                if (itemLocationApi == null || itemLocationApi.isEmpty())
                    itemLocations = itemLocationHD;
                else
                    itemLocations = itemLocationApi;

                if (itemLocations == null || itemLocations.isEmpty())
                    itemLocations = new ArrayList<ItemLocation>();

                Paper.book().write(ITEM_LOCATIONS, itemLocations);
            }

            @Override
            public void onFailure(Call<ArrayList<ItemLocation>> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }
        });
        itemLocations = Paper.book().read(ITEM_LOCATIONS);
    }
    public ArrayList<ItemLocation> getItemLocations() {
        return itemLocations;
    }

    public ArrayList<ItemLocation> getItemLocations(int itemId) {
        ArrayList<ItemLocation> another = new ArrayList<ItemLocation>();
        Item itm = new Item();
        for (Item i: items)
            if(i.getId()==itemId)
                itm = i;
        for (ItemLocation aux : itemLocations)
            if (itm.getId() == aux.getItem().getId())
                another.add(aux);
        return another;
    }

    public ArrayList<ItemLocation> getLocationItems(int locationId) {
        ArrayList<ItemLocation> another = new ArrayList<ItemLocation>();
        Location loc = new Location();
        for (Location l: locations)
            if(l.getId()==locationId)
                loc = l;
        for (ItemLocation aux : itemLocations)
            if (loc.getId() == aux.getLocation().getId())
                another.add(aux);
        return another;
    }

    public void addItemLocation(ItemLocation itemLocation) {
        itemLocations.add(itemLocation);
        Paper.book().write(ITEM_LOCATIONS, itemLocations);
    }

    public void editItemLocation(int position, ItemLocation itemLocation) {
        itemLocations.set(position, itemLocation);
        Paper.book().write(ITEM_LOCATIONS, itemLocations);
    }

    public void deleteItemLocation(int position) {
        itemLocations.remove(position);
        Paper.book().write(ITEM_LOCATIONS, itemLocations);
    }


    // BuyList Manipulation
    public void fetchBuylists(){
        if (!userId.equalsIgnoreCase("")) {
            api.getListas(userId).enqueue(new Callback<ArrayList<BuyList>>() {
                @Override
                public void onResponse(Call<ArrayList<BuyList>> call, Response<ArrayList<BuyList>> response) {
                    ArrayList<BuyList> buylistApi = response.body();
                    ArrayList<BuyList> buylistHD = Paper.book().read(BUYLISTS);
                    if (buylistApi == null || buylistApi.isEmpty())
                        buylists = buylistHD;
                    else
                        buylists = buylistApi;

                    if (buylists == null || buylists.isEmpty())
                        buylists = new ArrayList<BuyList>();

                    Paper.book().write(BUYLISTS, buylists);
                }

                @Override
                public void onFailure(Call<ArrayList<BuyList>> call, Throwable t) {
                    System.out.println("Error: " + t.getMessage());
                }
            });
        } else {
            Paper.book().write(BUYLISTS, new ArrayList<BuyList>());
        }
        buylists = Paper.book().read(BUYLISTS);
    }
    public ArrayList<BuyList> getBuyLists() {
        return buylists;
    }

    public void addBuyList(BuyList buyList) {
        Toast.makeText(context, "Buylist being added. Please Wait!", Toast.LENGTH_LONG).show();
        JSONObject buylistObj = null;
        try {
            buylistObj = new JSONObject(new Gson().toJson(buyList));
            buylistObj.put("user_id", preferences.getString("userId", ""));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        api.addLista(buylistObj.toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {

                    try {
                        String res = response.body().string();
                        JSONObject jsonResponse = new JSONObject(res);
                        System.out.println("Response: " + jsonResponse);
                        fetchBuylists();
                        Toast.makeText(context, "Buylist Added. Refresh the List!", Toast.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }
        });

    }

    public void editBuyList(int position, BuyList buyList) {
        buylists.set(position, buyList);
        Paper.book().write(BUYLISTS, buylists);
    }

    public void deleteBuyList(int position) {
        Toast.makeText(context, "Buylist being deleted. Please Wait!", Toast.LENGTH_LONG).show();
        api.deleteLista(buylists.get(position).getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String res = null;
                    try {
                        fetchBuylists();
                        res = response.body().string();
                        JSONObject jsonResponse = new JSONObject(res);
                        System.out.println("Response: " + jsonResponse);
                        fetchBuylists();
                        Toast.makeText(context, "Buylist Deleted. Refresh the List!", Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }
        });
    }

    public Double avgPrice(int position) {
        Double SUM = 0.0;

        if (getItemLocations(position).size() == 0)
            return 0.0;

        for (ItemLocation iL : getItemLocations(position))
            SUM += iL.getPrice();

        return SUM / getItemLocations(position).size();
    }

    //Purchase or Active Buylist Manipulation
    public void fetchPurchases(){
        purchases = Paper.book().read(PURCHASES);
        if (purchases == null)
            purchases = new ArrayList<Purchase>();
    }
    public void setPurchases(ArrayList<Purchase> purchases) {
        Paper.book().write(PURCHASES, purchases);
    }

    public void addPurchase(Purchase purchase) {
        for (Purchase p : purchases) {
            if (p.getItemLocation().getId() == purchase.getItemLocation().getId()) {
                p.setQuantity(p.getQuantity() + purchase.getQuantity());
                Paper.book().write(PURCHASES, purchases);
                return;
            }
        }
        purchases.add(purchase);
        Paper.book().write(PURCHASES, purchases);
    }

    public ArrayList<Purchase> getPurchases() {
        return purchases;
    }


}
