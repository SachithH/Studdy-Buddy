package com.learntv.studybuddy.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learntv.studybuddy.GradesActivity;
import com.learntv.studybuddy.MainActivity;
import com.learntv.studybuddy.R;
import com.learntv.studybuddy.SyllabusActivity;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter <CustomAdapter.MyViewHolder>{
    Context context;
    ArrayList<Integer> logos;
    private RecyclerViewClickListener listener;

    public CustomAdapter(Context applicationContext, ArrayList<Integer> logos, RecyclerViewClickListener listener){
        this.context = applicationContext;
        this.logos = logos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //inflate the item layout
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_gridview,viewGroup,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //set the data
        holder.getImageView().setImageResource(logos.get(position));
    }

    @Override
    public int getItemCount() {
        return logos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.icon);

            itemView.setOnClickListener(this);
        }

        public ImageView getImageView() {
            return imageView;
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view,getBindingAdapterPosition());

        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
