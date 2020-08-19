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
    private MediaPlayer mpRegisterContact;
    private MediaPlayer mpDialToCall;
    private MediaPlayer mpRecognizeObject;
    private MediaPlayer mpRecognizeText;
    private MediaPlayer mpVoiceAssistant;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Media players for notifying
        mpDing = MediaPlayer.create(Home.this, R.raw.ding);
        mpRegisterContact = MediaPlayer.create(Home.this, R.raw.register_contact);
        mpDialToCall = MediaPlayer.create(Home.this, R.raw.dial_to_call);
        mpRecognizeObject = MediaPlayer.create(Home.this, R.raw.recognize_object);
        mpRecognizeText = MediaPlayer.create(Home.this, R.raw.recognize_text);
        mpVoiceAssistant = MediaPlayer.create(Home.this, R.raw.voice_assistant);

        //Access Register Contact
        Button registerContact = findViewById(R.id.btnRegisterContact);
        registerContact.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(Home.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    mpDing.start();
                    Intent intent = new Intent(Home.this, RegisterContact.class);
                    startActivity(intent);
//                    mpRegisterContact.start();
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
        Button dialToCall = findViewById(R.id.btnDialToCall);
        dialToCall.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(Home.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    mpDing.start();
                    Intent intent = new Intent(Home.this, DialToCall.class);
                    startActivity(intent);
                    mpDialToCall.start();
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
        Button recognizeObject = findViewById(R.id.btnRecogizeObject);
        recognizeObject.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(Home.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    mpDing.start();
                    Intent intent = new Intent(Home.this, DetectorActivity.class);
                    startActivity(intent);
                    mpRecognizeObject.start();
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
        Button recognizeText = findViewById(R.id.btnRecogizeText);
        recognizeText.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(Home.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    mpDing.start();
                    Intent intent = new Intent(Home.this, RecognizeText.class);
                    startActivity(intent);
                    mpRecognizeText.start();
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
        Button voiceAssistant = findViewById(R.id.btnVoiceAssistant);
        voiceAssistant.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(Home.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    mpDing.start();
                    Intent intent = new Intent(Home.this, VoiceAssistant.class);
                    startActivity(intent);
                    mpVoiceAssistant.start();
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
}
