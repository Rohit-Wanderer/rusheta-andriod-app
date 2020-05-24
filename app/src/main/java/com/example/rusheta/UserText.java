package com.example.rusheta;

import java.util.Date;

public class UserText extends UserMessage {

    private String text;

    UserText(int msgType, String text, Date timeReceived, String sender) {
        super(msgType, timeReceived, sender);
        this.text = text;
    }

    @Override
    public String getMessage() {
        return text;
    }
}
