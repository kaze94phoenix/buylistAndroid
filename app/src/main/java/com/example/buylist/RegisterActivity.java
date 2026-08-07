package com.example.buylist;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.buylist.models.DataManager;
import com.example.buylist.models.User;
import com.example.buylist.models.UserType;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameEdit, emailEdit, passwordEdit, addressEdit;
    ProgressBar progressBar;

    DataManager manager;
    Button registerBtn;
    TextView loginLink;
    Spinner userTypeSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        manager = new DataManager(getApplicationContext());
        emailEdit = findViewById(R.id.editTextEmail);
        usernameEdit = findViewById(R.id.editTextUsername);
        passwordEdit = findViewById(R.id.editTextPassword);
        addressEdit = findViewById(R.id.editTextAddress);
        userTypeSp = findViewById(R.id.userTypeSp);
        registerBtn = findViewById(R.id.registerBtn);
        loginLink = findViewById(R.id.loginLink);
        progressBar = findViewById(R.id.progressBar);

        ArrayList<String> userTypes = new ArrayList<>();
        for (UserType uT : manager.getUserTypes())
            userTypes.add(uT.getName());
        ArrayAdapter<String> userTypesAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, userTypes);
        userTypeSp.setAdapter(userTypesAdapter);

        registerBtn.setOnClickListener(view -> {
            progressBar.setVisibility(VISIBLE);
            User user = new User(
                    usernameEdit.getText().toString(),
                    emailEdit.getText().toString(),
                    passwordEdit.getText().toString(),
                    addressEdit.getText().toString(),
                    "",
                    manager.getUserTypes().get(userTypeSp.getSelectedItemPosition()).getId()
            );
            manager.register(user, this, progressBar);
        });

        loginLink.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });


    }
}