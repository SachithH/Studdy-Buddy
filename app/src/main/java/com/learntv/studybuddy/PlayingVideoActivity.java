package com.learntv.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class PlayingVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_video);

//        Get video Url
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String videoUrl = extras.getString("VideoUrl");
        }

        Uri uri = Uri.parse("https://abhiandroid.com//ui/wp-content/uploads/2016/04/videoviewtestingvideo.mp4");
        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoURI(uri);
        videoView.start();
    }
}