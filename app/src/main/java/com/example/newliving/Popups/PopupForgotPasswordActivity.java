package com.example.newliving.Popups;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newliving.R;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PopupForgotPasswordActivity extends AppCompatActivity {
    String cookie;
    EditText eMailForPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_forgot_password);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            cookie = extras.getString("Cookie");
            System.out.println(cookie);
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.5),(int) (height*.4));

        eMailForPassword = findViewById(R.id.eMailForPassword);


        Button changePassword = findViewById(R.id.sendEmail);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!eMailForPassword.getText().toString().equals("")) {
                    demandPassword(new String[]{eMailForPassword.getText().toString()});

                }else{
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie eine eMail ein!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void demandPassword(String[] email){
        DemandPasswordRequest demandPasswordRequest = new DemandPasswordRequest();
        demandPasswordRequest.execute(email);
    }

    public class DemandPasswordRequest extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/registrierung/passwort-vergessen?email="+legend[0])
                    .method("GET", null)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String temp = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String cookie) {
            finish();
            Toast.makeText(getApplicationContext(), "Falls der Nutzer existiert, wird diesem ein neues Passwort zugesendet!", Toast.LENGTH_LONG).show();
        }

    }

}