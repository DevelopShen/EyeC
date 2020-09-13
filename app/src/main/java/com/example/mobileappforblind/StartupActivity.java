package com.example.mobileappforblind;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileappforblind.UserPresentActivity.BackgroundService;

public class StartupActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2500;
    private MediaPlayer welcome;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        Intent backgroundService = new Intent(getApplicationContext(), BackgroundService.class);
        startForegroundService(backgroundService);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        welcome.release();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(StartupActivity.this, Home.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
        welcome = MediaPlayer.create(StartupActivity.this, R.raw.welcome);
        welcome.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }
}
