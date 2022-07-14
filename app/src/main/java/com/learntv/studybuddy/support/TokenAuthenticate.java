package com.learntv.studybuddy.support;

import static com.learntv.studybuddy.support.validate.Validating.isValidPhoneNumber;
import static com.learntv.studybuddy.support.validate.Validating.validatePwd;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.learntv.studybuddy.GradesActivity;
import com.learntv.studybuddy.HomeActivity;
import com.learntv.studybuddy.MainActivity;
import com.learntv.studybuddy.SignInActivity;
import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.CommonResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenAuthenticate {
    private String token;
    private Context context;
    private CircularProgressIndicator circularProgress;//not required
    private SignInPost.login signInPostLogin;
    private SignInPost.showErrors signInPostError;//not required
    boolean validate = false;
    private login tokenAuthenticationLogin;

    public void setData(
            String token,
            Context context,
            CircularProgressIndicator circularProgress,
            SignInPost.showErrors signInPostError,
            SignInPost.login signInPostLogin,
            login tokenAuthenticationLogin){
        this.token = token;
        this.context = context;
        this.circularProgress = circularProgress;
        this.signInPostError = signInPostError;
        this.signInPostLogin = signInPostLogin;
        this.tokenAuthenticationLogin = tokenAuthenticationLogin;
    }

    public void setData(String token, Context context, SignInPost.login signInPostLogin, login tokenAuthenticationLogin){
        this.token = token;
        this.context = context;
        this.signInPostLogin = signInPostLogin;
        this.tokenAuthenticationLogin = tokenAuthenticationLogin;
    }

    public void checkToken(){
        (Api.getClient().auhtenticate(
                token
        )).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                CommonResponse checkTokenResponse = response.body();
                if(checkTokenResponse!=null){
                    if (checkTokenResponse.getError()!=null){
                        String errorCode = checkTokenResponse.getError().getStatusCode();
                        signIn();
                    }else {
                        loginToActivity(token);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call,@NonNull  Throwable t) {
                Toast.makeText(context,"Something went wrong please try again later",Toast.LENGTH_LONG).show();
                redirectToHome();
            }
        });
    }

    private void signIn() {
        PrefManager prefManager = new PrefManager(context);
        if (!prefManager.isUserLoggedOut()) {
            if (circularProgress!=null) circularProgress.setVisibility(View.VISIBLE);


            //user's email and password both are saved in preferences
            String savedMobile = prefManager.getMobile();
            String savedPassword = prefManager.getPassword();



            if (isValidPhoneNumber(savedMobile) && validatePwd(savedPassword)) {
                new SignInPost().signInWithServer(savedMobile,savedPassword,signInPostLogin,signInPostError,context,false,circularProgress);
            }


        }else{
            redirectToHome();
        }
    }

    public void redirectToHome(){
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void loginToActivity(String token) {
        tokenAuthenticationLogin.login(token);

    }

    public interface login{
        void login(String token);
    }
}
