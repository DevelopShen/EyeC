package com.example.mobileappforblind.ScreenOnOffActivity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.mobileappforblind.StartupActivity;

import java.util.List;

public class ScreenOnOffReceiver extends BroadcastReceiver {
    public final static String SCREEN_TOGGLE_TAG = "SCREEN_TOGGLE_TAG";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(Intent.ACTION_USER_PRESENT.equals(action) && !isAppForeground(context)) {
            Intent startActivityIntent = new Intent(context, StartupActivity.class);
            startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startActivityIntent);
            Log.d(SCREEN_TOGGLE_TAG, "Phone unlocked");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            Log.d(SCREEN_TOGGLE_TAG, "Phone locked");
        }
    }

    public boolean isAppForeground(Context mContext) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
                return false;
            }
        }

        return true;
    }
}
