package com.learntv.studybuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.LessonResponse;
import com.learntv.studybuddy.support.ShowErrors;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayingVideoActivity extends AppCompatActivity{
    private String line,token,videoUrl;
    ArrayList<String> URL = new ArrayList<>();
    private ExoPlayer player;
    StyledPlayerView playerView;
    private int videoId, quality;
    private final String apiKey="76b3d18521fa7f12d7ea0402214408140c108a17634d97c5";
    private final String apiSecret="v1iMCBWxbq2UE4k/GRWt7xRjMkZGvoCSlZNqu+yC7mDIOp+/2X/6AANpuwaBARkxyEDQzYo0nZrAB5IMLwK6Sw==";
    private LessonResponse lessonResponse;
    private TextView longDesc, heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_video);

        playerView = findViewById(R.id.player_view);
        longDesc = findViewById(R.id.longDesc);
        heading = findViewById(R.id.video_heading);

//        Get video Url
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            videoId = bundle.getInt("videoId");
            token = bundle.getString("token");
            quality = bundle.getInt("quality");
            Log.d("onCreate: ",token);
        }

        if (videoId>=0){
            getLessonData();
        }else{
            pushErrors("0","Sorry we can't play this video");
        }




    }

    private void getLessonData() {
        (Api.getClient().lesson_data(
                apiKey,
                apiSecret,
                token,
                videoId
        )).enqueue(new Callback<LessonResponse>() {
            @Override
            public void onResponse(@NonNull Call<LessonResponse> call,@NonNull Response<LessonResponse> response) {
                lessonResponse = response.body();
                if (lessonResponse!=null){
                    if (lessonResponse.getData()!=null&&lessonResponse.getErrors()==null){
                        videoUrl = lessonResponse.getData().get(0).getVideoUrls().get(quality).getVideoUrl();
                        GetVideoAsync getVideoAsync = new GetVideoAsync();
                        getVideoAsync.executeAsync();
                    }else {
                        if(lessonResponse.getErrors()!=null){
                            pushErrors(lessonResponse.getErrors().getStatusCode(),lessonResponse.getErrors().getDescription());
                        }
                    }
                }else {
                    pushErrors("0","Video Not Found");
                    Log.d("onResponse: ",apiKey);
                    Log.d("onResponse: ",apiSecret);
                    Log.d("onResponse: ",token);
                    Log.d("onResponse: ", String.valueOf(videoId));
                }

            }

            @Override
            public void onFailure(@NonNull Call<LessonResponse> call,@NonNull Throwable t) {
                pushErrors("0","Cannot connect with server");
                Log.d("onResponse: ", String.valueOf(t));
            }
        });
    }

    private void pushErrors(String errorCode,String description) {
        String errors = new ShowErrors().Errors(errorCode,description);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(PlayingVideoActivity.this);
        builder.setMessage(errors);
        builder.setCancelable(false);
        builder.setPositiveButton("Go back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        hideSystemBars hidingStatus = new hideSystemBars();

        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            descShow(false);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else {
            descShow(true);
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
                        while ((line = buffer.readLine()) != null) {
                            AsyncURL.add(line);
                        }

                        buffer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        ;
                    }

                } catch (IOException e) {
                    Log.d("executeAsync: ", e.toString());
                }
                handler.post(()->{
                    URL = AsyncURL;
                    if(!URL.isEmpty()){
                        playVideo(URL);
                    }else{
                        pushErrors("101","Can't play this video");
                    }
                });
            });

        }
    }

    private void descShow(boolean show){
        TextView content = findViewById(R.id.video_player_content);
        if(show){
            content.setVisibility(View.VISIBLE);
            heading.setVisibility(View.VISIBLE);
            longDesc.setVisibility(View.VISIBLE);
        }else{
            content.setVisibility(View.INVISIBLE);
            heading.setVisibility(View.INVISIBLE);
            longDesc.setVisibility(View.INVISIBLE);
        }
    }

    private void playVideo(ArrayList<String> url) {
        heading.setText(lessonResponse.getData().get(0).getHeading());
        longDesc.setText(lessonResponse.getData().get(0).getLongDesc());
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
                    Bundle bundle = new Bundle();
                    bundle.putString("token",token);
                    bundle.putInt("videoId",videoId);
                    intent.putExtras(bundle);
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