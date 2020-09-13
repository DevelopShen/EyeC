package com.example.mobileappforblind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class RegisterContact extends AppCompatActivity {
    private MediaPlayer mpErrorMessage;
    private MediaPlayer mpSavedContact;
    private MediaPlayer mpDing;
    private MediaPlayer mpSave;
    private MediaPlayer mpBack;
    private MediaPlayer mpRegisterContact;


    private static final int REQUEST_READ_CONTACT = 1;
    private static final int REQUEST_WRITE_CONTACT = 1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_contact);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mpDing = MediaPlayer.create(RegisterContact.this, R.raw.ding);
        mpSave = MediaPlayer.create(RegisterContact.this, R.raw.save);
        mpBack = MediaPlayer.create(RegisterContact.this, R.raw.back);
        mpSavedContact = MediaPlayer.create(RegisterContact.this, R.raw.contact_is_saved);
        mpErrorMessage = MediaPlayer.create(RegisterContact.this, R.raw.name_and_hp_no_nn);

        Button saveContact = findViewById(R.id.btnSaveContact);

        saveContact.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(RegisterContact.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    mpDing.start();

                    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                    int rawContact_NewID = ops.size();

                    try {
                        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
                    } catch (Exception exc) {
                        return false;
                    }

                    EditText name = findViewById(R.id.etName);
                    EditText phoneNumber = findViewById(R.id.etPhoneNumber);

                    if ((ContextCompat.checkSelfPermission(RegisterContact.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(RegisterContact.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACT);
                    } else if (ContextCompat.checkSelfPermission(RegisterContact.this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(RegisterContact.this, new String[]{Manifest.permission.WRITE_CONTACTS}, REQUEST_WRITE_CONTACT);
                    } else if (!name.getText().toString().isEmpty() && !phoneNumber.getText().toString().isEmpty()) {
                        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContact_NewID)
                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name.getText().toString())
                                .build());

                        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContact_NewID)
                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber.getText().toString()).build());

                        mpSavedContact.start();
                        Toast.makeText(RegisterContact.this, "Contact is saved!", Toast.LENGTH_LONG).show();

                        ContentProviderResult[] res = new ContentProviderResult[0];

                        try {
                            res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                        } catch (RemoteException RE) {
                            Log.e("getContentResolver", RE.getMessage());
                        } catch (OperationApplicationException OAE) {
                            OAE.printStackTrace();
                        }
                    } else {
                        mpErrorMessage.start();
                        Toast.makeText(RegisterContact.this, "Name and phone number cannot be empty!", Toast.LENGTH_LONG).show();
                    }

                    return super.onDoubleTap(e);
                }
                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {
                    vibratePhone(vibrator);
                    mpSave.start();
                    return false;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(RegisterContact.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    mpDing.start();
                    finish();
                    return super.onDoubleTap(e);
                }
                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {
                    vibratePhone(vibrator);
                    mpBack.start();
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

        mpSave.release();
        mpBack.release();
        mpSavedContact.release();
        mpErrorMessage.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mpRegisterContact = MediaPlayer.create(RegisterContact.this, R.raw.register_contact);
        mpRegisterContact.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
