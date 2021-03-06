package com.learntv.studybuddy;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.learntv.studybuddy.adapters.VODAdapter;
import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.CommonResponse;
import com.learntv.studybuddy.retrofit.VODResponse;
import com.learntv.studybuddy.retrofit.VODResponseData;
import com.learntv.studybuddy.support.ShowErrors;
import com.learntv.studybuddy.support.SignInPost;
import com.learntv.studybuddy.support.TokenAuthenticate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VODListActivity extends BaseActivity implements View.OnClickListener {
    VODAdapter.RecyclerViewClickListener listener;
    RecyclerView recyclerView;
    private VODResponse vodResponseData;
    private String token;
    private int gradeId;
    private int syllabusId;
    private int subjectId;
    VODAdapter vodAdapter;
    private SignInPost.login signInPostLogin;
    private SignInPost.showErrors signInPostError;
    private TokenAuthenticate.login tokenAuthenticateLogin;
    private CircularProgressIndicator circularProgressIndicator;
    private AppCompatEditText searchBar;
    private goToVideoClass video;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vodlist);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            token = extras.getString("token");
            gradeId = extras.getInt("gradeId");
            syllabusId = extras.getInt("syllabusId");
            subjectId = extras.getInt("subjectId");
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewVOD);

        circularProgressIndicator = findViewById(R.id.progress_circular_vodList);
        searchBar = findViewById(R.id.searchBar);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                filter(s);
            }
        });

        dialog = new Dialog(this);

        checkToken();
        Log.d("onCreate: ",token);


    }

    private void checkToken() {
        circularProgressIndicator.setVisibility(View.VISIBLE);
        setAction();
        TokenAuthenticate tokenAuthenticate = new TokenAuthenticate();
        tokenAuthenticate.setData(token,getApplicationContext(),circularProgressIndicator,signInPostError,signInPostLogin,tokenAuthenticateLogin);
        tokenAuthenticate.checkToken();
    }

    public void setAction(){
        signInPostLogin = new SignInPost.login(){

            @Override
            public void login(String token, String email) {
                onRestart();
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
                setVOD();
            }
        };
    }

    private void setVOD() {
        String apiKey="76b3d18521fa7f12d7ea0402214408140c108a17634d97c5";
        String apiSecret="v1iMCBWxbq2UE4k/GRWt7xRjMkZGvoCSlZNqu+yC7mDIOp+/2X/6AANpuwaBARkxyEDQzYo0nZrAB5IMLwK6Sw==";
        Log.d("setVOD: ",token);
        Log.d("setVOD: ", String.valueOf(gradeId));
        Log.d("setVOD: ", String.valueOf(subjectId));
        Log.d("setVOD: ", String.valueOf(syllabusId));

        (Api.getClient().video_list(
                apiKey,
                apiSecret,
                token,
                gradeId,
                subjectId,
                syllabusId
        )).enqueue(new Callback<VODResponse>() {
            @Override
            public void onResponse(@NonNull Call<VODResponse> call,@NonNull Response<VODResponse> response) {
                vodResponseData = response.body();
                if (vodResponseData!=null){
                    if (vodResponseData.getData()!=null&&vodResponseData.getError()==null){
                        setDataInRecyclerView(vodResponseData.getData());
                        circularProgressIndicator.setVisibility(View.INVISIBLE);
                    }else {
                        if (vodResponseData.getError()!=null) {
                            pushErrors(vodResponseData.getError().getStatusCode(), vodResponseData.getError().getDescription());
                        }
                    }
                }else{
                    Toast.makeText(VODListActivity.this,"No Data Found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<VODResponse> call,@NonNull Throwable t) {

            }
        });
    }

    private void pushErrors(String errorcode, String description) {
        ShowErrors showErrors = new ShowErrors();
        String errors = showErrors.Errors(errorcode,description);

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



    private void setDataInRecyclerView(List<VODResponseData> vodResponseDataIn) {
        setOnClickListener();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VODListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        TextView subjectName = (TextView) findViewById(R.id.subjectName);
        subjectName.setText(vodResponseData.getData().get(0).getSubjectS());

        vodAdapter = new VODAdapter(getApplicationContext(), vodResponseDataIn, listener);
        recyclerView.setAdapter(vodAdapter);
    }

    private void setOnClickListener() {
        listener = new VODAdapter.RecyclerViewClickListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View v, int position, List<VODResponseData> vodResponseDataNew) {
                Log.d("Position: ", String.valueOf(position));
                Log.d("Position: ", String.valueOf(vodResponseDataNew.get(position).getId()));
                if (v.getId() == R.id.favouriteBtn) {
                    setFavourite(position, vodResponseDataNew);
                } else {
                    showQuality(v, position, vodResponseDataNew);
                }

            };

        };
    }

    public void showQuality(View v, int position, List<VODResponseData> vodResponseDataNew){
        View qualityView = getLayoutInflater().inflate(R.layout.modal_quality,null);
        dialog.setContentView(qualityView);
        qualityView.findViewById(R.id.low).setOnClickListener(this);
        qualityView.findViewById(R.id.medium).setOnClickListener(this);
        qualityView.findViewById(R.id.high).setOnClickListener(this);
        dialog.show();
        video = new goToVideoClass();
        video.setData(position,vodResponseDataNew);
    }

    private void setFavourite(int position,List<VODResponseData> vodResponseDataNew){
        String apiKey="202a3b589d98404fdd00f1583959e1abb62653ce2d45b1e3";
        String apiSecret="dgXsZwxbb6j6r3wH6jLwfc0WwsMFId1d8sy8pJjgJcvuHvGl5EenFS3NehCbeB2+wh8mmS4QZPHcHDRz/px9ZQ7j";
        int videoId = vodResponseDataNew.get(position).getId();
        (Api.getClient().set_favourite(
                apiKey,
                apiSecret,
                token,
                videoId
        )).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call,@NonNull Response<CommonResponse> response) {
                CommonResponse favouriteResponse = response.body();
                if (favouriteResponse!=null){
                    if(favouriteResponse.getData()!=null){
                        requestSuccess(favouriteResponse.getData().getDescription());
                    }else{
                        if(favouriteResponse.getError()!=null){
                            requestSuccess(favouriteResponse.getError().getDescription());
                        }else{
                            requestSuccess("Something went wrong. Please try again later");
                        }
                    }
                }else{
                    requestSuccess("response null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call,@NonNull Throwable t) {
                requestSuccess("fail");
            }
        });
    }

    private void requestSuccess(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

    }

        @SuppressLint("NonConstantResourceId")
        public void onClick(View view) {
            int quality;
            switch (view.getId()){
                case R.id.medium:
                    quality = 1;
                    break;
                case R.id.high:
                    quality = 2;
                    break;
                default:
                    quality = 0;
                    break;
            }
            video.setQuality(quality);
            dialog.dismiss();
            video.goToVideo();
    }

    class goToVideoClass{
        int position,quality;
        List<VODResponseData> vodResponseDataNew;

        public void setData(int position, List<VODResponseData> vodResponseDataNew){
            this.position = position;
            this.vodResponseDataNew = vodResponseDataNew;
        }

        public void setQuality(int quality){
            this.quality = quality;
        }

        public void goToVideo(){
            Intent intent = new Intent(getApplicationContext(),PlayingVideoActivity.class);
            Integer videoId = vodResponseDataNew.get(position).getId();
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            bundle.putInt("grade",gradeId);
            bundle.putInt("syllabus",syllabusId);
            bundle.putInt("subject",subjectId);
            bundle.putInt("videoId",videoId);
            bundle.putInt("quality",quality);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_v_o_d_list,menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);

        return super.onCreateOptionsMenu(menu);
    }

    private void filter(String text) {

        // creating a new array list to filter our data.
        List<VODResponseData> filteredlist = new ArrayList<>();
        if (vodResponseData!=null) {
            // running a for loop to compare elements.
            for (VODResponseData item : vodResponseData.getData()) {
                // checking if the entered string matched with any item of our recycler view.
                if (item.getShortDesc().toLowerCase().contains(text.toLowerCase())
                        || item.getHeading().toLowerCase().contains(text.toLowerCase())) {
                    // if the item is matched we are
                    // adding it to our filtered list.
                    filteredlist.add(item);
                }
            }
            if (filteredlist.isEmpty()) {
                vodAdapter.filterList(filteredlist);
                // if no item is added in filtered list we are
                // displaying a toast message as no data found.
            } else {
                // at last we are passing that filtered
                // list to our adapter class.
                vodAdapter.filterList(filteredlist);
            }
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}