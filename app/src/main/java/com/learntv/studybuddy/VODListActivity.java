package com.learntv.studybuddy;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learntv.studybuddy.adapters.VODAdapter;
import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.VODResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VODListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    VODResponse VODListResponseData;
    String token;

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VODListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        VODAdapter vodAdapter = new VODAdapter(VODListActivity.this, VODListResponseData);
        recyclerView.setAdapter(vodAdapter);
    }


}