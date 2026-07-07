package com.example.buylist.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buylist.LocationDetailsActivity;
import com.example.buylist.R;
import com.example.buylist.models.DataManager;
import com.example.buylist.models.Location;
import com.example.buylist.models.LocationType;

import java.util.ArrayList;

public class ShoppingLocationAdapter extends RecyclerView.Adapter<ShoppingLocationAdapter.ViewHolder> {

    private ArrayList<Location> locations;
    //ID of each item used to navigate to or manipulate each item
    public static final String EXTRA_LOCATION_ID = "location_id";
    //Context of the RecyclerView activity
    private Activity activity;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private boolean options;
    Intent intent;

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void hasOptions(boolean options) {
        this.options = options;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_shoping_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.locationName.setText(locations.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button editLocation, deleteLocation;
        TextView locationName;
        RelativeLayout relativeLayout;
        DataManager dataManager;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            locationName = itemView.findViewById(R.id.locationName);
            editLocation = itemView.findViewById(R.id.btnEditLocation);
            deleteLocation = itemView.findViewById(R.id.btnDeleteLocation);
            relativeLayout = itemView.findViewById(R.id.simpleShoppingLocation);

            if (!options) {
                editLocation.setVisibility(View.GONE);
                deleteLocation.setVisibility(View.GONE);
            }

            editLocation.setOnClickListener(this);
            deleteLocation.setOnClickListener(this);
            relativeLayout.setOnClickListener(this);

            dataManager = new DataManager(itemView.getContext());

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.simpleShoppingLocation:
                    intent = new Intent(activity, LocationDetailsActivity.class);
                    intent.putExtra(EXTRA_LOCATION_ID, locations.get(getBindingAdapterPosition()).getId());
                    activity.startActivity(intent);
                    break;

                case R.id.btnEditLocation:
                    dialogBuilder = new AlertDialog.Builder(activity);
                    final View addLocationPopoutView = activity.getLayoutInflater().inflate(R.layout.add_location_popup, null);

                    Location locationX = new Location();

                    for (Location l : dataManager.getMyLocations())
                        if (locations.get(getBindingAdapterPosition()).getId() == l.getId())
                            locationX = l;

                    ArrayList<String> locTypeNames = dataManager.getLocationTypeNames();
                    ArrayAdapter<String> spinnerNames = new ArrayAdapter<>(
                            activity,
                            android.R.layout.simple_spinner_item,
                            locTypeNames
                    );
                    spinnerNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    Spinner locationTypeSp = addLocationPopoutView.findViewById(R.id.locationTypeSp);
                    locationTypeSp.setAdapter(spinnerNames);

                    EditText nameTxt = addLocationPopoutView.findViewById(R.id.locationNameTxt);
                    EditText addressTxt = addLocationPopoutView.findViewById(R.id.locationAddressTxt);
                    EditText geoLocTxt = addLocationPopoutView.findViewById(R.id.locationGeoTxt);

                    Button saveBtn = addLocationPopoutView.findViewById(R.id.saveLocation);
                    saveBtn.setText("Update");
                    Button cancelBtn = addLocationPopoutView.findViewById(R.id.cancelLocation);

                    dialogBuilder.setView(addLocationPopoutView);
                    dialog = dialogBuilder.create();
                    dialog.show();

                    for(int i=0; i<dataManager.getLocationTypes().size(); i++)
                        if(dataManager.getLocationTypes().get(i).getId()==locationX.getLocationType().getId())
                            locationTypeSp.setSelection(i);

                    nameTxt.setText(locationX.getName());
                    addressTxt.setText(locationX.getAddress());
                    geoLocTxt.setText(locationX.getGeolocation());

                    Location finalLocationX = locationX;
                    saveBtn.setOnClickListener(view3 -> {
                        finalLocationX.setLocationType(dataManager.getLocationTypes().get(locationTypeSp.getSelectedItemPosition()));
                        finalLocationX.setAddress(addressTxt.getText().toString());
                        finalLocationX.setName(nameTxt.getText().toString());
                        finalLocationX.setGeolocation(geoLocTxt.getText().toString());
                        dataManager.editLocation(locations.get(getBindingAdapterPosition()).getId(),finalLocationX);
                        dialog.dismiss();
                    });

                    cancelBtn.setOnClickListener(view1 -> dialog.dismiss());

                    break;

                case R.id.btnDeleteLocation:
                    dialogBuilder = new AlertDialog.Builder(activity);
                    final View viewDeleteLocation = activity.getLayoutInflater().inflate(R.layout.delete_item_popup, null);

                    Button yesBtn = viewDeleteLocation.findViewById(R.id.yesDelete);
                    Button noBtn = viewDeleteLocation.findViewById(R.id.noDelete);

                    TextView message = viewDeleteLocation.findViewById(R.id.deleteItemLabel);
                    message.setText("Do you wish do delete this Location?");

                    dialogBuilder.setView(viewDeleteLocation);
                    dialog = dialogBuilder.create();
                    dialog.show();

                    noBtn.setOnClickListener(view1 -> dialog.dismiss());

                    yesBtn.setOnClickListener(view2 -> {
                        dataManager.deleteLocation(getBindingAdapterPosition());
                        notifyItemRemoved(getBindingAdapterPosition());
                        dialog.dismiss();
                    });

                    break;
            }


        }
    }
}
