package com.example.newliving;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.newliving.Popups.PopupNewItemActivity;

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

public class Homepage extends AppCompatActivity {
    public String cookie;
    public String responseData;
    ListView viewChecklist;




    static ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        itemModelArrayList.clear();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            cookie = extras.getString("Cookie");
            System.out.println(cookie);
        }

        TextView title = findViewById(R.id.toolbar_title);
        title.setText("Umzugsplan");

        LoadRequest loadRequest = new LoadRequest();
        loadRequest.execute();


        ImageView backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                LogoutRequest logoutRequest = new LogoutRequest();
                logoutRequest.execute();
            }
        });

        ImageView profileIcon = findViewById(R.id.profile_icon);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, SettingsActivity.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });

        Button updateList = findViewById(R.id.buttonUpdateList);
        updateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList();
            }
        });

        Button newItem = findViewById(R.id.buttonNewItem);
        newItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, PopupNewItemActivity.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });

        ImageView houseIcon = findViewById(R.id.house_icon);
        houseIcon.setVisibility(View.INVISIBLE);

        ImageView calenderIcon = findViewById(R.id.calender_icon);
        calenderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, CalendarActivity.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });

        ImageView tippsIcon = findViewById(R.id.bulb_icon);
        tippsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, TippsActivity.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });

        ImageView carIcon = findViewById(R.id.car_icon);
        carIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, ServiceActivity.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });


    }
    public void updateList(){

        for(int i=0;i<itemModelArrayList.size();i++){
            //if(!itemModelArrayList.get(i).getPredetermined()){
                int temp = itemModelArrayList.get(i).getId();
                String temp0 = String.valueOf(temp);
                String temp1 = itemModelArrayList.get(i).getName();
                String temp2 = itemModelArrayList.get(i).getDate();
                ListUpdateRequest listUpdateRequest = new ListUpdateRequest();
                listUpdateRequest.execute(new String[]{temp0,temp1,temp2});
            //}
        }

    }

    class CustomAdapter extends BaseAdapter {
        Context context;
        ArrayList<ItemModel> arrayList;
        String response;

        public CustomAdapter(Context context, ArrayList<ItemModel> arrayList,String responses) {
            this.context = context;
            this.arrayList = arrayList;
            this.response = responses;
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
                convertView = LayoutInflater.from(context).inflate(R.layout.customlayout, parent, false);
            }

            convertView = getLayoutInflater().inflate(R.layout.customlayout,null);


            EditText editText = (EditText) convertView.findViewById(R.id.textList);
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox) ;
            ImageView delObject = (ImageView) convertView.findViewById(R.id.delButton);

            editText.setText(itemModelArrayList.get(position).getName());
            checkBox.setChecked(itemModelArrayList.get(position).getDone());

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    itemModelArrayList.get(position).setName(editText.getText().toString());
                }
            });


            if(!itemModelArrayList.get(position).getPredetermined()){

                delObject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int number = itemModelArrayList.get(position).getId();
                        DeleteRequest deleteCheckbox = new DeleteRequest();
                        itemModelArrayList.clear();
                        deleteCheckbox.execute(number);

                        LoadRequest loadProfiles = new LoadRequest();
                        loadProfiles.execute();
                        notifyDataSetChanged();
                    }
                });
            }else{
                delObject.setVisibility(View.INVISIBLE);
                editText.setFocusable(false);
            }


            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = itemModelArrayList.get(position).getId();
                }
            });

            return convertView;
        }

    }

    private void createList(String response) {
        JSONArray objectsArray = null;
        JSONObject namesObj = new JSONObject();
        try {
            objectsArray = new JSONArray(response);
            namesObj.put("Umzugsplan:", objectsArray);
            JSONArray profiles = namesObj.getJSONArray("Umzugsplan:");

            JSONObject objects = null;

            itemModelArrayList.clear();

            ArrayList<String> textList = new ArrayList<>();
            ArrayList<Integer> iD = new ArrayList<>();
            ArrayList<Boolean> checkbox = new ArrayList<>();
            ArrayList<String> date = new ArrayList<>();
            ArrayList<Boolean> predetermined = new ArrayList<>();

            String[] textListArray;
            Integer[] iDArray;
            Boolean[] checkboxArray;
            String[] dateArray;
            Boolean[] predeterminedArray;

            for(int i=0;i<profiles.length();i++){

                objects = profiles.getJSONObject(i);

                String checklistText = objects.getString("text");
                Integer id = objects.getInt("id");
                Boolean checked = objects.getBoolean("erledigt");
                String time = objects.getString("datum");
                Boolean notDeleteable = objects.getBoolean("vorgabe");

                textList.add(checklistText);
                iD.add(id);
                date.add(time);
                checkbox.add(checked);
                predetermined.add(notDeleteable);

            }


            textListArray =textList.toArray(new String[textList.size()]);
            iDArray = iD.toArray(new Integer[iD.size()]);
            dateArray = date.toArray(new String[date.size()]);
            checkboxArray = checkbox.toArray(new Boolean[checkbox.size()]);
            predeterminedArray = predetermined.toArray(new Boolean[predetermined.size()]);



            for (int i = 0; i < textListArray.length; i++) {
                ItemModel itemModel = new ItemModel();
                itemModel.setName(textListArray[i]);
                itemModel.setId(iDArray[i]);
                itemModel.setDate(dateArray[i]);
                itemModel.setDone(checkboxArray[i]);
                itemModel.setPredetermined(predeterminedArray[i]);
                itemModelArrayList.add(itemModel);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class LoadRequest extends AsyncTask<String, String, String> {
        String accResponse;

        @Override
        protected String doInBackground(String[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new okhttp3.Request.Builder()
                    .url("http://10.0.2.2:8080/api/eintrag")
                    .method("GET", null)
                    .addHeader("Cookie", cookie)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                accResponse = response.body().string();
                responseData = accResponse;
            } catch (IOException e) {
                e.printStackTrace();
            }


            return accResponse;
        }

        @Override
        protected void onPostExecute(String responseData) {
            createList(responseData);
            viewChecklist = (ListView) findViewById(R.id.inputList);
            CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),itemModelArrayList,responseData);

            viewChecklist.setAdapter(customAdapter);

        }

    }

    public class DeleteRequest extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/eintrag/löschen?id="+legend[0]) //TODO: umzugsplan item löschen
                    .method("DELETE", body)
                    .addHeader("Cookie", cookie)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responded = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

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

    public class ListUpdateRequest extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"text\": \""+legend[1]+"\",\r\n    \"datum\": \""+legend[2]+"\"\r\n}");
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/eintrag/update?id="+legend[0])
                    .method("PUT", body)
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

        @Override
        protected void onPostExecute(String data) {
            LoadRequest loadProfiles = new LoadRequest();
            loadProfiles.execute();

        }

    }


}