package com.example.buylist;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buylist.models.DataManager;
import com.example.buylist.models.User;

public class LoginActivity extends AppCompatActivity {
EditText usernameTxt, passwordTxt;
TextView registerLink;
SharedPreferences preferences;
ProgressBar progressBar;
Button loginBtn, guestBtn;

DataManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        manager = new DataManager(getApplicationContext());
        preferences = manager.getPreferences();

        usernameTxt = findViewById(R.id.editTextUsername);
        passwordTxt = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.loginBtn);
        guestBtn = findViewById(R.id.guestBtn);
        progressBar = findViewById(R.id.progressBar);
        registerLink = findViewById(R.id.registerLink);

        loginBtn.setOnClickListener(view ->{
            progressBar.setVisibility(VISIBLE);
            User user = new User(usernameTxt.getText().toString(),passwordTxt.getText().toString());
                manager.login(user,this, progressBar);
        });

        guestBtn.setOnClickListener(view ->{
            manager.getEditor().putString("loginStatus","guest");
            manager.getEditor().putString("userId","");
            manager.getEditor().putString("userType","CONS");
            manager.getEditor().putString("username","Guest");
            manager.getEditor().putString("token","");
            manager.getEditor().apply();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

        registerLink.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            finish();
        });
    }
}