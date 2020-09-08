package com.example.mobileappforblind;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VoiceAssistant extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO = 1;

    private SpeechRecognizer SR;
    private TextToSpeech TTS;
    private TextView tvOutput;

    private MediaPlayer mpDing;
    private MediaPlayer mpBack;
    private MediaPlayer mpRecordVoice;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_assistant);
        tvOutput = findViewById(R.id.tvOutput);

        mpDing = MediaPlayer.create(VoiceAssistant.this, R.raw.ding);
        mpBack = MediaPlayer.create(VoiceAssistant.this, R.raw.back);
        mpRecordVoice = MediaPlayer.create(VoiceAssistant.this, R.raw.record_voice);

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        ImageButton micButton = findViewById(R.id.micButton);
        micButton.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(VoiceAssistant.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    if (ContextCompat.checkSelfPermission(VoiceAssistant.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(VoiceAssistant.this, Manifest.permission.RECORD_AUDIO)) {
                            Toast.makeText(VoiceAssistant.this, "Trouble showing permission request", Toast.LENGTH_SHORT).show();
                        } else {
                            ActivityCompat.requestPermissions(VoiceAssistant.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
                        }
                    } else {
                        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                        SR.startListening(intent);
                    }
                    return super.onDoubleTap(e);
                }
                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {
                    vibratePhone(vibrator);
                    mpRecordVoice.start();
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
            private GestureDetector gestureDetector = new GestureDetector(VoiceAssistant.this, new GestureDetector.SimpleOnGestureListener() {
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

        initializeSpeechRecognizer();
        initializeTextToSpeech();
    }

    public void vibratePhone(Vibrator v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(150);
        }
    }

    private void initializeSpeechRecognizer() {
        if(SpeechRecognizer.isRecognitionAvailable(this)) {
            SR = SpeechRecognizer.createSpeechRecognizer(this);
            SR.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> resultsList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    processResult(resultsList.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void initializeTextToSpeech() {
        TTS = new TextToSpeech(VoiceAssistant.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(TTS.getEngines().size() == 0) {
                    Toast.makeText(VoiceAssistant.this, "There is no text to speech engine installed in this device.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    TTS.setLanguage(Locale.US);
                }
            }
        });
    }

    private void processResult(String command) {
        if(command.contains("what")) {
            if(command.contains("time")) {
                Date now = new Date();
                String time = DateUtils.formatDateTime(VoiceAssistant.this, now.getTime(), DateUtils.FORMAT_SHOW_TIME);
                String timeAnswerString = "The time now is " + time;
                speak(timeAnswerString);
                tvOutput.setText(timeAnswerString);
            } else if (command.contains("battery")) {
                this.registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                speakOutput();
            }
        } else if (command.contains("how much")) {
            if (command.contains("battery")) {
                this.registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                speakOutput();
            }
        } else if (command.contains("on")) {
            if (command.contains("flashlight") || command.contains("torch")) {
                CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                try {
                    String[] list = camManager.getCameraIdList();
                    camManager.setTorchMode(list[0], true);
                } catch (CameraAccessException cae) {
                    Toast.makeText(VoiceAssistant.this, "Flashlight not functioning.", Toast.LENGTH_LONG).show();
                }
                String flashlightOn = "Flashlight is turned on.";
                speak(flashlightOn);
                tvOutput.setText(flashlightOn);
            }
        } else if (command.contains("off")) {
            if (command.contains("flashlight") || command.contains("torch")) {
                CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                try {
                    camManager.setTorchMode(camManager.getCameraIdList()[0], false);
                } catch (CameraAccessException cae) {
                    Toast.makeText(VoiceAssistant.this, "Flashlight not functioning.", Toast.LENGTH_LONG).show();
                }
                String flashlightOff = "Flashlight is turned off.";
                speak(flashlightOff);
                tvOutput.setText(flashlightOff);
            }
        }
    }

    private void speak(String message) {
        if(Build.VERSION.SDK_INT >= 21) {
            TTS.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            TTS.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void speakOutput() {
        TTS = new TextToSpeech(VoiceAssistant.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(TTS.getEngines().size() == 0) {
                    Toast.makeText(VoiceAssistant.this, "There is no text to speech engine installed in this device.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    TTS.setLanguage(Locale.US);
                    speak(tvOutput.getText().toString());
                }
            }
        });
    }

    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            String batteryLevel = level + "%";
            tvOutput.setText(batteryLevel);
        }
    };

    @Override
    public void onDestroy() {
        // Shuts Down TTS
        if (TTS != null) {
            TTS.stop();
            TTS.shutdown();
        }

        SR.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (TTS != null) {
            TTS.stop();
        }

        super.onPause();
    }

    private MediaPlayer mpVoiceAssistant;

    @Override
    protected void onResume() {
        super.onResume();

        mpVoiceAssistant = MediaPlayer.create(VoiceAssistant.this, R.raw.voice_assistant);
        mpVoiceAssistant.start();
    }
}
