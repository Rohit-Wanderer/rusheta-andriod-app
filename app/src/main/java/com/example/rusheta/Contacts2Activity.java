package com.example.rusheta;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Contacts2Activity extends AppCompatActivity {
    private static final String TAG = "Contacts2Activity";
    //        private static final String BASE_URL = "http://10.0.2.2:3000";
//    private static final String BASE_URL = "http://localhost:3000";
    private static final String BASE_URL = "https://rusheta.herokuapp.com/";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    JsonApiPlaceHolder jsonApiPlaceHolder = retrofit.create(JsonApiPlaceHolder.class);

    RecyclerView recyclerView;
    MyContacts2Adapter myContacts2Adapter;
    List<Contacts2> contacts;
    SignalProtocolKeyGen signalProtocol;


    private static final String TAG_ANDROID_CONTACTS = "ANDROID_CONTACTS2";

    private void getAllContacts(SharedPreferences sharedPreferences) {
        try{

            String token = sharedPreferences.getString("token","");
            Call<List<Contacts2>> call = jsonApiPlaceHolder.getAllContacts(token);

            call.enqueue(new Callback<List<Contacts2>>() {
                @Override
                public void onResponse(Call<List<Contacts2>> call, Response<List<Contacts2>> response) {

                    if(!response.isSuccessful()){
                        Log.i("GetContactsNOSucesss",""+response.code());
                        return;
                    }

                    try {

                        List<Contacts2> validContacts = response.body();
                        Log.i("Valid Contacts2", validContacts.get(0).getName());
                        contacts = validContacts;
                        Log.i("CONTACTS SIZE::", String.valueOf(contacts.size()));
                        myContacts2Adapter = new MyContacts2Adapter(Contacts2Activity.this, contacts, signalProtocol);
                        recyclerView.setAdapter(myContacts2Adapter);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<List<Contacts2>> call, Throwable t) {
                    Log.i("FetchContactsLogFail",t.toString());

                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        SharedPreferences sharedPreferences
                = getSharedPreferences("RushetaData",
                MODE_PRIVATE);

        signalProtocol = new SignalProtocolKeyGen(sharedPreferences);
        contacts = new ArrayList<Contacts2>();
        recyclerView = findViewById(R.id.contactsRecyclerView);
        LinearLayoutManager myLinearLayoutManager = new LinearLayoutManager(Contacts2Activity.this);
        recyclerView.setLayoutManager(myLinearLayoutManager);
        myContacts2Adapter = new MyContacts2Adapter(Contacts2Activity.this, contacts, signalProtocol);
        recyclerView.setAdapter(myContacts2Adapter);
        getAllContacts(sharedPreferences);
        Toast.makeText(Contacts2Activity.this, "Contact data has been printed in the android monitor log..", Toast.LENGTH_SHORT).show();

    }

}
