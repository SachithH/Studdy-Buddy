package com.learntv.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learntv.studybuddy.adapters.SubjectAdapter;

import java.util.ArrayList;

public class SubjectsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Integer> logos = new ArrayList<>();
    private SubjectAdapter.RecyclerViewClickListener listener;
    private String token;
    private int syllabusId;
    private int gradeId;
    private String[] subjects = {
            "english",
            "geography",
            "history",
            "ict",
            "maths",
            "science",
            "sinhala",
            "tamil",

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gradeId = extras.getInt("gradeId");
            syllabusId = extras.getInt("syllabusId");
            token = extras.getString("token");
        }
        setInfo();

        //Set Adapter
        recyclerView = findViewById(R.id.recyclerViewSubjects);
        setAdapter();
    }

    private void setAdapter() {
        setOnClicklistener();
        GridLayoutManager gridView = new GridLayoutManager(getApplicationContext(), 3, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridView);
        //Create an object of custom Adapter
        SubjectAdapter customAdapter = new SubjectAdapter(getApplicationContext(), logos, listener, 1);
        recyclerView.setAdapter(customAdapter);
    }

    private void setOnClicklistener() {
        listener = new SubjectAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int pos) {
                Bundle bundle = new Bundle();
                bundle.putString("token",token);
                bundle.putInt("gradeId",gradeId);
                bundle.putInt("syllabusId",syllabusId);
                bundle.putInt("subjectId",pos+1);
                Intent intent = new Intent(getApplicationContext(),VODListActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                Log.d("Token: ",token);
            }
        };
    }

    private void setInfo() {
        logos.add(R.drawable.english);
        logos.add(R.drawable.geography);
        logos.add(R.drawable.history);
        logos.add(R.drawable.ict);
        logos.add(R.drawable.maths);
        logos.add(R.drawable.science);
        logos.add(R.drawable.sinhala);
        logos.add(R.drawable.tamil);
    }

}