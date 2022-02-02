package com.example.newliving.Popups;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newliving.Popups.PopupNewItemActivity;
import com.example.newliving.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PopupFriendsActivity extends AppCompatActivity {
    String cookie;
    Integer id;
    String names = "";
    TextView changeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_friends);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            cookie = extras.getString("Cookie");
            id = extras.getInt("ID");
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.7),(int) (height*.25));

        changeText = findViewById(R.id.helperNames);

        LoadHelperRequest loadHelperRequest = new LoadHelperRequest();
        loadHelperRequest.execute(String.valueOf(id));

        RelativeLayout mRelativeLayoutFriends = findViewById(R.id.popupFriends);
        mRelativeLayoutFriends.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return false;
            }
        });


    }


    private void createHelperList(String response) {
        JSONArray objectsArray = null;
        JSONObject namesObj = new JSONObject();
        try {
            objectsArray = new JSONArray(response);
            namesObj.put("Helfer:", objectsArray);
            JSONArray profiles = namesObj.getJSONArray("Helfer:");

            JSONObject objects = null;

            ArrayList<String> helper = new ArrayList<>();

            for(int i=0;i<profiles.length();i++){

                objects = profiles.getJSONObject(i);

                String helperNames = objects.getString("name");
                helper.add(helperNames);
                if(names.equals("")){
                    names += helperNames;
                }else{
                    names += ", "+helperNames;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class LoadHelperRequest extends AsyncTask<String, String, String> {
        String accResponse;

        @Override
        protected String doInBackground(String[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/eintrag/helfer?id="+legend[0])
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
            createHelperList(responseData);

            changeText.setText(names);
        }
    }
}