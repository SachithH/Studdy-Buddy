package com.learntv.studybuddy;

import static com.learntv.studybuddy.PasswordHashing.createHash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.learntv.studybuddy.support.PrefManager;
import com.learntv.studybuddy.support.SignInPost;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SignInActivity extends BaseActivity {
    private TextInputEditText mobileInput,passwordInput;
    private CheckBox rememberMe;
    private String hashPW;
    private CircularProgressIndicator circularProgress;
    private PrefManager prefManager;
    private SignInPost.login signInPostLogin;
    private SignInPost.showErrors signInPostError;
    private TextInputLayout mobileTextField,passwordTextField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        circularProgress = findViewById(R.id.progress_circular);
        mobileInput = findViewById(R.id.signInMobileNumber);
        passwordInput = findViewById(R.id.signInPassword);
        mobileTextField = findViewById(R.id.signInMobileNumberTextField);
        passwordTextField = findViewById(R.id.signInPasswordTextField);

        //remember me
        prefManager = new PrefManager(getApplicationContext());
        if (prefManager.isUserLoggedOut()) {
            circularProgress.setVisibility(View.VISIBLE);
            setAction();



        }else{
            circularProgress.setVisibility(View.INVISIBLE);
            //user's email and password both are saved in preferences
            String savedMobile = prefManager.getMobile();

            mobileInput.setText(savedMobile);
        }
        //end remember me

//        username and password
        rememberMe = findViewById(R.id.rememberMe);


        mobileInput.setOnFocusChangeListener((view, hasFocus) -> {
            String mobile = Objects.requireNonNull(mobileInput.getText()).toString().trim();
            if(!hasFocus)
            {
                isValidPhoneNumber(mobile);
            }else{
                mobileTextField.setErrorEnabled(false);
            };
        });

//        Password
        passwordInput.setOnFocusChangeListener((view, hasFocus) -> {
            String password = Objects.requireNonNull(passwordInput.getText()).toString().trim();
            if(!hasFocus) {
                validatePwd(password);
            }else{
                passwordTextField.setErrorEnabled(false);
            };
        });

        //Sign In Button
        Button signIn = findViewById(R.id.profileSignIn);

        signIn.setOnClickListener(view -> {
            setAction();
            String mobile = Objects.requireNonNull(mobileInput.getText()).toString().trim();
            String password = Objects.requireNonNull(passwordInput.getText()).toString().trim();
            //Create hash password



            // validate the fields and call sign method to implement the api
            if (isValidPhoneNumber(mobile) && validatePwd(password)) {
                circularProgress.setVisibility(View.VISIBLE);

                BackgroundSignIn backgroundSignIn = new BackgroundSignIn();
                backgroundSignIn.executeAsync(mobile,password);

            }

        });

        TextView forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(view -> {
            Intent forgotPasswordIntent = new Intent(SignInActivity.this,EnterMobileActivity.class);
            startActivity(forgotPasswordIntent);
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
                handler.post(()-> signIn(mobile,hashPW));
            });

        }
    }

    private void signIn(String mobile, String hashPW) {
        boolean remember = false;
        if (rememberMe!=null){
            remember = rememberMe.isChecked();
        }
        new SignInPost().signInWithServer(mobile,hashPW, getApplicationContext(),signInPostLogin, signInPostError,remember,circularProgress);
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
        signInPostLogin = (token, email) -> {
            Intent intent = new Intent(getApplicationContext(),SelectRoomsActivity.class);
            intent.putExtra("token",token);
            startActivity(intent);
            Log.d("loginToGrades: ",token);
            finish();
        };

        signInPostError = error -> {
            circularProgress.setVisibility(View.INVISIBLE);
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SignInActivity.this);
            builder.setMessage(error);
            builder.setCancelable(false);
            builder.setPositiveButton("OK", (dialogInterface, i) -> {

            });
            builder.setNeutralButton("Go back", (dialogInterface, i) -> finish());
            builder.show();
        };
    }
}