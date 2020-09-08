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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DialToCall extends AppCompatActivity {
    private static final int REQUEST_PHONE_CALL = 1;
    private EditText etPhoneNumber;

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
    private MediaPlayer mpDialToCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial_to_call);
        initializeView();
    }

    private void initializeView() {
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        int[] btnIdList = {R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn0,
        R.id.btnAsterisk, R.id.btnHash, R.id.btnDelete, R.id.btnCall, R.id.btnBack};

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
        etPhoneNumber.append(val);
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
                if(etPhoneNumber.getText().toString().isEmpty()) {
                    MediaPlayer mpInvalidPhone = MediaPlayer.create(DialToCall.this, R.raw.invalid_phone_no);
                    mpInvalidPhone.start();
                    Toast.makeText(getApplicationContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                } else if (ContextCompat.checkSelfPermission(DialToCall.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DialToCall.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + etPhoneNumber.getText()));
                    startActivity(intent);
                }
                break;
            case R.id.btnDelete:
                mpDing.start();
                mpDelete.start();
                if(etPhoneNumber.getText().toString().length() >= 1) {
                    String newETPhoneNumber = etPhoneNumber.getText().toString().substring(0, etPhoneNumber.getText().toString().length()-1);
                    etPhoneNumber.setText(newETPhoneNumber);
                }
                break;
            case R.id.btnBack:
                mpDing.start();
                finish();
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
        }
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mpDialToCall = MediaPlayer.create(DialToCall.this, R.raw.dial_to_call);
        mpDialToCall.start();
    }
}
