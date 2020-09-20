package com.example.rusheta.service.repository;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.rusheta.service.model.Contacts2;
import com.example.rusheta.service.remote.JsonApiPlaceHolder;
import com.example.rusheta.service.remote.RetrofitService;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsRepository {
    private static JsonApiPlaceHolder myInterface;
    private final MutableLiveData<List<Contacts2>> contacts = new MutableLiveData<>();

    public ContactsRepository() {
        myInterface = RetrofitService.getInterface();
    }

    public MutableLiveData<List<Contacts2>> getAllContacts(SharedPreferences sharedPreferences) {
        try {
            String token = sharedPreferences.getString("token", "");
            Call<List<Contacts2>> call = myInterface.getAllContacts(token);
            call.enqueue(new Callback<List<Contacts2>>() {
                @Override
                public void onResponse(@NotNull Call<List<Contacts2>> call, @NotNull Response<List<Contacts2>> response) {

                    if (!response.isSuccessful()) {
                        Log.i("GetContactsNOSucesss", "" + response.code());
                        return;
                    }
                    try {
                        contacts.setValue(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<Contacts2>> call, @NotNull Throwable t) {
                    contacts.postValue(null);
                    Log.i("FetchContactsLogFail", t.toString());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contacts;
    }
}
