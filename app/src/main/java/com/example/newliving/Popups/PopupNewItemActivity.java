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

public class PopupNewItemActivity extends AppCompatActivity {
    String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_new_item);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            cookie = extras.getString("Cookie");
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.7),(int) (height*.5));

        EditText getText = findViewById(R.id.textNewItem);
        EditText getDate = findViewById(R.id.dateNewItem);

        Button sendItem = findViewById(R.id.sendItem);
        sendItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendItem(new String[]{getText.getText().toString(),getDate.getText().toString()});
                finish();
                Toast.makeText(getApplicationContext(), "Umzugsplan erweitert!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendItem(String[] data){
        SendItemRequest sendItemRequest = new SendItemRequest();
        sendItemRequest.execute(data);
    }

    public class SendItemRequest extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"text\": \""+legend[0]+"\",\r\n    \"datum\": \""+legend[1]+"\"\r\n}");
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/eintrag/neu")
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