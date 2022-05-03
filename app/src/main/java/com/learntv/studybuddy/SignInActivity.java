package com.learntv.studybuddy;

import static com.learntv.studybuddy.PasswordHashing.createHash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.SignInResponse;
import com.learntv.studybuddy.support.hideSystemBars;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private hideSystemBars hidingStatus;
    private View decorView;
    private EditText email;
    private EditText password;
    private SignInResponse signInResponseData;
    private String hashPW;
    private String errors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        hidingStatus = new hideSystemBars();
        //hide status bar
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
            public void onSystemUiVisibilityChange(int visibility){
                if (visibility==0){
                    decorView.setSystemUiVisibility(hidingStatus.hideSystemBars());
                }
            }
        });
//        End of hide status bar

//        username and password
        email = findViewById(R.id.emailIn);
        password = findViewById(R.id.passwordIn);

        //Sign In Button
        Button signIn = findViewById(R.id.profileSignIn);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailStr = email.getText().toString().trim();
                String passwordStr = password.getText().toString().trim();
                // validate the fields and call sign method to implement the api
                if (validateEmail() && validatePwd()) {
                    try {
                        signIn(emailStr,passwordStr);
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                        e.printStackTrace();
                    }
                }

            }
        });




    }



    //    validate email
    private boolean validateEmail() {
        String emailString = email.getText().toString().trim();

        if (emailString.isEmpty() || !isValidEmail(emailString)){
            email.setError("Email is not valid");
            email.requestFocus();
            return false;
        }else {
            return true;
        }
    }

    private boolean isValidEmail(String emailString) {
        return !TextUtils.isEmpty(emailString) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches();
    }
//   end email validate

//    password validate
    private boolean validatePwd() {
        if (0 < password.getText().toString().trim().length()) {
            return true;
        }else {
            password.setError("Please Fill This");
            password.requestFocus();
            return false;
        }
//        end password validate


    }

//    Sign In Process
    private void signIn(String email,String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog

        try {
            hashPW = createHash(email,password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }


        (Api.getClient().login(
                email,
                hashPW
        )).enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                signInResponseData = response.body();
                if(signInResponseData.getSuccess()){
                    loginToGrades(signInResponseData.getToken());
                }else{
                            showErrors();
                    }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                StackTraceElement[] stktrace
                        = t.getStackTrace();
                for (int i=0; i<stktrace.length; i++){
                Log.d("response", "Index " + i
                        + " of stack trace"
                        + " array contains = "
                        + stktrace[i].toString());
                }
                Toast.makeText(getApplicationContext(),"Something went wrong. Please try again Later",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }

    });
    }

    private void showErrors() {
           switch (signInResponseData.getErrorcode()){
               case 100:
                   errors = "Something went wrong. Please try again Later";
                   break;
               case 101:
                   errors = "Username not found";
                   break;
               case 102:
                   errors = "Email validation error";
                   break;
               case 103:
                   errors = "Account not activated";
               case 104:
                   errors = "Login expired. Please Sign In";
               case 105:
                   errors = "Token of session is not provided";
           }
               Toast.makeText(getApplicationContext(),errors,Toast.LENGTH_SHORT).show();

        }

    private void loginToGrades(String token) {
        Intent intent = new Intent(getApplicationContext(),GradesActivity.class);
        intent.putExtra("token",token);
        startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            decorView.setSystemUiVisibility(hidingStatus.hideSystemBars());
        }
    }
}