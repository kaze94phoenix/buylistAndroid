package com.example.buylist.adapters;

import android.app.Activity;
import android.content.Intent;
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
        holder.priceLabel.setText(itemLocations.get(position).getPrice()+"0 MTS");
        holder.itemLabel.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_shopping_dark,0);
        holder.itemLabel.setText(itemLocations.get(position).getItem().getName());
    }


    @Override
    public int getItemCount() {
        return itemLocations.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView priceLabel, itemLabel;
        private Button addItemLoc;

        public ViewHolder(View itemView){
            super(itemView);
            priceLabel = itemView.findViewById(R.id.priceLocationLabel);
            itemLabel = itemView.findViewById(R.id.locationItemLabel);
            addItemLoc = itemView.findViewById(R.id.addItemLocBtn);
            addItemLoc.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //TODO: Implement this after makind the update/delete cascade
            EditText quantityTxt;
            dataManager = new DataManager(activity);
            Button addBtn, cancelBtn;
            switch (view.getId()){

                case R.id.addItemLocBtn:
                    dialogBuilder = new AlertDialog.Builder(activity);
                    final View editItemLocView = activity.getLayoutInflater().inflate(R.layout.add_item_to_buylist_popup,null);

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
/*
                SOME CODE TO BE REUSED
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

                    yesBtn.setOnClickListener(view4 -> {
                        for (int i = 0; i < dataManager.getItemLocations().size(); i++)
                            if (dataManager.getItemLocations().get(i).compareTo(itemLocations.get(getBindingAdapterPosition())) > 0) {
                                dataManager.deleteItemLocation(i);
                                itemLocations.remove(getBindingAdapterPosition());
                                notifyItemRemoved(getBindingAdapterPosition());
                            }
                        Toast.makeText(activity, "Item Location deleted", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });


                    noBtn.setOnClickListener(view3 -> dialog.dismiss());
                    break;
*/
            }
        }
    }
}
