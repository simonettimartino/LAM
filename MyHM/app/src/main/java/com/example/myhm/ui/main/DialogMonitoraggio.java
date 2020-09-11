package com.example.myhm.ui.main;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.myhm.DialogModficaRep;
import com.example.myhm.NotificationBrodcastReciver;
import com.example.myhm.NotificationBrodcastReciverMonitoraggio;
import com.example.myhm.PostponiActivity;
import com.example.myhm.R;
import com.example.myhm.Reports;
import com.example.myhm.Tupla;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.myhm.MyData.SHARED_PREFS;

public class DialogMonitoraggio extends AppCompatDialogFragment{

    private AppCompatSpinner spinner;
    private TextView giorni;
    private EditText valoreSoglia;
    ArrayList<String> list = new ArrayList<String>();

    private DatePickerDialog.OnDateSetListener mDateSetListener;


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_monitoraggio, null);

        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        if(sharedPreferences.getInt("peso", 0) > 3)list.add("peso");
        if(sharedPreferences.getInt("temperatura", 0) > 3)list.add("temperatura");
        if(sharedPreferences.getInt("glicemia", 0) > 3)list.add("glicemia");



        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, list);
        spinner = v.findViewById(R.id.spinnerMonitoraggio);
        spinner.setAdapter(adapter);
       // spinner.setOnItemSelectedListener(list.get(1));

        valoreSoglia = v.findViewById(R.id.valoreSoglia);

        giorni = (TextView) v.findViewById(R.id.giorniMonitoraggio);
        giorni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendario = Calendar.getInstance();
                int anno = calendario.get(Calendar.YEAR);
                int mese = calendario.get(Calendar.MONTH);
                int giorno = calendario.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        anno, mese, giorno);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        builder.setView(v);
        builder.setTitle("Monitoraggio Valori");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String dataSelezionata = month + "/" + dayOfMonth + "/" + year;

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Date startDate;
                try {
                    startDate = df.parse(dataSelezionata);
                    String newDateString = df.format(startDate); //inserirò questa data nelle shared preference è quella nel formato: 00-00-0000
                    giorni.setText(dataSelezionata);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        builder.setView(v);
        builder.setTitle("Monitoraggio Valori");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //System.out.println("Peso 0 " + reportNew.getPeso().getValore());



                if(valoreSoglia.getText().length()!=0 && giorni.getText().length()!=0){

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    Format f = new SimpleDateFormat("MM/dd/yyyy");
                    String oggi = f.format(new Date());

                    editor.putString("parametroMonitoraggio", spinner.getSelectedItem().toString());
                    editor.apply();
                    editor.putFloat("valSoglia", Float.parseFloat(valoreSoglia.getText().toString()));
                    editor.apply();
                    editor.putString("giornoMonitoraggio", giorni.getText().toString());
                    editor.apply();
                    editor.putString("oggiMonitoraggio", oggi);
                    editor.apply();

                    Toast.makeText(getActivity(), "Riceverai una notifica se il valore soglia sarà superato!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), NotificationBrodcastReciverMonitoraggio.class);
                    intent.setAction("MY_NOTIFICATION_POSTPONI");
                    // il canale è diffrente dalle altre notifiche
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
                    Date giorno2 = new Date();
                    try {
                         giorno2 = new SimpleDateFormat("MM/dd/yyyy").parse(giorni.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    alarmManager.set(AlarmManager.RTC_WAKEUP, giorno2.getTime(), pendingIntent);

                } else {
                    Toast.makeText(getActivity(), "Riempi tutti i campi!", Toast.LENGTH_SHORT).show();
                }

            }

        });


        return builder.create();
    }
}
