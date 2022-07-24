package com.learntv.studybuddy;

import static com.learntv.studybuddy.support.BottomNavigation.bottomNavigationFunction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.learntv.studybuddy.adapters.CustomAdapter;
import com.learntv.studybuddy.support.SignInPost;
import com.learntv.studybuddy.support.TokenAuthenticate;

public class GradesActivity extends BaseActivity {
    CircularProgressIndicator circularProgressIndicator;
    RecyclerView recyclerView;
    private CustomAdapter.RecyclerViewClickListener listener;
    private String token;
    final private int[] grades = {6, 7, 8, 9, 10, 11, 12, 13};
    private SignInPost.showErrors signInPostError;
    private TokenAuthenticate.login tokenAuthenticateLogin;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            token = extras.getString("token");
            Log.d("onCreate: ",token);
        }

        //Set Adapter
        recyclerView = findViewById(R.id.recyclerView);
        circularProgressIndicator = findViewById(R.id.progress_circular_grades);

        circularProgressIndicator.setVisibility(View.VISIBLE);
        checkToken();

        setAdapter();


//        Bottom Navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().setGroupCheckable(0,false,true);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                bottomNavigationFunction(getApplicationContext(),item.getItemId(),token);
                switch(item.getItemId()) {
                    case R.id.homeBottom:
                    case R.id.setting:
                    case R.id.walletBottom:
                        return true;
                    default:
                        return false;
                }

            }
        });
//        End Bottom navigation bar

    }

    private void checkToken() {
        setAction();
        TokenAuthenticate tokenAuthenticate = new TokenAuthenticate();
        tokenAuthenticate.setData(token,getApplicationContext(),circularProgressIndicator,signInPostError,null,tokenAuthenticateLogin);
        tokenAuthenticate.checkToken();
    }

    private void setAdapter() {
        setOnClicklistener();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2, GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        //Create an object of custom Adapter
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), grades, listener, 1);
        recyclerView.setAdapter(customAdapter);
    }

    public void setAction(){

        signInPostError = new SignInPost.showErrors(){
            @Override
            public void pushError(String error) {
                circularProgressIndicator.setVisibility(View.INVISIBLE);
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getApplicationContext());
                builder.setMessage(error);
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("Go back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.show();
            }
        };

        tokenAuthenticateLogin = new TokenAuthenticate.login(){
            @Override
            public void login(String token) {
                Toast.makeText(getApplicationContext(),"Token Verified",Toast.LENGTH_SHORT).show();
                circularProgressIndicator.setVisibility(View.INVISIBLE);
            }
        };
    }

    private void setOnClicklistener() {
        listener = new CustomAdapter.RecyclerViewClickListener() {

            @Override
            public void onClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("token",token);
                bundle.putInt("gradeId",grades[position]);
                Intent intent = new Intent(getApplicationContext(),SyllabusActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

}