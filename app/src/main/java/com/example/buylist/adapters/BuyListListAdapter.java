package com.example.buylist.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buylist.BuyListListActivity;
import com.example.buylist.EditBuyListActivity;
import com.example.buylist.R;
import com.example.buylist.models.BuyList;
import com.example.buylist.models.DataManager;

import org.w3c.dom.Text;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class BuyListListAdapter extends RecyclerView.Adapter<BuyListListAdapter.ViewHolder> {

    ArrayList<BuyList> buyLists;
    Activity activity;
    public final String EXTRA_ITEM_ID = "item_id";
    private DataManager dataManager;


    public BuyListListAdapter(){
    }

    public void setBuyLists(ArrayList<BuyList> buyLists) {
        this.buyLists = buyLists;
        notifyDataSetChanged();
    }

    public void setActivity(Activity activity){
        this.activity=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buylist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(buyLists.get(position).getName());
        holder.date.setText(String.valueOf(buyLists.get(position).getDate()));

    }

    @Override
    public int getItemCount() {
        return buyLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, date;
        RelativeLayout element;
        Button deleteBtn;
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataManager = new DataManager(activity);

            name = itemView.findViewById(R.id.buyListName);
            date = itemView.findViewById(R.id.buyListDate);
            element = itemView.findViewById(R.id.buylistElement);
            deleteBtn = itemView.findViewById(R.id.deleteBuyListBtn);

            element.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, EditBuyListActivity.class);
                    intent.putExtra(EXTRA_ITEM_ID,getAdapterPosition());
                    activity.startActivity(intent);
                }
            });


            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder = new AlertDialog.Builder(activity);
                    final View deletePopup = activity.getLayoutInflater().inflate(R.layout.delete_item_popup,null);
                    Button yesBtn = deletePopup.findViewById(R.id.yesDelete);
                    Button noBtn = deletePopup.findViewById(R.id.noDelete);
                    TextView deleteMessage = deletePopup.findViewById(R.id.deleteItemLabel);

                    deleteMessage.setText("Do you wish do delete this Buylist?");

                    dialogBuilder.setView(deletePopup);
                    dialog = dialogBuilder.create();
                    dialog.show();


                    noBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });


                    yesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dataManager.deleteBuyList(getAdapterPosition());
                            buyLists.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            dialog.dismiss();
                        }
                    });

                }
            });


        }
    }

}
