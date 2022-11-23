package com.example.buylist.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.bumptech.glide.Registry;
import com.bumptech.glide.load.model.ByteBufferEncoder;
import com.example.buylist.models.ItemType;
import com.example.buylist.models.Item;
import com.example.buylist.models.Location;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

import io.paperdb.Paper;

public class DataManager {

    private Context context;

    private static final String ITEMS_TYPE = "items type";
    private static final String ITEMS = "items";
    private static final String LOCATIONS = "locations";
    private static final String ITEM_LOCATIONS = "item_locations";
    private static final String BUYLISTS = "buylists";
    private static final String PURCHASES = "purchases";

    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;
    //private Gson gson;
    private Paper paper;


    private ArrayList<ItemType> itemTypes;
    private ArrayList<Item> items;
    private ArrayList<Location> locations;
    private ArrayList<ItemLocation> itemLocations;
    private ArrayList<BuyList> buyLists;
    private ArrayList<Purchase> purchases;

    public DataManager(Context context) {

        Paper.init(context);

        itemTypes = Paper.book().read(ITEMS_TYPE);
        if (itemTypes == null)
            itemTypes = new ArrayList<ItemType>();

        items = Paper.book().read(ITEMS);
        if (items == null)
            items = new ArrayList<Item>();

        locations = Paper.book().read(LOCATIONS);
        if (locations == null)
            locations = new ArrayList<Location>();

        itemLocations = Paper.book().read(ITEM_LOCATIONS);
        if (itemLocations == null)
            itemLocations = new ArrayList<ItemLocation>();

        buyLists = Paper.book().read(BUYLISTS);
        if (buyLists == null)
            buyLists = new ArrayList<BuyList>();

        purchases = Paper.book().read(PURCHASES);
        if (purchases == null)
            purchases = new ArrayList<Purchase>();




        /*
        sharedPreference = context.getSharedPreferences("shopping_db", Context.MODE_PRIVATE);
        editor = sharedPreference.edit();

        gson = new Gson();

        Type typeTypeItem = new TypeToken<ArrayList<ItemType>>() {
        }.getType();
        itemTypes = gson.fromJson(sharedPreference.getString(ITEMS_TYPE, null), typeTypeItem);
        if (itemTypes == null)
            itemTypes = new ArrayList<ItemType>();

        Type typeItem = new TypeToken<ArrayList<Item>>() {
        }.getType();
        items = gson.fromJson(sharedPreference.getString(ITEMS, null), typeItem);
        if (items == null)
            items = new ArrayList<Item>();


        Type typeLocation = new TypeToken<ArrayList<Location>>() {
        }.getType();
        locations = gson.fromJson(sharedPreference.getString(LOCATIONS, null), typeLocation);
        if (locations == null)
            locations = new ArrayList<Location>();

        Type typeItemLocation = new TypeToken<ArrayList<ItemLocation>>() {
        }.getType();
        itemLocations = gson.fromJson(sharedPreference.getString(ITEM_LOCATIONS, null), typeItemLocation);
        if (itemLocations == null)
            itemLocations = new ArrayList<ItemLocation>();

        Type typeBuylist = new TypeToken<ArrayList<BuyList>>() {
        }.getType();
        buyLists = gson.fromJson(sharedPreference.getString(BUYLISTS, null), typeBuylist);
        if (buyLists == null)
            buyLists = new ArrayList<BuyList>();

        Type typePurchase = new TypeToken<ArrayList<Purchase>>() {
        }.getType();
        purchases = gson.fromJson(sharedPreference.getString(PURCHASES, null), typePurchase);
        if (purchases == null)
            purchases = new ArrayList<Purchase>();
*/

    }

    //Item Type
    public ArrayList<ItemType> getItemTypes() {
        return itemTypes;
    }


    public void addItemType(ItemType itemType) {
        itemTypes.add(itemType);
        Paper.book().write(ITEMS_TYPE,itemTypes);

        //editor.putString(ITEMS_TYPE, gson.toJson(itemTypes));
        //editor.commit();
    }

    public void deleteItemType(int position) {
        ItemType itemType = itemTypes.get(position);
        itemTypes.remove(position);
        Paper.book().write(ITEMS_TYPE,itemTypes);

        //editor.putString(ITEMS_TYPE, gson.toJson(itemTypes));
        //editor.commit();
        for (int i = 0; i < items.size(); i++)
            if (items.get(i).getItemType().compareTo(itemType) > 0)
                deleteItem(i);
    }


    //Item
    public void editItem(int position, Item item) {
        Item itemA = items.get(position);
        items.set(position, item);
        Paper.book().write(ITEMS,items);
        //editor.putString(ITEMS, gson.toJson(items));
        //editor.commit();
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
        Paper.book().write(ITEMS,items);
        //editor.putString(ITEMS, gson.toJson(items));
        //editor.commit();
    }

