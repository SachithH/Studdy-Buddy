package com.learntv.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.CommonResponse;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordRecoveryCode extends BaseActivity {
    private String mobileNumber;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery_code);

        if (getIntent().getExtras()!=null) mobileNumber = getIntent().getExtras().getString("mobileNumber");


        MaterialButton enterBtn = findViewById(R.id.enterBtn);
        enterBtn.setOnClickListener(view -> {
            TextInputEditText recoveryOtpInput = findViewById(R.id.recoveryCodeOtp);
            String recoveryOtp = Objects.requireNonNull(recoveryOtpInput.getText()).toString();
            if (!recoveryOtp.equals("")){
                codeVerification(recoveryOtp);
            }
        });

    }

    private void codeVerification(String recoveryOtp) {
        final String apiKey = "40b5e4a44415a80818d65d4d1417df7e9b822e4db286ed5c";
        final String apiSecret = "63Srhl71FxVJuj90oJIHbXn7Ryf/sLiFivLw94SYQiC4qDR38dq4TcHTAW0GmJX5i32lLWdC/5+vgroqfda0936V";

        (Api.getClient().send_recovery_otp(
                mobileNumber,
                recoveryOtp,
                apiKey,
                apiSecret
        )).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call,@NonNull Response<CommonResponse> response) {
                CommonResponse verifyResponse = response.body();
                if (verifyResponse != null) {
                    if (verifyResponse.getData() != null && verifyResponse.getError() == null) {
                        requestSuccess(verifyResponse.getData().getDescription(),mobileNumber);
                    } else {
                        if (verifyResponse.getError() != null) {
                            requestFailed(verifyResponse.getError().getDescription());
                        } else {
                            requestFailed("Something went wrong. Please try again later");
                        }
                    }
                }else{
                    requestFailed("Something went wrong. Please try again later");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call,@NonNull Throwable t) {
                Toast.makeText(PasswordRecoveryCode.this, "Please check your internet connection and try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void requestSuccess(String description, String mobileNumber) {
        Toast.makeText(PasswordRecoveryCode.this, description, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(PasswordRecoveryCode.this,ResetPasswordActivity.class);
        intent.putExtra("mobileNumber",mobileNumber);
        startActivity(intent);
    }

    private void requestFailed(String error){
        Toast.makeText(PasswordRecoveryCode.this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}