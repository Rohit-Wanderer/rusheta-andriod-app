package com.example.rusheta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.UnsupportedEncodingException;
import java.security.Security;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {

    public static final String NICKNAME = "usernickname";
    EditText name;
    EditText phone;
    EditText password;

    SignalProtocolKeyGen signalProtocol;
    //            private static final String BASE_URL = "http://10.0.2.2:3000";
//    private static final String BASE_URL = "http://localhost:3000";
    private static final String BASE_URL = "https://rusheta.herokuapp.com/";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    JsonApiPlaceHolder jsonApiPlaceHolder = retrofit.create(JsonApiPlaceHolder.class);


    public void SignIn() {
        Intent i = new Intent(SignInActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        SignInActivity.this.finish();
    }

    public void onSignIn(View view) {

        name = findViewById(R.id.edittext_name);
        phone = findViewById(R.id.edittext_phone);
        password = findViewById(R.id.edittext_password);

        if (!name.getText().toString().isEmpty() && !phone.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {

            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            String phoneNumber = phone.getText().toString();
            try {
                Phonenumber.PhoneNumber phoneNumberProto = phoneUtil.parse(phoneNumber, "IN");
                phoneNumber = phoneUtil.format(phoneNumberProto, PhoneNumberUtil.PhoneNumberFormat.E164);
                Log.i("PHONE NUMBERS SIGN IN", phoneNumber);
            } catch (NumberParseException e) {
                e.printStackTrace();
                return;
            }
            createUser(phoneNumber, name.getText().toString(), password.getText().toString());
        } else
            Toast.makeText(this, "Enter Valid Details", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
        SharedPreferences sharedPreferences
                = getSharedPreferences("RushetaData",
                MODE_PRIVATE);

//        sharedPreferences.edit().clear().commit();
        signalProtocol = new SignalProtocolKeyGen(sharedPreferences);

        if (!sharedPreferences.getString("token", "").isEmpty()) {
            SignIn();
        }

        setContentView(R.layout.signin_screen);

    }

    private void createUser(String phone, String name, String password) {

        try {
            CryptoClass cryptoClass = new CryptoClass();

            String secret1 = cryptoClass.encryptToRSAString(cryptoClass.getKey());

            String secret2 = cryptoClass.encryptToRSAString(cryptoClass.getIV());

            String Phone = new String(Base64.encode(cryptoClass.encrypt(phone.getBytes()), Base64.DEFAULT));

            String Name = new String(Base64.encode(cryptoClass.encrypt(name.getBytes()), Base64.DEFAULT));

            String Password = new String(Base64.encode(cryptoClass.encrypt(password.getBytes()), Base64.DEFAULT));


            String identityKeyString = ObjectSerializationClass.getStringFromObject(
                    signalProtocol.identityKeyPair.kp.getPublic()
            );
            String identityKey = new String(Base64.encode(
                    cryptoClass.encrypt(
                            identityKeyString.getBytes()),
                    Base64.DEFAULT));

            String ephemeralKeyString = ObjectSerializationClass.getStringFromObject(
                    signalProtocol.ephemeralKeyPair.kp.getPublic()
            );
            String ephemeralKey = new String(Base64.encode(
                    cryptoClass.encrypt(
                            ephemeralKeyString.getBytes()),
                    Base64.DEFAULT));

            String signature = new String(Base64.encode(
                    cryptoClass.encrypt(
                            signalProtocol.signKey(signalProtocol.ephemeralKeyPair.kp.getPublic()).getBytes()),
                    Base64.DEFAULT));

            ///Double Encryption logic.

            User user = new User(Name, Phone, Password, secret1, secret2, identityKey, ephemeralKey, signature);
            Call<User> call = jsonApiPlaceHolder.createUser(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (!response.isSuccessful()) {
                        Log.i("createUserLogNoSuccess", "" + response.code());
                        return;
                    }

                    SharedPreferences sharedPreferences
                            = getSharedPreferences("RushetaData",
                            MODE_PRIVATE);

                    SharedPreferences.Editor myEdit
                            = sharedPreferences.edit();

                    myEdit.putString(
                            "name",
                            name);
                    myEdit.putString(
                            "phone",
                            phone);
                    myEdit.putString(
                            "secret1",
                            new String(Base64.encode(cryptoClass.getKey(), Base64.DEFAULT)));
                    myEdit.putString(
                            "secret2",
                            new String(Base64.encode(cryptoClass.getIV(), Base64.DEFAULT)));

                    try {
                        String Token = new String(
                                cryptoClass.decrypt(Base64.decode(response.body().getToken(), Base64.DEFAULT)),
                                "UTF-8");

                        myEdit.putString("token", Token);
                        myEdit.apply();
                        SignIn();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.i("createUserLogFail", t.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
