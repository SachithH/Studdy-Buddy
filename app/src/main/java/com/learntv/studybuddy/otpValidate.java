package com.learntv.studybuddy;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.CommonResponse;
import com.learntv.studybuddy.support.ShowErrors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class otpValidate extends BaseActivity {
    private TextView countDownTimer, resend, numberText;
    private EditText otpInput;
    public int counter;
    private final String apiKey = "40b5e4a44415a80818d65d4d1417df7e9b822e4db286ed5c";
    private final String apiSecret = "63Srhl71FxVJuj90oJIHbXn7Ryf/sLiFivLw94SYQiC4qDR38dq4TcHTAW0GmJX5i32lLWdC/5+vgroqfda0936V";
    private String mobileNumber = "",password;
    private CommonResponse otpResponse,otpVerifyResponse;
    private CountDownTimer timerCD;
    private String deviceId;

    @SuppressLint({"SetTextI18n", "HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_validate);
        numberText = findViewById(R.id.numberText);

        MaterialButton verifyBtn = findViewById(R.id.verifyBtn);

        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            password = bundle.getString("password");
            mobileNumber = bundle.getString("contact");
            numberText.setText("OTP has been sent to "+mobileNumber+" number\nEnter OTP within 60 seconds");
        }else {
            Toast.makeText(this,"Something went wrong please try again later",Toast.LENGTH_SHORT).show();
            finish();
        }
        countDownTimer = (TextView) findViewById(R.id.counterVal);
        resend = (TextView) findViewById(R.id.resend);
        startRequest();

        verifyBtn.setOnClickListener((view)->{
                String otpString = otpInput.getText().toString().trim();
                verifyOtp(otpString);
        });

        otpInput = findViewById(R.id.mobileNumber);
    }

    private void startRequest(){
        counter = 60;
        if (timerCD!=null){
            timerCD.cancel();
        }
        timerCD = new timer().CDTimer;
        timerCD.start();
        requestOtp(mobileNumber);
    }

    private class timer {
        CountDownTimer CDTimer = new CountDownTimer(60000, 1000){
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished){
                countDownTimer.setText("Time : "+ counter +" s");
                counter--;
            }
            @SuppressLint("SetTextI18n")
            public  void onFinish(){
                countDownTimer.setText("Time : 0 s");
                resend.setClickable(true);
                resend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startRequest();
                        Toast.makeText(getApplicationContext(),"Resending OTP Request...",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
    }
    

    private void requestOtp(String mobNumber) {

        (Api.getClient().otpRequest(
                apiKey,
                apiSecret,
                mobNumber
        )).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                otpResponse = response.body();
                if(otpResponse!=null){
                    if (otpResponse.getData()!=null){
                        Toast.makeText(getApplicationContext(),otpResponse.getData().getDescription(),Toast.LENGTH_LONG).show();
                        Log.d("onResponse: ","otpRequestResponse");
                    }else {
                        if (otpResponse.getError()!=null){
                            Toast.makeText(getApplicationContext(),otpResponse.getError().getDescription(),Toast.LENGTH_LONG).show();
                            Log.d("onResponse: ","otpRequestResponseError");
                        }else {
                            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Failed to send otp request",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void verifyOtp(String otpString) {
        Log.d("verifyOtp: ","Verify");
        (Api.getClient().otpVerify(
                apiKey,
                apiSecret,
                mobileNumber,
                otpString
        )).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                otpVerifyResponse = response.body();
                if (otpVerifyResponse!=null){
                    if (otpVerifyResponse.getData()!=null){
                        Toast.makeText(getApplicationContext(),otpVerifyResponse.getData().getDescription(),Toast.LENGTH_LONG).show();
                        Log.d("onResponse: ","otpResponse");
//                        new SignUpPost().signUpMobile(mobileNumber,password,device_id,signUpPostError,signUpPostSuccess);
                        goToPromoCodeActivity();
                    }else {
                        if (otpVerifyResponse.getError()!=null){
                            pushErrors(otpVerifyResponse.getError().getStatusCode(),otpVerifyResponse.getError().getDescription());
                            Log.d("onResponse: ","otpResponseGetError");
                            Log.d("verifyOtp: ",mobileNumber);
                        }
                    }
                }else {
                    pushErrors("0","Failed to verify OTP");
                    Log.d("onResponse: ","OtpResponseFailure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                pushErrors("0","Failed to connect with the server");
            }
        });
    }

    private void goToPromoCodeActivity() {
        Intent intent = new Intent(otpValidate.this,promoCodeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("mobileNo",mobileNumber);
        bundle.putString("password",password);
        bundle.putString("deviceId",deviceId);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void pushErrors(String errorcode, String Description) {
        ShowErrors showErrors = new ShowErrors();
        String errors = showErrors.Errors(errorcode,Description);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(otpValidate.this);
        builder.setMessage(errors);
        builder.setCancelable(false);
        builder.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"You can't go back at this moment",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerCD!=null){
            timerCD.cancel();
        }
    }
}