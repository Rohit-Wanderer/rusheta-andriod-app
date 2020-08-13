package com.example.rusheta;

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

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    @NonNull
    public String getAESKey() {
        return AESKey;
    }

    public void setAESKey(@NonNull String AESKey) {
        this.AESKey = AESKey;
    }
}
