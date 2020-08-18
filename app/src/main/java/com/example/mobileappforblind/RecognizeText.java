package com.example.mobileappforblind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Locale;

public class RecognizeText extends AppCompatActivity {
    private EditText etResult;
    private ImageView ivImagePreview;

    private MediaPlayer mpCaptureText;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 1000;
    private static final int IMAGE_PICK_GALLERY_CODE = 1001;

    private String[] cameraPermission;
    private String[] storagePermission;

    private Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize_text);

        etResult = findViewById(R.id.etResult);
        etResult.setEnabled(false);

        ivImagePreview = findViewById(R.id.ivImagePreview);
        Button btnInstantCaptureImage = findViewById(R.id.btnInstantCaptureImage);

        mpCaptureText = MediaPlayer.create(RecognizeText.this, R.raw.capture_text);

        btnInstantCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpCaptureText.start();
                if (!checkCameraPermission()) {
                    //camera permission not allowed, so request it
                    requestCameraPermission();
                } else {
                    //permission allowed, take picture
                    pickCamera();
                }
            }
        });

        //camera permission
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //storage permission
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        initializeTextToSpeech();
    }

    //ActionBar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //handle ActionBar item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addImage) {
            if (!checkStoragePermission()) {
                //storage permission not allowed, so request it
                requestStoragePermission();
            } else {
                //permission allowed, access storage
                pickGallery();
            }
        }
        if (id == R.id.settings) {
            Toast.makeText(RecognizeText.this, "Settings", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    private void pickGallery() {
        //intent to pick image from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);

        //set intent type to image
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickCamera() {
        //intent to take image from camera, it will also be saved to storage to get high quality image
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to Text");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(RecognizeText.this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean resultForStorageAccess = ContextCompat.checkSelfPermission(RecognizeText.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return resultForStorageAccess;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(RecognizeText.this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        /*Check camera permission and return the result
         *In order to get high quality image we have to save image to external storage first before inserting to image view*/
        boolean resultForCameraAccess = ContextCompat.checkSelfPermission(RecognizeText.this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean resultForStorageAccess = ContextCompat.checkSelfPermission(RecognizeText.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return resultForCameraAccess && resultForStorageAccess;
    }

    //Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAllowed = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAllowed = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAllowed && writeStorageAllowed) {
                        pickCamera();
                    } else {
                        Toast.makeText(RecognizeText.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean writeStorageAllowed = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAllowed) {
                        pickGallery();
                    } else {
                        Toast.makeText(RecognizeText.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    //Handle image result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //got image from camera
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                ivImagePreview.setImageURI(image_uri);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) ivImagePreview.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!textRecognizer.isOperational()) {
                    Toast.makeText(RecognizeText.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder SB = new StringBuilder();

                    //get text from SB until there is no text
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItem = items.valueAt(i);
                        SB.append(myItem.getValue());
                        SB.append("\n");
                    } //set text to edit text
                    etResult.setText(SB.toString());
                    speak(SB.toString());
                }
            } else if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON) //enable image guidelines
                        .start(RecognizeText.this);
            }
        }

        //get cropped image from gallery
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri(); //get image uri

                //set image to image view
                ivImagePreview.setImageURI(resultUri);

                //get drawable bitmap or text recognition
                BitmapDrawable bitmapDrawable = (BitmapDrawable) ivImagePreview.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if (!textRecognizer.isOperational()) {
                    Toast.makeText(RecognizeText.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder SB = new StringBuilder();

                    //get text from SB until there is no text
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItem = items.valueAt(i);
                        SB.append(myItem.getValue());
                        SB.append("\n");
                    } //set text to edit text
                    etResult.setText(SB.toString());
                    speak(SB.toString());
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(RecognizeText.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        TTS = new TextToSpeech(RecognizeText.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(TTS.getEngines().size() == 0) {
                    Toast.makeText(RecognizeText.this, "There is no text to speech engine installed in this device.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    TTS.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }

    protected void onDestroy() {
        if (TTS != null) {
            TTS.stop();
            TTS.shutdown();
        }

        super.onDestroy();

        mpCaptureText.release();
    }

    protected void onPause() {
        if (TTS != null) {
            TTS.stop();
        }

        super.onPause();
    }
}
