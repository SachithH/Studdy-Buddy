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

public class SubjectAdapter extends RecyclerView.Adapter <SubjectAdapter.MyViewHolder>{
    Context context;
    ArrayList<Integer> drawable;
    private RecyclerViewClickListener listener;

    public SubjectAdapter(Context applicationContext, ArrayList<Integer> drawable, RecyclerViewClickListener listener, int column){
        this.context = applicationContext;
        this.drawable = drawable;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //inflate the item layout
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_subjects,viewGroup,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //set the data
        holder.getImageView().setImageResource(drawable.get(position));
    }

    @Override
    public int getItemCount() {
        return drawable.size();
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
