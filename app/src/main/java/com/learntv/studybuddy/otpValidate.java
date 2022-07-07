package com.learntv.studybuddy;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.CommonResponse;
import com.learntv.studybuddy.support.ShowErrors;
import com.learntv.studybuddy.support.SignUpPost;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class otpValidate extends AppCompatActivity {
    private TextView countDownTimer, resend, numberText;
    private EditText otpInput;
    public int counter;
    private final String apiKey = "40b5e4a44415a80818d65d4d1417df7e9b822e4db286ed5c";
    private final String apiSecret = "63Srhl71FxVJuj90oJIHbXn7Ryf/sLiFivLw94SYQiC4qDR38dq4TcHTAW0GmJX5i32lLWdC/5+vgroqfda0936V";
    private String mobileNumber = "",username,email,password;
    private CommonResponse otpResponse,otpVerifyResponse;
    private SignUpPost.showErrors signUpPostError;
    private SignUpPost.showSuccess signUpPostSuccess;
    private CountDownTimer timerCD;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_validate);
        numberText = findViewById(R.id.numberText);

        Button verifyBtn = findViewById(R.id.verifyBtn);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            username = bundle.getString("username");
            email = bundle.getString("email");
            password = bundle.getString("password");
            mobileNumber = bundle.getString("contact");
            numberText.setText("OTP has been sent to "+mobileNumber+" number");

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String otpString = otpInput.getText().toString().trim();
                    verifyOtp(otpString);
                }
            });
        }
        countDownTimer = (TextView) findViewById(R.id.counterVal);
        resend = (TextView) findViewById(R.id.resend);
        startRequest();

        otpInput = findViewById(R.id.mobileNumber);
    }

    private void startRequest(){
        resend.setVisibility(View.INVISIBLE);
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
                countDownTimer.setText("Enter OTP within "+String.valueOf(counter)+" seconds");
                counter--;
            }
            @SuppressLint("SetTextI18n")
            public  void onFinish(){
                countDownTimer.setText("Please check your number and click resend");
                resend.setVisibility(View.VISIBLE);
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
        setAlertDialog();
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
                        new SignUpPost().signUpEmailAndMobile(username,email,password,mobileNumber,signUpPostError,signUpPostSuccess);
                    }else {
                        if (otpVerifyResponse.getError()!=null){
                            pushErrors(otpVerifyResponse.getError().getStatusCode(),otpVerifyResponse.getError().getDescription());
                            Log.d("onResponse: ","otpResponseGetError");
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
    }



    public void setAlertDialog() {
        signUpPostError = new SignUpPost.showErrors() {

            @Override
            public void pushError(String error) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(otpValidate.this);
                builder.setMessage(error);
                builder.setCancelable(false);
                builder.setPositiveButton("Go back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.show();
            }
        };


        signUpPostSuccess = new SignUpPost.showSuccess() {

            @Override
            public void pushSuccess(String error) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(otpValidate.this);
                builder.setMessage(error);
                builder.setCancelable(false);
                builder.setPositiveButton("Go Home", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"Your account has been activated.\nSign In and Enjoy!",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        };
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