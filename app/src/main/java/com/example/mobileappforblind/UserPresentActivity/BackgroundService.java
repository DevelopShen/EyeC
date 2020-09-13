package com.example.mobileappforblind.UserPresentActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class BackgroundService extends Service {
    private UserPresentReceiver userPresentReceiver = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Create an IntentFilter instance.
        IntentFilter intentFilter = new IntentFilter();

        // Add phone unlocked action.
        intentFilter.addAction("android.intent.action.USER_PRESENT");

        // Set broadcast receiver priority.
        intentFilter.setPriority(999);

        // Create a phone unlocked broadcast receiver.
        userPresentReceiver = new UserPresentReceiver();

        // Register the broadcast receiver with the intent filter object.
        registerReceiver(userPresentReceiver, intentFilter);

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "EyeC_running";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Running",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unregister screenOnOffReceiver when destroy.
        if(userPresentReceiver !=null)
        {
            unregisterReceiver(userPresentReceiver);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        this.stopForeground(true);
        this.stopSelf();
    }
}
