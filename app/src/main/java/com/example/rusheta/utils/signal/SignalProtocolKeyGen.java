package com.example.rusheta.utils.signal;

import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.example.rusheta.utils.signal.kdf.HKDFv2;
import com.example.rusheta.utils.signal.keys.EphemeralKeyPair;
import com.example.rusheta.utils.signal.keys.IdentityKeyPair;

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

    private IdentityKeyPair identityKeyPair;
    private EphemeralKeyPair ephemeralKeyPair;

    public SignalProtocolKeyGen(SharedPreferences sharedPreferences) {

        try {
            identityKeyPair = new IdentityKeyPair(sharedPreferences);
            ephemeralKeyPair = new EphemeralKeyPair(sharedPreferences);

        } catch (IOException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchProviderException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public IdentityKeyPair getIdentityKeyPair() {
        return identityKeyPair;
    }

    public EphemeralKeyPair getEphemeralKeyPair() {
        return ephemeralKeyPair;
    }

    public Boolean verifyKey(PublicKey identityPublicKey, PublicKey ephemeralPublicKey, String signatureString) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
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

    public String signKey(PublicKey publicKey) throws InvalidKeyException, SignatureException, NoSuchProviderException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String data = new String(Base64.encode(publicKey.getEncoded(),Base64.DEFAULT));
        Signature s = Signature.getInstance("SHA256withECDSA","SC");
        s.initSign(identityKeyPair.getKp().getPrivate());
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
        return keyAgreement.generateSecret();
    }

    public byte[] getAESKey(PublicKey identityKeyPairReceived, PublicKey ephemeralKeyPairReceived) throws IOException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        byte[] DH1 = generateSecret(identityKeyPair.getKp().getPrivate(), ephemeralKeyPairReceived);
        byte[] DH2 = generateSecret(ephemeralKeyPair.getKp().getPrivate(), identityKeyPairReceived);
        byte[] DH3 = generateSecret(ephemeralKeyPair.getKp().getPrivate(), ephemeralKeyPairReceived);
        ByteArrayOutputStream my_stream = new ByteArrayOutputStream();
        my_stream.write(DH1);
        my_stream.write(DH2);
        my_stream.write(DH3);
        byte[] DH = my_stream.toByteArray();
        HKDFv2 hkdFv2 = new HKDFv2();
        return hkdFv2.deriveSecrets(DH,"wabalabadubdub".getBytes(),DH1.length);
    }

    public byte[] genAESKey(PublicKey identityKeyPairReceived, PublicKey ephemeralKeyPairReceived) throws IOException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        byte[] DH1 = generateSecret(ephemeralKeyPair.getKp().getPrivate(), identityKeyPairReceived);
        byte[] DH2 = generateSecret(identityKeyPair.getKp().getPrivate(), ephemeralKeyPairReceived);
        byte[] DH3 = generateSecret(ephemeralKeyPair.getKp().getPrivate(), ephemeralKeyPairReceived);
        ByteArrayOutputStream my_stream = new ByteArrayOutputStream();
        my_stream.write(DH1);
        my_stream.write(DH2);
        my_stream.write(DH3);
        byte[] DH = my_stream.toByteArray();
        HKDFv2 hkdFv2 = new HKDFv2();
        return hkdFv2.deriveSecrets(DH, "wabalabadubdub".getBytes(), DH1.length);
    }



}
