package com.example.buylist.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buylist.R;
import com.example.buylist.models.DataManager;
import com.example.buylist.models.Purchase;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * Adapter for the buylist on the main activity [BUYLIST Fragment]
 */
public class BuyListAdapter extends RecyclerView.Adapter<BuyListAdapter.ViewHolder> {

    ArrayList<Purchase> buylist;
    DataManager dataManager;
    boolean options;
    Context context;


    public BuyListAdapter() {
        buylist = new ArrayList<>();
        options = true;
    }

    public void hasOptions(boolean options) {
        this.options = options;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buylist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.item.setText(buylist.get(position).getItemLocation().getItem().getName());
        try {
            holder.location.setText(buylist.get(position).getItemLocation().getLocation().getName() + " \n(" + dataManager.getDistanceItems().get(position) + ")");
        } catch (IndexOutOfBoundsException e) {
            holder.location.setText(buylist.get(position).getItemLocation().getLocation().getName() + " \n(Loading...)");
        }
        holder.price.setText(buylist.get(position).getItemLocation().getPrice() * buylist.get(position).getQuantity() + "0 MTS");
        holder.quantity.setText(buylist.get(position).getQuantity() + " Unit(s)");

        if (!options) {
            holder.edit.setVisibility(View.INVISIBLE);
            holder.delete.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return buylist.size();
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void setBuylist(ArrayList<Purchase> buylist) {
        this.buylist = buylist;
    }

    public void makeDestinationsString() {
        ArrayList<String> destinationNames = new ArrayList<>();

        for (Purchase p : buylist)
            destinationNames.add(p.getItemLocation().getLocation().getGeolocation());

        String dest = "";
        if (destinationNames.isEmpty())
            return;
        dest = destinationNames.get(0);
        for (int i = 1; i < destinationNames.size(); i++)
            dest += "|" + destinationNames.get(i);
        dataManager.fetchDistanceItems(dest);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView item, location, price, quantity, quantityEdit;
        RelativeLayout buyListElement;
        Button edit, delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buyListElement = itemView.findViewById(R.id.buylistElement);
            item = itemView.findViewById(R.id.itemBuylistName);
            location = itemView.findViewById(R.id.locationBuylistName);
            price = itemView.findViewById(R.id.priceBuylistName);
            quantity = itemView.findViewById(R.id.qttyBuylistName);
            quantityEdit = itemView.findViewById(R.id.qttyBuylistEdit);
            edit = itemView.findViewById(R.id.changeQttyBuylist);
            delete = itemView.findViewById(R.id.removeBuylist);

            buyListElement.setOnClickListener(this);
            edit.setOnClickListener(this);
            delete.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case (R.id.buylistElement):
                    String cords = buylist.get(getBindingAdapterPosition()).getItemLocation().getLocation().getGeolocation();
                    // Create a Uri from an intent string. Use the result to create an Intent.
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + cords);
// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
// Make the Intent explicit by setting the Google Maps package
                    mapIntent.setPackage("com.google.android.apps.maps");
// Attempt to start an activity that can handle the Intent
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(mapIntent);
                    }
                    break;

                case (R.id.changeQttyBuylist):
                    if (quantity.getVisibility() == View.VISIBLE) {
                        quantity.setVisibility(View.INVISIBLE);
                        quantityEdit.setVisibility(View.VISIBLE);
                        quantityEdit.setText(String.valueOf(buylist.get(getBindingAdapterPosition()).getQuantity()));
                    } else {
                        buylist.get(getBindingAdapterPosition()).setQuantity(Integer.parseInt(quantityEdit.getText().toString()));
                        dataManager.setPurchases(buylist);
                        notifyItemChanged(getBindingAdapterPosition());
                        quantity.setVisibility(View.VISIBLE);
                        quantityEdit.setVisibility(View.INVISIBLE);
                    }
                    break;

                case (R.id.removeBuylist):
                    int position = getBindingAdapterPosition();
                    Purchase temp = buylist.get(position);
                    String tempDis = dataManager.getDistanceItems().get(position);
                    buylist.remove(position);
                    dataManager.getDistanceItems().remove(position);
                    dataManager.setPurchases(buylist);
                    notifyItemRemoved(position);
                    Snackbar.make((View) itemView.getParent(), "Removing " + temp.getItemLocation().getItem().getName(), Snackbar.LENGTH_LONG).setAction("Undo", view1 -> {
                        buylist.add(position, temp);
                        dataManager.getDistanceItems().add(position, tempDis);
                        dataManager.setPurchases(buylist);
                        notifyItemInserted(position);
                    }).show();
                    break;

            }

        }
    }

}
