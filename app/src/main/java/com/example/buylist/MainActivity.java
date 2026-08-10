package com.example.buylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.buylist.fragments.ListsNStatsFragment;
import com.example.buylist.fragments.MyLocationsFragment;
import com.example.buylist.fragments.ProductsNLocalsFragment;
import com.example.buylist.models.DataManager;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    TextView username;
    Button logoutBtn;
    View header;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataManager manager = new DataManager(getApplicationContext());

        preferences = manager.getPreferences();

        if(preferences.getString("loginStatus","false").equalsIgnoreCase("false")){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.mainView);

        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        header = navigationView.getHeaderView(0);
        username = header.findViewById(R.id.userLabel);
        logoutBtn = header.findViewById(R.id.logoutBtn);
        MenuItem item = navigationView.getMenu().getItem(2);
        if(preferences.getString("userType","").equalsIgnoreCase("CONS")){
            item.setVisible(false);
        }

        username.setText(preferences.getString("username",""));
        logoutBtn.setOnClickListener(view ->{
            manager.logout(this);
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, new ListsNStatsFragment()).commit();
            navigationView.setCheckedItem(R.id.listsNStats);
        }

    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.listsNStats:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, new ListsNStatsFragment()).commit();
                break;
            case R.id.locationsNProducts:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, new ProductsNLocalsFragment()).commit();
                break;
            case R.id.myLocations:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, new MyLocationsFragment()).commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}