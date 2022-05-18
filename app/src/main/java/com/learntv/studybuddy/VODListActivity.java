package com.learntv.studybuddy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learntv.studybuddy.adapters.CustomAdapter;
import com.learntv.studybuddy.adapters.VODAdapter;
import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.VODResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VODListActivity extends AppCompatActivity {
    VODAdapter.RecyclerViewClickListener listener;
    RecyclerView recyclerView;
    VODResponse VODListResponseData;
    String token;
    private String VideoUrl;
    Bundle more_extras = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vodlist);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            token = extras.getString("token");
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewVOD);
        getVODData();
    }

    private void getVODData() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(VODListActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog

        (Api.getClient().grade6_2011_sinhalaList(
                token
        )).enqueue(new Callback<VODResponse>() {
            @Override
            public void onResponse(Call<VODResponse> call, Response<VODResponse> response) {
                VODListResponseData = response.body();
                setDataInRecyclerView();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<VODResponse> call, Throwable t) {
                Toast.makeText(VODListActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void setDataInRecyclerView() {
        setOnClicklistener();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VODListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        VODAdapter vodAdapter = new VODAdapter(getApplicationContext(), VODListResponseData, listener);
        recyclerView.setAdapter(vodAdapter);
    }

    private void setOnClicklistener() {
        listener = new VODAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                VideoUrl = VODListResponseData.getList().get(position).getVideo();
                more_extras.putString("VideoUrl",VideoUrl);
                Intent intent = new Intent(getApplicationContext(),PlayingVideoActivity.class);
                intent.putExtras(more_extras);
                startActivity(intent);
            }
        };
    }


}