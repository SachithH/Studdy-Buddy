package com.learntv.studybuddy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.learntv.studybuddy.R;
import com.learntv.studybuddy.retrofit.VODResponseData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VODAdapter extends RecyclerView.Adapter<VODAdapter.UsersViewHolder>{
    Context context;
    List<VODResponseData> vodResponseData;
    RecyclerViewClickListener listener;
    private final String[] url = {
            "http://edutv.lk/img/grade-06.jpg",
            "http://edutv.lk/img/grade-07.jpg",
            "http://edutv.lk/img/grade-08.jpg",
            "http://edutv.lk/img/grade-09.jpg",
            "http://edutv.lk/img/grade-10.jpg",
            "http://edutv.lk/img/grade-11.jpg",
            "http://edutv.lk/img/grade-12.jpg"
    };

    public VODAdapter(Context context, List<VODResponseData> vodResponseData, RecyclerViewClickListener listener) {
        this.vodResponseData = vodResponseData;
        this.context = context;
        this.listener = listener;
    }

    // method for filtering our recyclerview items.
    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<VODResponseData> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        vodResponseData = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_vod, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        //set the data
        String headingText= vodResponseData.get(position).getHeading();
        String shortDescText= vodResponseData.get(position).getShortDesc();
        holder.getHeading().setText(headingText);
        holder.getShortDesc().setText(shortDescText);

        int pos = position%7;

        String uri = url[pos];
        Picasso.get().load(uri).into(holder.getVodThumb());
    }

    @Override
    public int getItemCount() {
        return vodResponseData.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final AppCompatImageButton favouriteBtn;
        private final TextView heading;
        private final TextView shortDesc;
        private final AppCompatImageView vodThumb;
        
        public UsersViewHolder(View view) {
            super(view);
            vodThumb = view.findViewById(R.id.VODThumb);
            heading = view.findViewById(R.id.heading);
            shortDesc = view.findViewById(R.id.shortDesc);
            favouriteBtn = view.findViewById(R.id.favouriteBtn);

            vodThumb.setOnClickListener(this);
            heading.setOnClickListener(this);
            shortDesc.setOnClickListener(this);
            favouriteBtn.setOnClickListener(this);
        }

        public TextView getHeading(){
            return heading;
        }

        public TextView getShortDesc(){
            return shortDesc;
        }

        public AppCompatImageView getVodThumb(){
            return vodThumb;
        }

        public AppCompatImageButton getFavouriteBtn(){
            return favouriteBtn;
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getBindingAdapterPosition(), vodResponseData);
        }


    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position, List<VODResponseData> vodResponse);
    }


}
