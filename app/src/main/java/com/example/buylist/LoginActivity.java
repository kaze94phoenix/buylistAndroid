package com.example.buylist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.buylist.models.ApiService;
import com.example.buylist.models.DataManager;
import com.example.buylist.models.RetrofitInstance;
import com.example.buylist.models.User;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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