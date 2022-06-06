package com.learntv.studybuddy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learntv.studybuddy.R;
import com.learntv.studybuddy.retrofit.VODResponse;

import java.util.ArrayList;
import java.util.List;

public class VODAdapter extends RecyclerView.Adapter<VODAdapter.UsersViewHolder>{
    Context context;
    List<VODResponse> VODResponseData;
    List<String> allDesc = new ArrayList<>();
    RecyclerViewClickListener listener;

    public VODAdapter(Context context, List<VODResponse> VODResponseData, RecyclerViewClickListener listener) {
        this.VODResponseData = VODResponseData;
        this.context = context;
        this.listener = listener;
    }

    // method for filtering our recyclerview items.
    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<VODResponse> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        VODResponseData = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_vod, parent, false);
        UsersViewHolder usersViewHolder = new UsersViewHolder(view);
        return usersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        allDesc.add(VODResponseData.get(position).getLessonE());
        //set the data
        holder.name.setText(VODResponseData.get(position).getVideo().getUrl());
        if(VODResponseData.get(position).getImage()!=null){
            holder.thumb.setImageURI(null);
            holder.thumb.setImageURI(Uri.parse(VODResponseData.get(position).getImage()));
        }
    }

    @Override
    public int getItemCount() {
        return VODResponseData.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        ImageView thumb;
        public UsersViewHolder(View view) {
            super(view);
            thumb = view.findViewById(R.id.VODThumb);
            name = view.findViewById(R.id.VODName);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getBindingAdapterPosition());
        }


    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }


}
