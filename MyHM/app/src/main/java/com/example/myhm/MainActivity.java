package com.example.myhm;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;
import 	androidx.appcompat.app.AppCompatActivity;
import 	com.google.android.material.tabs.TabLayout;
import androidx.room.Room;
import com.example.myhm.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {
    public static AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "datiDB").build();

    }




}
