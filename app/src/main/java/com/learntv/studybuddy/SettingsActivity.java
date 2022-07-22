package com.learntv.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.google.android.material.button.MaterialButton;
import com.learntv.studybuddy.support.PrefManager;

public class SettingsActivity extends BaseActivity {
    PrefManager prefManager;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        overridePendingTransition(R.anim.slide_up,R.anim.slide_up_pre_activity);

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
        logoutBtn.setOnClickListener(view-> logout());
        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        prefManager = new PrefManager(getApplicationContext());
        int themeId;
        if (prefManager.getThemeId()==0){
            themeId = R.id.blue;
        }else{
            themeId = prefManager.getThemeId();
        }

        radioGroup.check(themeId);
        radioGroup.setOnCheckedChangeListener((group,checkedId)->{
            switch (checkedId) {
                case R.id.light:
                    prefManager.saveTheme(checkedId,R.style.Theme_StudyBuddy_Light);
                    break;
                case R.id.dark:
                    prefManager.saveTheme(checkedId,R.style.Theme_StudyBuddy_Dark);
                    break;
                default:
                    prefManager.saveTheme(checkedId,0);
            }
            Intent intent = new Intent(SettingsActivity.this,SelectRoomsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down_pre_activity,R.anim.slide_down);
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