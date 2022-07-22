package com.learntv.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.learntv.studybuddy.support.SignUpPost;

import java.util.Objects;

public class promoCodeActivity extends BaseActivity {

    private String mobileNo,password,deviceId,promoCode="";
    private TextInputEditText promoCodeInput;
    private MaterialTextView skip;
    private MaterialButton promoCodeVerifyBtn;
    private SignUpPost.showErrors signUpPostError;
    private SignUpPost.showSuccess signUpPostSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_code);

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null){
            mobileNo = bundle.getString("mobileNo");
            password = bundle.getString("password");
            deviceId = bundle.getString("deviceId");
        }
        
        skip = findViewById(R.id.promoCodeSkip);
        promoCodeInput = findViewById(R.id.promoCodeInput);
        promoCodeVerifyBtn = findViewById(R.id.promoCodeVerifyBtn);

        setAlertDialog();

        promoCodeVerifyBtn.setOnClickListener(view -> {
            promoCode = Objects.requireNonNull(promoCodeInput.getText()).toString().trim();
            new SignUpPost().signUpMobile(mobileNo,password,deviceId,promoCode,signUpPostError,signUpPostSuccess);
        });

        skip.setOnClickListener(view -> {
            new SignUpPost().signUpMobile(mobileNo,password,deviceId,promoCode,signUpPostError,signUpPostSuccess);
        });
        
    }

    public void setAlertDialog() {
        signUpPostError = new SignUpPost.showErrors() {

            @Override
            public void pushError(String error) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(promoCodeActivity.this);
                builder.setMessage(error);
                builder.setCancelable(false);
                builder.setPositiveButton("Go back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.show();
            }
        };


        signUpPostSuccess = new SignUpPost.showSuccess() {

            @Override
            public void pushSuccess(String error) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(promoCodeActivity.this);
                builder.setMessage(error);
                builder.setCancelable(false);
                builder.setPositiveButton("Go Home", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"Your account has been activated.\nSign In and Enjoy!",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        };
    }

}