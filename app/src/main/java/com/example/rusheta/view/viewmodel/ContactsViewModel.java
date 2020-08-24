package com.example.rusheta.view.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.rusheta.service.model.Contacts2;
import com.example.rusheta.service.repository.ContactsRepository;

import java.util.List;


public class ContactsViewModel extends AndroidViewModel {
    private final ContactsRepository contactsRepository;


    private MutableLiveData<List<Contacts2>> contacts = new MutableLiveData<>();

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        contactsRepository = new ContactsRepository();
    }

    public MutableLiveData<List<Contacts2>> getContactsRepository(SharedPreferences sharedPreferences) {
        contacts = loadContactsData(sharedPreferences);
        return contacts;
    }

    private MutableLiveData<List<Contacts2>> loadContactsData(SharedPreferences sharedPreferences) {
        return contactsRepository.getAllContacts(sharedPreferences);
    }
}
