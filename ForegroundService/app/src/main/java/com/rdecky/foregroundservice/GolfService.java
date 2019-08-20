package com.rdecky.foregroundservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Random;

public class GolfService extends Service {

    public static final String TAG = "*" + GolfService.class.getSimpleName();
    private static final String CHANNEL_ID = "GolfServiceChannel";
    private static final int NOTIFICATION_ID = 1;

    boolean allowRebinding = true;

    private final IBinder binder = new GolfBinder();
    private final Random random = new Random();

    class GolfBinder extends Binder {
        GolfService getService() {
            return GolfService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand()");
        startInForeground();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "onBind()");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(TAG, "onUnbind()");
        return allowRebinding;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(TAG, "onRebind()");
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy()");
    }

    @Override
    public void onTrimMemory(int level) {
        Log.v(TAG, "onTrimMemory()");
    }

    public int calcFib(int number) {
        if (number <= 1) {
            return number;
        }
        return calcFib(number - 2) + calcFib(number - 1);
    }

    public String getGolfClub() {
        int club = random.nextInt(9);
        if (club == 0) {
            return "Pitching Wedge";
        }
        if (club <= 3) {
            return club + " wood";
        }

        return club + " iron";
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
