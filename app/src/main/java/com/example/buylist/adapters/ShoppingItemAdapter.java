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
    private ArrayList<Integer> selected;
    private boolean selectMode;
    private ActionMode actionMode;
    //ID of each item used to navigate to or manipulate each item
    public static final String EXTRA_ITEM_ID = "item_id";
    //Context of the RecyclerView activity
    private Activity activity;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private boolean isSelectAll;
    Intent intent;
    DataManager dataManager;

    public ShoppingItemAdapter() {
        selected = new ArrayList<>();
        isSelectAll = false;
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
        holder.txtItemAvgPrice.setText(String.valueOf(dataManager.avgPrice(position)));
        if (isSelectAll) {
            holder.itemView.setBackgroundResource(R.color.purple_200);
            holder.txtItemAvgPrice.setTextColor(Color.WHITE);
            holder.txtItemName.setTextColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            holder.txtItemAvgPrice.setTextColor(Color.GRAY);
            holder.txtItemName.setTextColor(Color.GRAY);
        }
    }


    @Override
    public int getItemCount() {
        return items.size();

    }


    //Inner Class ViewHolder that takes the View Items to be used on the adapter
    //implements on click listener
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtItemName, txtItemAvgPrice;
        private Button editBtn, deleteBtn;
        ActionMode.Callback callback;

        //  private ArrayList<Item> items;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //     this.items=items
            txtItemName = itemView.findViewById(R.id.itemName);
            txtItemAvgPrice = itemView.findViewById(R.id.itemAvgPrice);
            editBtn = itemView.findViewById(R.id.btnEditItem);
            deleteBtn = itemView.findViewById(R.id.btnDeleteItem);
            dataManager = new DataManager(activity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectMode && selected.contains(getAdapterPosition())) {
                        itemView.setBackgroundColor(Color.TRANSPARENT);
                        txtItemAvgPrice.setTextColor(Color.GRAY);
                        txtItemName.setTextColor(Color.GRAY);
                        selected.remove((Integer) getAdapterPosition());
                        //callback.onDestroyActionMode(actionMode);
                    } else if (selectMode && !selected.contains(getAdapterPosition())) {
                        itemView.setBackgroundResource(R.color.purple_200);
                        txtItemAvgPrice.setTextColor(Color.WHITE);
                        txtItemName.setTextColor(Color.WHITE);
                        selected.add(getAdapterPosition());
                    } else {
                        intent = new Intent(activity, ItemDetailsActivity.class);
                        intent.putExtra(EXTRA_ITEM_ID, getAdapterPosition());
                        activity.startActivity(intent);
                    }

                    if (selectMode && selected.isEmpty()) {
                        selectMode = false;
                        actionMode.finish();
                    }
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (actionMode != null)
                        return false;

                    callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
                            mode.setTitle("Select Items");
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.delete:
                                    dialogBuilder = new AlertDialog.Builder(activity);
                                    final View deleteItemPopout = activity.getLayoutInflater().inflate(R.layout.delete_item_popup, null);

                                    Button yesBtn = deleteItemPopout.findViewById(R.id.yesDelete);
                                    Button noBtn = deleteItemPopout.findViewById(R.id.noDelete);
                                    TextView text = deleteItemPopout.findViewById(R.id.deleteItemLabel);

                                    if (selected.size() <= 1)
                                        text.setText("Do you want to delete the Item?");
                                    else
                                        text.setText("Do you want to delete the Items?");

                                    dialogBuilder.setView(deleteItemPopout);
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
                                            deleteSelected();
                                            mode.finish();
                                            dialog.dismiss();
                                        }
                                    });



                                    return true;
                                case R.id.selectAll:
                                    selectAll();
                                    return true;
                                default:
                                    return false;
                            }
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            actionMode = null;
                            isSelectAll = false;
                            notifyDataSetChanged();
                        }
                    };


                    actionMode = ((AppCompatActivity) itemView.getContext()).startSupportActionMode(callback);

                    itemView.setBackgroundResource(R.color.purple_200);
                    txtItemAvgPrice.setTextColor(Color.WHITE);
                    txtItemName.setTextColor(Color.WHITE);
                    selected.add(getAdapterPosition());
                    selectMode = true;
                    return true;


                }

            });


            //gets the button to use the onclicklistener
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder = new AlertDialog.Builder(activity);
                    final View editItemPopoutView = activity.getLayoutInflater().inflate(R.layout.add_item_popup, null);
                    /////////////
                    EditText nameTxt = editItemPopoutView.findViewById(R.id.itemNameTxt);
                    EditText descriptionTxt = editItemPopoutView.findViewById(R.id.itemDescriptionTxt);
                    Spinner spinner = editItemPopoutView.findViewById(R.id.itemTypeSpinner);
                    Button editBtn = editItemPopoutView.findViewById(R.id.btnSaveItem);
                    Button cancelBtn = editItemPopoutView.findViewById(R.id.btnCancelItem);
                    Button addItemType = editItemPopoutView.findViewById(R.id.goAddItemType);
                    ArrayList<String> another = new ArrayList<String>();


                    /////////////
                    nameTxt.setText(items.get(getAdapterPosition()).getName());
                    descriptionTxt.setText(items.get(getAdapterPosition()).getDescription());

                    if (dataManager.getItemTypes() != null) {
                        for (ItemType a : dataManager.getItemTypes()) {
                            another.add(a.getName());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, another);

                        spinner.setAdapter(arrayAdapter);

                        for (int i = 0; i < dataManager.getItemTypes().size(); i++)
                            if (items.get(getAdapterPosition()).getItemType().compareTo(dataManager.getItemTypes().get(i)) > 0)
                                spinner.setSelection(i);


                    }


                    /////////////////////////
                    dialogBuilder.setView(editItemPopoutView);
                    dialog = dialogBuilder.create();
                    dialog.show();

                    ////////////////////

                    editBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int itemPosition = spinner.getSelectedItemPosition();
                            dataManager.editItem(getAdapterPosition(), new Item(nameTxt.getText().toString(), descriptionTxt.getText().toString(), dataManager.getItemTypes().get(itemPosition)));
                            items.set(getAdapterPosition(), new Item(nameTxt.getText().toString(), descriptionTxt.getText().toString(), dataManager.getItemTypes().get(itemPosition)));
                            notifyItemChanged(getAdapterPosition());
                            Toast.makeText(activity, "Item Edited", Toast.LENGTH_SHORT).show();
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
                            intent = new Intent(activity, AddItemTypeActivity.class);
                            activity.startActivity(intent);
                        }
                    });
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder = new AlertDialog.Builder(activity);
                    final View deleteItemPopout = activity.getLayoutInflater().inflate(R.layout.delete_item_popup, null);

                    Button yesBtn = deleteItemPopout.findViewById(R.id.yesDelete);
                    Button noBtn = deleteItemPopout.findViewById(R.id.noDelete);


                    dialogBuilder.setView(deleteItemPopout);
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
                            dataManager.deleteItem(getAdapterPosition());
                            items.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            Toast.makeText(activity, "Item Deleted", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                }
            });


        }

        public void deleteDialogBox() {
            //TODO: Metodo para popups
        }


        private void deleteSelected() {
            for (int i = 0; i < items.size(); i++)
                if (selected.contains(i)) {
                    dataManager.deleteItem(i);
                    items.remove(i);
                    notifyItemRemoved(i);

                }
            if (selected.size() > 1)
                Toast.makeText(activity, "Items Deleted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(activity, "Item Deleted", Toast.LENGTH_SHORT).show();

            selected.clear();
        }

        private void selectAll() {
            for (int i = 0; i < items.size(); i++)
                if (!selected.contains(i))
                    selected.add(i);
            if (!isSelectAll) {
                isSelectAll = true;
                notifyDataSetChanged();
            } else {
                isSelectAll = false;
                notifyDataSetChanged();
            }

        }


    }


}
