package com.example.buylist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buylist.R;
import com.example.buylist.models.ItemLocation;
import com.example.buylist.models.Purchase;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Adapter for the list to filter the items to add on the main Buylist [BUYLIST Fragment]
 */
public class AddBuyListAdapter extends RecyclerView.Adapter<AddBuyListAdapter.ViewHolder> implements Filterable {

    ArrayList<ItemLocation> itemLocations;
    ArrayList<ItemLocation> itemsLocationsFiltered;
    public ArrayList<Purchase> aux;

    public AddBuyListAdapter() {
    }

    public void setItemLocations(ArrayList<ItemLocation> itemLocations) {
        this.itemLocations = itemLocations;
        itemsLocationsFiltered = new ArrayList<>(itemLocations);
        aux = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buylist_item_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemName.setText(itemsLocationsFiltered.get(position).getItem().getName());
        holder.locationName.setText(itemsLocationsFiltered.get(position).getLocation().getName());
        holder.price.setText(itemsLocationsFiltered.get(position).getPrice() +"0 MTS");
        holder.quantity.setText("1");
        //Actions taken upon checking or unchecking the element of the list
        holder.isSelected.setOnClickListener(view -> {
            if (holder.isSelected.isChecked())
                //If checked it adds the selected item and quantity(Purchase) to an extra list
                aux.add(new Purchase(itemsLocationsFiltered.get(holder.getBindingAdapterPosition()), Integer.parseInt(holder.quantity.getText().toString())));
            else
                //If unchecked removes Purchases added to the extra list based on the item.
                for (Purchase p : aux)
                    if (p.getItemLocation().getId() == itemsLocationsFiltered.get(holder.getBindingAdapterPosition()).getId())
                        aux.remove(p);
        });

    }


    @Override
    public int getItemCount() {
        return itemsLocationsFiltered.size();
    }

    //Filering values
    @Override
    public Filter getFilter() {
        return filter;
    }

    //Creating the object containing the filtered results
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<ItemLocation> filtered = new ArrayList<ItemLocation>();
            //If the search field is empty will show all items
            if (charSequence.toString().isEmpty())
                filtered.addAll(itemLocations);
            else
                //If it's not empty will filter using lower case text, and it will store on a filtered results object
                for (ItemLocation iL : itemLocations)
                    if (iL.getItem().getName().toLowerCase().contains(charSequence.toString().toLowerCase()))
                        filtered.add(iL);

            FilterResults filterResults = new FilterResults();
            filterResults.values = filtered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            //Updating the adapter list
            itemsLocationsFiltered.clear();
            itemsLocationsFiltered.addAll((Collection<? extends ItemLocation>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, locationName, price, quantity;
        CheckBox isSelected;

        public ViewHolder(View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemBuylist);
            locationName = itemView.findViewById(R.id.locationBuylist);
            price = itemView.findViewById(R.id.priceBuylist);
            quantity = itemView.findViewById(R.id.qttyAddBuylistText);
            isSelected = itemView.findViewById(R.id.checkBoxBuylist);

        }
    }

}
