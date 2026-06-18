package com.example.buylist.fragments;

import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.buylist.R;
import com.example.buylist.adapters.ShoppingLocationAdapter;
import com.example.buylist.models.DataManager;
import com.example.buylist.models.Location;
import com.example.buylist.models.LocationType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyLocationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyLocationsFragment extends Fragment implements View.OnClickListener {


    private RecyclerView recyclerView;
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

        shoppingLocationAdapter = new ShoppingLocationAdapter();
        shoppingLocationAdapter.hasOptions(true);
        shoppingLocationAdapter.setLocations(dataManager.getMyLocations());
        shoppingLocationAdapter.setActivity(getActivity());

        recyclerView = view.findViewById(R.id.locationsListView);
        recyclerView.setAdapter(shoppingLocationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
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
        EditText addressTxt = addLocationPopoutView.findViewById(R.id.locationAdressTxt);
        EditText geoLocTxt = addLocationPopoutView.findViewById(R.id.locationGeoTxt);

        Button saveBtn = addLocationPopoutView.findViewById(R.id.saveLocation);
        Button cancelBtn = addLocationPopoutView.findViewById(R.id.cancelLocation);

        dialogBuilder.setView(addLocationPopoutView);
        dialog = dialogBuilder.create();
        dialog.show();
        locationTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                locationX.setLocationType(dataManager.getLocationTypes().get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        saveBtn.setOnClickListener(view -> {
            locationX.setAddress(addressTxt.getText().toString());
            locationX.setName(nameTxt.getText().toString());
            locationX.setGeolocation(geoLocTxt.getText().toString());
            dataManager.addLocation(locationX);
            shoppingLocationAdapter.setLocations(dataManager.getMyLocations());
            recyclerView.setAdapter(shoppingLocationAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            Toast.makeText(getContext(), "Location Added", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        cancelBtn.setOnClickListener(view -> dialog.dismiss());
    }

    /**
     // Source - https://stackoverflow.com/a/54051106
     // Posted by Алексей Мальченко
     // Retrieved 2026-06-18, License - CC BY-SA 4.0

     class MyAsyncTask extends AsyncTask<Void, Void, List<String>> {
     MyAdapter myAdapter;
     ArrayList<String> values = new ArrayList<>();

     public MyAsyncTask(MyAdapter adapter) {
     this.myAdapter = myAdapter;
     }

     @Override
     protected List<String> doInBackground(String... params) {
     ArrayList<String> result = new ArrayList<>();
     // long operation, for example: get results from url
     return result;
     }

     @Override
     protected void onPostExecute(List<String> list) {
     myAdapter.setNewList(list);
     }
     }

     class MyAdapter {

     private List<String> list;

     ............

     void setNewList(List<String> list) {
     this.list = list;
     notifyDataSetChanged();
     }

     ............
     }

     */
}