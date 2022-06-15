package com.learntv.studybuddy;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.GoogleApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleLogin extends AppCompatActivity {

    private String url;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.google.com");
//        signInWithServer();
    }

//    private void signInWithServer() {
//        (Api.getClient().googleApi(
//        )).enqueue(new Callback<GoogleApi>() {
//
//
//            @Override
//            public void onResponse(Call<GoogleApi> call, Response<GoogleApi> response) {
//                GoogleApi googleApi = response.body();
//                url = googleApi.getUrl();
//                Log.d("onResponse: ", url);
//                if (url != null){
//                    webView.loadUrl("");
//               }
//            }
//
//            @Override
//            public void onFailure(Call<GoogleApi> call, Throwable t) {
//
//            }
//        });
//    }
}