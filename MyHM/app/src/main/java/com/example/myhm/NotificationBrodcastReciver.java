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
import android.widget.Toast;

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
        if(!inDB){

            System.out.println("sono entrato nel doNotifica");

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

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
            mBuilder.setContentTitle("Non hai inserito nessun report oggi");
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
