package com.example.buylist;

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
Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameTxt = findViewById(R.id.editTextUsername);
        passwordTxt = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.loginBtn);


        ApiService api = RetrofitInstance.getApiInterface();

        loginBtn.setOnClickListener(view ->{
            User user = new User(usernameTxt.getText().toString(),passwordTxt.getText().toString());
                api.login(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body()!=null){
                    try {
                        String res = response.body().string();
                        JSONObject jsonResponse = new JSONObject(res);
                        System.out.println("JSON: "+ jsonResponse);
                    } catch (IOException|JSONException e) {
                        throw new RuntimeException(e);
                    }

                }else{
                    System.out.println("Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Error: "+t.getMessage());
            }
        });
        });






        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }
}