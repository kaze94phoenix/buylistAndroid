package com.example.buylist.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buylist.R;
import com.example.buylist.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsNLocalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsNLocalsFragment extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;
    ProductsFragment productsFragment;
    LocationsFragment locationsFragment;

    public ProductsNLocalsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products_n_locals, container, false);

        productsFragment = new ProductsFragment();
        locationsFragment = new LocationsFragment();

        viewPager = view.findViewById(R.id.locationsNProductsPager);
        tabLayout = view.findViewById(R.id.locationsNProductsTabs);

        tabLayout.setupWithViewPager(viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);
        viewPagerAdapter.addFragment(productsFragment,"Products");
        viewPagerAdapter.addFragment(locationsFragment,"Locations");
        viewPager.setAdapter(viewPagerAdapter);

        // Inflate the layout for this fragment
        return view;
    }
}