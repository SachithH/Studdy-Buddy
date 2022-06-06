package com.learntv.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
    final private String[] grades = {
            "grade-06",
            "grade-07",
            "grade-08",
            "grade-09",
            "grade-10",
            "grade-11",
            "grade-12",
            "grade-13"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            token = extras.getString("token");
        }

        //add image item to arraylist
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
                bundle.putString("grade",grades[position]);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

}