package com.learntv.studybuddy.support;

import android.util.Log;

import androidx.annotation.NonNull;

import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.SignUpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpPost {
    private SignUpResponse signUpResponseData;
    private final String apiKey = "a3373f7506773a10929a35195c0b27f0530dea0d1c0a442b";
    private final String apiSecret = "8iosgUEdR9F3TfQULMn1LBLFOwm7tmKi+reTRQNt69n98LdHqJFDPBKuxEefzJF7pv7e9fax5QeOxEdtnKJJroaj";
    private showErrors signUpPostError;
    private showSuccess signUpPostSuccess;


    //    SIGN UP PROCESS
    public void signUpMobile(
            String contact,
            String password,
            String deviceId,
            String promoCode,
            showErrors signUpPostError,
            showSuccess signUpPostSuccess

    ){
        this.signUpPostError = signUpPostError;
        this.signUpPostSuccess = signUpPostSuccess;

        (Api.getClient().registerMobile(
                contact,
                password,
                deviceId,
                promoCode,
                apiKey,
                apiSecret
        )).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(@NonNull Call<SignUpResponse> call, @NonNull Response<SignUpResponse> response) {
                signUpResponseData = response.body();
                if (signUpResponseData != null) {
                    if (signUpResponseData.getError()==null){
                        pushSuccess("Your account has been successfully created");
                    }else{
                        pushErrors(signUpResponseData.getError().getStatusCode(),signUpResponseData.getError().getDescription());
                        Log.d("onResponse: ",signUpResponseData.getError().getStatusCode());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SignUpResponse> call, @NonNull Throwable t) {
                StackTraceElement[] stktrace
                        = t.getStackTrace();
                for (int i = 0; i < stktrace.length; i++) {
                    Log.d("response", "Index " + i
                            + " of stack trace"
                            + " array contains = "
                            + stktrace[i].toString());
                }
                pushErrors("0","Sorry, we are unable to complete the sign up process now. Make sure you are connect with internet and try again later.");

            }

        });
    }
//    END SIGN UP PROCESS

    public void pushErrors(String errorCode,String description) {
        Log.d("pushErrors: ",errorCode);
        String errors = new ShowErrors().Errors(errorCode,description);
        Log.d("pushErrors: ",errors);
        signUpPostError.pushError(errors);
    }

    public interface showErrors{
        void pushError(String error);
    }

    public void pushSuccess(String msg){
        signUpPostSuccess.pushSuccess(msg);
    }

    public interface showSuccess{
        void pushSuccess(String msg);
    }
}
