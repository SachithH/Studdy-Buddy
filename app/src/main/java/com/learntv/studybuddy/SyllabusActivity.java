package com.learntv.studybuddy;

import static com.learntv.studybuddy.support.BottomNavigation.bottomNavigationFunction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SyllabusActivity extends BaseActivity implements View.OnClickListener {
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
    private int gradeId;
    private String[] syllabus = {
            "Syllabus-2011",
            "Syllabus-2016",
            "Syllabus-2023"
    };
    private int syl_pos = 0;



    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gradeId = extras.getInt("gradeId");
            token = extras.getString("token");
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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().setGroupCheckable(0,false,true);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            bottomNavigationFunction(getApplicationContext(),item.getItemId(),token);
            switch(item.getItemId()) {
                case R.id.homeBottom:
                case R.id.setting:
                case R.id.walletBottom:
                    return true;
                default:
                    return false;
            }

        });


    }


    private void setAction() {
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        extra_details.putString("token",token);
        extra_details.putInt("gradeId",gradeId);

        switch(view.getId()){
            case R.id.Syllabus2011:
                syl_pos = 2011;
                break;
            case R.id.Syllabus2016:
                syl_pos = 2016;
                break;
            case R.id.Syllabus2023:
                syl_pos = 2023;
                break;
        }

        extra_details.putInt("syllabusId",syl_pos);
        Intent intent = new Intent(getApplicationContext(),SubjectsActivity.class);
        intent.putExtras(extra_details);
        startActivity(intent);
    }
}