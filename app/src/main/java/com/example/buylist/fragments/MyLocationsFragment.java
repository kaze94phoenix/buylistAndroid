package com.example.buylist.fragments;

import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.buylist.R;
import com.example.buylist.adapters.ShoppingLocationAdapter;
import com.example.buylist.models.DataManager;
import com.example.buylist.models.Location;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyLocationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyLocationsFragment extends Fragment implements View.OnClickListener {


    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    DataManager dataManager;
    ShoppingLocationAdapter shoppingLocationAdapter;
    FloatingActionButton actionButton;

    public MyLocationsFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_locations, container, false);

        dataManager = new DataManager(getContext());
        progressBar = view.findViewById(R.id.progressBar);

        shoppingLocationAdapter = new ShoppingLocationAdapter();
        shoppingLocationAdapter.hasOptions(true);
        shoppingLocationAdapter.setLocations(dataManager.getMyLocations());
        shoppingLocationAdapter.setActivity(getActivity());
        shoppingLocationAdapter.setProgressBar(progressBar);

        recyclerView = view.findViewById(R.id.locationsListView);
        recyclerView.setAdapter(shoppingLocationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            dataManager.fetchMyLocations();
            shoppingLocationAdapter.setLocations(dataManager.getMyLocations());
            recyclerView.setAdapter(shoppingLocationAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            swipeRefreshLayout.setRefreshing(false);
        });

        actionButton = view.findViewById(R.id.addLocationFloatBtn);
        actionButton.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    public void onClick(View view) {
        goToAddLocation();
    }

    public void goToAddLocation() {

        dialogBuilder = new AlertDialog.Builder(getContext());
        final View addLocationPopoutView = getLayoutInflater().inflate(R.layout.add_location_popup, null);
        Location locationX = new Location();

        ArrayList<String> locTypeNames = dataManager.getLocationTypeNames();
        ArrayAdapter<String> spinnerNames = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                locTypeNames
        );
        spinnerNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner locationTypeSp = addLocationPopoutView.findViewById(R.id.locationTypeSp);
        locationTypeSp.setAdapter(spinnerNames);

        EditText nameTxt = addLocationPopoutView.findViewById(R.id.locationNameTxt);
        EditText addressTxt = addLocationPopoutView.findViewById(R.id.locationAddressTxt);
        EditText geoLocTxt = addLocationPopoutView.findViewById(R.id.locationGeoTxt);

        Button saveBtn = addLocationPopoutView.findViewById(R.id.saveLocation);
        Button cancelBtn = addLocationPopoutView.findViewById(R.id.cancelLocation);

        dialogBuilder.setView(addLocationPopoutView);
        dialog = dialogBuilder.create();
        dialog.show();

        saveBtn.setOnClickListener(view -> {
            progressBar.setVisibility(VISIBLE);
            locationX.setLocationType(dataManager.getLocationTypes().get(locationTypeSp.getSelectedItemPosition()));
            locationX.setAddress(addressTxt.getText().toString());
            locationX.setName(nameTxt.getText().toString());
            locationX.setGeolocation(geoLocTxt.getText().toString());
            dataManager.addLocation(locationX, progressBar, shoppingLocationAdapter);
            dialog.dismiss();
        });

        cancelBtn.setOnClickListener(view -> dialog.dismiss());
    }


}