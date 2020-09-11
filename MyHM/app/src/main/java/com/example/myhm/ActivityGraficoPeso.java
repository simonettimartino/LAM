package com.example.myhm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.room.Room;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.myhm.MyData.SHARED_PREFS;

public class ActivityGraficoPeso extends AppCompatActivity {

    private List<Reports>  list;
    private ArrayList arrayList;
    private static String grafico;

    public static void setGrafico(String x){
        grafico = x;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_peso);

        new AsyncTaskRiceviReports().execute();
    }







    public class AsyncTaskRiceviReports extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            list = MainActivity.appDatabase.dataAccessObject().getDati();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            BarChart barChart = findViewById(R.id.idGraf1);
            System.out.println(barChart);

            ArrayList <BarEntry> arrayList = new ArrayList<>();

            for(int i = 0; i < list.size(); i++){
                try {
                    Date d = new SimpleDateFormat("MM/dd/yyyy").parse(list.get(i).getData());
                    Date dO = Calendar.getInstance().getTime();
                    float x = (float)((dO.getTime() - d.getTime())/(1000 * 60 * 60 * 24));
                    System.out.println("x: " + x);
                    float y = 0;
                    switch (grafico){
                        case "peso":
                            y = (float)list.get(i).getPeso().getValore();
                            break;
                        case "temperatura" :
                            y = (float)list.get(i).getTemperatura().getValore();
                            break;
                        case "glicemia" :
                            y = (float)list.get(i).getGlicemia().getValore();
                            break;
                    }
                    arrayList.add(new BarEntry(x, y));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            BarDataSet barDataSet = new BarDataSet(arrayList, "graf");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(16f);

            BarData barData = new BarData(barDataSet);
            barChart.setFitBars(true);
            barChart.setData(barData);
            barChart.animateY(2000);
            barChart.invalidate();


        }
    }

    public static class NotificationBrodcastReciverMonitoraggio extends BroadcastReceiver {

        private final String CHANNEL_ID = "report_notification";
        private AppDatabase appDatabase;
        private List<Reports> reports;
        boolean inDB = false;
        private Context context;

        @Override
        public void onReceive(Context context, Intent intent) {
            this.context = context;
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, "datiDB").build();
            Log.d("TEST1","sono qui 3331");
            new AsyncTaskGetDati().execute();

        }

        public class AsyncTaskGetDati extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                reports = appDatabase.dataAccessObject().getDati();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d("TEST1","sono qui 1");
                checkInDB();
                doNotifica(context);
            }
        }

        private void doNotifica(Context context) {
            if((!inDB)){

                System.out.println("sono entrato nel doNotifica");

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

                Intent repeating_intent = new Intent(context, MainActivity.class);
                repeating_intent.putExtra("t",true);
                repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Intent intent = new Intent(context, PostponiActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent intent2 = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);


                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);

                mBuilder.setSmallIcon(R.drawable.ic_baseline_edit_24);
                mBuilder.setContentIntent(pendingIntent);

                SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

                String orario = prefs.getString("orario", " hh : mm ");


                mBuilder.setContentTitle("Non hai inserito il rep!   " + orario);
                mBuilder.setContentText("Aggiungi un report!");
                mBuilder.setPriority(Notification.PRIORITY_MAX);
                mBuilder.setStyle(bigText);
                //mBuilder.setAutoCancel(true);
                mBuilder.addAction(R.drawable.ic_baseline_edit_24, "NEW REP", pendingIntent);
                mBuilder.addAction(R.drawable.ic_baseline_redo_24,"POSTPONI", intent2);

                Boolean bool = repeating_intent.getBooleanExtra("t", false);
                Log.d("NBR", String.valueOf(bool));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(
                            CHANNEL_ID,
                            "Titolo canale",
                            NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);
                    mBuilder.setChannelId(CHANNEL_ID);
                    Log.i("Notify", "Alarm"); //Optional, used for debuging.
                }

                notificationManager.notify(100, mBuilder.build());




            }
        }

        private void checkInDB() {
            Date data = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String dataString = df.format(data);
            if(dataString.startsWith("0"))
                dataString = dataString.substring(1);
            if(reports.size()==0){
                inDB = false;
                Log.d("TEST4","sono qui 4");
            }
            for(int i = 0; i < reports.size(); i++){
                Log.d("TEST2","sono qui 2  " + dataString + "data in db " + reports.get(i).getData());
                if(reports.get(i).getData().equals(dataString)){
                    inDB = true;
                    Log.d("TEST3","sono qui 3");
                    break;
                }
            }
        }
    }
}

