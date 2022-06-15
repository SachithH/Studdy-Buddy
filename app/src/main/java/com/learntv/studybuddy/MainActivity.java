package com.learntv.studybuddy;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.SignIn;
import com.learntv.studybuddy.support.hideSystemBars;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private View decorView;
    private VideoView mVideoView;
    private MediaController mediaController;
    private hideSystemBars hidingStatus;
    private String clientId = "390938103180-oohm564b8l2cgv3p5th3i095nv5h30sg.apps.googleusercontent.com";
    private GoogleSignInClient mGoogleSignInClient;
    private SignIn googleSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hidingStatus = new hideSystemBars();

        //hide status bar
//        decorView = getWindow().getDecorView();
//        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
//            public void onSystemUiVisibilityChange(int visibility) {
//                if (visibility == 0) {
//                    decorView.setSystemUiVisibility(hidingStatus.hideSystemBars());
//                }
//            }
//        });

        //Add Video
//        mVideoView = (VideoView) findViewById(R.id.bgVideoView);
//
//        if (mediaController == null) {
//            mediaController = new MediaController(MainActivity.this);
//            mediaController.setAnchorView(mVideoView);
//        }
//
//        mVideoView.setMediaController(mediaController);
//        mVideoView.setVideoURI(Uri.parse("android.resource://" +
//                getPackageName() + "/" +
//                R.raw.bg_video));
//
//        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                mediaPlayer.setLooping(true);
//            }
//        });
//        mVideoView.start();
        // video player end

        //Start Sign Up Activity
        Button signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        //Start Sign In Activity
        Button signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        //        End Sign Up from Sign Up Button

//      Configure sign-in to request the user's ID, email address, and basic
//      profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("390938103180-oohm564b8l2cgv3p5th3i095nv5h30sg.apps.googleusercontent.com")
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set the dimensions of the sign-in button.
        ImageButton signInButton = findViewById(R.id.googleSignInBtn);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
//        End Google Sign In
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleActivityResult.launch(signInIntent);
    }

    private void signInGoo() {
        Intent signIntent = new Intent(getApplicationContext(),GoogleLogin.class);
        startActivity(signIntent);
    }

    ActivityResultLauncher<Intent> googleActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        handleSignInResult(task);
                    }else {
                        Log.d("Sign In", "Failed ");
                    }
                }
            });

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d("Sign In", "SignInResult:Login Success");
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("Sign In", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account!=null){
            String googleToken = account.getIdToken();
            signInWithGoogle(googleToken,clientId);
        }
    }

    private void signInWithGoogle(String googleToken, String clientId) {
        Api.getClient().googleApi(
                clientId,
                googleToken
        ).enqueue(new Callback<SignIn>() {
            @Override
            public void onResponse(Call<SignIn> call, Response<SignIn> response) {
                googleSignIn = response.body();
                if (googleSignIn != null) {
                    if (googleSignIn.getStatus().equals("success")) {
                        loginToGrades(
                                googleSignIn.getData().getToken());
                    }
                }
            }

            @Override
            public void onFailure(Call<SignIn> call, Throwable t) {
                Log.d("OnFailed", "Failed");
            }
        });
    }

    private void loginToGrades(String token) {
        Intent intent = new Intent(getApplicationContext(),GradesActivity.class);
        intent.putExtra("token",token);
        startActivity(intent);
        finish();
    }

//    End of the Google Sign In

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus){
//            decorView.setSystemUiVisibility(hidingStatus.hideSystemBars());
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


}