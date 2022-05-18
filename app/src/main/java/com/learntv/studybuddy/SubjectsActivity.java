package com.learntv.studybuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.learntv.studybuddy.adapters.CustomAdapter;

import java.util.ArrayList;

public class SubjectsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Integer> logos = new ArrayList<>();
    private CustomAdapter.RecyclerViewClickListener listener;
    private int position;
    private String token;
    private String Syllabus;
    private String Grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("POSITION");
            Grade = extras.getString("grade");
            Syllabus = extras.getString("Syllabus");
            token = extras.getString("token");
        }

//        Button GradesBtn = (Button)findViewById(R.id.GradesBtn);
//        GradesBtn.setText(Grade);
//        Button SylBtn = (Button)findViewById(R.id.SylBtn);
//        SylBtn.setText(Syllabus);

        //add item to arraylist
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
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), logos, listener);
        recyclerView.setAdapter(customAdapter);
    }

    private void setOnClicklistener() {
        listener = new CustomAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("token",token);
                Intent intent = new Intent(getApplicationContext(),VODListActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }

    private void setInfo() {
        logos.add(R.drawable.sinhala);
        logos.add(R.drawable.science);
        logos.add(R.drawable.maths);
        logos.add(R.drawable.history);
        logos.add(R.drawable.geography);
        logos.add(R.drawable.english);
        logos.add(R.drawable.tamil);
        logos.add(R.drawable.ict);
    }
}