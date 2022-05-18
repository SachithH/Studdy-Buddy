package com.learntv.studybuddy;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learntv.studybuddy.adapters.CustomAdapter;

import java.util.ArrayList;

public class GradesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Integer> logos = new ArrayList<>();
    private CustomAdapter.RecyclerViewClickListener listener;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            token = extras.getString("token");
        }

        //add item to arraylist
        setInfo();

        //Set Adapter
        recyclerView = findViewById(R.id.recyclerView);
        setAdapter();
    }

    private void setAdapter() {
        setOnClicklistener();
        GridLayoutManager gridView = new GridLayoutManager(getApplicationContext(), 3, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridView);
        //Create an object of custom Adapter
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), logos, listener);
        recyclerView.setAdapter(customAdapter);
    }

    private void setOnClicklistener() {
        listener = new CustomAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("POSITION",position);
                bundle.putString("token",token);
                Intent intent = new Intent(getApplicationContext(),SyllabusActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }

    private void setInfo() {
        logos.add(R.drawable.grade6);
        logos.add(R.drawable.grade7);
        logos.add(R.drawable.grade8);
        logos.add(R.drawable.grade9);
        logos.add(R.drawable.grade10);
        logos.add(R.drawable.grade11);
        logos.add(R.drawable.grade12);
        logos.add(R.drawable.grade13);
    }

}