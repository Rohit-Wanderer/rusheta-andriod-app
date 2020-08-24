package com.example.rusheta.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("secret1")
    @Expose
    private String secret1;

    @SerializedName("secret2")
    @Expose
    private String secret2;

    @SerializedName("identityKey")
    @Expose
    private String identityKey;

    @SerializedName("ephemeralKey")
    @Expose
    private String ephemeralKey;

    @SerializedName("signature")
    @Expose
    private String signature;

    @SerializedName("token")
    @Expose
    private String token = null;

    public String getSecret1() {
        return secret1;
    }

    public void setSecret1(String secret1) {
        this.secret1 = secret1;
    }

    public String getSecret2() {
        return secret2;
    }

    public void setSecret2(String secret2) {
        this.secret2 = secret2;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
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

    public User(String name, String phone, String password, String secret1, String secret2, String identityKey, String ephemeralKey, String signature) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.secret1 = secret1;
        this.secret2 = secret2;
        this.identityKey = identityKey;
        this.ephemeralKey = ephemeralKey;
        this.signature = signature;
    }
}
