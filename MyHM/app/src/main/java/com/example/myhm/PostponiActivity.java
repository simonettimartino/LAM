package com.example.myhm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

public class PostponiActivity extends AppCompatActivity {

    private Button post;
    private RadioButton radioButton;
    private RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postponi);

        post = findViewById(R.id.buttonPost);
        radioGroup = findViewById(R.id.radioGroup);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                String s = radioButton.getText().toString();
                s = s.substring(0,1);

                int mag = Integer.parseInt(s);
                int h = 0;

                SharedPreferences prefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
                String orario = prefs.getString("orario", " hh : mm ");


                  int ore = Integer.parseInt(orario.substring(0, orario.indexOf(":")));
                  int minuti = Integer.parseInt(orario.substring(orario.indexOf(":")+1));

                  if (mag + minuti > 59){
                      h = 1;
                      mag = mag + minuti - 59;
                  }


                Calendar sceltaOrarioCalendario = Calendar.getInstance();
                sceltaOrarioCalendario.set(Calendar.HOUR_OF_DAY, ore + h);
                sceltaOrarioCalendario.set(Calendar.MINUTE, minuti + mag);
                sceltaOrarioCalendario.set(Calendar.SECOND, 0);

                Intent intent = new Intent(PostponiActivity.this, NotificationBrodcastReciver.class);
                intent.setAction("MY_NOTIFICATION_MESSAGE");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(PostponiActivity.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, sceltaOrarioCalendario.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);



            }
        });

    }
    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this, "SELEZIONATO: " + radioButton.getText(),
                Toast.LENGTH_SHORT).show();
    }
}