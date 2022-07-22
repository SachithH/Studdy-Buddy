package com.learntv.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.learntv.studybuddy.support.PrefManager;
import com.learntv.studybuddy.support.TokenAuthenticate;

public class SelectRoomsActivity extends BaseActivity {
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_rooms);

        checkToken();

        MaterialButton gradesBtn = findViewById(R.id.learningRoom);
        gradesBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SelectRoomsActivity.this,GradesActivity.class);
            intent.putExtra("token",token);
            startActivity(intent);
        });
    }

    private void checkToken() {
        PrefManager prefManager = new PrefManager(getApplicationContext());
        token = prefManager.getToken();
        if(token.isEmpty()){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }else{
            TokenAuthenticate tokenAuthenticate = new TokenAuthenticate();
            tokenAuthenticate.setData(token,getApplicationContext(),null, null);
            tokenAuthenticate.checkToken();
        }
    }

}