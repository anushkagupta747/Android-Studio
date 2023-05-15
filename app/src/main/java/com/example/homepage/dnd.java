package com.example.homepage;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class dnd extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;
    private TextView videoDescription;
    private TextView dosAndDonts;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dnd);

        videoView = findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoDescription = findViewById(R.id.videoDescription);
        videoDescription.setText("Awareness Video You Must Watch!!");

        dosAndDonts = findViewById(R.id.dosAndDonts);
        dosAndDonts.setText("\n\nDos:\n- Stick with a trusted adult\n- Be cautious with personal information\n- If any Emergency Use SOS Button\n- Don't: \n Don't accept gifts or treats\n- Don't approach unfamiliar vehicles or enter homes");

        Button playVideoButton = findViewById(R.id.playVideoButton);
        playVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo();
            }
        });
    }

    private void playVideo() {
        // Replace the path with the path of your local video file
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        videoView.start();
    }
}
