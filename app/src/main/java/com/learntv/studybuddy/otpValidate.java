package com.learntv.studybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.CommonResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class otpValidate extends AppCompatActivity {
    private TextView countDownTimer;
    private EditText otpInput;
    public int counter = 60;
    private final String apiKey = "40b5e4a44415a80818d65d4d1417df7e9b822e4db286ed5c";
    private final String apiSecret = "63Srhl71FxVJuj90oJIHbXn7Ryf/sLiFivLw94SYQiC4qDR38dq4TcHTAW0GmJX5i32lLWdC/5+vgroqfda0936V";
    private String mobileNumber = "";
    private CommonResponse otpResponse,otpVerifyResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_validate);

        Button verifyBtn = findViewById(R.id.verifyBtn);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            mobileNumber = bundle.getString("mobile");
            requestOtp(mobileNumber);
            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String otpString = otpInput.getText().toString().trim();
                    verifyOtp(otpString);
                }
            });
        }
        countDownTimer = (TextView) findViewById(R.id.counter);
        timer();
        otpInput = findViewById(R.id.mobileNumber);
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
                    }else {
                        if (otpResponse.getError()!=null){
                            Toast.makeText(getApplicationContext(),otpResponse.getError().getDescription(),Toast.LENGTH_LONG).show();
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
                        finish();
                    }else {
                        if (otpVerifyResponse.getError()!=null){
                            Toast.makeText(getApplicationContext(),otpVerifyResponse.getError().getDescription(),Toast.LENGTH_LONG).show();
                        }
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Failed to verify OTP",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Failed to connect with server",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void timer(){
        new CountDownTimer(60000, 1000){
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished){
                countDownTimer.setText(String.valueOf(counter)+" s");
                counter--;
            }
            @SuppressLint("SetTextI18n")
            public  void onFinish(){
                countDownTimer.setText("00");
            }
        }.start();
    }
}