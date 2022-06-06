package com.learntv.studybuddy;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learntv.studybuddy.adapters.MCQAdapter;

public class MCQActivity extends AppCompatActivity {
    MCQAdapter.RecyclerViewClickListener listener;
    private RecyclerView answersList;
    private MCQAdapter mcqAdapter;
    private TextView countDownTimer;
    public int counter = 60;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq);

        answersList = findViewById(R.id.answersList);
        countDownTimer = findViewById(R.id.countDownTimer);
        setAdapter();
        timer();
    }

    public void timer(){
        new CountDownTimer(60000, 1000){
            public void onTick(long millisUntilFinished){
                countDownTimer.setText(String.valueOf(counter));
                counter--;
            }
            public  void onFinish(){
                countDownTimer.setText("00");
            }
        }.start();
    }

    public void setAdapter(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MCQActivity.this);
        answersList.setLayoutManager(linearLayoutManager);
        mcqAdapter = new MCQAdapter(getApplicationContext());
        answersList.setAdapter(mcqAdapter);
    }




}