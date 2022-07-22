package com.learntv.studybuddy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.learntv.studybuddy.support.PrefManager;

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PrefManager preferenceManager = new PrefManager(getApplicationContext());
        if (preferenceManager.getTheme()!=0){
            setTheme(preferenceManager.getTheme());
        }
        super.onCreate(savedInstanceState);
    }
}
