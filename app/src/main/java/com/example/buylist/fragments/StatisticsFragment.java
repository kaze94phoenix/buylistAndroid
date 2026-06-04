package com.example.buylist.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.buylist.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    RadioButton listsButton, balanceButton;


    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ListsFragment listsFragment = new ListsFragment();
        BalanceFragment balanceFragment = new BalanceFragment();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        listsButton = view.findViewById(R.id.listsButton);
        balanceButton = view.findViewById(R.id.balanceButton);


        replaceFragment(balanceFragment);


        listsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listsButton.isChecked())
                    replaceFragment(listsFragment);
            }
        });

        balanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(balanceButton.isChecked())
                    replaceFragment(balanceFragment);
            }
        });


        return view;


    }

    //Custom method that replaces an active fragment for another passed on params
    public void replaceFragment(Fragment fragment){
            fragmentManager = getActivity().getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.statisticsLayout,fragment);
            fragmentTransaction.commit();
    }

}