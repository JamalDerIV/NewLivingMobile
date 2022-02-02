package com.example.newliving;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newliving.Popups.PopupForgotPasswordActivity;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    String cookie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        Button buttonLogin = findViewById(R.id.login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailEditText = findViewById(R.id.emailLogin);
                String email = emailEditText.getText().toString();

                EditText passwordEditText = findViewById(R.id.passwordLogin);
                String password = passwordEditText.getText().toString();

                loginAttempt(new String[]{email,password});

            }
        });

        Button buttonRegister = findViewById(R.id.registerLogin);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        Button changePassword = findViewById(R.id.forgotPassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PopupForgotPasswordActivity.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });

    }

    public void loginAttempt(String[] loginData){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.execute(loginData);
    }

    public class LoginRequest extends AsyncTask<String, String, String> {
        boolean loggedIn;
        String jsessionid;

        @Override
        protected String doInBackground(String[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"email\": \""+legend[0]+"\",\r\n    \"passwort\": \""+legend[1]+"\"\r\n}");
            Request request = new okhttp3.Request.Builder()
                    .url("http://10.0.2.2:8080/api/login")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try {
                Response responded = client.newCall(request).execute();
                String temp = responded.body().string();
                if(temp.equals("true")){
                    this.loggedIn = true;
                    List<String> Cookielist = responded.headers().values("Set-Cookie");
                    cookie = (Cookielist .get(0).split(";"))[0];
                }else {
                    this.loggedIn = false;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String cookies) {
            if(loggedIn) {
                Intent intent = new Intent(MainActivity.this, Homepage.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "Versuch fehlgeschlagen!", Toast.LENGTH_SHORT).show();
            }
        }
    }


}