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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.learntv.studybuddy.retrofit.SignIn;
import com.learntv.studybuddy.support.PrefManager;
import com.learntv.studybuddy.support.SignInPost;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SignInActivity extends AppCompatActivity {
    private TextInputEditText mobileInput,passwordInput;
    private CheckBox rememberMe;
    private SignIn signUpResponseData;
    private String hashPW;
    private String errors = "Something went wrong. Please try again Later";
    private CircularProgressIndicator circularProgress;
    private PrefManager prefManager;
    private MaterialAlertDialogBuilder builder;
    private final String apiKey = "be2f97570be40d5595fddaa64995b6534e6bae5ba9e86ed0";
    private final String apiSecret = "lu55mgL5sIuDNcxCfXOkydElrfr6ehxyhrNsB8aBqe0ASPIX9XB6c5k8+4NfV15SMv0aipGd0gtzwbrDEqVf3T4A";
    private SignInPost.login signInPostLogin;
    private SignInPost.showErrors signInPostError;
    private TextInputLayout mobileTextField,passwordTextField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        circularProgress = findViewById(R.id.progress_circular);
//        End of hide status bar

        //remember me
        prefManager = new PrefManager(getApplicationContext());
        if (!prefManager.isUserLoggedOut()) {
            circularProgress.setVisibility(View.VISIBLE);
            setAction();
            //user's email and password both are saved in preferences
            String savedMobile = prefManager.getMobile();
            String savedPassword = prefManager.getPassword();



            if (isValidPhoneNumber(savedMobile) && validatePwd(savedPassword)) {
                    signIn(savedMobile,savedPassword);
            }


        }else{
            circularProgress.setVisibility(View.INVISIBLE);
        }
        //end remember me

//        username and password
        mobileInput = findViewById(R.id.signInMobileNumber);
        passwordInput = findViewById(R.id.signInPassword);
        rememberMe = findViewById(R.id.rememberMe);
        mobileTextField = findViewById(R.id.signInMobileNumberTextField);
        passwordTextField = findViewById(R.id.signInPasswordTextField);


        mobileInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                String mobile = mobileInput.getText().toString().trim();
                isValidPhoneNumber(mobile);
            }
        });

//        Password
        passwordInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                String password = passwordInput.getText().toString().trim();
                validatePwd(password);
            }
        });

        //Sign In Button
        Button signIn = findViewById(R.id.profileSignIn);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAction();
                String mobile = mobileInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                //Create hash password



                // validate the fields and call sign method to implement the api
                if (isValidPhoneNumber(mobile) && validatePwd(password)) {
                    circularProgress.setVisibility(View.VISIBLE);

                    BackgroundSignIn backgroundSignIn = new BackgroundSignIn();
                    backgroundSignIn.executeAsync(mobile,password);

                }

            }
        });




    }

    //    Async Task is deprecated
//    Executor for Async
    public class BackgroundSignIn{
        private final Executor executor = Executors.newSingleThreadExecutor();
        private final Handler handler = new Handler(Looper.getMainLooper());

        public void executeAsync (String mobile, String password){
            executor.execute(()->{
                try {
                    hashPW = createHashPW(mobile,password);
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    e.printStackTrace();
                }
                handler.post(()->{
                        signIn(mobile,hashPW);
                });
            });

        }
    }

    private void signIn(String mobile, String hashPW) {
        boolean remember = false;
        if (rememberMe!=null){
            remember = rememberMe.isChecked();
        }
        new SignInPost().signInWithServer(mobile,hashPW, signInPostLogin, signInPostError,getApplicationContext(),remember,circularProgress);
    }

    //    validate email
    private boolean isValidPhoneNumber(String mobileNumber) {
        boolean digitsOnly = TextUtils.isDigitsOnly(mobileNumber);

        if (digitsOnly&&mobileNumber.length() == 10) {
            mobileTextField.setErrorEnabled(false);
            return true;
        } else {
            mobileTextField.setErrorEnabled(true);
            mobileTextField.setError("Please Enter Valid Number");
            return false;
        }

    }

//    password validate
    private boolean validatePwd(String passwordStr) {
        if (0 < passwordStr.length()) {
            passwordTextField.setErrorEnabled(false);
            return true;
        }else {
            passwordTextField.setErrorEnabled(true);
            passwordTextField.setError("Please Fill This");
            return false;
        }
//        end password validate


    }

    public String createHashPW(String email,String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return createHash(email, password);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (prefManager.isUserLoggedOut()) {
        circularProgress.setVisibility(View.GONE);}
    }

    public void setAction(){
        signInPostLogin = new SignInPost.login(){

            @Override
            public void login(String token, String email) {
                Intent intent = new Intent(getApplicationContext(),GradesActivity.class);
                intent.putExtra("token",token);
                startActivity(intent);
                Log.d("loginToGrades: ",token);
                finish();
            }
        };

        signInPostError = new SignInPost.showErrors(){
            @Override
            public void pushError(String error) {
                circularProgress.setVisibility(View.INVISIBLE);
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SignInActivity.this);
                builder.setMessage(error);
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNeutralButton("Go back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.show();
            }
        };
    }
}