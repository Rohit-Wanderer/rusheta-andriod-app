package com.example.rusheta;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;

public abstract class ObjectSerializationClass {

    static  String getStringFromObject(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o =  new ObjectOutputStream(b);
        o.writeObject(obj);
        byte[] res = b.toByteArray();
        o.close();
        b.close();
        return new String(Base64.encode(res, Base64.DEFAULT));
    }
    static Object getObjectFromString(String string) throws IOException, ClassNotFoundException {
        byte[] data = Base64.decode(string, Base64.DEFAULT);
        ByteArrayInputStream bi = new ByteArrayInputStream(data);
        ObjectInputStream oi = new ObjectInputStream(bi);
        Object obj = oi.readObject();
        oi.close();
        bi.close();
        return obj;
    }
}