package com.example.newliving;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newliving.Popups.PopupChangePasswordActivity;
import com.example.newliving.Popups.PopupShareLinkActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SettingsActivity extends AppCompatActivity {
    public String cookie;
    EditText settingsName;
    EditText emailChange;
    EditText oldPLZ;
    EditText oldAddress;
    EditText newPLZ;
    EditText newAddress;
    EditText iban;
    TextView checklistLink;
    String nameTemp,emailTemp,pLZTemp,addressTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            cookie = extras.getString("Cookie");
            System.out.println(cookie);
        }

        settingsName = findViewById(R.id.settingsName);

        emailChange = findViewById(R.id.editTextEMail);
        emailChange.getText();

        oldPLZ = findViewById(R.id.oldPLZ);
        oldPLZ.getText();
        oldAddress = findViewById(R.id.oldStreet);
        oldAddress.getText();
        newPLZ = findViewById(R.id.newPLZ);
        newPLZ.getText();
        newAddress = findViewById(R.id.newStreet);
        newAddress.getText();

        checklistLink = findViewById(R.id.textViewChecklistLink);

        iban = findViewById(R.id.ibanSettings);
        iban.getText();


        LoadAccountRequest loadAccountRequest = new LoadAccountRequest();
        loadAccountRequest.execute();


        ImageView backIcon = findViewById(R.id.back_icon_settings);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button sendNewEmail = findViewById(R.id.buttonSendEMail);
        sendNewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameTemp.equals(settingsName.getText().toString())){
                    updateAccount(new String[]{"",emailChange.getText().toString(),"","","","",""});
                }else if(emailTemp.equals(emailChange.getText().toString())){
                    updateAccount(new String[]{settingsName.getText().toString(),"","","","","",""});
                }else{
                    updateAccount(new String[]{settingsName.getText().toString(),emailChange.getText().toString(),"","","","",""});
                }

            }
        });

        Button changePassword = findViewById(R.id.buttonChangePassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, PopupChangePasswordActivity.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });

        Button changeAddress = findViewById(R.id.buttonSendAddress);
        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pLZTemp.equals(oldPLZ.getText().toString())&&addressTemp.equals(oldAddress.getText().toString())){
                    updateAccount(new String[]{"","","","",newPLZ.getText().toString(),newAddress.getText().toString(),""});
                }else if(pLZTemp.equals(oldPLZ.getText().toString())){
                    updateAccount(new String[]{"","","",oldAddress.getText().toString(),newPLZ.getText().toString(),newAddress.getText().toString(),""});
                }else if(addressTemp.equals(oldAddress.getText().toString())){
                    updateAccount(new String[]{"","",oldPLZ.getText().toString(),"",newPLZ.getText().toString(),newAddress.getText().toString(),""});
                }else{
                    updateAccount(new String[]{"","",oldPLZ.getText().toString(),oldAddress.getText().toString(),newPLZ.getText().toString(),newAddress.getText().toString(),""});
                }
            }
        });

        Button generateLink = findViewById(R.id.buttonLinkGenerate);
        generateLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkGenerateRequest linkGenerateRequest = new LinkGenerateRequest();
                linkGenerateRequest.execute();
            }
        });

        Button checklistLinkShare = findViewById(R.id.buttonShareLink);
        checklistLinkShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checklistLink.getText().toString().equals("-Link deaktiviert-")||checklistLink.getText().toString().equals("-Kein Link generiert-")){
                    Toast.makeText(getApplicationContext(), "Bitte generieren Sie zuerst einen Link.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(SettingsActivity.this, PopupShareLinkActivity.class);
                    intent.putExtra("Cookie", cookie);
                    startActivity(intent);
                }

            }
        });

        Button checklistLinkDeactivate = findViewById(R.id.buttonDeactivateLink);
        checklistLinkDeactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checklistLink.setText("-Link deaktiviert-");
                LinkDeactivateRequest linkDeactivateRequest = new LinkDeactivateRequest();
                linkDeactivateRequest.execute();
            }
        });


        Button ibanSend = findViewById(R.id.ibanButton);
        ibanSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAccount(new String[]{"","","","","","",iban.getText().toString()});
            }
        });

        Button logout = findViewById(R.id.buttonLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutRequest logoutRequest = new LogoutRequest();
                logoutRequest.execute();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public void updateAccount(String[] loginData){
        AccountUpdateRequest accountUpdateRequest = new AccountUpdateRequest();
        accountUpdateRequest.execute(loginData);
    }


    public class LogoutRequest extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/ausloggen")
                    .method("GET", null)
                    .addHeader("Cookie", cookie)
                    .build();
            try {
                Response response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class LoadAccountRequest extends AsyncTask<String, String, String> {
        String accResponse;

        @Override
        protected String doInBackground(String[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/account")
                    .method("GET", null)
                    .addHeader("Cookie", cookie)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                accResponse = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return accResponse;
        }

        @Override
        protected void onPostExecute(String responseData) {
            JSONObject namesObj = null;
            try {
                namesObj = new JSONObject(responseData);
                settingsName.setText(namesObj.getString("name"), TextView.BufferType.EDITABLE);
                nameTemp = settingsName.getText().toString();
                emailChange.setText(namesObj.getString("email"), TextView.BufferType.EDITABLE);
                emailTemp = emailChange.getText().toString();
                oldPLZ.setText(namesObj.getString("altPLZ"), TextView.BufferType.EDITABLE);
                pLZTemp = oldPLZ.getText().toString();
                oldAddress.setText(namesObj.getString("altAdresse"), TextView.BufferType.EDITABLE);
                addressTemp = oldAddress.getText().toString();
                newPLZ.setText(namesObj.getString("neuPLZ"), TextView.BufferType.EDITABLE);
                newAddress.setText(namesObj.getString("neuAdresse"), TextView.BufferType.EDITABLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class AccountUpdateRequest extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"name\": \""+legend[0]+"\",\r\n    \"email\": \""+legend[1]+"\",\r\n    \"altPLZ\": \""+legend[2]+"\",\r\n    \"altAdresse\": \""+legend[3]+"\",\r\n    \"neuPLZ\": \""+legend[4]+"\",\r\n    \"neuAdresse\": \""+legend[5]+"\",\r\n    \"iban\": \""+legend[6]+"\"\r\n}");
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/account/update-daten")
                    .method("PUT", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Cookie", cookie)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responded = response.body().string();
                String accResponse = responded;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class LinkGenerateRequest extends AsyncTask<String, String, String> {
        String generatedLink;

        @Override
        protected String doInBackground(String[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/link/neu")
                    .method("GET", null)
                    .addHeader("Cookie", cookie)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                generatedLink = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return generatedLink;
        }

        @Override
        protected void onPostExecute(String responseData) {
            checklistLink.setText(responseData);
        }
    }

    public class LinkDeactivateRequest extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/link/deaktivieren")
                    .method("GET", null)
                    .addHeader("Cookie", cookie)
                    .build();
            try {
                Response response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}