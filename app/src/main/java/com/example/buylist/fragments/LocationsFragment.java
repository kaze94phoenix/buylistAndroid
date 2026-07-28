package com.example.buylist.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buylist.R;
import com.example.buylist.adapters.ShoppingLocationAdapter;
import com.example.buylist.models.DataManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    DataManager dataManager;
    ShoppingLocationAdapter shoppingLocationAdapter;


    public LocationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_locations, container, false);

        dataManager = new DataManager(getContext());

        shoppingLocationAdapter = new ShoppingLocationAdapter();
        shoppingLocationAdapter.hasOptions(false);
        shoppingLocationAdapter.setLocations(dataManager.getLocations());
        shoppingLocationAdapter.setActivity(getActivity());

        recyclerView = view.findViewById(R.id.locationsListView);
        recyclerView.setAdapter(shoppingLocationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            shoppingLocationAdapter.setLocations(dataManager.getLocations());
            recyclerView.setAdapter(shoppingLocationAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            swipeRefreshLayout.setRefreshing(false);
        });

        // Inflate the layout for this fragment
        return view;
    }
}