    public void deleteItem(int position) {
        Item item = items.get(position);
        items.remove(position);
        Paper.book().write(ITEMS,items);
        //editor.putString(ITEMS, gson.toJson(items));
        //editor.commit();
        for (int i = 0; i < itemLocations.size(); i++)
            if (item.compareTo(itemLocations.get(i).getItem()) > 0)
                deleteItemLocation(i);
    }

    //Location
    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void addLocation(Location location) {
        locations.add(location);
        Paper.book().write(LOCATIONS,locations);
        //editor.putString(LOCATIONS, gson.toJson(locations));
        //editor.commit();
    }

    public void editLocation(int position, Location location) {
        Location locationA = locations.get(position);
        locations.set(position, location);
        Paper.book().write(LOCATIONS,locations);
        //editor.putString(LOCATIONS, gson.toJson(locations));
        //editor.commit();
        for (int i = 0; i < itemLocations.size(); i++)
            if (itemLocations.get(i).getLocation().compareTo(locationA) > 0) {
                ItemLocation itemLocation = itemLocations.get(i);
                itemLocation.setItem(items.get(position));
                editItemLocation(i, itemLocation);
            }

    }

    public void deleteLocation(int position) {
        Location location = locations.get(position);
        locations.remove(position);
        Paper.book().write(LOCATIONS,locations);
        //editor.putString(LOCATIONS, gson.toJson(locations));
        //editor.commit();
        for (int i = 0; i < itemLocations.size(); i++)
            if (location.compareTo(itemLocations.get(i).getLocation()) > 0)
                deleteItemLocation(i);
    }


    //Item Location
    public ArrayList<ItemLocation> getItemLocations() {
        return itemLocations;
    }

    public ArrayList<ItemLocation> getItemLocations(int itemId) {
        ArrayList<ItemLocation> another = new ArrayList<ItemLocation>();
        for (ItemLocation aux : itemLocations)
            if (getItems().get(itemId).compareTo(aux.getItem()) > 0)
                another.add(aux);
        return another;
    }

    public ArrayList<ItemLocation> getLocationItems(int locationId) {
        ArrayList<ItemLocation> another = new ArrayList<ItemLocation>();
        for (ItemLocation aux : itemLocations)
            if (getLocations().get(locationId).compareTo(aux.getLocation()) > 0)
                another.add(aux);
        return another;
    }

    public void addItemLocation(ItemLocation itemLocation) {
        itemLocations.add(itemLocation);
        Paper.book().write(ITEM_LOCATIONS,itemLocations);
        //editor.putString(ITEM_LOCATIONS, gson.toJson(itemLocations));
        //editor.commit();
    }

    public void editItemLocation(int position, ItemLocation itemLocation) {
        itemLocations.set(position, itemLocation);
        Paper.book().write(ITEM_LOCATIONS,itemLocations);
        //editor.putString(ITEM_LOCATIONS, gson.toJson(itemLocations));
        //editor.commit();
    }

    public void deleteItemLocation(int position) {
        itemLocations.remove(position);
        Paper.book().write(ITEM_LOCATIONS,itemLocations);
        //editor.putString(ITEM_LOCATIONS, gson.toJson(itemLocations));
        //editor.commit();
    }


    // BuyList
    public ArrayList<BuyList> getBuyLists() {
        return buyLists;
    }

    public void addBuyList(BuyList buyList) {
        buyLists.add(buyList);
        Paper.book().write(BUYLISTS,buyLists);
        //editor.putString(BUYLISTS, gson.toJson(buyLists));
        //editor.commit();
    }

    public void editBuyList(int position, BuyList buyList) {
        buyLists.set(position, buyList);
        Paper.book().write(BUYLISTS,buyLists);
        //editor.putString(BUYLISTS, gson.toJson(buyLists));
        //editor.commit();
    }

    public void deleteBuyList(int position) {
        buyLists.remove(position);
        Paper.book().write(BUYLISTS,buyLists);
        //editor.putString(BUYLISTS, gson.toJson(buyLists));
        //editor.commit();
    }

    public Double avgPrice(int position) {
        Double SUM = 0.0;
        for (ItemLocation iL : getItemLocations(position))
            SUM += iL.getPrice();

        return SUM / getItemLocations(position).size();
    }

    //Purchase or Active Buylist
    public void setPurchases(ArrayList<Purchase> purchases) {
        Paper.book().write(PURCHASES,purchases);
        //editor.putString(PURCHASES, gson.toJson(purchases));
        //editor.commit();
    }


    public ArrayList<Purchase> getPurchases() {
        return purchases;
    }


}
