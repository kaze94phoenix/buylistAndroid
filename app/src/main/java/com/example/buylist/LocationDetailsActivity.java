package com.example.buylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.buylist.adapters.LocationItemAdapter;
import com.example.buylist.adapters.ShoppingLocationAdapter;
import com.example.buylist.models.DataManager;
import com.example.buylist.models.Location;

import java.util.ArrayList;

public class LocationDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView locationName, locationType, locationAddress;
    private Button locationBt;
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
        locationBt = findViewById(R.id.locationBt);

        locationName.setText(location.getName());
        locationType.setText(location.getLocationType().getName());
        locationAddress.setText(location.getAddress());

        locationItemAdapter = new LocationItemAdapter();
        locationItemAdapter.setActivity(this);
        locationItemAdapter.setItemLocations(dataManager.getLocationItems(locationId));

        recyclerView = findViewById(R.id.locationProductsRV);
        recyclerView.setAdapter(locationItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        locationBt.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        String cords = location.getGeolocation();
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + cords);
// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
// Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");
// Attempt to start an activity that can handle the Intent
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}