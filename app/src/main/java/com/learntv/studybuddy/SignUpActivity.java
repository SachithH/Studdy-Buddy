package com.learntv.studybuddy;

import static com.learntv.studybuddy.PasswordHashing.createHash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.SignUpResponse;
import com.learntv.studybuddy.support.hideSystemBars;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    private CircularProgressIndicator circularProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        circularProgress = findViewById(R.id.progress_circular);
        circularProgress.setVisibility(View.INVISIBLE);



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
                String SignUpContacts = USignUpContacts.getText().toString().trim();

                BackgroundSignUp backgroundSignUp = new BackgroundSignUp();
                if (validate()){
                    backgroundSignUp.executeAsync(SignUpUsername,SignUpEmail,SignUpPassword,SignUpContacts);
                    circularProgress.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    //    Async Task is deprecated
//    Executor for Async
    public class BackgroundSignUp{
        private final Executor executor = Executors.newSingleThreadExecutor();
        private final Handler handler = new Handler(Looper.getMainLooper());

        public void executeAsync (String username, String emailStr, String passwordStr, String contacts){
            executor.execute(()->{
                try {
                    hashPW = createHashPW(emailStr,passwordStr);
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    e.printStackTrace();
                }
                handler.post(()->{
                    try {
                        signUp(username, emailStr,hashPW, contacts);
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                        e.printStackTrace();
                        showMessages("Sorry, we are unable to complete the sign up process now. Make sure you are connect with internet and try again later.");
                    }
                });
            });

        }
    }

    public String createHashPW(String email,String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return createHash(email, password);
    }





    //    SIGN UP PROCESS
    private void signUp(String signUpUsername, String signUpEmail, String hashPassword, String signUpContacts) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (validate()&&matchPW()) {


            (Api.getClient().register(
                    signUpEmail,
                    hashPassword,
                    signUpUsername,
                    signUpContacts
            )).enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(@NonNull Call<SignUpResponse> call, @NonNull Response<SignUpResponse> response) {
                    signUpResponseData = response.body();
                    if (signUpResponseData != null) {
                        showMessages(signUpResponseData.getMessage());
                        circularProgress.setVisibility(View.INVISIBLE);
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
                    showMessages("Sorry, we are unable to complete the sign up process now. Make sure you are connect with internet and try again later.");

                }

            });
        }
    }
//    END SIGN UP PROCESS

    private void showMessages(String message) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SignUpActivity.this);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Go To Home", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

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
        boolean isNotUsernameEmpty = 0<USignUpUsername.getText().toString().trim().length();
        boolean isNotEmailEmpty = validateEmail();
        boolean isNotPasswordEmpty = validatePassword();
        boolean isNotPasswordMatch = matchPW();
        boolean isNotContactsEmpty = 10==USignUpContacts.getText().toString().trim().length();

        if (!isNotContactsEmpty){
            USignUpContacts.setError("Please Enter Valid Number");
            USignUpContacts.requestFocus();
        }
        if (!isNotPasswordEmpty){
            USignUpPassword.setError("Password is required");
            USignUpPassword.requestFocus();
        }
        if (!isNotEmailEmpty){
            USignUpEmail.setError("Email is required");
            USignUpEmail.requestFocus();
        }
        if (!isNotUsernameEmpty){
            USignUpUsername.setError("Username is required");
            USignUpUsername.requestFocus();
        }



        return isNotUsernameEmpty && isNotEmailEmpty && isNotPasswordEmpty && isNotPasswordMatch && isNotContactsEmpty;

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            decorView.setSystemUiVisibility(hidingStatus.hideSystemBars());
        }
    }
}