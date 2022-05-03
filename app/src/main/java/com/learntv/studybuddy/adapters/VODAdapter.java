package com.learntv.studybuddy.adapters;

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

import java.util.List;

public class VODAdapter extends RecyclerView.Adapter<VODAdapter.UsersViewHolder> {
    private final List<com.learntv.studybuddy.retrofit.List> dataList;
    Context context;
    VODResponse VODResponseData;

    public VODAdapter(Context context, VODResponse VODResponseData) {
        this.VODResponseData = VODResponseData;
        this.context = context;
        this.dataList = VODResponseData.getList();
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
        //set the data
        holder.name.setText("id:"+dataList.get(position).getFile());
        holder.thumb.setImageURI(null);
        holder.thumb.setImageURI(Uri.parse(dataList.get(position).getImage()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView thumb;
        public UsersViewHolder(View view) {
            super(view);
            thumb = view.findViewById(R.id.VODThumb);
            name = view.findViewById(R.id.VODName);
        }
    }
}
