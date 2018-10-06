package com.example.brijesh.chatapplication.model;

public class ChatModel {
    private String name;
    private String msg;
    private int type;
    private String time;

    public ChatModel(String name, String message, int type, String time) {
        this.name=name;
        this.msg=message;
        this.type=type;
        this.time=time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
