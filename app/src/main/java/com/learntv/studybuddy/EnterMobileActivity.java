package com.learntv.studybuddy;

import static com.learntv.studybuddy.support.validate.Validating.isValidPhoneNumber;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class EnterMobileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mobile);


        MaterialButton mobileNumberBtn = findViewById(R.id.enterBtn);
        mobileNumberBtn.setOnClickListener(view -> {
            TextInputEditText mobileNumberInput = findViewById(R.id.mobileNumber);
            String mobileNumber = Objects.requireNonNull(mobileNumberInput.getText()).toString();
            if (isValidPhoneNumber(mobileNumber)){
                requestRecoveryCode(mobileNumber);
            }
        });

    }

    private void requestRecoveryCode(String mobileNumber) {
        final String apiKey = "40b5e4a44415a80818d65d4d1417df7e9b822e4db286ed5c";
        final String apiSecret = "63Srhl71FxVJuj90oJIHbXn7Ryf/sLiFivLw94SYQiC4qDR38dq4TcHTAW0GmJX5i32lLWdC/5+vgroqfda0936V";

        (Api.getClient().request_recovery(
                mobileNumber,
                apiKey,
                apiSecret
        )).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                CommonResponse requestResponse = response.body();
                if (requestResponse != null) {
                    if (requestResponse.getData() != null && requestResponse.getError() == null) {
                        Toast.makeText(EnterMobileActivity.this, requestResponse.getData().getDescription(), Toast.LENGTH_LONG).show();
                        requestSuccess(mobileNumber);
                    } else {
                        if (requestResponse.getError() != null) {
                            requestFailed(requestResponse.getError().getDescription());
                        } else {
                            requestFailed("Something went wrong. Please try again later");                        }
                    }
                }else{
                    requestFailed("Something went wrong. Please try again later");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                Log.d("onFailure: ",t.toString());
                Toast.makeText(EnterMobileActivity.this, "Please check your internet connection and try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void requestSuccess(String mobileNumber) {
        Intent intent = new Intent(EnterMobileActivity.this,PasswordRecoveryCode.class);
        intent.putExtra("mobileNumber",mobileNumber);
        startActivity(intent);
    }

    private void requestFailed(String error){
        Toast.makeText(EnterMobileActivity.this, error, Toast.LENGTH_LONG).show();
    }
}