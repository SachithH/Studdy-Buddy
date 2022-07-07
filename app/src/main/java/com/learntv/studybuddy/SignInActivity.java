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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.learntv.studybuddy.retrofit.SignIn;
import com.learntv.studybuddy.support.PrefManager;
import com.learntv.studybuddy.support.SignInPost;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SignInActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
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
                setAction();
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
            });

        }
    }

    private void signIn(String emailStr, String hashPW) {
        boolean remember = false;
        if (rememberMe!=null){
            remember = rememberMe.isChecked();
        }
        new SignInPost().signInWithServer(emailStr,hashPW, signInPostLogin, signInPostError,getApplicationContext(),remember,circularProgress);
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