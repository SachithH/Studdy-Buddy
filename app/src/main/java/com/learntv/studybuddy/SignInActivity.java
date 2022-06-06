package com.learntv.studybuddy;

import static com.learntv.studybuddy.PasswordHashing.createHash;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    private CircularProgressIndicator circularProgress;
    private PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        circularProgress = findViewById(R.id.progress_circular);

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
        prefManager = new PrefManager(getApplicationContext());
        if (!prefManager.isUserLoggedOut()) {
            circularProgress.setVisibility(View.VISIBLE);
            //user's email and password both are saved in preferences
            String savedEmail = prefManager.getEmail();
            String savedPassword = prefManager.getPassword();



            if (validateEmail(savedEmail) && validatePwd(savedPassword)) {
                    signIn(savedEmail,savedPassword);
            }


        }else{
            circularProgress.setVisibility(View.INVISIBLE);
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
                    circularProgress.setVisibility(View.VISIBLE);

                    BackgroundSignIn backgroundSignIn = new BackgroundSignIn();
                    backgroundSignIn.executeAsync(emailStr,passwordStr);

                }

            }
        });




    }

    //    Async Task is deprecated
//    Executor for Async
    public class BackgroundSignIn{
        private final Executor executor = Executors.newSingleThreadExecutor();
        private final Handler handler = new Handler(Looper.getMainLooper());

        public void executeAsync (String emailStr, String passwordStr){
            executor.execute(()->{
                try {
                    hashPW = createHashPW(emailStr,passwordStr);
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    e.printStackTrace();
                }
                handler.post(()->{
                        signIn(emailStr,hashPW);
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showMessages("Can't connect right now");
                    }
                },
                        4000);
            });

        }
    }

    private void signIn(String emailStr, String hashPW) {
        try {
            signInWithServer(emailStr,hashPW);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Something Went Wrong Please Try Again Later", Toast.LENGTH_LONG).show();
        }
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

    public String createHashPW(String email,String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return createHash(email, password);
    }

//    Sign In Process
    private void signInWithServer(String email,String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        circularProgress.setVisibility(View.VISIBLE);

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
                        circularProgress.setVisibility(View.INVISIBLE);
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
                showMessages("Something went wrong. Check your internet connection and try again");

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
                   break;
               case 104:
                   errors = "Login expired. Please Sign In";
                   break;
               case 105:
                   errors = "Token of session is not provided";
                   break;
               default:
                   errors = "something went wrong. Please try again later";
           }
               showMessages(errors);

        }

    private void loginToGrades(String token,String email) {
        Intent intent = new Intent(getApplicationContext(),GradesActivity.class);
        intent.putExtra("token",token);
        startActivity(intent);
        finish();
    }

    private void sureMessage() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SignInActivity.this);
        builder.setMessage("");
        builder.setCancelable(false);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                circularProgress.setVisibility(View.GONE);

            }
        });
        builder.show();
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
        if (prefManager.isUserLoggedOut()) {
        circularProgress.setVisibility(View.GONE);}
    }

    private void showMessages(String error) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SignInActivity.this);
        builder.setMessage(error);
        builder.setCancelable(false);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                circularProgress.setVisibility(View.GONE);

            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                circularProgress.setVisibility(View.GONE);

            }
        });
        builder.show();

    }
}