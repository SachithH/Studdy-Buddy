package com.learntv.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class WalletActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        overridePendingTransition(R.anim.slide_up,R.anim.slide_up_pre_activity);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down_pre_activity,R.anim.slide_down);
    }
}