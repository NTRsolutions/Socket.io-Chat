package com.example.brijesh.chatapplication.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ListChangeRegistry;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.brijesh.chatapplication.R;
import com.example.brijesh.chatapplication.SocketConnection;
import com.example.brijesh.chatapplication.adapter.ChatAdapter;
import com.example.brijesh.chatapplication.databinding.ActivityChatBinding;
import com.example.brijesh.chatapplication.model.ChatModel;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private String userName;

    private List<ChatModel> modelList;
    private ChatAdapter chatAdapter;
    private Socket mSocket;

    private boolean isExited = false;
    private LinearLayoutManager lm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chat);
        setSupportActionBar(binding.myToolbar);
        userName = getIntent().getStringExtra("name");
        binding.toolbarTitle.setText(userName);

        modelList = new ArrayList<>();
        chatAdapter = new ChatAdapter(ChatActivity.this, modelList);
        lm = new LinearLayoutManager(ChatActivity.this);
        lm.setStackFromEnd(true);
        binding.rvChat.setLayoutManager(lm);
        binding.rvChat.setAdapter(chatAdapter);
        binding.rvChat.scrollToPosition(binding.rvChat.getAdapter().getItemCount() - 1);


        binding.ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                String message = binding.editText.getText().toString().trim();
                if(!message.equalsIgnoreCase("")){
                    binding.editText.setText("");
                    emitSocket(message);
                    chatAdapter.addNewMsg(new ChatModel("",message,0,""));
                    binding.rvChat.scrollToPosition(binding.rvChat.getAdapter().getItemCount() - 1);
                }
                else {
                    Toast.makeText(ChatActivity.this, "Please enter your message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        joinEmit();

    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void emitSocket(String message){
        JSONObject sendMessage = new JSONObject();
        try {
            sendMessage.put("message",message);
            sendMessage.put("type",1);
            sendMessage.put("name",userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("chat message",sendMessage);
    }


    private void joinEmit(){
        mSocket = SocketConnection.getInstance().getSocket();
        mSocket.on("receive message",onNewMessage);
        mSocket.connect();
        Log.d("mytag", "joinEmit: "+ mSocket.connected());
        JSONObject joinMessage = new JSONObject();
        try {
            joinMessage.put("message","joined this room");
            joinMessage.put("type",2);
            joinMessage.put("name",userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("chat message",joinMessage);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = null;
                    try {
                        data = (JSONObject) args[0];
                    }catch (ClassCastException e){
                        String data2 = (String) args[0];
                        try {
                            data = new JSONObject(data2);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    Log.d("mytag", "run: "+data);

                    String message = null;
                    try {
                        message = data.getString("message");
                        int type = data.getInt("type");
                        String name = data.getString("name");

                        chatAdapter.addNewMsg(new ChatModel(name,message,type,getTime()));
                        binding.rvChat.scrollToPosition(binding.rvChat.getAdapter().getItemCount() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private String getTime(){
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy h:mm a");
        String timeText = outputFormat.format(new Date());
        return timeText;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isExited){
            exit();
        }
    }

    private void exit(){
        JSONObject leaveMessage = new JSONObject();
        try {
            leaveMessage.put("message","left this room");
            leaveMessage.put("type",2);
            leaveMessage.put("name",userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.off("chat message");
        mSocket.emit("disconnect room",leaveMessage);
        SocketConnection.getInstance().disconnect();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(ChatActivity.this)
                .setMessage("Are you sure you want to exit this room?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isExited = true;
                        exit();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
    }


}
