package com.learntv.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class SyllabusActivity extends AppCompatActivity {
    Bundle extra_details = new Bundle();
    int position = 0;
    private String extra_message;
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
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("POSITION");
            token = extras.getString("token");
        }
            Button GradeBtn = findViewById(R.id.GradesBtn);
            GradeBtn.setText(Grades[position]);

//            Syllabus
        Button Syllabus_2011 = findViewById(R.id.Syllabus2011);
        Syllabus_2011.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extra_details.putString("Syllabus","Syllabus 2011");
                extra_details.putInt("POSITION",position);
                extra_details.putString("token",token);
                Intent intent = new Intent(getApplicationContext(),SubjectsActivity.class);
                intent.putExtras(extra_details);
                startActivity(intent);
            }
        });

    }
}