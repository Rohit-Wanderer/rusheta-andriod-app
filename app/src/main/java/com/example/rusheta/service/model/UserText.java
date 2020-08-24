package com.example.rusheta.service.model;

import java.util.Date;

public class UserText extends UserMessage {

    private String text;

    public UserText(int msgType, String text, Date timeReceived, String sender) {
        super(msgType, timeReceived, sender);
        this.text = text;
    }

    @Override
    public String getMessage() {
        return text;
    }
}
