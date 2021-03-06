package com.example.brijesh.chatapplication;

import android.app.Application;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class SocketConnection extends Application {
    private static SocketConnection mInstance;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.ChatLive);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static SocketConnection getInstance(){
        return mInstance;
    }

    public  Socket getSocket() {
        return mSocket;
    }

    public Socket disconnect(){
        return mSocket.disconnect();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
