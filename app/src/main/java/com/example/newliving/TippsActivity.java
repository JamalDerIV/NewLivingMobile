package com.example.newliving;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TippsActivity extends AppCompatActivity {
    public String cookie;
    public String tippsData;

    ArrayList<String> textname = new ArrayList<>();
    String[] textnameArray;

    ArrayList<String> text = new ArrayList<>();
    String[] textArray;

    static ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipps);

        itemModelArrayList.clear();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            cookie = extras.getString("Cookie");
            System.out.println(cookie);
        }

        TippsRequest tippsRequest = new TippsRequest();
        tippsRequest.execute();


        ImageView backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView title = findViewById(R.id.toolbar_title);
        title.setText("Tipps");

        ImageView profileIcon = findViewById(R.id.profile_icon);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TippsActivity.this, SettingsActivity.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });

        ImageView houseIcon = findViewById(R.id.house_icon);
        houseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TippsActivity.this, Homepage.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });

        ImageView calenderIcon = findViewById(R.id.calender_icon);
        calenderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TippsActivity.this, CalendarActivity.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });

        ImageView bulbIcon = findViewById(R.id.bulb_icon);
        bulbIcon.setVisibility(View.INVISIBLE);

        ImageView carIcon = findViewById(R.id.car_icon);
        carIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TippsActivity.this, ServiceActivity.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });
    }

    class CustomAdapter extends BaseAdapter {
        Context context;
        ArrayList<ItemModel> arrayList;

        public CustomAdapter(Context context,ArrayList<ItemModel> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return itemModelArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemModelArrayList.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView ==  null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.customlayouttipps, parent, false);
            }

            convertView = getLayoutInflater().inflate(R.layout.customlayouttipps,null);

            ExpandableTextView expTv1 = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
            expTv1.setText(arrayList.get(position).getText());

            TextView name = (TextView) convertView.findViewById(R.id.textName);
            name.setText(arrayList.get(position).getTextName());
            //expTv1.setText(itemModelArrayList.get(position).getText()+": "+itemModelArrayList.get(position).getText());

            return convertView;
        }

    }

    private void createList(String response) {
        JSONArray namesArray = null;
        JSONObject namesObj = new JSONObject();
        try {
            namesArray = new JSONArray(response);
            namesObj.put("Tipps:", namesArray);
            JSONArray profiles = namesObj.getJSONArray("Tipps:");

            JSONObject names = null;
            for(int i=0;i<profiles.length();i++){

                names = profiles.getJSONObject(i);

                String name = names.getString("name");
                String content = names.getString("text");

                textname.add(name);
                text.add(content);
            }


            textnameArray =textname.toArray(new String[textname.size()]);
            textArray =text.toArray(new String[text.size()]);


            for (int i = 0; i < textnameArray.length; i++) {
                ItemModel itemModel = new ItemModel();
                itemModel.setTextName(textnameArray[i]);
                itemModel.setText(textArray[i]);
                itemModelArrayList.add(itemModel);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class TippsRequest extends AsyncTask<String, String, String> {
        String tippsResponse;

        @Override
        protected String doInBackground(String[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/tipp")
                    .method("GET", null)
                    .addHeader("Cookie", cookie)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                tippsResponse = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return tippsResponse;
        }

        @Override
        protected void onPostExecute(String tippsDatas) {
            createList(tippsDatas);

            ListView viewTippsList = (ListView) findViewById(R.id.inputTippsList);
            CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),itemModelArrayList);

            viewTippsList.setAdapter(customAdapter);

        }
    }
}