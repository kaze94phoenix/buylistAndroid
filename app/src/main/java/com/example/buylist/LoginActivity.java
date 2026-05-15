package com.example.buylist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buylist.models.DataManager;
import com.example.buylist.models.User;

public class LoginActivity extends AppCompatActivity {
EditText usernameTxt, passwordTxt;
SharedPreferences preferences;

Button loginBtn, guestBtn;

DataManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        manager = new DataManager(getApplicationContext());
        preferences = manager.getPreferences();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameTxt = findViewById(R.id.editTextUsername);
        passwordTxt = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.loginBtn);
        guestBtn = findViewById(R.id.guestBtn);

        loginBtn.setOnClickListener(view ->{
            User user = new User(usernameTxt.getText().toString(),passwordTxt.getText().toString());
                manager.login(user,this);
        });

        guestBtn.setOnClickListener(view ->{
            manager.getEditor().putString("loginStatus","guest");
            manager.getEditor().apply();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
    }
}