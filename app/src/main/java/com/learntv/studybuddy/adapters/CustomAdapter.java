package com.learntv.studybuddy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learntv.studybuddy.R;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter <CustomAdapter.MyViewHolder>{
    private  int column;
    Context context;
    ArrayList<String> logos;
    private RecyclerViewClickListener listener;
    int[] colOne = {0,2,4,6};
    int[] colTwo = {1,3,5,7};

    public CustomAdapter(Context applicationContext, ArrayList<String> logos, RecyclerViewClickListener listener, int column){
        this.context = applicationContext;
        this.logos = logos;
        this.listener = listener;
        this.column = column;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //inflate the item layout
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_grades,viewGroup,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //set the data
        holder.getTextView().setText(logos.get(position));
    }

    @Override
    public int getItemCount() {
        return logos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.grade);

            itemView.setOnClickListener(this);
        }

        public TextView getTextView() {
            return textView;
        }

        @Override
        public void onClick(View view) {
            int fixedPos;
            switch (column){
                case 1:
                    fixedPos = colOne[getBindingAdapterPosition()];
                    break;
                default:
                    fixedPos = colTwo[getBindingAdapterPosition()];
                    break;
            }
            listener.onClick(view,fixedPos);

        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
