package com.example.myhm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class MyData extends Fragment {


    private TextView nome;
    private String n;
    private EditText h;
    private Button aggiorna, rep;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NOME = "nome";
    public static final String ORARIO = "orario";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mydata, container, false);

        nome = view.findViewById(R.id.nome1);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        n = sharedPreferences.getString(NOME, "");
        nome.setText(n);

        h = view.findViewById(R.id.editTextTime);
        h.setHint(sharedPreferences.getString(ORARIO, " hh : mm "));

        rep = view.findViewById(R.id.buttonRep);
        rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ReportsActivity.class));
            }
        });
        aggiorna = view.findViewById(R.id.aggiorna);
        aggiorna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!h.getText().toString().equals("") && h.getText().toString().matches("\\d\\d:\\d\\d")){
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(ORARIO, h.getText().toString());
                    editor.apply();
                    Toast.makeText(getContext(), "Data saved", Toast.LENGTH_SHORT).show();
                    SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
                    h.setText("");
                    h.setHint(sharedPreferences.getString(ORARIO, " hh : mm "));
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putBoolean("notifica", false);
                    boolean notifica = prefs.getBoolean("notifica", true);
                    if (notifica) {
                        setUpNotification();
                        editor.putBoolean("notifica", false);
                    }
                }else {
                    Toast.makeText(getView().getContext(), "Inserisci l'orario nel formato giusto!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        return view;
    }

    public void setUpNotification() {

        int ore = 12, minuti = 0;

        SharedPreferences prefs = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
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

        Intent intent = new Intent(getActivity(), NotificationBrodcastReciver.class);
        intent.setAction("MY_NOTIFICATION_MESSAGE");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, sceltaOrarioCalendario.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent); //
    }

}


