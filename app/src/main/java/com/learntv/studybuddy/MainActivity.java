package com.learntv.studybuddy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.learntv.studybuddy.support.PrefManager;
import com.learntv.studybuddy.support.SignInPost;
import com.learntv.studybuddy.support.TokenAuthenticate;

public class MainActivity extends BaseActivity {
    private Handler handler;
    private SignInPost.login signInPostLogin;
    boolean validate = false;
    private String token;
    private TokenAuthenticate.login tokenAuthenticateLogin;
    private CircularProgressIndicator circularProgressIndicator;
    private SignInPost.showErrors signInPostError;
    private String android_id;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circularProgressIndicator = findViewById(R.id.progress_circular_splash);

        circularProgressIndicator.setVisibility(View.INVISIBLE);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkToken();
            }
        },3000);

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("onCreate: ",android_id);

    }



    private void checkToken() {
        circularProgressIndicator.setVisibility(View.VISIBLE);
        PrefManager prefManager = new PrefManager(getApplicationContext());
        token = prefManager.getToken();
        if(token.isEmpty()){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }else{
            setAction();
            TokenAuthenticate tokenAuthenticate = new TokenAuthenticate();
            tokenAuthenticate.setData(token,getApplicationContext(),signInPostLogin, tokenAuthenticateLogin);
            tokenAuthenticate.checkToken();
        }
    }

    public void setAction(){
        signInPostLogin = new SignInPost.login(){

            @Override
            public void login(String token, String email) {
                Intent intent = new Intent(getApplicationContext(),SelectRoomsActivity.class);
                intent.putExtra("token",token);
                startActivity(intent);
                Log.d("loginToGrades: ",token);
                finish();
                circularProgressIndicator.setVisibility(View.INVISIBLE);
            }
        };

        signInPostError = new SignInPost.showErrors(){

            @Override
            public void pushError(String error) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        };

        tokenAuthenticateLogin = new TokenAuthenticate.login(){
            @Override
            public void login(String token) {
                Intent intent = new Intent(getApplicationContext(),SelectRoomsActivity.class);
                intent.putExtra("token",token);
                startActivity(intent);
                Log.d("loginToGrades: ",token);
                finish();
                circularProgressIndicator.setVisibility(View.INVISIBLE);
            }
        };
    }
}