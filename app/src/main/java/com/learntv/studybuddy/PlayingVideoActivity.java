package com.learntv.studybuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.learntv.studybuddy.support.hideSystemBars;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlayingVideoActivity extends AppCompatActivity{
    String videoUrl;
    String line;
    ArrayList<String> URL = new ArrayList<>();
    private ExoPlayer player;
    StyledPlayerView playerView;
    private hideSystemBars hidingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_video);

        playerView = findViewById(R.id.player_view);

//        Get video Url
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            videoUrl = extras.getString("VideoUrl");
        }

        GetVideoAsync getVideoAsync = new GetVideoAsync();
        getVideoAsync.executeAsync();


    }



    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        hidingStatus = new hideSystemBars();

        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player!=null){
            player.stop();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player!=null){
            player.stop();
        }
    }

    //    Async Task is deprecated
//    Executor for Async
    public class GetVideoAsync{
        private final Executor executor = Executors.newSingleThreadExecutor();
        private final Handler handler = new Handler(Looper.getMainLooper());

        public void executeAsync (){
            executor.execute(()->{
                ArrayList<String> AsyncURL = new ArrayList<>();
                try {
                    InputStream inputStream = new URL(videoUrl).openStream();
                    try {
                        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
                        while ((line = buffer.readLine())!=null){
                            AsyncURL.add(line);
                        }

                        buffer.close();
                    } catch (IOException e) {
                        e.printStackTrace();;
                    }

                } catch (IOException e) {
                    Log.d("executeAsync: ",e.toString());
                }
                handler.post(()->{
                    URL = AsyncURL;
                    if(!URL.isEmpty()){
                        playVideo(URL);
                    }else{
                        showMessage(Environment.getExternalStorageDirectory().toString()+"/2011");
                    }
                });
            });

        }
    }

    private void showMessage(String error) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(PlayingVideoActivity.this);
        builder.setMessage(error);
        builder.setCancelable(false);
        builder.setPositiveButton("Go back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }

    private void playVideo(ArrayList<String> url) {
        List<MediaItem> items = new ArrayList<>();
        player = new ExoPlayer.Builder(PlayingVideoActivity.this).build();
        playerView.setPlayer(player);
        playerView.setKeepScreenOn(true);

        for (int i=0; i<url.size(); i++){
            Uri uri = Uri.parse(url.get(i));
            items.add(MediaItem.fromUri(uri));
        }
        player.setMediaItems(items);
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == player.STATE_ENDED){
                    Intent intent = new Intent(getApplicationContext(),MCQActivity.class);
                    startActivity(intent);
                    finish();
                }
                Player.Listener.super.onPlaybackStateChanged(playbackState);
            }
        });

        player.prepare();
        player.play();
    }


}