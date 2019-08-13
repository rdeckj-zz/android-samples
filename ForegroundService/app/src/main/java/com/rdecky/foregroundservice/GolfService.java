package com.rdecky.foregroundservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class GolfService extends Service {

    public static final String TAG = GolfService.class.getSimpleName();

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy()");
        super.onDestroy();
    }

    private static final String CHANNEL_ID = "GolfServiceChannel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand()");
        startInForeground();
        return START_NOT_STICKY;
        //return START_STICKY;
        //return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "onBind()");
        return null;
    }

    private void startInForeground() {
        createNotificationChannel();
        Notification notification = getNotification();

        startForeground(NOTIFICATION_ID, notification);
    }

    private Notification getNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_golf_course)
                    .setContentTitle("Lets go golfing!")
                    .setContentText("It's a FOREground service, after all...")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("It's a FOREground service, after all..."))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
    }

    private void createNotificationChannel() {
        CharSequence name = getString(R.string.golf_service_channel);
        String description = getString(R.string.golf_service_channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
