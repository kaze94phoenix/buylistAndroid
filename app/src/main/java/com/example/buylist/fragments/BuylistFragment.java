package com.example.buylist.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buylist.MainActivity;
import com.example.buylist.R;
import com.example.buylist.adapters.AddBuyListAdapter;
import com.example.buylist.adapters.BuyListAdapter;
import com.example.buylist.models.BuyList;
import com.example.buylist.models.DataManager;
import com.example.buylist.models.Location;
import com.example.buylist.models.Purchase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 * Fragment for the [BUYLIST] on [Lists and Statistics]
 */
public class BuylistFragment extends Fragment {

    private AlertDialog dialog;
    private TextView totalTxt;
    SharedPreferences preferences;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AlertDialog.Builder dialogBuilder;
    private RecyclerView buylist;
    private DataManager dataManager;
    private BuyListAdapter buyListAdapter;
    private ArrayList<Purchase> purchases;
    FloatingActionButton addToBuyList, saveBuylist, currentLocation;
    private FusedLocationProviderClient locationClient;
    private static final int LOCATION_PERMISSION_REQUEST = 1001;

    public BuylistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buylist, container, false);

        preferences = getContext().getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String origin = preferences.getString("geolocation", "");

        dataManager = new DataManager(getContext());
        buyListAdapter = new BuyListAdapter();
        buyListAdapter.setContext(getContext());
        purchases = dataManager.getPurchases();

        totalTxt = view.findViewById(R.id.totalTxt);
        totalTxt.setText("Total: "+dataManager.getPurchasesTotal()+"0 MT(s)");

        buyListAdapter.setBuylist(purchases);
        buyListAdapter.setDataManager(dataManager);

        buylist = view.findViewById(R.id.buylist);

        buylist.setAdapter(buyListAdapter);
        buylist.setLayoutManager(new LinearLayoutManager(getContext()));

        addToBuyList = view.findViewById(R.id.addItemBtn);
        addToBuyList.setOnClickListener(view1 -> addBuyListItems());

        saveBuylist = view.findViewById(R.id.saveBuylistBtn);
        saveBuylist.setOnClickListener(view2 -> saveBuyListItems());

        locationClient = LocationServices.getFusedLocationProviderClient(getContext());
        currentLocation = view.findViewById(R.id.currentLocBtn);
        currentLocation.setOnClickListener(view3 -> currentLocation());

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            buyListAdapter.setBuylist(purchases);
            buyListAdapter.makeDestinationsString(origin);
            buylist.setAdapter(buyListAdapter);
            buylist.setLayoutManager(new LinearLayoutManager(getContext()));
            swipeRefreshLayout.setRefreshing(false);
        });


        // Inflate the layout for this fragment
        return view;
    }

    private void currentLocation() {

//Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return;
        }

        // Fetch the last known location
        locationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                // Get latitude and longitude
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                buyListAdapter.setBuylist(purchases);
                buyListAdapter.makeDestinationsString(lat+","+lon);
                buylist.setAdapter(buyListAdapter);
                buylist.setLayoutManager(new LinearLayoutManager(getContext()));
            } else {
                // Display error message if location is null
                Toast.makeText(getContext(), "Unable to get location!", Toast.LENGTH_LONG).show();
            }
        });

    }


    public void addBuyListItems() {
        //Building the popup to add items to the buylist
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View addItemView = getLayoutInflater().inflate(R.layout.add_item_buylist, null);

        Spinner locationSpinner = addItemView.findViewById(R.id.locationFilter);
        ArrayList<String> locationNames = new ArrayList<>();
        locationNames.add("All");
        for (Location loc : dataManager.getLocations())
            locationNames.add(loc.getName());
        ArrayAdapter<String> locations = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, locationNames);
        locationSpinner.setAdapter(locations);

        Button add = addItemView.findViewById(R.id.addSelected);
        Button dismiss = addItemView.findViewById(R.id.cancelPopout);

        //Composing the items list
        RecyclerView items = addItemView.findViewById(R.id.itemsRecyclerView);
        AddBuyListAdapter listAdapter = new AddBuyListAdapter();
        listAdapter.setItemLocations(dataManager.getItemLocations());
        items.setAdapter(listAdapter);
        items.setLayoutManager(new LinearLayoutManager(getContext()));

        dialogBuilder.setView(addItemView);
        dialog = dialogBuilder.create();

        dialog.show();

        //Adding elements selected to the buylist
        add.setOnClickListener(view -> {
            if (purchases.isEmpty())
                //If there are no items on the buylist the items checked on the extra list on the adapter are added to it
                for (Purchase p : listAdapter.aux)
                    purchases.add(p);
            else {
                //If there are some items on the buylist, the previous action will be performed, and also quantities will be updated if there are repeated ones
                for (Purchase p : listAdapter.aux) {
                    boolean found = false;
                    for (Purchase pp : purchases)
                        if (pp.getItemLocation().getId() == p.getItemLocation().getId()) {
                            //Every new added item is compared with the existent ones, if they exist it will update its quantities
                            pp.setQuantity(pp.getQuantity() + p.getQuantity());
                            found = true;
                        }
                    if (!found)
                        //If there are no existent elements it will add a brand new one
                        purchases.add(p);


                }

            }
            dataManager.setPurchases(purchases);
            buyListAdapter.setBuylist(purchases);
            buylist.setAdapter(buyListAdapter);
            totalTxt.setText(dataManager.getPurchasesTotal()+"0 MT(s)");
            buylist.setLayoutManager(new LinearLayoutManager(getContext()));
            dialog.dismiss();
        });

        //SearchView and usage of the filter query option
        SearchView searchView = addItemView.findViewById(R.id.itemSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapter.getFilter().filter(newText);
                return true;
            }
        });

        dismiss.setOnClickListener(view -> dialog.dismiss());

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    listAdapter.setItemLocations(dataManager.getItemLocations());
                    items.setAdapter(listAdapter);
                    items.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    listAdapter.setItemLocations(dataManager.getLocationItems(i - 1));
                    items.setAdapter(listAdapter);
                    items.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void saveBuyListItems() {
        dataManager = new DataManager(getContext());
        Date date = new Date();
        dataManager.addBuyList(new BuyList("BuyList #" + dataManager.getBuyLists().size(), date, purchases));
    }


}