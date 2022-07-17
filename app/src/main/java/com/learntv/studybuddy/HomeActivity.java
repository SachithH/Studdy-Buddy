package com.learntv.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.learntv.studybuddy.retrofit.SignIn;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Start Sign Up Activity
        Button signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });


        //Start Sign In Activity
        Button signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        //        End Sign Up from Sign Up Button

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus){
//            decorView.setSystemUiVisibility(hidingStatus.hideSystemBars());
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


}