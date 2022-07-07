package com.learntv.studybuddy;

import static com.learntv.studybuddy.PasswordHashing.createHash;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
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
    private EditText USignUpUsername,USignUpEmail,USignUpPassword,USignUpRetypePassword,USignUpContact;
    private TextInputLayout FieldUSignUpUsername,FieldUSignUpEmail,FieldUSignUpPassword,FieldUSignUpRetypePassword,FieldUSignUpContact;
    private String userName,email,password,retypePassword,contact;
    private String hashPW;
    private SignUpResponse signUpResponseData;
    private CircularProgressIndicator circularProgress;
    private CheckBox termsCheckBox;
    private final String apiKey = "a3373f7506773a10929a35195c0b27f0530dea0d1c0a442b";
    private final String apiSecret = "8iosgUEdR9F3TfQULMn1LBLFOwm7tmKi+reTRQNt69n98LdHqJFDPBKuxEefzJF7pv7e9fax5QeOxEdtnKJJroaj";
    private String errors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        circularProgress = findViewById(R.id.progress_circular);
        circularProgress.setVisibility(View.INVISIBLE);
        TextView termsAndConditionText = findViewById(R.id.TermsAndConditionsText);
        termsCheckBox = findViewById(R.id.TermsAndConditions);


        USignUpUsername = findViewById(R.id.SignUpUsername);
        USignUpEmail = findViewById(R.id.SignUpEmailOrPhone);
        USignUpPassword = findViewById(R.id.SignUpPassword);
        USignUpRetypePassword = findViewById(R.id.SignUpRetypePassword);
        USignUpContact = findViewById(R.id.SignUpContact);

//        UserName
        USignUpUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                userName = USignUpUsername.getText().toString().trim();
                if (hasFocus){
                    FieldUSignUpUsername.setHelperTextEnabled(true);
                    FieldUSignUpUsername.setHelperText("Type your username");
                }else{
                    if (userName.length()>0){
                        FieldUSignUpUsername.setHelperTextEnabled(false);
                        FieldUSignUpUsername.setErrorEnabled(false);
                    }else {
                        FieldUSignUpUsername.setHelperTextEnabled(true);
                        FieldUSignUpUsername.setError("Username required");
                    }
                }
            }
        });

//        Email
        USignUpEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                email = USignUpEmail.getText().toString().trim();
                if (hasFocus){
                    FieldUSignUpEmail.setHelperTextEnabled(true);
                    FieldUSignUpEmail.setHelperText("Eg: abc@abc.com");
                }else{
                    if (isValidEmail(email)){
                        FieldUSignUpEmail.setHelperTextEnabled(false);
                        FieldUSignUpEmail.setErrorEnabled(false);
                    }else {
                        FieldUSignUpEmail.setHelperTextEnabled(true);
                        FieldUSignUpEmail.setError("Please enter valid email");
                    }
                }
            }
        });

//        Password
        USignUpPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                password = USignUpPassword.getText().toString().trim();
                if (hasFocus){
                    FieldUSignUpPassword.setHelperTextEnabled(true);
                    FieldUSignUpPassword.setHelperText("Password should be between 8 and 64 Characters \n" +
                            "Include at least two of the following:\n" +
                            "   An uppercase character\n" +
                            "   A lowercase character\n" +
                            "   A number\n" +
                            "   A special character");
                }else{
                    if (password.length()>7){
                        FieldUSignUpPassword.setHelperTextEnabled(false);
                        FieldUSignUpPassword.setErrorEnabled(false);
                    }else {
                        FieldUSignUpPassword.setHelperTextEnabled(false);
                        FieldUSignUpPassword.setHelperTextEnabled(true);
                        if (password.length()>0){
                            FieldUSignUpPassword.setError("Password cannot be empty");
                        }else{
                            FieldUSignUpPassword.setError("Password should be between 8 and 64 Characters");
                        }
                    }
                }
            }
        });

//        ReTypePassword
        USignUpRetypePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                retypePassword = USignUpRetypePassword.getText().toString().trim();
                if (hasFocus){
                    FieldUSignUpRetypePassword.setHelperTextEnabled(true);
                    FieldUSignUpRetypePassword.setHelperText("Password should be match");
                }else{
                    if (password == null){
                        FieldUSignUpRetypePassword.setError("Password doesn't match");
                    }else {
                        if (password.equals(retypePassword)){
                            FieldUSignUpRetypePassword.setHelperTextEnabled(false);
                            FieldUSignUpRetypePassword.setErrorEnabled(false);
                        }else {
                            FieldUSignUpRetypePassword.setHelperTextEnabled(true);
                            FieldUSignUpRetypePassword.setError("Password doesn't match");
                        }
                    }

                }
            }
        });

//        Contact
        USignUpContact.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                contact = USignUpContact.getText().toString().trim();
                if (hasFocus){
                    FieldUSignUpContact.setHelperTextEnabled(true);
                    FieldUSignUpContact.setHelperText("Eg: 0712345678");
                }else{
                    if (contact.length() == 10) {
                        FieldUSignUpContact.setHelperTextEnabled(false);
                        FieldUSignUpContact.setErrorEnabled(false);

                    } else {
                        FieldUSignUpContact.setHelperTextEnabled(true);
                        FieldUSignUpContact.setError("Please enter valid mobile number");
                    }

                }
            }
        });

        FieldUSignUpUsername = findViewById(R.id.UsernameTextField);
        FieldUSignUpEmail = findViewById(R.id.EmailOrPhoneTextField);
        FieldUSignUpPassword = findViewById(R.id.PasswordTextField);
        FieldUSignUpRetypePassword = findViewById(R.id.RetypePasswordTextField);
        FieldUSignUpContact = findViewById(R.id.contactTextField);




