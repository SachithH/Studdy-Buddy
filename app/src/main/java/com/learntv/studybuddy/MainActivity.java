package com.learntv.studybuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.learntv.studybuddy.support.PrefManager;
import com.learntv.studybuddy.support.SignInPost;
import com.learntv.studybuddy.support.TokenAuthenticate;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.crypto.Cipher;

public class MainActivity extends AppCompatActivity {
    private Handler handler;
    private SignInPost.login signInPostLogin;
    boolean validate = false;
    private String token;
    private TokenAuthenticate.login tokenAuthenticateLogin;
    private CircularProgressIndicator circularProgressIndicator;
    private SignInPost.showErrors signInPostError;

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

    }



    private void checkToken() {
        circularProgressIndicator.setVisibility(View.VISIBLE);
        PrefManager prefManager = new PrefManager(getApplicationContext());
        token = prefManager.getToken();
        if(token.isEmpty()){
            token = "notToken";
        }
        setAction();
        TokenAuthenticate tokenAuthenticate = new TokenAuthenticate();
        tokenAuthenticate.setData(token,getApplicationContext(),signInPostLogin, tokenAuthenticateLogin);
        tokenAuthenticate.checkToken();
    }

    public void setAction(){
        signInPostLogin = new SignInPost.login(){

            @Override
            public void login(String token, String email) {
                Intent intent = new Intent(getApplicationContext(),GradesActivity.class);
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
                Intent intent = new Intent(getApplicationContext(),GradesActivity.class);
                intent.putExtra("token",token);
                startActivity(intent);
                Log.d("loginToGrades: ",token);
                finish();
                circularProgressIndicator.setVisibility(View.INVISIBLE);
            }
        };
    }
}