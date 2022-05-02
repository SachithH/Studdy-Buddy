package com.learntv.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;

public class SyllabusActivity extends AppCompatActivity {
    int position = 0;
    String []Grades = {
            "Grade 06",
            "Grade 07",
            "Grade 08",
            "Grade 09",
            "Grade 10",
            "Grade 11",
            "Grade 12",
            "Grade 13"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("POSITION");
        }
            Button SyllabusBtn = findViewById(R.id.SylBtn);
            SyllabusBtn.setText(Grades[position]);

    }
}