//        Sign Up From Sign Up Button
        Button signUpBtn = findViewById(R.id.SignUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = USignUpUsername.getText().toString().trim();
                email = USignUpEmail.getText().toString().trim();
                password = USignUpPassword.getText().toString().trim();
                contact = USignUpContact.getText().toString().trim();

                BackgroundSignUp backgroundSignUp = new BackgroundSignUp();
                if (validate()){
                    backgroundSignUp.executeAsync(userName,email,password,contact);
                    circularProgress.setVisibility(View.VISIBLE);
                }
            }
        });

        termsAndConditionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (termsCheckBox.isChecked()){
                    termsCheckBox.setChecked(false);
                }else {
                    termsCheckBox.setChecked(true);
                }
            }
        });

        setupHyperlink(termsAndConditionText);

    }



    private void setupHyperlink(TextView termsTextView) {
        termsTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //    Async Task is deprecated
//    Executor for Async
    public class BackgroundSignUp{
        private final Executor executor = Executors.newSingleThreadExecutor();
        private final Handler handler = new Handler(Looper.getMainLooper());

        public void executeAsync (String username, String emailStr, String passwordStr, String contactStr){
            executor.execute(()->{
                try {
                    hashPW = createHashPW(emailStr,passwordStr);
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    e.printStackTrace();
                }
                handler.post(()->{
                    circularProgress.setVisibility(View.INVISIBLE);
                        otpVerification(username, emailStr, hashPW, contactStr);
                });
            });
        }
    }

    private void otpVerification(String username, String email, String password, String contact) {
        Intent intent = new Intent(getApplicationContext(),otpValidate.class);
        Bundle bundle = new Bundle();
        bundle.putString("username",username);
        bundle.putString("email",email);
        bundle.putString("password",password);
        bundle.putString("contact",contact);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public String createHashPW(String email,String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return createHash(email, password);
    }

    private boolean validatePassword() {
        int passwordLength = USignUpPassword.getText().toString().trim().length();
        if (7>=passwordLength){
            FieldUSignUpPassword.setErrorEnabled(true);
            FieldUSignUpPassword.setError("Password must be at least 8 characters long.");
            USignUpPassword.requestFocus();
        }else {
            FieldUSignUpPassword.setErrorEnabled(false);
        }

        return !(7 >=passwordLength);
    }

    private boolean matchPW() {
        String password = USignUpPassword.getText().toString().trim();
        String repassword = USignUpRetypePassword.getText().toString().trim();

        if (password.matches(repassword)){
            return true;
        }else {
            FieldUSignUpRetypePassword.setErrorEnabled(true);
            FieldUSignUpRetypePassword.setError("Password doesn't match");
            USignUpRetypePassword.requestFocus();
            return false;
        }
    }

    //    validate email

    private boolean isValidEmail(String emailString) {
        return !TextUtils.isEmpty(emailString) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches();
    }

    private boolean isValidPhoneNumber(String mobileNumber){
        boolean digitsOnly = TextUtils.isDigitsOnly(mobileNumber);

        if (digitsOnly){
            return mobileNumber.length() == 10;
        }else {
            return false;
        }

    }
//   end email validate

    private boolean validate() {
        String EmailOrNumberStr = USignUpEmail.getText().toString().trim();
        boolean isEmail = isValidEmail(EmailOrNumberStr);
        boolean isNotUsernameEmpty = 0<USignUpUsername.getText().toString().trim().length();
        boolean isNotPasswordEmpty = validatePassword();
        boolean isNotPasswordMatch = matchPW();
        boolean isValidContact = isValidPhoneNumber(contact);
        boolean isAcceptedTermsAndConditions = isAcceptedTermsAndConditions();

//        CheckBox
        if (!isAcceptedTermsAndConditions){
            showError("You have to accept terms and conditions");
        }

//        Contact
        if (!isValidContact){
            FieldUSignUpContact.setErrorEnabled(true);
            FieldUSignUpContact.setError("Username is required");
            USignUpContact.requestFocus();
        }else {
            FieldUSignUpContact.setErrorEnabled(false);
        }

//        Password
        if (!isNotPasswordEmpty){
            FieldUSignUpPassword.setErrorEnabled(true);
            FieldUSignUpPassword.setError("Password is required");
            USignUpPassword.requestFocus();
        }else {
            FieldUSignUpPassword.setHelperTextEnabled(false);
        }

//        Email
        if (!(isEmail)){
            FieldUSignUpEmail.setErrorEnabled(true);
            FieldUSignUpEmail.setError("Please Type Valid Email or Mobile Number");
            USignUpEmail.requestFocus();
        }else {
            FieldUSignUpEmail.setErrorEnabled(false);
        }

//        UserName
        if (!isNotUsernameEmpty) {
            FieldUSignUpUsername.setErrorEnabled(true);
            FieldUSignUpUsername.setError("Username is required");
            USignUpUsername.requestFocus();
        }else {
            FieldUSignUpUsername.setErrorEnabled(false);
        }




        return isNotUsernameEmpty && isEmail && isNotPasswordEmpty && isNotPasswordMatch && isValidContact && isAcceptedTermsAndConditions;

    }

    private void showError(String errors) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SignUpActivity.this);
        builder.setMessage(errors);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNeutralButton("Go back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private boolean isAcceptedTermsAndConditions() {
        return termsCheckBox.isChecked();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}