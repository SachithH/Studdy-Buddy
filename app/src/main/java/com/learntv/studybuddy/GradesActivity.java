package com.learntv.studybuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.learntv.studybuddy.adapters.CustomAdapter;
import com.learntv.studybuddy.support.SignInPost;
import com.learntv.studybuddy.support.TokenAuthenticate;

import java.util.ArrayList;

public class GradesActivity extends AppCompatActivity {
    CircularProgressIndicator circularProgressIndicator;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    ArrayList<String> firstColumn = new ArrayList<>();
    ArrayList<String> secondColumn = new ArrayList<>();
    private CustomAdapter.RecyclerViewClickListener listener;
    private String token;
    final private int[] grades = {
            6,
            7,
            8,
            9,
            10,
            11,
            12,
            13
    };
    private MaterialToolbar topAppBar;
    private DrawerLayout drawerLayout;
    private SignInPost.showErrors signInPostError;
    private SignInPost.login signInPostLogin;
    private TokenAuthenticate.login tokenAuthenticateLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            token = extras.getString("token");
        }

        //Set Adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView2 = findViewById(R.id.recyclerView2);
        circularProgressIndicator = findViewById(R.id.progress_circular_grades);

        circularProgressIndicator.setVisibility(View.VISIBLE);
        checkToken();
        //add image item to arraylist
        setInfo();

        setAdapter();


        topAppBar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,topAppBar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    private void checkToken() {
        setAction();
        TokenAuthenticate tokenAuthenticate = new TokenAuthenticate();
        tokenAuthenticate.setData(token,getApplicationContext(),circularProgressIndicator,signInPostError,signInPostLogin,tokenAuthenticateLogin);
        tokenAuthenticate.checkToken();
    }

    private void setAdapter() {
        setOnClicklistener();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1, GridLayoutManager.VERTICAL,false);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getApplicationContext(),1, GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView2.setLayoutManager(gridLayoutManager2);
        //Create an object of custom Adapter
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), firstColumn, listener, 1);
        CustomAdapter customAdapter2 = new CustomAdapter(getApplicationContext(), secondColumn, listener,2);
        recyclerView.setAdapter(customAdapter);
        recyclerView2.setAdapter(customAdapter2);
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

    private void setInfo() {
        firstColumn.add("6");
        secondColumn.add("7");
        firstColumn.add("8");
        secondColumn.add("9");
        firstColumn.add("10");
        secondColumn.add("11");
        firstColumn.add("12");
        secondColumn.add("13");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

}