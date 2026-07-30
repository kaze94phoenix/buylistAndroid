package com.example.buylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.buylist.adapters.BuyListAdapter;
import com.example.buylist.models.DataManager;
import com.example.buylist.models.Purchase;

import java.util.ArrayList;

public class BuyListDetailsActivity extends AppCompatActivity {
    //Same as the AddBuyListActivity
    private RecyclerView buylist;
    private DataManager dataManager;
    private BuyListAdapter buyListAdapter;
    TextView totalTxt;
    private ArrayList<Purchase> purchases;
    Intent intent;
    public final String EXTRA_ITEM_ID = "item_id";
    int itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_list_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        totalTxt = findViewById(R.id.totalTxt);

        dataManager = new DataManager(this);

        buyListAdapter = new BuyListAdapter();
        buyListAdapter.hasOptions(false);

        intent = getIntent();

        itemID = intent.getIntExtra(EXTRA_ITEM_ID, 0);

        purchases = dataManager.getBuyLists().get(itemID).getPurchases();

        totalTxt.setText("Total: "+dataManager.getPurchasesTotal(purchases)+"0 MT(s)");

        buylist = findViewById(R.id.buylist);
        buyListAdapter.setBuylist(purchases);
        buylist.setAdapter(buyListAdapter);
        buylist.setLayoutManager(new LinearLayoutManager(BuyListDetailsActivity.this));
    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}