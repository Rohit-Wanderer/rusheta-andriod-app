package com.example.rusheta;

import java.util.Date;

public abstract class UserMessage {
    private int msgType ;
    private Date timeReceived;
    private String sender;


    UserMessage(int msgType, Date timeReceived, String sender) {

        this.msgType = msgType;
        this.timeReceived = timeReceived;
        this.sender = sender;
    }

    public int  getMsgType(){
        return msgType;
    }
    public Date getTimeReceived(){
        return timeReceived;
    }
    public String getMsgSender(){
        return sender;
    }

    public abstract Object getMessage();
}
