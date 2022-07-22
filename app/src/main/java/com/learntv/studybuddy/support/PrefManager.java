package com.learntv.studybuddy.support;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    Context context;

    public PrefManager(Context context){
        this.context = context;
    }

    public void saveLoginDetails(String mobile,String password){
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Mobile",mobile);
        editor.putString("Password",password);
        editor.apply();
    }

    public void saveLoginToken(String token){
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Token",token);
        editor.apply();
    }

    public String getMobile() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Mobile","");
    }

    public String getPassword(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Password","");
    }

    public String getToken(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Token","");
    }

    public boolean isUserLoggedOut() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        boolean isEmailEmpty = sharedPreferences.getString("Mobile", "").isEmpty();
        boolean isPasswordEmpty = sharedPreferences.getString("Password", "").isEmpty();
        return isEmailEmpty || isPasswordEmpty;
    }

    public void saveTheme(int id, int theme){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppTheme",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Theme",theme);
        editor.putInt("Id",id);
        editor.apply();
    }

    public int getTheme(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppTheme",Context.MODE_PRIVATE);
        return sharedPreferences.getInt("Theme",0);
    }

    public int getThemeId(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppTheme",Context.MODE_PRIVATE);
        return sharedPreferences.getInt("Id",0);
    }
}
