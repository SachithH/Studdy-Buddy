package com.learntv.studybuddy;


import static com.learntv.studybuddy.PasswordHashing.createHash;
import static com.learntv.studybuddy.support.validate.Validating.validatePwd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.CommonResponse;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends BaseActivity {
    private String mobileNumber;
    private String hashPW,password,repassword;
    private CircularProgressIndicator circularProgress;
    TextInputEditText passwordET,rePasswordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        circularProgress = findViewById(R.id.progress_circular);
        circularProgress.setVisibility(View.INVISIBLE);

        if (getIntent().getExtras()!=null){
            mobileNumber = getIntent().getExtras().getString("mobileNumber");
        }else{
            finish();
        }

        passwordET = (TextInputEditText) findViewById(R.id.password);
        rePasswordET = (TextInputEditText) findViewById(R.id.confirmPassword);
        new setErrors().setErrors();

        MaterialButton enterBtn = (MaterialButton) findViewById(R.id.enterBtn);
        enterBtn.setOnClickListener(view -> {
            password = Objects.requireNonNull(passwordET.getText()).toString().trim();
            repassword = Objects.requireNonNull(rePasswordET.getText()).toString().trim();
            if (password.equals(repassword)&&validatePwd(password)){
                new BackgroundResetPw().executeAsync(password,mobileNumber);
            }else{
                Toast.makeText(getApplicationContext(),"Please enter valid password",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class setErrors implements View.OnFocusChangeListener{
        TextInputLayout passwordTF;
        TextInputLayout rePasswordTF;

        private void setErrors() {
            passwordTF = (TextInputLayout) findViewById(R.id.passwordTF);
            rePasswordTF = (TextInputLayout) findViewById(R.id.rePasswordTF);

//        Password
            passwordET.setOnFocusChangeListener(this);
            rePasswordET.setOnFocusChangeListener(this);
        }


//        onFocusChange Start
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onFocusChange (View view,boolean hasFocus){
            String password = Objects.requireNonNull(passwordET.getText()).toString().trim();
            String repassword = Objects.requireNonNull(rePasswordET.getText()).toString().trim();
            switch (view.getId()) {
                case R.id.password:
                    if (hasFocus) {
                        passwordTF.setHelperTextEnabled(false);
                        passwordTF.setErrorEnabled(false);
                    } else {
                        if (password.length() > 7) {
                            passwordTF.setHelperTextEnabled(false);
                            passwordTF.setErrorEnabled(false);
                        } else {
                            passwordTF.setHelperTextEnabled(false);
                            passwordTF.setHelperTextEnabled(true);
                            if (password.length() > 0) {
                                passwordTF.setError("Password cannot be empty");
                            } else {
                                passwordTF.setError("Password should be between 8 and 64 Characters");
                            }
                        }
                    }
                    break;
                case R.id.confirmPassword:
                    if (hasFocus) {
                        rePasswordTF.setErrorEnabled(false);
                        rePasswordTF.setHelperTextEnabled(true);
                        rePasswordTF.setHelperText("Password should be match");
                    } else {
                        if (password.equals(repassword)) {
                            rePasswordTF.setHelperTextEnabled(false);
                            rePasswordTF.setErrorEnabled(false);
                        } else {
                            rePasswordTF.setErrorEnabled(true);
                            rePasswordTF.setHelperTextEnabled(true);
                            rePasswordTF.setError("Password doesn't match");
                        }

                    }
                default:
                    break;
            }
        }
//        onFocusChange End
    }

    //    Async Task is deprecated
//    Executor for Async
    public class BackgroundResetPw{
        private final Executor executor = Executors.newSingleThreadExecutor();
        private final Handler handler = new Handler(Looper.getMainLooper());

        public void executeAsync (String passwordStr, String contactStr){
            circularProgress.setVisibility(View.VISIBLE);
            executor.execute(()->{
                try {
                    hashPW = createHashPW(contactStr,passwordStr);
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    e.printStackTrace();
                }
                handler.post(()->{
                    circularProgress.setVisibility(View.INVISIBLE);
                    setUpNewPassword(hashPW, contactStr);
                });
            });
        }
    }

    public String createHashPW(String contact,String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return createHash(contact, password);
    }

    private void setUpNewPassword(String password, String mobileNumber){


        final String apiKey = "a3373f7506773a10929a35195c0b27f0530dea0d1c0a442b";
        final String apiSecret = "8iosgUEdR9F3TfQULMn1LBLFOwm7tmKi+reTRQNt69n98LdHqJFDPBKuxEefzJF7pv7e9fax5QeOxEdtnKJJroaj";

        (Api.getClient().request_new_pw(
                mobileNumber,
                password,
                apiKey,
                apiSecret
        )).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call,@NonNull Response<CommonResponse> response) {
                CommonResponse newPwResponse = response.body();
                if (newPwResponse!=null){
                    if (newPwResponse.getData()!=null){
                        requestSuccess();
                    }else{
                        if (newPwResponse.getError()!=null){
                            requestFailed(newPwResponse.getError().getDescription());
                        }else{
                            requestFailed("Something went wrong. Please try again later");
                        }
                    }
                }else{
                    requestFailed("Something went wrong. Please try again later");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call,@NonNull Throwable t) {
                requestFailed("Something went wrong. Please try again later");
            }
        });
    }

    private void requestSuccess() {
        Intent intent = new Intent(ResetPasswordActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    private void requestFailed(String error){
        Toast.makeText(ResetPasswordActivity.this, error, Toast.LENGTH_LONG).show();
    }
}