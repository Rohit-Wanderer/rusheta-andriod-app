package com.example.rusheta;

import android.graphics.Bitmap;

import java.util.Date;

public class UserImage extends UserMessage {

    private Bitmap image;

    UserImage(int msgType, Bitmap image, Date timeReceived, String sender) {
        super(msgType, timeReceived, sender);
        this.image = image;
    }

    @Override
    public Bitmap getMessage() {
        return image;
    }
}
