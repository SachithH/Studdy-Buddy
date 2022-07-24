package com.learntv.studybuddy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learntv.studybuddy.R;
import com.learntv.studybuddy.retrofit.VODResponseData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VODAdapter extends RecyclerView.Adapter<VODAdapter.UsersViewHolder>{
    private final Context context;
    List<VODResponseData> vodResponseData;
    RecyclerViewClickListener listener;

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
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_vod, viewGroup, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        //set the data
        String headingText= vodResponseData.get(position).getHeading();
        String shortDescText= vodResponseData.get(position).getShortDesc();
        holder.getHeading().setText(headingText);
        holder.getShortDesc().setText(shortDescText);

        if (vodResponseData.get(position).getFavoritedState()!=null){
            if (vodResponseData.get(position).getFavoritedState().equals("True")){
                holder.getFavouriteBtn().setChecked(true);
            }else{
                holder.favouriteBtn.setChecked(false);
            }
                Log.d("onBindViewHolder: ", String.valueOf(vodResponseData.get(position).getId()));
                Log.d("onBindViewHolder: ",vodResponseData.get(position).getFavoritedState());
        }

        String uri = vodResponseData.get(position).getThumb();
        Picasso.get().load(uri).placeholder(R.drawable.gradient_background).into(holder.getVodThumb());
    }

    @Override
    public int getItemCount() {
        return vodResponseData.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final CheckBox favouriteBtn;
        private final TextView heading;
        private final TextView shortDesc;
        private final ImageView vodThumb;
        
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

        public ImageView getVodThumb(){
            return vodThumb;
        }

        public CheckBox getFavouriteBtn(){
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
