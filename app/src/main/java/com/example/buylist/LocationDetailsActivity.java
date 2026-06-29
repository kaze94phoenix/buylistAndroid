package com.example.buylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.buylist.adapters.LocationItemAdapter;
import com.example.buylist.adapters.ShoppingLocationAdapter;
import com.example.buylist.models.DataManager;
import com.example.buylist.models.Location;

import java.util.ArrayList;

public class LocationDetailsActivity extends AppCompatActivity {

    private TextView locationName, locationType, locationAddress;
    private DataManager dataManager;
    private ArrayList<Location> locations;
    private Intent intent;
    private int locationId;
    private Location location;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LocationItemAdapter locationItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataManager = new DataManager(this);
        intent = getIntent();
        locationId = intent.getIntExtra(ShoppingLocationAdapter.EXTRA_LOCATION_ID, 0);

        locations = dataManager.getLocations();
        for(Location l:locations)
            if(l.getId()==locationId)
                location=l;

        locationName = findViewById(R.id.locationDName);
        locationType = findViewById(R.id.locationDType);
        locationAddress = findViewById(R.id.locationDAdress);

        locationName.setText(location.getName());
        locationType.setText(location.getLocationType().getName());
        locationAddress.setText(location.getAddress());

        locationItemAdapter = new LocationItemAdapter();
        locationItemAdapter.setActivity(this);
        locationItemAdapter.setItemLocations(dataManager.getLocationItems(locationId));

        recyclerView = findViewById(R.id.locationProductsRV);
        recyclerView.setAdapter(locationItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            locationItemAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(locationItemAdapter);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}