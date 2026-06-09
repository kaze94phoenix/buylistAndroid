package com.example.buylist.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buylist.R;
import com.example.buylist.adapters.BuyListListAdapter;
import com.example.buylist.models.DataManager;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ListsFragment extends Fragment {

    RecyclerView buylistList;
    public DataManager dataManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    BuyListListAdapter buyListListAdapter;
    Intent intent;



    public ListsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lists, container, false);

        dataManager = new DataManager(getActivity());

        buyListListAdapter = new BuyListListAdapter();
        buyListListAdapter.setActivity(getActivity());
        buyListListAdapter.setBuyLists(dataManager.getBuyLists());

        buylistList = view.findViewById(R.id.buyListListTest);
        buylistList.setAdapter(buyListListAdapter);
        buylistList.setLayoutManager(new LinearLayoutManager(getActivity()));


        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            dataManager = new DataManager(getContext());
            buyListListAdapter.setBuyLists(dataManager.getBuyLists());
            buylistList.setAdapter(buyListListAdapter);
            buylistList.setLayoutManager(new LinearLayoutManager(getContext()));
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }


}