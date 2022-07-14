package com.learntv.studybuddy.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.learntv.studybuddy.HomeActivity;
import com.learntv.studybuddy.R;
import com.learntv.studybuddy.SettingsActivity;
import com.learntv.studybuddy.WalletActivity;

public class BottomNavigation {
    @SuppressLint("NonConstantResourceId")
    public static void bottomNavigationFunction(Context context, int id){
        switch(id) {
            case R.id.homeBottom:
                Intent home = new Intent(context, HomeActivity.class);
                home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(home);
                break;
            case R.id.walletBottom:
                Intent wallet = new Intent(context, WalletActivity.class);
                wallet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(wallet);
                break;
            case R.id.setting:
                Intent setting = new Intent(context, SettingsActivity.class);
                setting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(setting);
                break;
            default:
                break;


        }
    }
}
