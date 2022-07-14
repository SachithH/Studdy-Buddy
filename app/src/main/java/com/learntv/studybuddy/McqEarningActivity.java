package com.learntv.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class McqEarningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq_earning);

        TextView earningView = findViewById(R.id.earning);

        int earning = getIntent().getIntExtra("earning",0);

        if (earning>0){
            earningView.setText("You won\n"+String.valueOf(earning)+" Coins");
        }else{
            finish();
        }

    }
}