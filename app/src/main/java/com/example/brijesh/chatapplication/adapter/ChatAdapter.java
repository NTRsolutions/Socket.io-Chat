package com.example.brijesh.chatapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.brijesh.chatapplication.R;
import com.example.brijesh.chatapplication.model.ChatModel;

import java.util.ArrayList;
import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContex;
    private List<ChatModel> chatModels;
    private int mType=0;


    public ChatAdapter(Context contex, List<ChatModel> chatModels) {
        this.mContex = contex;
        this.chatModels = chatModels;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==0){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row_user, parent, false);
            return new ChatHolder(v);
        }else if(viewType==1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row_other, parent, false);
            return new ChatHolder(v);
        }else if(viewType == 2){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_exit,parent,false);
            return new ExitHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(mType==0){ //our message
            ChatHolder chHolder = (ChatHolder) holder;
            chHolder.msg.setText(chatModels.get(position).getMsg());
            chHolder.time.setText(""+chatModels.get(position).getTime());
        }else if(mType==1){ //other user's message
            ChatHolder chHolder = (ChatHolder) holder;
            chHolder.msg.setText(chatModels.get(position).getMsg());
            chHolder.time.setText(""+chatModels.get(position).getTime());
            chHolder.name.setText(chatModels.get(position).getName());
        }else if(mType == 2){ // user left/join/total participants  message
            ExitHolder exitHolder = (ExitHolder) holder;
            String exitText = chatModels.get(position).getName()+" "+chatModels.get(position).getMsg();
            exitHolder.name.setText(exitText);
        }
    }

    @Override
    public int getItemViewType(int position) {
        mType = chatModels.get(position).getType();
        return chatModels.get(position).getType();
    }


    @Override
    public int getItemCount() {
        return chatModels.size();
    }

    public void addNewMsg(ChatModel chatModel){
        chatModels.add(chatModel);
        notifyDataSetChanged();
    }

}
