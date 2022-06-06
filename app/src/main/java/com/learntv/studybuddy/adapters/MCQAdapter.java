package com.learntv.studybuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learntv.studybuddy.R;

import java.util.ArrayList;

public class MCQAdapter extends RecyclerView.Adapter<MCQAdapter.MyViewHolder> {
    private Context context;
    VODAdapter.RecyclerViewClickListener listener;
    ArrayList<String> mcqList = new ArrayList<>();

    public MCQAdapter(Context context) {
        this.context = context;
        mcqList.add("ඕලූ");
        mcqList.add("නෙළුම්");
        mcqList.add("මානෙල්");
        mcqList.add("අරලිය");
    }

    @NonNull
    @Override
    public MCQAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_mcq,viewGroup,false);
        return new MyViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mcqList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.getButton().setText(mcqList.get(position));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button button;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            button = (Button) itemView.findViewById(R.id.button);
        }

        public Button getButton() {
            return button;
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view,getBindingAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
