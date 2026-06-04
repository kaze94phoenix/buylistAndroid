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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.buylist.AddItemTypeActivity;
import com.example.buylist.ItemsListActivity;
import com.example.buylist.MainActivity;
import com.example.buylist.R;
import com.example.buylist.adapters.ShoppingItemAdapter;
import com.example.buylist.models.DataManager;
import com.example.buylist.models.Item;
import com.example.buylist.models.ItemType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    DataManager dataManager;
    ShoppingItemAdapter shoppingItemAdapter;
    FloatingActionButton actionButton;
    Intent intent;



    public ProductsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        dataManager = new DataManager(getContext());
        shoppingItemAdapter = new ShoppingItemAdapter();

        shoppingItemAdapter.setItems(dataManager.getItems());
        shoppingItemAdapter.setActivity(getActivity());


        recyclerView = view.findViewById(R.id.itemsListView);

        recyclerView.setAdapter(shoppingItemAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shoppingItemAdapter.setItems(dataManager.getItems());
                recyclerView.setAdapter(shoppingItemAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        actionButton = view.findViewById(R.id.addItemFloatBtn);
        actionButton.setOnClickListener(this);




        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onClick(View view) {
        goToAddItems();
    }

    public void goToAddItems(){

        dialogBuilder = new AlertDialog.Builder(getContext());
        final View addItemPopoutView = getLayoutInflater().inflate(R.layout.add_item_popup,null);

        Spinner spinner = addItemPopoutView.findViewById(R.id.itemTypeSpinner);
        ArrayList<String> another = new ArrayList<String>();
        if(dataManager.getItemTypes()!=null)
            for (ItemType a : dataManager.getItemTypes())
                another.add(a.getName());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, another);

        spinner.setAdapter(arrayAdapter);


        EditText nameTxt = addItemPopoutView.findViewById(R.id.itemNameTxt);
        EditText descriptionTxt = addItemPopoutView.findViewById(R.id.itemDescriptionTxt);
        Button saveBtn = addItemPopoutView.findViewById(R.id.btnSaveItem);
        Button cancelBtn = addItemPopoutView.findViewById(R.id.btnCancelItem);
        Button addItemType = addItemPopoutView.findViewById(R.id.goAddItemType);
        int itemPosition = spinner.getSelectedItemPosition();

        ///////////////////////

        dialogBuilder.setView(addItemPopoutView);
        dialog = dialogBuilder.create();
        dialog.show();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dataManager.addItems(new Item(nameTxt.getText().toString(),descriptionTxt.getText().toString(), dataManager.getItemTypes().get(itemPosition)));
                Toast.makeText(getContext(), "Item Added", Toast.LENGTH_SHORT).show();
                shoppingItemAdapter.notifyItemInserted(shoppingItemAdapter.getItemCount()-1);
                dialog.dismiss();

            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        addItemType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), AddItemTypeActivity.class);
                getActivity().startActivity(intent);
            }
        });






    }

}