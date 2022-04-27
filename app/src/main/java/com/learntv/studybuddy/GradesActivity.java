package com.learntv.studybuddy;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.learntv.studybuddy.adapters.CustomAdapter;

public class GradesActivity extends AppCompatActivity {
    GridView gradesGrid;

    int[] logos = {
            R.drawable.grade6,
            R.drawable.grade7,
            R.drawable.grade8,
            R.drawable.grade9,
            R.drawable.grade10,
            R.drawable.grade11,
            R.drawable.grade12,
            R.drawable.grade13
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        gradesGrid = findViewById(R.id.gradesGrid);
        //Create an object of custom Adapter
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), logos);
        gradesGrid.setAdapter(customAdapter);
    }
}