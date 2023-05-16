package com.example.homepage;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QuerySnapshot;
//
//public class sos extends AppCompatActivity {
//
//    private static final int REQUEST_CALL_PHONE_PERMISSION = 1;
//    private Button sosButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sos);
//
//        sosButton = findViewById(R.id.sos_button);
//        sosButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String phoneNumber = SPUChildSupport.getPhone(getApplicationContext());
//                makeSosCall(phoneNumber);
//            }
//        });
//
//        String parentName = SPUMaster.getParentId(getApplicationContext()); // Replace with the actual parent name
//        getParentPhone(parentName);
//    }
//
//    private void makeSosCall(String phoneNumber) {
//        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setData(Uri.parse("tel:" + phoneNumber));
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
//        } else {
//            startActivity(callIntent);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == REQUEST_CALL_PHONE_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                String phoneNumber = SPUChildSupport.getPhone(getApplicationContext());
//                makeSosCall(phoneNumber);
//            }
//        }
//    }
//
//    private void getParentPhone(String parentName) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("parent_details")
//                .whereEqualTo("parent_name", parentName)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            QuerySnapshot querySnapshot = task.getResult();
//                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
//                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
//                                String phoneNumber = documentSnapshot.getString("phone");
//                                // Use the phoneNumber as needed
//                                Log.d("Phone Number", phoneNumber);
//                                SPUChildSupport.savePhone(getApplicationContext(), phoneNumber);
//                            } else {
//                                // No matching parent with the given name found
//                                Log.d("Phone Number", "No matching parent found");
//                            }
//                        } else {
//                            // Error occurred while querying the database
//                            Log.e("Phone Number", "Error occurred: " + task.getException());
//                        }
//                    }
//                });
//    }
//}


import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class sos extends AppCompatActivity {
    private static final int SOS_SOUND_DURATION = 5000; // Duration of SOS sound in milliseconds
    private static final int SOS_SOUND_DELAY = 200; // Delay between SOS sound beeps in milliseconds

    private Button sosButton;
    private boolean isFlashlightOn = false;
    private SoundPool soundPool;
    private int sosSoundId;
    private AudioManager audioManager;
    private AudioFocusRequest audioFocusRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        sosButton = findViewById(R.id.sos_button);
        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOnFlashlight();
                playSosSound();
            }
        });

        // Initialize SoundPool for playing the SOS sound
        SoundPool.Builder builder = new SoundPool.Builder();
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();
        builder.setAudioAttributes(audioAttributes);
        soundPool = builder.build();
        sosSoundId = soundPool.load(this, R.raw.audio, 1);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
    }

    private void turnOnFlashlight() {
        PackageManager packageManager = getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            try {
                if (!isFlashlightOn) {
                    isFlashlightOn = true;
                    CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
                    if (cameraManager != null) {
                        String cameraId = cameraManager.getCameraIdList()[0];
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            cameraManager.setTorchMode(cameraId, true);
                        }
                    }
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Flashlight is not available on this device.", Toast.LENGTH_SHORT).show();
        }
    }

    private void playSosSound() {
        int result = audioManager.requestAudioFocus(audioFocusChangeListener,
                AudioManager.STREAM_ALARM,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            final Handler handler = new Handler();
            final Runnable playSoundRunnable = new Runnable() {
                @Override
                public void run() {
                    soundPool.play(sosSoundId, 1, 1, 1, 0, 1);
                    handler.postDelayed(this, SOS_SOUND_DELAY);
                }
            };

            handler.postDelayed(playSoundRunnable, SOS_SOUND_DELAY);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.removeCallbacks(playSoundRunnable);
                    releaseAudioFocus();
                    turnOffFlashlight();
                }
            }, SOS_SOUND_DURATION);
        }
    }

    private void turnOffFlashlight() {
        if (isFlashlightOn) {
            isFlashlightOn = false;
            CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
            try {
                if
                (cameraManager != null) {
                    String cameraId = cameraManager.getCameraIdList()[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraId, false);
                    }
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void releaseAudioFocus() {
        if (audioManager != null && audioFocusRequest != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioManager.abandonAudioFocusRequest(audioFocusRequest);
            }
            audioFocusRequest = null;
        }
    }

    private final AudioManager.OnAudioFocusChangeListener audioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        releaseAudioFocus();
                    }
                }
            };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        releaseAudioFocus();
    }
}