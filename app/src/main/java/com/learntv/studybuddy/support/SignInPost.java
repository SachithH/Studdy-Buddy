package com.learntv.studybuddy.support;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.learntv.studybuddy.SignInActivity;
import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.SignIn;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInPost {
    private final String apiKey = "be2f97570be40d5595fddaa64995b6534e6bae5ba9e86ed0";
    private final String apiSecret = "lu55mgL5sIuDNcxCfXOkydElrfr6ehxyhrNsB8aBqe0ASPIX9XB6c5k8+4NfV15SMv0aipGd0gtzwbrDEqVf3T4A";
    private SignIn signUpResponse;
    private login signInPostLogin;
    private showErrors signInPostError;
    private Context context;
    private boolean rememberMe = false;
    private CircularProgressIndicator circularProgress;


    //    Sign In Process
    public void signInWithServer(
            String email,
            String password,
            login signInPostLogin,
            showErrors signInPostError,
            Context context,
            boolean rememberMeIn,
            CircularProgressIndicator circularProgress){
        this.context = context;
        this.rememberMe = rememberMeIn;
        this.signInPostLogin = signInPostLogin;
        this.signInPostError = signInPostError;
        this.circularProgress = circularProgress;

        if (circularProgress!=null) circularProgress.setVisibility(View.VISIBLE);
        Log.d("signInWithServer: ",password);
        (Api.getClient().login(
                email,
                password,
                apiKey,
                apiSecret
        )).enqueue(new Callback<SignIn>() {
            @Override
            public void onResponse(@NonNull Call<SignIn> call, @NonNull Response<SignIn> response) {
                signUpResponse = response.body();
                if (signUpResponse != null) {
                    if (circularProgress!=null) circularProgress.setVisibility(View.INVISIBLE);
                    if(signUpResponse.getStatus().equals("success")){
                        if (rememberMe){
                            saveLoginDetails(email,password);
                        }
                        loginToGrades(
                                signUpResponse.getData().getToken(),
                                email);
                    }else{
                        if (signUpResponse.getError()!=null){
                            pushErrors(signUpResponse.getError().getStatusCode(),signUpResponse.getError().getDescription());
                        }else{
                            pushErrors("0","Something went wrong!");
                        }

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SignIn> call, @NonNull Throwable t) {
                StackTraceElement[] stktrace
                        = t.getStackTrace();
                for (int i=0; i<stktrace.length; i++){
                    Log.d("response", "Index " + i
                            + " of stack trace"
                            + " array contains = "
                            + stktrace[i].toString());
                }
                pushErrors("0","Something went wrong. Check your internet connection and try again");

            }

        });
    }

    private void saveLoginDetails(String email, String hashPW) {
        PrefManager prefManager = new PrefManager(context);
        prefManager.saveLoginDetails(email,hashPW);
    }

    public void loginToGrades(String token, String email) {
        PrefManager prefManager = new PrefManager(context);
        prefManager.saveLoginToken(token);
        signInPostLogin.login(token,email);

    }

    public interface login{
        void login(String token,String email);
    }

    public void pushErrors(String errorCode,String description) {
        Log.d("pushErrors: ",errorCode);
        String errors = new ShowErrors().Errors(errorCode,description);
        Log.d("pushErrors: ",errors);
        if (signInPostError!=null)signInPostError.pushError(errors);
    }

    public interface showErrors{
        void pushError(String error);
    }
}
