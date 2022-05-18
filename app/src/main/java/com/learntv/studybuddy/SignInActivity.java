package com.learntv.studybuddy;

import static com.learntv.studybuddy.PasswordHashing.createHash;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.SignInResponse;
import com.learntv.studybuddy.support.PrefManager;
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
    private CheckBox rememberMe;
    private SignInResponse signUpResponseData;
    private String hashPW;
    private String errors = "Something went wrong. Please try again Later";


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

        //remember me
        PrefManager prefManager = new PrefManager(getApplicationContext());
        if (!prefManager.isUserLoggedOut()) {
            //user's email and password both are saved in preferences
            String savedEmail = prefManager.getEmail();
            String savedPassword = prefManager.getPassword();

            if (validateEmail(savedEmail) && validatePwd(savedPassword)) {
                try {
                    signIn(savedEmail,savedPassword);
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Something Went Wrong Please Try Again Later", Toast.LENGTH_LONG).show();
                }
            }


        }
        //end remember me

//        username and password
        email = findViewById(R.id.emailIn);
        password = findViewById(R.id.passwordIn);
        rememberMe = findViewById(R.id.rememberMe);

        //Sign In Button
        Button signIn = findViewById(R.id.profileSignIn);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailStr = email.getText().toString().trim();
                String passwordStr = password.getText().toString().trim();
                //Create hash password

                // validate the fields and call sign method to implement the api
                if (validateEmail(emailStr) && validatePwd(passwordStr)) {
                    hashPW = createHashPW(emailStr,passwordStr);
                    try {
                        signIn(emailStr,hashPW);
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Something Went Wrong Please Try Again Later", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });




    }



    //    validate email
    private boolean validateEmail(String emailString) {

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
    private boolean validatePwd(String passwordStr) {
        if (0 < passwordStr.length()) {
            return true;
        }else {
            password.setError("Please Fill This");
            password.requestFocus();
            return false;
        }
//        end password validate


    }

    public String createHashPW(String email,String password){
        try {
            return createHash(email,password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return password;
    }

//    Sign In Process
    private void signIn(String email,String password) throws NoSuchAlgorithmException, InvalidKeySpecException {

        (Api.getClient().login(
                email,
                password
        )).enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(@NonNull Call<SignInResponse> call, @NonNull Response<SignInResponse> response) {
                signUpResponseData = response.body();
                if (signUpResponseData != null) {
                    if(signUpResponseData.getSuccess()){
                        if (rememberMe.isChecked()){saveLoginDetails(email,hashPW);}
                        loginToGrades(
                                signUpResponseData.getToken(),
                                email);
                    }else{
                                showErrors();
                        }
                }
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
                showMessages("Something went wrong. Please try again later");

            }

    });
    }

    private void saveLoginDetails(String email, String hashPW) {
        PrefManager prefManager = new PrefManager(getApplicationContext());
        prefManager.saveLoginDetails(email,hashPW);
    }

    private void showErrors() {
           switch (signUpResponseData.getErrorcode()){
               case 100:
                   errors = "Internal server error. Please try again Later";
                   break;
               case 101:
                   errors = "Username or Password incorrect";
                   break;
               case 102:
                   errors = "Please enter email correct and try again";
                   break;
               case 103:
                   errors = "Account not activated";
               case 104:
                   errors = "Login expired. Please Sign In";
               case 105:
                   errors = "Token of session is not provided";
           }
               showMessages(errors);

        }

    private void loginToGrades(String token,String email) {
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

    @Override
    protected void onStart() {
        super.onStart();
        String savedEmail = new PrefManager(getApplicationContext()).getEmail();
        String savedPassword = new PrefManager(getApplicationContext()).getPassword();
        if (savedEmail!=null){
            email.setText(savedEmail);
        }
        if (savedPassword!=null){
            password.setText(savedPassword);
        }

    }

    private void showMessages(String error) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SignInActivity.this);
        builder.setMessage(error);
        builder.setCancelable(true);
        builder.show();

    }
}