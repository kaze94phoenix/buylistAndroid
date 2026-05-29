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
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private int itemId;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemLocationAdapter itemLocationAdapter;
    public static final String EXTRA_ITEM_ID = "item_id";

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

        itemLocationAdapter = new ItemLocationAdapter();

        itemLocationAdapter.setActivity(this);
        itemLocationAdapter.setItemLocations(dataManager.getItemLocations(itemId));



        recyclerView = findViewById(R.id.locationPricesRV);

        recyclerView.setAdapter(itemLocationAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemLocationAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(itemLocationAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });



    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void addItemLocation(View view) {

        dialogBuilder = new AlertDialog.Builder(this);
        final View addItemLocationView = getLayoutInflater().inflate(R.layout.add_item_location_popup, null);
        ArrayList<String> locationsNames = new ArrayList<>();

        Spinner locationsSpinner = addItemLocationView.findViewById(R.id.locationSpinner);
        locationsSpinner.setVisibility(View.GONE);
        if (dataManager.getLocations() != null) {
            for (Location a : locations) {
                locationsNames.add(a.getName());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locationsNames);
            locationsSpinner.setAdapter(arrayAdapter);
        }

            EditText priceTxt = addItemLocationView.findViewById(R.id.itemPriceTxt);
            Button cancelBtn = addItemLocationView.findViewById(R.id.btnCancelItemLocation);
            Button saveBtn = addItemLocationView.findViewById(R.id.btnSaveItemLocation);
            Button addLocation = addItemLocationView.findViewById(R.id.goAddLocation);


            dialogBuilder.setView(addItemLocationView);
            dialog = dialogBuilder.create();
            dialog.show();



            addLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(ItemDetailsActivity.this, AddLocationActivity.class);
                    intent.putExtra(EXTRA_ITEM_ID,itemId);
                    startActivity(intent);
                }
            });


            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataManager.addItemLocation(new ItemLocation(locations.get(locationsSpinner.getSelectedItemPosition()),items.get(itemId),Double.parseDouble(priceTxt.getText().toString())));
                    ArrayList<ItemLocation> test = dataManager.getItemLocations(itemId);
                    itemLocationAdapter.setItemLocations(test);
                    itemLocationAdapter.notifyItemInserted(itemLocationAdapter.getItemCount()-1);
                    Toast.makeText(ItemDetailsActivity.this, "Item Location Added", Toast.LENGTH_SHORT).show();
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