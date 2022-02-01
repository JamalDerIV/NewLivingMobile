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

public class PopupShareLinkActivity extends AppCompatActivity {
    public String cookie;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            cookie = extras.getString("Cookie");
            System.out.println(cookie);
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.7),(int) (height*.25));

        editText = findViewById(R.id.sendToEMail);

        Button sendEmails = findViewById(R.id.sendEmail);
        sendEmails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLink(new String[]{editText.getText().toString(),null});
                finish();
                Toast.makeText(getApplicationContext(), "eMail versandt!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendLink(String[] emails){
        SendLinkRequest sendLinkRequest = new SendLinkRequest();
        sendLinkRequest.execute(emails);
    }

    public class SendLinkRequest extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "[\r\n    \""+legend[0]+"\",\r\n    \""+legend[1]+"\"\r\n]");
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/link/teilen")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
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