package com.example.buylist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buylist.R;
import com.example.buylist.models.DataManager;
import com.example.buylist.models.Purchase;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**Adapter for the buylist on the main activity [BUYLIST Fragment]
 */
public class BuyListAdapter extends RecyclerView.Adapter<BuyListAdapter.ViewHolder> {

    ArrayList<Purchase> buylist;
    DataManager dataManager;
    boolean options;

    public BuyListAdapter(){
        buylist = new ArrayList<>();
        options = true;
    }

    public void hasOptions(boolean options){
        this.options=options;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buylist_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item.setText(buylist.get(position).getItemLocation().getItem().getName());
        holder.location.setText(buylist.get(position).getItemLocation().getLocation().getName());
        holder.price.setText(String.valueOf(buylist.get(position).getItemLocation().getPrice()*buylist.get(position).getQuantity()+"0 MTS"));
        holder.quantity.setText(buylist.get(position).getQuantity()+" Unit(s)");

        if(!options){
            holder.edit.setVisibility(View.INVISIBLE);
            holder.delete.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return buylist.size();
    }

    public void setDataManager(DataManager dataManager){
        this.dataManager = dataManager;
    }
    public void setBuylist(ArrayList<Purchase> buylist) {
        this.buylist = buylist;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView item, location, price,quantity,quantityEdit;
        Button edit,delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.itemBuylistName);
            location = itemView.findViewById(R.id.locationBuylistName);
            price = itemView.findViewById(R.id.priceBuylistName);
            quantity = itemView.findViewById(R.id.qttyBuylistName);
            quantityEdit = itemView.findViewById(R.id.qttyBuylistEdit);
            edit = itemView.findViewById(R.id.changeQttyBuylist);
            delete = itemView.findViewById(R.id.removeBuylist);

            edit.setOnClickListener(this);
            delete.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case (R.id.changeQttyBuylist):
                    if(quantity.getVisibility()==View.VISIBLE){
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

                case(R.id.removeBuylist):
                    int position = getBindingAdapterPosition();
                    Purchase temp = buylist.get(position);
                    buylist.remove(position);
                    dataManager.setPurchases(buylist);
                    notifyItemRemoved(position);
                    Snackbar.make((View) itemView.getParent(),"Removing "+temp.getItemLocation().getItem().getName(),Snackbar.LENGTH_LONG).setAction("Undo", view1 -> {
                        buylist.add(position,temp);
                        dataManager.setPurchases(buylist);
                        notifyItemInserted(position);
                    }).show();
                    break;

            }

        }
    }

}
