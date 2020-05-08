package com.ansh1999.tasks;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ForegroundService extends Service {
    NotificationManager notificationManager;
    NotificationChannel notificationChannel;
    public static final String CHANNEL_ID = "tasksServiceChannel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String mytask=intent.getStringExtra("task");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra("notification_status","1");
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String tasks[]=mytask.split("\n");
        String str="";
        for (int i=0;i<tasks.length;i++){
            if (i==tasks.length-1)
                str+="\u2022 "+tasks[i];
            else
                str+="\u2022 "+tasks[i]+"\n";
        }
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(str))
                .setContentText(str)
                .setSmallIcon(R.drawable.ic_notifications_active_white_24dp)
                .setContentIntent(pendingIntent)

                .setFullScreenIntent(pendingIntent,true)
                .build();

        Log.e("TAG", "onStartCommand: Service" + notification);
        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Tasks Service Channel",
                    NotificationManager.IMPORTANCE_MIN
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
