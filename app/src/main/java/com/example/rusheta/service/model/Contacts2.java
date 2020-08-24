package com.example.rusheta.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contacts2 {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("id")
    @Expose
    private String contactId;

    @SerializedName("identityKey")
    @Expose
    private String identityKey;

    @SerializedName("ephemeralKey")
    @Expose
    private String ephemeralKey;

    @SerializedName("signature")
    @Expose
    private String signature;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getIdentityKey() {
        return identityKey;
    }

    public void setIdentityKey(String identityKey) {
        this.identityKey = identityKey;
    }

    public String getEphemeralKey() {
        return ephemeralKey;
    }

    public void setEphemeralKey(String ephemeralKey) {
        this.ephemeralKey = ephemeralKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Contacts2(String name, String phone, String contactId, String identityKey, String ephemeralKey, String signature) {
        this.name = name;
        this.phone = phone;
        this.contactId = contactId;
        this.identityKey = identityKey;
        this.ephemeralKey = ephemeralKey;
        this.signature = signature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
