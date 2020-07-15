package com.example.myhm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import 	androidx.appcompat.app.AppCompatActivity;
import 	com.google.android.material.tabs.TabLayout;
import androidx.room.Room;
import com.example.myhm.ui.main.SectionsPagerAdapter;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static AppDatabase appDatabase;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private Integer n = 0;
    private ViewPager viewPager;
    private Intent intentNotfiche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart){
            startActivity(new Intent(MainActivity.this, StartActivity.class));
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        }

        setUpNotification();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "datiDB").build();
        viewPager.setCurrentItem(n);
    }

    public void setUpNotification() {

        int ore = 12, minuti = 0;

        SharedPreferences prefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        String orario = prefs.getString("orario", " hh : mm ");

        if(!orario.equals(" hh : mm ")) {
            ore = Integer.parseInt(orario.substring(0, orario.indexOf(":")));
            minuti = Integer.parseInt(orario.substring(orario.indexOf(":")+1));
        }
        System.out.println("ore: " + ore + " minuti:" + minuti);
        Calendar sceltaOrarioCalendario = Calendar.getInstance();
        sceltaOrarioCalendario.set(Calendar.HOUR_OF_DAY, ore);
        sceltaOrarioCalendario.set(Calendar.MINUTE, minuti);
        sceltaOrarioCalendario.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, NotificationBrodcastReciver.class);
        intent.setAction("MY_NOTIFICATION_MESSAGE");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, sceltaOrarioCalendario.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent); //
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();

        Boolean bool = intent.getBooleanExtra("t", false);
        Boolean bool1 = intent.getBooleanExtra("y", false);
        Log.d("bool", String.valueOf(bool));
        Log.d("bool1", String.valueOf(bool1));
        if (bool) {
            n = 2;
            Log.d("if1", "sono entrato nel if");
            sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
            viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabs = findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);
            appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "datiDB").build();
            viewPager.setCurrentItem(n);
            bool = false;
        }


    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        // set the string passed from the service to the original intent
        setIntent(intent);

    }
}
