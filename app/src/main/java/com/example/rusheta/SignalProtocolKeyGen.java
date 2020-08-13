package com.example.rusheta;

import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.KeyAgreement;

public class SignalProtocolKeyGen {

    IdentityKeyPair identityKeyPair;
    EphemeralKeyPair ephemeralKeyPair;

    SignalProtocolKeyGen(SharedPreferences sharedPreferences){

        try {
            identityKeyPair = new IdentityKeyPair(sharedPreferences);
            ephemeralKeyPair = new EphemeralKeyPair(sharedPreferences);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    Boolean verifyKey(PublicKey identityPublicKey, PublicKey ephemeralPublicKey, String signatureString) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] signature = Base64.decode(signatureString, Base64.NO_WRAP | Base64.URL_SAFE);
        String data = new String(Base64.encode(ephemeralPublicKey.getEncoded(), Base64.DEFAULT));
        Signature s = Signature.getInstance("SHA256withECDSA","SC");
        s.initVerify(identityPublicKey);
        s.update(data.getBytes());
        boolean valid = s.verify(signature);
        if(valid)
            Log.i("VAILD","True");
        else
            Log.i("VAILD", "False");
        return valid;
    }

    String signKey(PublicKey publicKey) throws InvalidKeyException, SignatureException, NoSuchProviderException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String data = new String(Base64.encode(publicKey.getEncoded(),Base64.DEFAULT));
        Signature s = Signature.getInstance("SHA256withECDSA","SC");
        s.initSign(identityKeyPair.kp.getPrivate());
        s.update(data.getBytes());
        byte[] signature = s.sign();
        String ret = Base64.encodeToString(signature, Base64.NO_WRAP | Base64.URL_SAFE);
        Log.i("Signature", ret);
        return ret;
    }

    byte[] generateSecret(PrivateKey privateKey, PublicKey publicKey) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH","SC");
        keyAgreement.init(privateKey);
        keyAgreement.doPhase(publicKey,true);
        byte[] value = keyAgreement.generateSecret();
        return value;
    }

    byte[] getAESKey(PublicKey identityKeyPairReceived,PublicKey ephemeralKeyPairReceived) throws IOException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        byte[] DH1 = generateSecret(identityKeyPair.kp.getPrivate(),ephemeralKeyPairReceived);
        byte[] DH2 = generateSecret(ephemeralKeyPair.kp.getPrivate(),identityKeyPairReceived);
        byte[] DH3 = generateSecret(ephemeralKeyPair.kp.getPrivate(),ephemeralKeyPairReceived);
        ByteArrayOutputStream my_stream = new ByteArrayOutputStream();
        my_stream.write(DH1);
        my_stream.write(DH2);
        my_stream.write(DH3);
        byte[] DH = my_stream.toByteArray();
        HKDFv2 hkdFv2 = new HKDFv2();
        byte[] secret = hkdFv2.deriveSecrets(DH,"wabalabadubdub".getBytes(),DH1.length);
        return secret;
    }

    byte[] genAESKey(PublicKey identityKeyPairReceived, PublicKey ephemeralKeyPairReceived) throws IOException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        byte[] DH1 = generateSecret(ephemeralKeyPair.kp.getPrivate(), identityKeyPairReceived);
        byte[] DH2 = generateSecret(identityKeyPair.kp.getPrivate(), ephemeralKeyPairReceived);
        byte[] DH3 = generateSecret(ephemeralKeyPair.kp.getPrivate(), ephemeralKeyPairReceived);
        ByteArrayOutputStream my_stream = new ByteArrayOutputStream();
        my_stream.write(DH1);
        my_stream.write(DH2);
        my_stream.write(DH3);
        byte[] DH = my_stream.toByteArray();
        HKDFv2 hkdFv2 = new HKDFv2();
        byte[] secret = hkdFv2.deriveSecrets(DH, "wabalabadubdub".getBytes(), DH1.length);
        return secret;
    }



}
