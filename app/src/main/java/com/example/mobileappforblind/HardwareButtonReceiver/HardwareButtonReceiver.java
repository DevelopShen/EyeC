package com.example.mobileappforblind.HardwareButtonReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.mobileappforblind.StartupActivity;

public class HardwareButtonReceiver extends BroadcastReceiver {
    public final static String SCREEN_TOGGLE_TAG = "SCREEN_TOGGLE_TAG";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(Intent.ACTION_SCREEN_OFF.equals(action))
        {
            Log.d(SCREEN_TOGGLE_TAG, "Screen is turn off.");
        } else if(Intent.ACTION_SCREEN_ON.equals(action))
        {
            Intent startActivity = new Intent(context, StartupActivity.class);
            startActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startActivity);
            Log.d(SCREEN_TOGGLE_TAG, "Screen is turn on.");
        }
    }
}
