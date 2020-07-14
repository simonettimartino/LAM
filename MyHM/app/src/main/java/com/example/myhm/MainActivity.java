package com.example.myhm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;
import 	androidx.appcompat.app.AppCompatActivity;
import 	com.google.android.material.tabs.TabLayout;
import androidx.room.Room;
import com.example.myhm.ui.main.SectionsPagerAdapter;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static AppDatabase appDatabase;

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
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "datiDB").build();

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
        Calendar sceltaOrarioCalendario = Calendar.getInstance(); //mi servirà per trovare l'orario corretto
        sceltaOrarioCalendario.set(Calendar.HOUR_OF_DAY, ore);
        sceltaOrarioCalendario.set(Calendar.MINUTE, minuti);
        sceltaOrarioCalendario.set(Calendar.SECOND, 0); //non faccio settare i secondi all'utente
        //Toast.makeText(getApplicationContext(), "Orario in cui riceverai la notifica: " + oreInt + ":" + minutiInt, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, NotificationBrodcastReciver.class);
        intent.setAction("MY_NOTIFICATION_MESSAGE");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //considero anche se il device va in sleep mode
        //come secondo parametro gli passo la data/ora scelta dall'utente
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, sceltaOrarioCalendario.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent); //
    }



}
