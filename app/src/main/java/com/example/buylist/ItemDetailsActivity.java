package com.example.buylist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buylist.adapters.ItemLocationAdapter;
import com.example.buylist.adapters.ShoppingItemAdapter;
import com.example.buylist.models.DataManager;
import com.example.buylist.models.Item;
import com.example.buylist.models.ItemLocation;
import com.example.buylist.models.Location;

import java.util.ArrayList;

public class ItemDetailsActivity extends AppCompatActivity {
    private TextView itemName, itemDescription, itemPrice, itemType;
    private DataManager dataManager;
    private ArrayList<Item> items;
    private ArrayList<Location> locations;
    private Intent intent;
    private int itemId;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemLocationAdapter itemLocationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataManager = new DataManager(this);

        intent = getIntent();
        itemId = intent.getIntExtra(ShoppingItemAdapter.EXTRA_ITEM_ID, 0);

        items = dataManager.getItems();
        locations = dataManager.getLocations();

        itemName = findViewById(R.id.itemDName);
        itemDescription = findViewById(R.id.itemDDescription);
        itemPrice = findViewById(R.id.itemDPrice);
        itemType = findViewById(R.id.itemDType);

        itemName.setText(items.get(itemId).getName());
        itemDescription.setText(items.get(itemId).getDescription());
        itemType.setText(items.get(itemId).getItemType().getName());
        itemPrice.setText(dataManager.avgPrice(itemId)+"0 MTS");

        itemLocationAdapter = new ItemLocationAdapter();
        itemLocationAdapter.setActivity(this);
        itemLocationAdapter.setItemLocations(dataManager.getItemLocations(itemId));

        recyclerView = findViewById(R.id.locationPricesRV);
        recyclerView.setAdapter(itemLocationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            itemLocationAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(itemLocationAdapter);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}