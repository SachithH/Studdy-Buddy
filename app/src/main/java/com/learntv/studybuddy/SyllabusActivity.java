package com.learntv.studybuddy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SyllabusActivity extends AppCompatActivity implements View.OnClickListener {
    Bundle extra_details = new Bundle();
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
    private String token;
    private String grade;
    private String[] syllabus = {
            "Syllabus-2011",
            "Syllabus-2016",
            "Syllabus-2023"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            grade = extras.getString("grade");
            position = extras.getInt("POSITION");
            token = extras.getString("token");
            Toast.makeText(SyllabusActivity.this,grade,Toast.LENGTH_SHORT).show();
        }
            Button GradeBtn = findViewById(R.id.GradesBtn);
            GradeBtn.setText(Grades[position]);

//            Syllabus
        Button Syllabus_2011 = (Button) findViewById(R.id.Syllabus2011);
        Button Syllabus_2016 = (Button) findViewById(R.id.Syllabus2016);
        Button Syllabus_2023 = (Button) findViewById(R.id.Syllabus2023);

        Syllabus_2011.setOnClickListener(this);
        Syllabus_2016.setOnClickListener(this);
        Syllabus_2023.setOnClickListener(this);


    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
      int syl_pos = 0;
        extra_details.putString("Syllabus","Syllabus 2011");
        extra_details.putInt("POSITION",position);
        extra_details.putString("token",token);
        extra_details.putString("grade",grade);

        switch(view.getId()){
            case R.id.Syllabus2011:
                break;
            case R.id.Syllabus2016:
                syl_pos = 1;
                break;
            case R.id.Syllabus2023:
                syl_pos = 2;
                break;
        }

        extra_details.putString("syllabus",syllabus[syl_pos]);
        Intent intent = new Intent(getApplicationContext(),SubjectsActivity.class);
        intent.putExtras(extra_details);
        startActivity(intent);
    }
}