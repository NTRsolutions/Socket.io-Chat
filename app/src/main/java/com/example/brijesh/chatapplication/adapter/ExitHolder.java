package com.example.brijesh.chatapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.brijesh.chatapplication.R;

public class ExitHolder extends RecyclerView.ViewHolder {
    TextView name;
    public ExitHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.tv_exit);
    }
}
