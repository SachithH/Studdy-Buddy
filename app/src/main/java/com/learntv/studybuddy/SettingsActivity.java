package com.learntv.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;
import com.learntv.studybuddy.support.PrefManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageButton messageBtn = findViewById(R.id.messageBtn);

        messageBtn.setOnClickListener(view->{
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:"+"0756814424"));
            startActivity(sendIntent);
        });

        ImageButton messengerBtn = findViewById(R.id.messengerBtn);
        messengerBtn.setOnClickListener(view->{
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb-messenger://user/1"));
                startActivity(intent);
            } catch (Exception e) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.me/fb"));
                startActivity(intent);
            }
        });
        ImageButton whatsappBtn = findViewById(R.id.whatsappBtn);
        whatsappBtn.setOnClickListener(view->{
            String url = "https://api.whatsapp.com/send?phone="+"+94 713986684";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        MaterialButton logoutBtn = findViewById(R.id.logout);
        logoutBtn.setOnClickListener(view->{
            logout();
        });
    }

    private void logout() {
        PrefManager prefManager = new PrefManager(getApplicationContext());
        prefManager.saveLoginToken("");
        prefManager.saveLoginDetails(prefManager.getMobile(), "");
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}