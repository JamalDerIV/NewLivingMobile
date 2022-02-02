package com.example.newliving.Popups;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newliving.R;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PopupChangePasswordActivity extends AppCompatActivity {
    public String cookie;
    EditText oldPassword;
    EditText newPassword;
    EditText newPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_change_password);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            cookie = extras.getString("Cookie");
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.5),(int) (height*.4));

        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        newPasswordConfirm = findViewById(R.id.newPasswordConfirm);

        String newPW = newPassword.getText().toString();
        String newPWC = newPasswordConfirm.getText().toString();


        Button changePassword = findViewById(R.id.sendEmail);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newPW.equals(newPWC) && !newPW.equals("") && !newPW.equals(" ")) {
                    sendPassword(new String[]{oldPassword.getText().toString(), newPassword.getText().toString()});

                }else{
                    Toast.makeText(getApplicationContext(), "Passwörter stimmen nicht überein!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendPassword(String[] emails){
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.execute(emails);
    }

    public class ChangePasswordRequest extends AsyncTask<String,String,String> {
        boolean changedPassword;

        @Override
        protected String doInBackground(String[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"altPasswort\": \""+legend[0]+"\",\r\n    \"neuPasswort\": \""+legend[1]+"\"\r\n}");
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/account/update-passwort")
                    .method("PUT", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Cookie", cookie)
                    .build();
            try {
                Response responded = client.newCall(request).execute();
                String temp = responded.body().string();
                this.changedPassword = temp.equals("true");

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String cookie) {
            if(changedPassword) {
                finish();
                Toast.makeText(getApplicationContext(), "Passwort erfolgreich geändert!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Versuch fehlgeschlagen!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}