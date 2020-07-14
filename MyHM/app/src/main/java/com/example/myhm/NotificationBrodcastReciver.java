package com.example.myhm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationBrodcastReciver extends BroadcastReceiver {

    private final String CHANNEL_ID = "report_notification";
    private AppDatabase appDatabase;
    private List<Reports> reports;
    boolean inDB = false;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "datiDB").build();


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
            checkInDB();
            doNotifica(context);
        }
    }

    private void doNotifica(Context context) {
        if(!inDB){

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent repeating_intent = new Intent(context, MainActivity.class);
            repeating_intent.putExtra("fromNotify",true);
            repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent posponi = new Intent(context, MainActivity.class);
            posponi.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent posponiPendInt = PendingIntent.getActivity(context, 100, posponi, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);

            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setContentTitle("Non hai inserito nessun report oggi");
            mBuilder.setContentText("Aggiungi un report!");
            mBuilder.setPriority(Notification.PRIORITY_MAX);
            mBuilder.setStyle(bigText);
            mBuilder.setAutoCancel(true); // se l'utente tocca la notifica, essa si chiude subito
            mBuilder.addAction(R.drawable.ic_launcher_background, "Aggiungi", pendingIntent);
            mBuilder.addAction(R.drawable.ic_launcher_background, "Posponi", posponiPendInt);

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
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String dataString = df.format(data);

        for(int i = 0; i < reports.size(); i++){
            if(reports.get(i).getData().equals(dataString)){
                inDB = true;
            }
        }
    }
}