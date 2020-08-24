package com.example.rusheta.service.model;

import android.graphics.Bitmap;

import java.util.Date;

public class UserImage extends UserMessage {

    private Bitmap image;

    public UserImage(int msgType, Bitmap image, Date timeReceived, String sender) {
        super(msgType, timeReceived, sender);
        this.image = image;
    }

    @Override
    public Bitmap getMessage() {
        return image;
    }
}
