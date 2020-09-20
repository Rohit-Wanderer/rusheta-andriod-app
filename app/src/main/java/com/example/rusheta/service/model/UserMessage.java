package com.example.rusheta.service.model;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import java.util.UUID;

@androidx.room.Entity(tableName = "messages_table")
public class UserMessage {
    @NonNull
    @PrimaryKey
    private String messageId;

    private int msgType;
    private String timeReceived;
    private String contactId;
    private String sender;
    private String message;

    public UserMessage(int msgType, String timeReceived, String contactId, String sender, String message) {
        this.messageId = UUID.randomUUID().toString();
        this.msgType = msgType;
        this.timeReceived = timeReceived;
        this.contactId = contactId;
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setContactId(String message) {
        this.message = message;
    }

    public String getContactId() {
        return contactId;
    }

    @NonNull
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(@NonNull String messageId) {
        this.messageId = messageId;
    }

    public String getTimeReceived() {
        return timeReceived;
    }

    public int getMsgType() {
        return msgType;
    }


}
