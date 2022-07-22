package com.learntv.studybuddy;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.learntv.studybuddy.adapters.MCQAdapter;
import com.learntv.studybuddy.retrofit.Api;
import com.learntv.studybuddy.retrofit.LessonData;
import com.learntv.studybuddy.retrofit.LessonResponse;
import com.learntv.studybuddy.retrofit.McqData;
import com.learntv.studybuddy.retrofit.McqResponse;
import com.learntv.studybuddy.support.ShowErrors;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MCQActivity extends BaseActivity {
    MCQAdapter.RecyclerViewClickListener listener;
    MCQAdapter.RecyclerViewLongClickListener longClickListener;
    private RecyclerView answersList;
    private TextView question,headingQ;
    private MCQAdapter mcqAdapter;
    private TextView countDownTimer;
    public int counter = 60;
    private int mcqCount = 0, mcqCountPlus = 1;
    private String token;
    private int videoId;
    private LessonResponse lessonResponse;
    private final String apiKey="76b3d18521fa7f12d7ea0402214408140c108a17634d97c5";
    private final String apiSecret="v1iMCBWxbq2UE4k/GRWt7xRjMkZGvoCSlZNqu+yC7mDIOp+/2X/6AANpuwaBARkxyEDQzYo0nZrAB5IMLwK6Sw==";
    private final String ansApiKey="202a3b589d98404fdd00f1583959e1abb62653ce2d45b1e3";
    private final String ansApiSecret="dgXsZwxbb6j6r3wH6jLwfc0WwsMFId1d8sy8pJjgJcvuHvGl5EenFS3NehCbeB2+wh8mmS4QZPHcHDRz/px9ZQ7j";
    private LessonData lessonData;
    boolean checked = false,submit = false;
    private int tempPosition=10;
    private CountDownTimer timerCD;
    private McqResponse mcqResponse;
    private CircularProgressIndicator mcqProgressCircular;
    private int earning = 0;
    private Dialog dialog;
    private String started_at,ended_at;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            token = bundle.getString("token");
            videoId = bundle.getInt("videoId");
            Log.d("onCreate: ", token);

            getLessonData();
        }
        answersList = findViewById(R.id.answersList);
        countDownTimer = findViewById(R.id.countDownTimer);
        headingQ = findViewById(R.id.headingQ);
        question = findViewById(R.id.question);
        mcqProgressCircular = findViewById(R.id.progress_circular_mcq);

        timerCD = new timer().countDownTime;
        timerCD.start();

        ShapeableImageView shapeableImageView = findViewById(R.id.mcqHeadingImageView);
        Picasso.get().load("http://edutv.lk/video/thumb/maths-06-t1-l01-ep02-q-1.jpg").placeholder(R.drawable.gradient_background).into(shapeableImageView);


        started_at = getDateTime();
    }

    private String getDateTime(){
        Date startAt = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(startAt);
    }

    private void getLessonData() {
        (Api.getClient().lesson_data(
                apiKey,
                apiSecret,
                token,
                videoId
        )).enqueue(new Callback<LessonResponse>() {
            @Override
            public void onResponse(@NonNull Call<LessonResponse> call, @NonNull Response<LessonResponse> response) {
                lessonResponse = response.body();
                if (lessonResponse!=null){
                    if (lessonResponse.getData()!=null&&lessonResponse.getErrors()==null){
                        lessonData = lessonResponse.getData().get(0);
                        createView();
                    }else {
                        if(lessonResponse.getErrors()!=null){
                            pushErrors(lessonResponse.getErrors().getStatusCode(),lessonResponse.getErrors().getDescription());
                        }
                    }
                }else {
                    pushErrors("104","Video Not Found");
                }

            }

            @Override
            public void onFailure(@NonNull Call<LessonResponse> call,@NonNull Throwable t) {
                pushErrors("104","Video Not Found");
            }
        });
    }

    private void createView() {
        if (mcqCount<lessonResponse.getData().get(0).getMcq().size()){
            String questionText = lessonResponse.getData().get(0).getMcq().get(mcqCount).getQuestion();
            String headingQText = lessonResponse.getData().get(0).getMcq().get(mcqCount).getHeading();
            headingQ.setText(headingQText);
            question.setText(questionText);
            setDataInRecyclerView();
        }else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            Intent intent = new Intent(MCQActivity.this,McqEarningActivity.class);
            intent.putExtra("earning",earning);
            startActivity(intent);
            finish();
        }
    }

    public class timer{
        CountDownTimer countDownTime = new CountDownTimer(60000, 1000){
            public void onTick(long millisUntilFinished){
                countDownTimer.setText(String.valueOf(counter));
                mcqProgressCircular.setProgressCompat(counter,true);
                Log.d("onTick: ", String.valueOf(counter));
                counter--;
            }
            public  void onFinish(){
                mcqProgressCircular.setProgressCompat(0,true);
                countDownTimer.setText("0");
                ended_at = getDateTime();
                checkAnswer(tempPosition);
            }
        };
    }

    public void setDataInRecyclerView(){
        setOnClickListener();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MCQActivity.this);
        answersList.setLayoutManager(linearLayoutManager);
        mcqAdapter = new MCQAdapter(getApplicationContext(),lessonData,listener,longClickListener,mcqCount);
        answersList.setAdapter(mcqAdapter);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setOnClickListener() {
        listener = (v, position, button, lessonData) -> {
            if (!submit) {
                selectAnswer(position);
                if (!checked) {
                    Toast.makeText(getApplicationContext(), "Touch and hold for fast submitting", Toast.LENGTH_SHORT).show();
                    checked = true;
                }
            }
        };

        longClickListener = (v, position, button, number, lessonData) -> {
            if (!submit){
                submit = true;
                checkAnswer(position);
                button.setBackground(getResources().getDrawable(R.drawable.selected_answer_btn,getTheme()));
                number.setBackground(getResources().getDrawable(R.drawable.selected_answer_btn,getTheme()));
            }
            return true;
        };


    }

    private void selectAnswer(int position) {
        mcqAdapter.McqChange(mcqCount,position);
        tempPosition = position;
    }

    private void checkAnswer(int position) {
        timerCD.cancel();
        int finalPosition;

        if (!submit) {
            finalPosition = tempPosition;
        } else {
            finalPosition = position;
        }
        mcqAdapter.McqSubmit(true,finalPosition);
        checkAnswerStatus(finalPosition, mcqCount);

    }

    @SuppressLint("SetTextI18n")
    private void nextView(){

        mcqCount = mcqCount + 1;
        mcqCountPlus = mcqCount+1;

        checked = false;
        submit = false;


        createView();
        timerCD.cancel();
        countDownTimer.setText("60");
        counter = 60;
        mcqProgressCircular.setProgressCompat(counter,true);
        timerCD = new timer().countDownTime;
        timerCD.start();
    }

    private void checkAnswerStatus(int position, int qId){
        int quetionId = lessonResponse.getData().get(0).getMcq().get(qId).getId();
        int optionId;
        if (position<4){
            optionId = lessonResponse.getData().get(0).getMcq().get(qId).getOptions().get(position).getId();
        }else {
            optionId = lessonResponse.getData().get(0).getMcq().get(qId).getOptions().get(0).getId();
        }
        Log.d("checkAnswerStatus: ",ansApiKey);
        Log.d("checkAnswerStatus: ",ansApiSecret);
        Log.d("checkAnswerStatus: ",token);
        Log.d("checkAnswerStatus: ", String.valueOf(quetionId));
        Log.d("checkAnswerStatus: ", String.valueOf(optionId));
        Log.d("checkAnswerStatus: ", String.valueOf(videoId));
        Log.d("checkAnswerStatus: ", started_at);
        Log.d("checkAnswerStatus: ", ended_at);
        (Api.getClient().check_mcq(
                token,
                ansApiKey,
                ansApiSecret,
                videoId,
                quetionId,
                optionId,
                started_at,
                ended_at


        )).enqueue(new Callback<McqResponse>() {
            @Override
            public void onResponse(@NonNull Call<McqResponse> call,@NonNull Response<McqResponse> response) {
                mcqResponse = response.body();
                if (mcqResponse!=null){
                    if (mcqResponse.getData()!=null){
                        McqData mcqData = mcqResponse.getData();
                        int correctOption = mcqData.getOptionId();
                        int sQuestionId = mcqData.getQuestionId();
                        boolean answerStates = mcqData.getAnswer();
                        answerView(answerStates);
                        addEarn();
                        Log.d("onResponse: ", String.valueOf(earning));
                        String bool = String.valueOf(mcqResponse.getData().getAnswer());
                        Log.d("onResponse: ",bool);
                    }else{
                        if (mcqResponse.getErrors()!=null){
                            String Errors = mcqResponse.getErrors().getDescription();
                            String errorCode = mcqResponse.getErrors().getStatusCode();
                            if(errorCode.equals("1016")){
                                if (position<4) {
                                    boolean isCorrect = lessonResponse.getData().get(0).getMcq().get(qId).getOptions().get(position).getIsCorrect();
                                    Toast.makeText(MCQActivity.this, "You do not earn coins from this question. Because you have already answered this question", Toast.LENGTH_LONG).show();
                                    answerView(isCorrect);
                                }else{
                                    answerView(false);
                                }
                            }else{
                                Log.d("onResponse: ",errorCode);
                            pushErrors(errorCode,Errors);
                            }
                        }
                    }
                }else{
                    pushErrors("0","Sorry! Something went wrong please try again later");
                }
            }

            @Override
            public void onFailure(@NonNull Call<McqResponse> call,@NonNull Throwable t) {
                Log.d("onResponse: ",t.toString());
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void answerView(boolean answerState) {
        dialog = new Dialog(this);
        @SuppressLint("InflateParams")
        View mcqFrag = getLayoutInflater().inflate(R.layout.modal_mcq,null);
        ShapeableImageView correct = mcqFrag.findViewById(R.id.correct);
        ShapeableImageView wrong = mcqFrag.findViewById(R.id.wrong);
        AppCompatTextView stateText = mcqFrag.findViewById(R.id.stateText);
        ImageButton backButton = mcqFrag.findViewById(R.id.mcq_modal_back_button);
        MaterialButton goForward = mcqFrag.findViewById(R.id.mcq_modal_go_forward_btn);

        if (answerState){
            correct.setVisibility(View.VISIBLE);
            wrong.setVisibility(View.GONE);
            stateText.setText(R.string.correct);
            stateText.setTextColor(getResources().getColor(R.color.green,getTheme()));
        }else {
            wrong.setVisibility(View.VISIBLE);
            correct.setVisibility(View.GONE);
            stateText.setText(R.string.wrong);
            stateText.setTextColor(getResources().getColor(R.color.red,getTheme()));
        }
        dialog.getWindow().getAttributes().windowAnimations = R.style.mcqDialogAnimation;
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.mcq_modal_bg));
        dialog.setContentView(mcqFrag);
        dialog.setCancelable(false);
        goForward.setOnClickListener(view -> {
            addEarn();
            nextView();
            dialog.dismiss();
        });
        backButton.setOnClickListener(view -> {
            finish();
        });
        dialog.show();
        Log.d("answerView: ","answerView");
    }

    private void addEarn() {
        if (mcqResponse.getData()!=null){
            if (mcqResponse.getData().getEarning()!=null){
                String earningStr = mcqResponse.getData().getEarning();
                if (earningStr.equals("")){
                    earningStr = "0";}
                earning += Integer.parseInt(earningStr);
            }
        }
    }

    private void pushErrors(String errorcode, String description) {
        ShowErrors showErrors = new ShowErrors();
        String errors = showErrors.Errors(errorcode,description);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MCQActivity.this);
        builder.setMessage(errors);
        builder.setCancelable(false);
        builder.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MCQActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerCD!=null)
        timerCD.cancel();
    }
}