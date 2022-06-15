package com.learntv.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.learntv.studybuddy.adapters.CustomAdapter;

import java.util.ArrayList;

public class GradesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    ArrayList<String> firstColumn = new ArrayList<>();
    ArrayList<String> secondColumn = new ArrayList<>();
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
        recyclerView2 = findViewById(R.id.recyclerView2);
        setAdapter();
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