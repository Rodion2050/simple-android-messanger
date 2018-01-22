package com.example.r205.messanger.utils;

/**
 * Created by r205 on 13.01.2018.
 */

public class MessageInfo {
    private int senderID;
    private int receiverID;
    private String receiverName;
    private String senderName;
    private String text;

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public MessageInfo(int senderID, int receiverID, String text) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.text = text;
    }

    public MessageInfo(){
        this.senderID = -1;
        this.receiverID = -1;
        this.text = "";
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "senderID=" + senderID +
                ", receiverID=" + receiverID +
                ", text='" + text + '\'' +
                '}';
    }
}

