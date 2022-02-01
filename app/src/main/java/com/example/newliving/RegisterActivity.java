package com.example.newliving;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        EditText nameEditText = findViewById(R.id.nameRegister);
        EditText emailEditText = findViewById(R.id.emailRegister);
        EditText emailConfirmEditText = findViewById(R.id.emailRegisterConfirm);
        EditText passwordEditText = findViewById(R.id.passwordRegister);
        EditText passwordConfirmEditText = findViewById(R.id.passwordRegisterConfirm);

        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String emailConfirm = emailConfirmEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String passwordConfirm = passwordConfirmEditText.getText().toString();

                if(email.equals(emailConfirm)&&password.equals(passwordConfirm)){
                    register(new String[]{name, email, emailConfirm, password, passwordConfirm});
                }
            }
        });
    }

    private void register(String[] registerData) {
        RegisterRequest loginRequest = new RegisterRequest();
        loginRequest.execute(registerData);

    }

    public class RegisterRequest extends AsyncTask<String, String, String> {
        boolean registered;

        @Override
        protected String doInBackground(String[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"email\": \""+legend[1]+"\",\r\n    \"name\": \""+legend[0]+"\",\r\n    \"passwort\": \""+legend[3]+"\"\r\n}");
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("http://10.0.2.2:8080/api/registrierung")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try {
                Response responded = client.newCall(request).execute();
                String temp =responded.body().string();
                this.registered = temp.equals("true");

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String cookie) {
            if(registered) {
                finish();
                Toast.makeText(getApplicationContext(),"Account erfolgreich registriert!",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Registrierung fehlgeschlagen!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}