package com.learntv.studybuddy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.learntv.studybuddy.R;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int logos[];
    LayoutInflater inflater;

    public CustomAdapter(Context applicationContext, int[] logos){
        this.context = applicationContext;
        this.logos = logos;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return logos.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_gridview,null);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageResource(logos[i]);
        return view;
    }
}
