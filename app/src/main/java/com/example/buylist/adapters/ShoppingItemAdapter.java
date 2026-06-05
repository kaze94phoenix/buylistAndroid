package com.example.buylist.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buylist.AddItemTypeActivity;
import com.example.buylist.ItemDetailsActivity;
import com.example.buylist.R;
import com.example.buylist.models.DataManager;
import com.example.buylist.models.Item;
import com.example.buylist.models.ItemType;

import java.util.ArrayList;

public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.ViewHolder> {
    private ArrayList<Item> items;
    //ID of each item used to navigate to or manipulate each item
    public static final String EXTRA_ITEM_ID = "item_id";
    //Context of the RecyclerView activity
    private Activity activity;
    private DataManager dataManager;
    private Intent intent;

    public ShoppingItemAdapter() {
    }

    //Sets the list of items of the adapter
    public void setItems(ArrayList<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    //Sets the context/Activity of the recyclerView
    public void setActivity(Activity activity) {
        this.activity = activity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //sets and inflates the view with the viewholder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_shopping_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //binds the attributes of the model to the viewHolder
        holder.txtItemName.setText(items.get(position).getName());
        dataManager = new DataManager(activity);
        holder.txtItemAvgPrice.setText(dataManager.avgPrice(position) + "0 MTS");
    }


    @Override
    public int getItemCount() {
        return items.size();

    }


    //Inner Class ViewHolder that takes the View Items to be used on the adapter
    //implements on click listener
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtItemName, txtItemAvgPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItemName = itemView.findViewById(R.id.itemName);
            txtItemAvgPrice = itemView.findViewById(R.id.itemAvgPrice);
            dataManager = new DataManager(activity);

            itemView.setOnClickListener(view -> {
                intent = new Intent(activity, ItemDetailsActivity.class);
                intent.putExtra(EXTRA_ITEM_ID, getAdapterPosition());
                activity.startActivity(intent);
            });
        }


    }
}
