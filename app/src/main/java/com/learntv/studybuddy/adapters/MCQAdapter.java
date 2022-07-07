package com.learntv.studybuddy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learntv.studybuddy.R;
import com.learntv.studybuddy.retrofit.LessonData;
import com.learntv.studybuddy.retrofit.LessonMcqOption;

import java.util.List;

public class MCQAdapter extends RecyclerView.Adapter<MCQAdapter.MyViewHolder> {
    private final Context context;
    RecyclerViewClickListener listener;
    RecyclerViewLongClickListener longClickListener;
    LessonData lessonData;
    List<LessonMcqOption> lessonMcqOption;
    private final String[] listNumber = {
            "A",
            "B",
            "C",
            "D"
    };
    private int selectVal=10, finalPos = 10;
    private int qId;
    private boolean checkVal=false;

    public MCQAdapter(Context context, LessonData lessonData, RecyclerViewClickListener listener, RecyclerViewLongClickListener longClickListener, int qId) {
        this.context = context;
        this.lessonData = lessonData;
        this.listener = listener;
        this.longClickListener = longClickListener;
        this.qId = qId;
        lessonMcqOption = lessonData.getMcq().get(qId).getOptions();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void McqChange(int qId, int selectVal){
        this.qId = qId;
        this.selectVal = selectVal;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void McqSubmit(boolean checkVal,int finalPos){
        this.checkVal = checkVal;
        this.finalPos = finalPos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_mcq,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return lessonData.getMcq().get(qId).getOptions().size();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Button mcqButton = holder.getButton();
        Button number = holder.getNumber();
        number.setText(listNumber[position]);
        mcqButton.setText(lessonData.getMcq().get(qId).getOptions().get(position).getOption());
        if (selectVal==position&&!checkVal){
            mcqButton.setBackground(context.getResources().getDrawable(R.drawable.selected_answer_btn,context.getTheme()));
            number.setBackground(context.getResources().getDrawable(R.drawable.selected_answer_btn,context.getTheme()));
        }else {
            mcqButton.setBackground(context.getResources().getDrawable(R.drawable.mcq_answer_btn,context.getTheme()));
            number.setBackground(context.getResources().getDrawable(R.drawable.mcq_answer_btn,context.getTheme()));
        }
        if (checkVal){
            if (lessonMcqOption.get(position).getIsCorrect()){
                mcqButton.setBackground(context.getResources().getDrawable(R.drawable.mcq_correct_btn,context.getTheme()));
                number.setBackground(context.getResources().getDrawable(R.drawable.mcq_correct_btn,context.getTheme()));
            }else {
                if (finalPos==position){
                    mcqButton.setBackground(context.getResources().getDrawable(R.drawable.mcq_wrong_answer,context.getTheme()));
                    number.setBackground(context.getResources().getDrawable(R.drawable.mcq_wrong_answer,context.getTheme()));
                }
            }
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        private Button button;
        private Button number;
        public MyViewHolder(@NonNull View view) {
            super(view);
            number = (Button) itemView.findViewById(R.id.number);
            button = (Button) itemView.findViewById(R.id.button);

            number.setOnClickListener(this);
            number.setOnLongClickListener(this);
            button.setOnClickListener(this);
            button.setOnLongClickListener(this);

        }

        public Button getButton() {
            return button;
        }
        public Button getNumber() {return number;}

        @Override
        public void onClick(View view) {
            listener.onClick(view,getBindingAdapterPosition(), getButton(),lessonData);
        }

        @Override
        public boolean onLongClick(View view) {
            longClickListener.onLongClick(view,getBindingAdapterPosition(),getButton(),getNumber(),lessonData);
            return true;
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position, Button button, LessonData lessonData);
    }

    public interface RecyclerViewLongClickListener{
        boolean onLongClick(View v, int position, Button button, Button number, LessonData lessonData);
    }
}
