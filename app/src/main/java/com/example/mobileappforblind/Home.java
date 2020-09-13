package com.example.mobileappforblind;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.mobileappforblind.ObjectDetection.DetectorActivity;

public class Home extends AppCompatActivity {
    private MediaPlayer mpDing;
    private MediaPlayer mpGoodbye;
    private MediaPlayer mpRegisterContact;
    private MediaPlayer mpDialToCall;
    private MediaPlayer mpRecognizeObject;
    private MediaPlayer mpRecognizeText;
    private MediaPlayer mpVoiceAssistant;
    private MediaPlayer mpExitApp;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Media players for notifying
        mpDing = MediaPlayer.create(Home.this, R.raw.ding);
        mpGoodbye = MediaPlayer.create(Home.this, R.raw.goodbye);
        mpRegisterContact = MediaPlayer.create(Home.this, R.raw.register_contact);
        mpDialToCall = MediaPlayer.create(Home.this, R.raw.dial_to_call);
        mpRecognizeObject = MediaPlayer.create(Home.this, R.raw.recognize_object);
        mpRecognizeText = MediaPlayer.create(Home.this, R.raw.recognize_text);
        mpVoiceAssistant = MediaPlayer.create(Home.this, R.raw.voice_assistant);
        mpExitApp = MediaPlayer.create(Home.this, R.raw.exit_application);

        //Access Register Contact
        Button btnRegisterContact = findViewById(R.id.btnRegisterContact);
        btnRegisterContact.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(Home.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    mpDing.start();
                    Intent intent = new Intent(Home.this, RegisterContact.class);
                    startActivity(intent);
                    return super.onDoubleTap(e);
                }
                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {
                    vibratePhone(vibrator);
                    mpRegisterContact.start();
                    return false;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        //Access Dial to Call
        Button btnDialToCall = findViewById(R.id.btnDialToCall);
        btnDialToCall.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(Home.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    mpDing.start();
                    Intent intent = new Intent(Home.this, DialToCall.class);
                    startActivity(intent);
                    return super.onDoubleTap(e);
                }
                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {
                    vibratePhone(vibrator);
                    mpDialToCall.start();
                    return false;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        //Access Recognize Object
        Button btnRecognizeObject = findViewById(R.id.btnRecogizeObject);
        btnRecognizeObject.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(Home.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    mpDing.start();
                    Intent intent = new Intent(Home.this, DetectorActivity.class);
                    startActivity(intent);
                    return super.onDoubleTap(e);
                }
                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {
                    vibratePhone(vibrator);
                    mpRecognizeObject.start();
                    return false;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        //Access Recognize Text
        Button btnRecognizeText = findViewById(R.id.btnRecogizeText);
        btnRecognizeText.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(Home.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    mpDing.start();
                    Intent intent = new Intent(Home.this, RecognizeText.class);
                    startActivity(intent);
                    return super.onDoubleTap(e);
                }
                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {
                    vibratePhone(vibrator);
                    mpRecognizeText.start();
                    return false;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        //Access Voice Assistant
        Button btnVoiceAssistant = findViewById(R.id.btnVoiceAssistant);
        btnVoiceAssistant.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(Home.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    mpDing.start();
                    Intent intent = new Intent(Home.this, VoiceAssistant.class);
                    startActivity(intent);
                    return super.onDoubleTap(e);
                }
                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {
                    vibratePhone(vibrator);
                    mpVoiceAssistant.start();
                    return false;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        Button btnExitApp = findViewById(R.id.btnExitApp);
        btnExitApp.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(Home.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    mpGoodbye.start();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return super.onDoubleTap(e);
                }
                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {
                    vibratePhone(vibrator);
                    mpExitApp.start();
                    return false;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    public void vibratePhone(Vibrator v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(150);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mpDing.release();
        mpGoodbye.release();
        mpRegisterContact.release();
        mpDialToCall.release();
        mpRecognizeObject.release();
        mpRecognizeText.release();
        mpVoiceAssistant.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        MediaPlayer mpHomePage = MediaPlayer.create(Home.this, R.raw.homepage);
        mpHomePage.start();
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
