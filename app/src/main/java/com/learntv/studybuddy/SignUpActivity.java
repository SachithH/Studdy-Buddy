package com.learntv.studybuddy;

import static android.content.ContentValues.TAG;
import static com.learntv.studybuddy.PasswordHashing.createHash;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.SignUpResponse;
import com.learntv.studybuddy.support.hideSystemBars;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private hideSystemBars hidingStatus;
    private View decorView;
    private EditText USignUpUsername;
    private EditText USignUpEmail;
    private EditText USignUpPassword;
    private EditText USignUpRetypePassword;
    private EditText USignUpContacts;
    private String hashPW;
    private SignUpResponse signUpResponseData;
    private GoogleSignInClient mGoogleSignInClient;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        USignUpUsername = findViewById(R.id.SignUpUsername);
        USignUpEmail = findViewById(R.id.SignUpEmail);
        USignUpPassword = findViewById(R.id.SignUpPassword);
        USignUpRetypePassword = findViewById(R.id.SignUpRetypePassword);
        USignUpContacts = findViewById(R.id.SignUpContacts);

        //hide status bar
        hidingStatus = new hideSystemBars();
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
            public void onSystemUiVisibilityChange(int visibility){
                if (visibility==0){
                    decorView.setSystemUiVisibility(hidingStatus.hideSystemBars());
                }
            }
        });
//        end hide status bar

//        Sign Up From Sign Up Button
        Button signUpBtn = findViewById(R.id.SignUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String SignUpUsername = USignUpUsername.getText().toString().trim();
                String SignUpEmail = USignUpEmail.getText().toString().trim();
                String SignUpPassword = USignUpPassword.getText().toString().trim();
                String SignUpRetypePassword = USignUpRetypePassword.getText().toString().trim();
                int SignUpContacts = 0123;
                try {
                    SignUpContacts = Integer.parseInt(USignUpContacts.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    if (!(validate()&&validateEmail()&&validatePassword()&&matchPW())){
                        USignUpContacts.setError("Please enter valid number");
                    }

                }

                try {
                    signUp(SignUpUsername,SignUpEmail,SignUpPassword, SignUpContacts);
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    e.printStackTrace();
                    showMessages("Sorry, we are unable to complete the sign up process now. Make sure you are connect with internet and try again later.");
                }
            }
        });


    }





    //    SIGN UP PROCESS
    private void signUp(String signUpUsername, String signUpEmail, String signUpPassword, int signUpContacts) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (validate()&&matchPW()) {

            try {
                hashPW = createHash(signUpEmail, signUpPassword);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
                showMessages("Something went wrong. Please try again later!");
            }


            (Api.getClient().register(
                    signUpEmail,
                    hashPW,
                    signUpUsername,
                    signUpContacts
            )).enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                    signUpResponseData = response.body();
                    if (signUpResponseData.getSuccess()) {
                        showMessages(signUpResponseData.getMessage());
                    } else {
                        showMessages(signUpResponseData.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable t) {
                    StackTraceElement[] stktrace
                            = t.getStackTrace();
                    for (int i = 0; i < stktrace.length; i++) {
                        Log.d("response", "Index " + i
                                + " of stack trace"
                                + " array contains = "
                                + stktrace[i].toString());
                    }
                    showMessages("Sorry, we are unable to complete the sign up process now. Make sure you are connect with internet and try again later.");

                }

            });
        }
    }
//    END SIGN UP PROCESS

    private void showMessages(String message) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SignUpActivity.this);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();

    }

    private boolean validatePassword() {
        int passwordLength = USignUpPassword.getText().toString().trim().length();
        if (7>=passwordLength){
            USignUpPassword.setError("Password must be at least 8 characters long.");
            USignUpPassword.requestFocus();
        }

        return !(7 >=passwordLength);
    }

    private boolean matchPW() {
        String password = USignUpPassword.getText().toString().trim();
        String repassword = USignUpRetypePassword.getText().toString().trim();

        if (password.matches(repassword)){
            return true;
        }else {
            USignUpPassword.setError("Password doesn't match");
            USignUpRetypePassword.setError("Password doesn't match");
            USignUpRetypePassword.requestFocus();
            return false;
        }
    }

    //    validate email
    private boolean validateEmail() {
        String emailString = USignUpEmail.getText().toString().trim();

        if (emailString.isEmpty() || !isValidEmail(emailString)){
            USignUpEmail.setError(" Email must be a valid email address ");
            USignUpEmail.requestFocus();
            return false;
        }else {
            return true;
        }
    }

    private boolean isValidEmail(String emailString) {
        return !TextUtils.isEmpty(emailString) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches();
    }
//   end email validate

    private boolean validate() {
        boolean isUsernameEmpty = 0<USignUpUsername.getText().toString().trim().length();
        boolean isEmailEmpty = validateEmail();
        boolean isPasswordEmpty = validatePassword();
        boolean isPasswordMatch = matchPW();
        boolean isContactsEmpty = 10==USignUpContacts.getText().toString().trim().length();

        if (!isContactsEmpty){
            USignUpContacts.setError("Please Enter Valid Number");
            USignUpContacts.requestFocus();
        }
        if (!isPasswordEmpty){
            USignUpPassword.setError("Password is required");
            USignUpPassword.requestFocus();
        }
        if (!isEmailEmpty){
            USignUpEmail.setError("Email is required");
            USignUpEmail.requestFocus();
        }
        if (!isUsernameEmpty){
            USignUpUsername.setError("Username is required");
            USignUpUsername.requestFocus();
        }



        return isUsernameEmpty && isEmailEmpty && isPasswordEmpty && isPasswordMatch && isContactsEmpty;

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            decorView.setSystemUiVisibility(hidingStatus.hideSystemBars());
        }
    }
}