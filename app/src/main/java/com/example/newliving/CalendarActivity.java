package com.example.newliving;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity {
    public String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            cookie = extras.getString("Cookie");
            System.out.println(cookie);
        }

        TextView title = findViewById(R.id.toolbar_title);
        title.setText("Kalender");


        ImageView backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView profileIcon = findViewById(R.id.profile_icon);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, SettingsActivity.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });

        ImageView houseIcon = findViewById(R.id.house_icon);
        houseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, Homepage.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });

        ImageView calenderIcon = findViewById(R.id.calender_icon);
        calenderIcon.setVisibility(View.INVISIBLE);

        ImageView bulbIcon = findViewById(R.id.bulb_icon);
        bulbIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, TippsActivity.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });

        ImageView carIcon = findViewById(R.id.car_icon);
        carIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, ServiceActivity.class);
                intent.putExtra("Cookie", cookie);
                startActivity(intent);
            }
        });
    }
}