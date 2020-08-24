package com.example.rusheta.view.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rusheta.R;
import com.example.rusheta.service.model.Contacts2;
import com.example.rusheta.utils.signal.SignalProtocolKeyGen;
import com.example.rusheta.view.adapter.MyContacts2Adapter;
import com.example.rusheta.view.viewmodel.ContactsViewModel;

import java.util.ArrayList;
import java.util.List;

public class Contacts2Activity extends AppCompatActivity {
    private static final String TAG = "Contacts2Activity";

    RecyclerView recyclerView;
    MyContacts2Adapter myContacts2Adapter;
    List<Contacts2> contacts;
    SignalProtocolKeyGen signalProtocol;
    ContactsViewModel contactsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        recyclerView = findViewById(R.id.contactsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(Contacts2Activity.this));
        SharedPreferences sharedPreferences
                = getSharedPreferences("RushetaData",
                MODE_PRIVATE);
        signalProtocol = new SignalProtocolKeyGen(sharedPreferences);
        contacts = new ArrayList<>();

        contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
        contactsViewModel.getContactsRepository(sharedPreferences).observe(this, newContacts -> {
            contacts = newContacts;
            myContacts2Adapter = new MyContacts2Adapter(Contacts2Activity.this, contacts, signalProtocol);
            recyclerView.setAdapter(myContacts2Adapter);
        });
        myContacts2Adapter = new MyContacts2Adapter(Contacts2Activity.this, contacts, signalProtocol);
        recyclerView.setAdapter(myContacts2Adapter);
    }


}
