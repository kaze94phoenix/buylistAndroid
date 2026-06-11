package com.example.buylist.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buylist.R;
import com.example.buylist.models.DataManager;
import com.example.buylist.models.ItemLocation;
import com.example.buylist.models.Purchase;

import java.util.ArrayList;

public class ItemLocationAdapter extends RecyclerView.Adapter<ItemLocationAdapter.ViewHolder> {
    private ArrayList<ItemLocation> itemLocations;
    //Context of the RecyclerView activity
    private Activity activity;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private DataManager dataManager;

    public ItemLocationAdapter() {
    }

    //Sets the list of items of the adapter
    public void setItemLocations(ArrayList<ItemLocation> itemLocations) {
        this.itemLocations = itemLocations;
        notifyDataSetChanged();
    }

    public ArrayList<ItemLocation> getItemLocations() {
        return itemLocations;
    }

    //Sets the context/Activity of the recyclerView
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemLocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //sets and inflates the view with the viewholder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_item_location, parent, false);
        ItemLocationAdapter.ViewHolder holder = new ItemLocationAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemLocationAdapter.ViewHolder holder, int position) {
        //binds the attributes of the model to the viewHolder
        holder.priceLabel.setText(itemLocations.get(position).getPrice() +"0 MTS");
        holder.locationLabel.setText(itemLocations.get(position).getLocation().getName());
    }


    @Override
    public int getItemCount() {
        return itemLocations.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView priceLabel, locationLabel;
        private Button addItemLoc;


        public ViewHolder(View itemView) {
            super(itemView);
            priceLabel = itemView.findViewById(R.id.priceLocationLabel);
            locationLabel = itemView.findViewById(R.id.locationItemLabel);
            addItemLoc = itemView.findViewById(R.id.addItemLocBtn);

            addItemLoc.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            EditText quantityTxt;
            dataManager = new DataManager(activity);
            Button addBtn, cancelBtn;
            switch (view.getId()) {

                case R.id.addItemLocBtn:
                    dialogBuilder = new AlertDialog.Builder(activity);
                    final View editItemLocView = activity.getLayoutInflater().inflate(R.layout.add_item_to_buylist_popup, null);

                    quantityTxt = editItemLocView.findViewById(R.id.itemQuantityTxt);
                    addBtn = editItemLocView.findViewById(R.id.btnSaveItemLocation);
                    cancelBtn = editItemLocView.findViewById(R.id.btnCancelItemLocation);

                    dialogBuilder.setView(editItemLocView);
                    dialog = dialogBuilder.create();
                    dialog.show();

                    cancelBtn.setOnClickListener(view1 -> dialog.dismiss());
                    addBtn.setOnClickListener(view2 -> {
                        Purchase purchase = new Purchase();
                        purchase.setQuantity(Integer.parseInt(quantityTxt.getText().toString()));
                        purchase.setItemLocation(getItemLocations().get(getBindingAdapterPosition()));
                        purchase.setPurchased(false);
                        dataManager.addPurchase(purchase);

                        Toast.makeText(activity, "Item Added to List", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
                    break;
            }
        }
    }
}
