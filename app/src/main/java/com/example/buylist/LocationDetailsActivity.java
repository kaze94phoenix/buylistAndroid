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
import com.example.buylist.adapters.LocationItemAdapter;
import com.example.buylist.adapters.ShoppingLocationAdapter;
import com.example.buylist.models.DataManager;
import com.example.buylist.models.Item;
import com.example.buylist.models.ItemLocation;
import com.example.buylist.models.Location;

import java.util.ArrayList;

public class LocationDetailsActivity extends AppCompatActivity {

    private TextView locationName, locationDescription, locationAddress;
    private DataManager dataManager;
    private ArrayList<Item> items;
    private ArrayList<Location> locations;
    private Intent intent;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private int locationId;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LocationItemAdapter locationItemAdapter;
    public static final String EXTRA_LOCATION_ID = "location_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataManager = new DataManager(this);
        intent = getIntent();
        locationId = intent.getIntExtra(ShoppingLocationAdapter.EXTRA_LOCATION_ID, 0);

        items = dataManager.getItems();
        locations = dataManager.getLocations();

        locationName = findViewById(R.id.locationDName);
        locationDescription = findViewById(R.id.locationDDescription);
        locationAddress = findViewById(R.id.locationDAdress);

        locationName.setText(locations.get(locationId).getName());
        locationDescription.setText(locations.get(locationId).getDescription());
        locationAddress.setText(locations.get(locationId).getAddress());

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

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void addItemLocation(View view) {

        dialogBuilder = new AlertDialog.Builder(this);
        final View addItemLocationView = getLayoutInflater().inflate(R.layout.add_item_location_popup, null);
        ArrayList<String> itemsNames = new ArrayList<>();

        Spinner itemsSpinner = addItemLocationView.findViewById(R.id.locationSpinner);
        if (dataManager.getItems() != null) {
            for (Item a : items) {
                itemsNames.add(a.getName());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsNames);
            itemsSpinner.setAdapter(arrayAdapter);
        }

        EditText price = addItemLocationView.findViewById(R.id.itemPriceTxt);
        TextView itemsLabel = addItemLocationView.findViewById(R.id.locationLabel);
        Button cancelBtn = addItemLocationView.findViewById(R.id.btnCancelItemLocation);
        Button saveBtn = addItemLocationView.findViewById(R.id.btnSaveItemLocation);
        Button addItem = addItemLocationView.findViewById(R.id.goAddLocation);

        itemsLabel.setText("Items");

        dialogBuilder.setView(addItemLocationView);
        dialog = dialogBuilder.create();
        dialog.show();



        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(LocationDetailsActivity.this, AddItemActivity.class);
                intent.putExtra(EXTRA_LOCATION_ID,locationId);
                startActivity(intent);
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataManager.addItemLocation(new ItemLocation(locations.get(locationId),items.get(itemsSpinner.getSelectedItemPosition()),Double.parseDouble(price.getText().toString())));
                ArrayList<ItemLocation> test = dataManager.getLocationItems(locationId);
                locationItemAdapter.setItemLocations(test);
                locationItemAdapter.notifyItemInserted(locationItemAdapter.getItemCount()-1);
                Toast.makeText(LocationDetailsActivity.this, "Item Location Added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }


}