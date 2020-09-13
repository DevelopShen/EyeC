package com.example.mobileappforblind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class DialToCall extends AppCompatActivity {
    private static final int REQUEST_PHONE_CALL = 1;
    private TextView tvPhoneNumber;

    private MediaPlayer mpDing;
    private MediaPlayer mpOne;
    private MediaPlayer mpTwo;
    private MediaPlayer mpThree;
    private MediaPlayer mpFour;
    private MediaPlayer mpFive;
    private MediaPlayer mpSix;
    private MediaPlayer mpSeven;
    private MediaPlayer mpEight;
    private MediaPlayer mpNine;
    private MediaPlayer mpZero;
    private MediaPlayer mpAsterisk;
    private MediaPlayer mpHash;
    private MediaPlayer mpCall;
    private MediaPlayer mpDelete;
    private MediaPlayer mpBack;
    private MediaPlayer mpSpeakPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial_to_call);
        initializeView();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        initializeTextToSpeech();
    }

    private void initializeView() {
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        int[] btnIdList = {R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn0,
        R.id.btnAsterisk, R.id.btnHash, R.id.btnDelete, R.id.btnCall, R.id.btnBack, R.id.btnSpeakPhoneNumber};

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mpDing = MediaPlayer.create(DialToCall.this, R.raw.ding);
        mpOne = MediaPlayer.create(DialToCall.this, R.raw.one);
        mpTwo = MediaPlayer.create(DialToCall.this, R.raw.two);
        mpThree = MediaPlayer.create(DialToCall.this, R.raw.three);
        mpFour = MediaPlayer.create(DialToCall.this, R.raw.four);
        mpFive = MediaPlayer.create(DialToCall.this, R.raw.five);
        mpSix = MediaPlayer.create(DialToCall.this, R.raw.six);
        mpSeven = MediaPlayer.create(DialToCall.this, R.raw.seven);
        mpEight = MediaPlayer.create(DialToCall.this, R.raw.eight);
        mpNine = MediaPlayer.create(DialToCall.this, R.raw.nine);
        mpZero = MediaPlayer.create(DialToCall.this, R.raw.zero);
        mpAsterisk = MediaPlayer.create(DialToCall.this, R.raw.asterisk);
        mpHash = MediaPlayer.create(DialToCall.this, R.raw.hash);
        mpCall = MediaPlayer.create(DialToCall.this, R.raw.call);
        mpDelete = MediaPlayer.create(DialToCall.this, R.raw.delete);
        mpBack = MediaPlayer.create(DialToCall.this, R.raw.back);
        mpSpeakPhoneNumber = MediaPlayer.create(DialToCall.this, R.raw.speak_phone_number);

        for (int id: btnIdList) {
            final View view = findViewById(id);
            view.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(DialToCall.this, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        doubleTapButton(view);
                        return super.onDoubleTap(e);
                    }
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent event) {
                        vibratePhone(vibrator);
                        singleTapButton(view);
                        return false;
                    }
                });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return false;
                }
            });
        }
    }

    public void display(String val) {
        tvPhoneNumber.append(val + " ");
    }

    public void doubleTapButton(View view) {
        switch(view.getId()) {
            case R.id.btn1:
                mpDing.start();
                mpOne.start();
                display("1");
                break;
            case R.id.btn2:
                mpDing.start();
                mpTwo.start();
                display("2");
                break;
            case R.id.btn3:
                mpDing.start();
                mpThree.start();
                display("3");
                break;
            case R.id.btn4:
                mpDing.start();
                mpFour.start();
                display("4");
                break;
            case R.id.btn5:
                mpDing.start();
                mpFive.start();
                display("5");
                break;
            case R.id.btn6:
                mpDing.start();
                mpSix.start();
                display("6");
                break;
            case R.id.btn7:
                mpDing.start();
                mpSeven.start();
                display("7");
                break;
            case R.id.btn8:
                mpDing.start();
                mpEight.start();
                display("8");
                break;
            case R.id.btn9:
                mpDing.start();
                mpNine.start();
                display("9");
                break;
            case R.id.btn0:
                mpDing.start();
                mpZero.start();
                display("0");
                break;
            case R.id.btnAsterisk:
                mpDing.start();
                mpAsterisk.start();
                display("*");
                break;
            case R.id.btnHash:
                mpDing.start();
                mpHash.start();
                display("#");
                break;
            case R.id.btnCall:
                mpDing.start();
                mpCall.start();
                if(tvPhoneNumber.getText().toString().isEmpty()) {
                    MediaPlayer mpInvalidPhone = MediaPlayer.create(DialToCall.this, R.raw.invalid_phone_no);
                    mpInvalidPhone.start();
                    Toast.makeText(getApplicationContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                } else if (ContextCompat.checkSelfPermission(DialToCall.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DialToCall.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + tvPhoneNumber.getText()));
                    startActivity(intent);
                }
                break;
            case R.id.btnDelete:
                mpDing.start();
                mpDelete.start();
                if(tvPhoneNumber.getText().toString().length() >= 1) {
                    String newETPhoneNumber = tvPhoneNumber.getText().toString().substring(0, tvPhoneNumber.getText().toString().length()-2);
                    tvPhoneNumber.setText(newETPhoneNumber);
                }
                break;
            case R.id.btnBack:
                mpDing.start();
                finish();
                break;
            case R.id.btnSpeakPhoneNumber:
                if (tvPhoneNumber.getText().toString().equals("")) {
                    speak("Phone number is empty.");
                } else {
                    speak(tvPhoneNumber.getText().toString());
                }
                break;
        }
    }

    public void singleTapButton (View view) {
        switch (view.getId()) {
            case R.id.btn1:
                mpOne.start();
                break;
            case R.id.btn2:
                mpTwo.start();
                break;
            case R.id.btn3:
                mpThree.start();
                break;
            case R.id.btn4:
                mpFour.start();
                break;
            case R.id.btn5:
                mpFive.start();
                break;
            case R.id.btn6:
                mpSix.start();
                break;
            case R.id.btn7:
                mpSeven.start();
                break;
            case R.id.btn8:
                mpEight.start();
                break;
            case R.id.btn9:
                mpNine.start();
                break;
            case R.id.btn0:
                mpZero.start();
                break;
            case R.id.btnAsterisk:
                mpAsterisk.start();
                break;
            case R.id.btnHash:
                mpHash.start();
                break;
            case R.id.btnCall:
                mpCall.start();
                break;
            case R.id.btnDelete:
                mpDelete.start();
                break;
            case R.id.btnBack:
                mpBack.start();
                break;
            case R.id.btnSpeakPhoneNumber:
                mpSpeakPhoneNumber.start();
                break;
        }
    }

    public void vibratePhone(Vibrator v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(150);
        }
    }

    private TextToSpeech TTS;

    private void speak(String message) {
        if(Build.VERSION.SDK_INT >= 21) {
            TTS.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            TTS.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void initializeTextToSpeech() {
        TTS = new TextToSpeech(DialToCall.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(TTS.getEngines().size() == 0) {
                    Toast.makeText(DialToCall.this, "There is no text to speech engine installed in this device.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    TTS.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (TTS != null) {
            TTS.stop();
            TTS.shutdown();
        }

        mpAsterisk.release();
        mpCall.release();
        mpDelete.release();
        mpBack.release();
        mpOne.release();
        mpTwo.release();
        mpThree.release();
        mpFour.release();
        mpFive.release();
        mpSix.release();
        mpSeven.release();
        mpEight.release();
        mpNine.release();
        mpZero.release();
        mpSpeakPhoneNumber.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        MediaPlayer mpDialToCall = MediaPlayer.create(DialToCall.this, R.raw.dial_to_call);
        mpDialToCall.start();
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
