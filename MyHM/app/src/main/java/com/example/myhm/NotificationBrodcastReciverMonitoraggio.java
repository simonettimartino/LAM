package com.example.myhm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.room.Room;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.myhm.MyData.SHARED_PREFS;

public class NotificationBrodcastReciverMonitoraggio extends BroadcastReceiver {

    private final String CHANNEL_ID = "monitoraggio";
    private AppDatabase appDatabase;
    private List<Reports> reports;
    boolean inDB = false;
    private Context context;
    private Date giorno1, giorno2, data;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "datiDB").build();
        //Log.d("TEST1","sono qui 3331");
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
            SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            float soglia = prefs.getFloat("valSoglia", 0);
            String parametro = prefs.getString("parametroMonitoraggio", "");

            try {
                Date giorno1 = new SimpleDateFormat("MM/dd/yyyy").parse(prefs.getString("oggiMonitoraggio", ""));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                Date giorno2 = new SimpleDateFormat("MM/dd/yyyy").parse(prefs.getString("giornoMonitoraggio", ""));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            double media = 0.0;

            for (int i = 0; i < reports.size(); i++){
                try {
                    data=new SimpleDateFormat("MM/dd/yyyy").parse(reports.get(i).getData());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (data.after(giorno1) && data.before(giorno2)){
                    switch (parametro){
                        case "peso" :
                            media = media + reports.get(i).getPeso().getValore();
                            break;
                        case "temperatura" :
                            media = media + reports.get(i).getTemperatura().getValore();
                            break;
                        case "glicemia" :
                            media = media + reports.get(i).getGlicemia().getValore();
                            break;
                    }

                }
            }
            if (media > soglia)
                doNotifica(context);
        }
    }

    private void doNotifica(Context context) {

            System.out.println("sono entrato nel doNotifica");

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            /*
            Intent repeating_intent = new Intent(context, MainActivity.class);
            repeating_intent.putExtra("t",true);
            repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intent = new Intent(context, PostponiActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent intent2 = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            */

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);

            mBuilder.setSmallIcon(R.drawable.ic_baseline_edit_24);
            //mBuilder.setContentIntent(pendingIntent);




            mBuilder.setContentTitle("Hai superato il valore soglia " );

            mBuilder.setPriority(Notification.PRIORITY_MAX);
            mBuilder.setStyle(bigText);


            // controllo che non
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "Titolo canale",
                        NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(CHANNEL_ID);
                Log.i("Notify", "Alarm"); //Optional, used for debuging.
            }

            notificationManager.notify(101, mBuilder.build());

    }


}
