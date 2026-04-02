package com.example.buylist.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buylist.R;
import com.example.buylist.models.DataManager;
import com.example.buylist.models.ItemLocation;
import com.example.buylist.models.Location;

import java.util.ArrayList;

public class LocationItemAdapter extends RecyclerView.Adapter<LocationItemAdapter.ViewHolder> {
    private ArrayList<ItemLocation> itemLocations, another;
    //ID of each item used to navigate to or manipulate each item
    public static final String EXTRA_ITEM_ID = "item_id";
    //Context of the RecyclerView activity
    private Activity activity;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    Intent intent;
    private DataManager dataManager;

    public LocationItemAdapter() {
    }

    //Sets the list of items of the adapter
    public void setItemLocations(ArrayList<ItemLocation> itemLocations) {
        this.itemLocations = itemLocations;
        notifyDataSetChanged();
    }

    public ArrayList<ItemLocation> getItemLocations(){
        return itemLocations;
    }

    //Sets the context/Activity of the recyclerView
    public void setActivity(Activity activity){
        this.activity=activity;
    }






    @NonNull
    @Override
    public LocationItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //sets and inflates the view with the viewholder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_item_location,parent,false);
        LocationItemAdapter.ViewHolder holder = new LocationItemAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationItemAdapter.ViewHolder holder, int position) {
        //binds the attributes of the model to the viewHolder
        holder.priceLabel.setText(String.valueOf(itemLocations.get(position).getPrice()));
        holder.itemLabel.setText(itemLocations.get(position).getItem().getName());
    }


    @Override
    public int getItemCount() {
        return itemLocations.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView priceLabel, itemLabel;
        private Button editItemLoc, deleteItemLoc;
        private CheckBox itemCheck;


        public ViewHolder(View itemView){
            super(itemView);
            priceLabel = itemView.findViewById(R.id.priceLocationLabel);
            itemLabel = itemView.findViewById(R.id.locationItemLabel);
            editItemLoc = itemView.findViewById(R.id.editItemLocBtn);
            deleteItemLoc = itemView.findViewById(R.id.deleteItemLocBtn);

            editItemLoc.setVisibility(View.GONE);
            deleteItemLoc.setVisibility(View.GONE);

            editItemLoc.setOnClickListener(this);
            deleteItemLoc.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {
            //TODO: Implement this after makind the update/delete cascade
            EditText priceTxt;
            Spinner locationSpinner;
            ArrayList<String> locationsNames = new ArrayList<>();
            dataManager = new DataManager(activity);
            Button editBtn, cancelBtn;
            switch (view.getId()){

                case R.id.editItemLocBtn:
                    dialogBuilder = new AlertDialog.Builder(activity);
                    final View editItemLocView = activity.getLayoutInflater().inflate(R.layout.add_item_location_popup,null);

                    priceTxt = editItemLocView.findViewById(R.id.itemPriceTxt);
                    priceTxt.setText(String.valueOf(itemLocations.get(getAdapterPosition()).getPrice()));

                    editBtn = editItemLocView.findViewById(R.id.btnSaveItemLocation);
                    editBtn.setText("Edit");

                    cancelBtn = editItemLocView.findViewById(R.id.btnCancelItemLocation);

                    locationSpinner = editItemLocView.findViewById(R.id.locationSpinner);
                    if (dataManager.getLocations() != null) {
                        for (Location a : dataManager.getLocations()) {
                            locationsNames.add(a.getName());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, locationsNames);
                        locationSpinner.setAdapter(arrayAdapter);

                        for(int i=0; i<dataManager.getLocations().size(); i++)
                            if(itemLocations.get(getAdapterPosition()).getLocation().compareTo(dataManager.getLocations().get(i))>0)
                                locationSpinner.setSelection(i);


                    }

                    dialogBuilder.setView(editItemLocView);
                    dialog = dialogBuilder.create();
                    dialog.show();


                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });


                    editBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ItemLocation itemLocation = new ItemLocation();
                            itemLocation.setItem(itemLocations.get(getAdapterPosition()).getItem());
                            itemLocation.setLocation(dataManager.getLocations().get(locationSpinner.getSelectedItemPosition()));
                            itemLocation.setPrice(Double.parseDouble(priceTxt.getText().toString()));

                            for (int i = 0; i < dataManager.getItemLocations().size(); i++)
                                if (dataManager.getItemLocations().get(i).compareTo(itemLocations.get(getAdapterPosition())) > 0)
                                {
                                    dataManager.editItemLocation(i, itemLocation);
                            itemLocations.set(getAdapterPosition(), itemLocation);
                            notifyItemChanged(getAdapterPosition());
                        }
                            Toast.makeText(activity, "Item Location Edited", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    break;


                case R.id.deleteItemLocBtn:
                    Button yesBtn, noBtn;
                    TextView questionLabel;

                    dialogBuilder = new AlertDialog.Builder(activity);
                    final View deleteItemLocView = activity.getLayoutInflater().inflate(R.layout.delete_item_popup,null);

                    questionLabel = deleteItemLocView.findViewById(R.id.deleteItemLabel);
                    questionLabel.setText("Do you want to delete Item Location?");

                    yesBtn = deleteItemLocView.findViewById(R.id.yesDelete);
                    noBtn = deleteItemLocView.findViewById(R.id.noDelete);

                    dialogBuilder.setView(deleteItemLocView);
                    dialog = dialogBuilder.create();
                    dialog.show();

                    yesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for (int i = 0; i < dataManager.getItemLocations().size(); i++)
                                if (dataManager.getItemLocations().get(i).compareTo(itemLocations.get(getAdapterPosition())) > 0) {
                                    dataManager.deleteItemLocation(i);
                                    itemLocations.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                }
                            Toast.makeText(activity, "Item Location deleted", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });


                    noBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    break;

            }
        }
    }
}
