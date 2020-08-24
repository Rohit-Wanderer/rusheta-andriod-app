package com.example.rusheta.service.model;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@androidx.room.Entity(tableName = "chats_table")
public class Chat implements Serializable {

    @NonNull
    @PrimaryKey
    private String contactId;

    @NonNull
    private String phone;
    @NonNull
    private String name;
    @NonNull
    private  String AESKey;

    public Chat(@NonNull String contactId, @NonNull String phone, @NonNull String name,@NonNull String AESKey) {
        this.contactId = contactId;
        this.phone = phone;
        this.name = name;
        this.AESKey = AESKey;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    @NonNull
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @NonNull
    public void setName(String name) {
        this.name = name;
    }
    @NonNull
    public String getContactId() {
        return contactId;
    }

    @NonNull
    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    @NonNull
    public String getAESKey() {
        return AESKey;
    }

    @NonNull
    public void setAESKey(@NonNull String AESKey) {
        this.AESKey = AESKey;
    }
}
