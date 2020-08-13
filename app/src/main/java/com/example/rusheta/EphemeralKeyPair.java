package com.example.rusheta;

import android.content.SharedPreferences;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.ECGenParameterSpec;



public class EphemeralKeyPair {
    KeyPair kp = null;
    public EphemeralKeyPair(SharedPreferences sharedPreferences) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, IOException, ClassNotFoundException {

        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_EC, "SC");
        if(!sharedPreferences.contains("ephemeralKeyPair")){
            kpg.initialize(new ECGenParameterSpec("secp224k1"));
            kp = kpg.generateKeyPair();
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ObjectOutputStream o =  new ObjectOutputStream(b);
            o.writeObject(kp);
            byte[] res = b.toByteArray();
            o.close();
            b.close();
            String Key = new String(Base64.encode(res, Base64.DEFAULT));
            myEdit.putString("ephemeralKeyPair", Key);
            myEdit.apply();
            return;
        }

        String keyPair = sharedPreferences.getString("ephemeralKeyPair", "");
        byte[] data = Base64.decode(keyPair, Base64.DEFAULT);
        ByteArrayInputStream bi = new ByteArrayInputStream(data);
        ObjectInputStream oi = new ObjectInputStream(bi);
        Object obj = oi.readObject();
        oi.close();
        bi.close();
        kp = (KeyPair) obj;

    }
}
