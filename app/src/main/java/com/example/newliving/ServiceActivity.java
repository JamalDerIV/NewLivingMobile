package com.example.newliving;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class ServiceActivity extends AppCompatActivity {
    String cookie;
    String euro = "€";


    static ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();

    String[] items = {"Gesamtpreis","Kilometer Preis", "Stunden Preis"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        itemModelArrayList.clear();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            cookie = extras.getString("Cookie");
            System.out.println(cookie);
        }

        TextView title = findViewById(R.id.toolbar_title);
        title.setText("Dienstleistungen");

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton radioButtonA = findViewById(R.id.radioAnhaenger);
        RadioButton radioButtonT = findViewById(R.id.radioTransporter);

        radioButtonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadServices("/art?art=anhänger");
            }
        });
        radioButtonT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadServices("/art?art=transporter");
            }
        });

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,items);

        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if(position==0){
                    SortRequest sortRequest = new SortRequest();
                    sortRequest.execute("gesamt");

                }else if(position==1){
                    SortRequest sortRequest = new SortRequest();
                    sortRequest.execute("kilometer");
                }else if(position==2){
                    SortRequest sortRequest = new SortRequest();
                    sortRequest.execute("stunde");
                }
                radioGroup.clearCheck();
            }
        });

        loadServices("");

        ImageView backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView houseIcon = findViewById(R.id.house_icon);
        houseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, Homepage.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });

        ImageView calenderIcon = findViewById(R.id.calender_icon);
        calenderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, CalendarActivity.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });

        ImageView tippsIcon = findViewById(R.id.bulb_icon);
        tippsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, TippsActivity.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });

        ImageView carIcon = findViewById(R.id.car_icon);
        carIcon.setVisibility(View.INVISIBLE);
    }

    private void loadServices(String data) {
        ServicesGetRequest servicesGetRequest = new ServicesGetRequest();
        servicesGetRequest.execute(data);
    }

    private void createList(String response) {
        JSONArray objectsArray = null;
        JSONObject namesObj = new JSONObject();
        try {
            objectsArray = new JSONArray(response);
            namesObj.put("Dienstleistungen:", objectsArray);
            JSONArray profiles = namesObj.getJSONArray("Dienstleistungen:");

            JSONObject objects = null;

            itemModelArrayList.clear();

            ArrayList<String> nameCompany = new ArrayList<>();
            ArrayList<String> address = new ArrayList<>();
            ArrayList<String> type = new ArrayList<>();
            ArrayList<Integer> iD = new ArrayList<>();
            ArrayList<String> deposit = new ArrayList<>();
            ArrayList<String> priceKM = new ArrayList<>();
            ArrayList<String> priceHour = new ArrayList<>();
            ArrayList<String> fullPrice = new ArrayList<>();

            String[] nameCompanyArray;
            String[] addressArray;
            String[] typeArray;
            Integer[] iDArray;
            String[] depositArray;
            String[] priceKMArray;
            String[] priceHourArray;
            String[] fullPriceArray;


            for(int i=0;i<profiles.length();i++){

                objects = profiles.getJSONObject(i);

                String nameC = objects.getString("name");
                String addresS = objects.getString("anschrift");
                String typE = objects.getString("typ");
                Integer id = objects.getInt("id");
                String deposiT = objects.getString("kaution");
                String priceK = objects.getString("preisProKilometer");
                String priceH = objects.getString("preisProStunde");
                String fullP = objects.getString("gesamtPreis");

                nameCompany.add(nameC);
                address.add(addresS);
                type.add(typE);
                iD.add(id);
                deposit.add(deposiT);
                priceKM.add(priceK);
                priceHour.add(priceH);
                fullPrice.add(fullP);

            }

            nameCompanyArray =nameCompany.toArray(new String[nameCompany.size()]);
            addressArray =address.toArray(new String[address.size()]);
            typeArray = type.toArray(new String[type.size()]);
            iDArray = iD.toArray(new Integer[iD.size()]);
            depositArray = deposit.toArray(new String[deposit.size()]);
            priceKMArray = priceKM.toArray(new String[priceKM.size()]);
            priceHourArray = priceHour.toArray(new String[priceHour.size()]);
            fullPriceArray = fullPrice.toArray(new String[fullPrice.size()]);

            for (int i = 0; i < nameCompanyArray.length; i++) {
                ItemModel itemModel = new ItemModel();
                itemModel.setNameCompany(nameCompanyArray[i]);
                itemModel.setAddress(addressArray[i]);
                itemModel.setType(typeArray[i]);
                itemModel.setId(iDArray[i]);
                itemModel.setDeposit(depositArray[i]);
                itemModel.setPriceKM(priceKMArray[i]);
                itemModel.setPriceHour(priceHourArray[i]);
                itemModel.setFullPrice(fullPriceArray[i]);
                itemModelArrayList.add(itemModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class CustomAdapter extends BaseAdapter {
        Context context;
        ArrayList<ItemModel> arrayList;

        public CustomAdapter(Context context, ArrayList<ItemModel> arrayList) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.customlayoutservice, parent, false);
            }
            convertView = getLayoutInflater().inflate(R.layout.customlayoutservice,null);


            TextView nameCompany = (TextView) convertView.findViewById(R.id.nameService);
            TextView address = (TextView) convertView.findViewById(R.id.addressService);
            TextView type = (TextView) convertView.findViewById(R.id.typeService);
            TextView priceKM = (TextView) convertView.findViewById(R.id.priceKMService);
            TextView priceHour = (TextView) convertView.findViewById(R.id.priceHourService);
            TextView deposit = (TextView) convertView.findViewById(R.id.depositService);
            TextView fullPrice = (TextView) convertView.findViewById(R.id.priceService);
            Button buy = (Button) convertView.findViewById(R.id.buyService);



            nameCompany.setText(itemModelArrayList.get(position).getNameCompany());
            address.setText(itemModelArrayList.get(position).getAddress());
            type.setText(itemModelArrayList.get(position).getType());
            priceKM.setText(itemModelArrayList.get(position).getPriceKM()+euro);
            priceHour.setText(itemModelArrayList.get(position).getPriceHour()+euro);
            deposit.setText(itemModelArrayList.get(position).getDeposit()+euro);
            fullPrice.setText(itemModelArrayList.get(position).getFullPrice()+euro);

            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BuyServiceRequest buyServiceRequest = new BuyServiceRequest();
                    buyServiceRequest.execute(String.valueOf(itemModelArrayList.get(position).getId()));
                }
            });

            return convertView;
        }

    }



    public class ServicesGetRequest extends AsyncTask<String,String,String> {
        String accResponse;

        @Override
        protected String doInBackground(String[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/dienstleistung"+legend[0])
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
        protected void onPostExecute(String data) {
            itemModelArrayList.clear();
            createList(data);
            ListView viewChecklist = (ListView) findViewById(R.id.inputServicesList);
            CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),itemModelArrayList);

            viewChecklist.setAdapter(customAdapter);

        }

    }

    public class SortRequest extends AsyncTask<String,String,String> {
        String accResponse;

        @Override
        protected String doInBackground(String[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/dienstleistung/sortiert?art="+legend[0])
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
        protected void onPostExecute(String data) {
            itemModelArrayList.clear();
            createList(data);
            ListView viewChecklist = (ListView) findViewById(R.id.inputServicesList);
            CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),itemModelArrayList);

            viewChecklist.setAdapter(customAdapter);

        }

    }

    public class BuyServiceRequest extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String[] legend) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/dienstleistung/buchen?id="+legend[0])
                    .method("GET", null)
                    .addHeader("Cookie", cookie)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responded = response.body().string();
                String accresponse = responded;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }
}