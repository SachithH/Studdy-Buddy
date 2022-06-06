package com.learntv.studybuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.learntv.studybuddy.adapters.VODAdapter;
import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.VODResponse;
import com.learntv.studybuddy.retrofit.ValidateToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VODListActivity extends AppCompatActivity {
    VODAdapter.RecyclerViewClickListener listener;
    RecyclerView recyclerView;
    List<VODResponse> VODListResponseData;
    String token;
    private String VideoUrl;
    Bundle more_extras = new Bundle();
    private String grade;
    private String syllabus;
    private String subject;
    private ValidateToken validation;
    private String errors;
    VODAdapter vodAdapter;
    private Toolbar mToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vodlist);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            token = extras.getString("token");
            grade = extras.getString("grade");
            syllabus = extras.getString("syllabus");
            subject = extras.getString("subject");
        }

        mToolBar = findViewById(R.id.toolBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewVOD);
        setSupportActionBar(mToolBar);
        checkToken();
        setVOD();
    }

    private void setVOD() {

        (Api.getClient().video_list(
                token,
                grade,
                syllabus,
                subject
        )).enqueue(new Callback<List<VODResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<VODResponse>> call, @NonNull Response<List<VODResponse>> response) {
                VODListResponseData = response.body();
                if (VODListResponseData != null) {
                    setDataInRecyclerView();
                } else {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(VODListActivity.this);
                    builder.setMessage("No data at this moment");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setVOD();
                        }
                    });
                    builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    builder.show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<VODResponse>> call, @NonNull Throwable t) {
                Log.d("Response:", t.toString());
            }
        });
    }

    private void checkToken(){
        (Api.getClient().auhtenticate(
                token
        )).enqueue(new Callback<ValidateToken>() {
            @Override
            public void onResponse(Call<ValidateToken> call, Response<ValidateToken> response) {
                validation = response.body();
                if (validation != null) {
                    if (!validation.getSuccess()) {
                        showErrors(validation.getErrorcode());
                    }
                }
            }

            @Override
            public void onFailure(Call<ValidateToken> call, Throwable t) {
                Log.d("Response:",t.toString());
            }
        });
    }

    private void showErrors(Integer errorcode) {
        switch (errorcode){
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
                errors = "Login expired. Please Sign In again";
                break;
            case 105:
                errors = "Token of session is not provided, Please Sign in again";
                break;
            default:
                errors = "something went wrong. Please try again later";
        }
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(VODListActivity.this);
        builder.setMessage(errors);
        builder.setCancelable(false);
        builder.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(VODListActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    private void setDataInRecyclerView() {
        setOnClicklistener();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VODListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        vodAdapter = new VODAdapter(getApplicationContext(), VODListResponseData, listener);
        recyclerView.setAdapter(vodAdapter);
    }

    private void setOnClicklistener() {
        listener = new VODAdapter.RecyclerViewClickListener() {

            @Override
            public void onClick(View v, int position) {
                VideoUrl = VODListResponseData.get(position).getVideo().getUrl();
                more_extras.putString("VideoUrl",VideoUrl);
                Intent intent = new Intent(getApplicationContext(),PlayingVideoActivity.class);
                intent.putExtras(more_extras);
                startActivity(intent);
            };

        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_v_o_d_list,menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<VODResponse> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (VODResponse item : VODListResponseData) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getLessonE().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            vodAdapter.filterList(filteredlist);
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            vodAdapter.filterList(filteredlist);
        }
    }


}