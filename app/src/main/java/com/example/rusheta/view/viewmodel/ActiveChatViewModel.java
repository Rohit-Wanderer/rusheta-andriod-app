package com.example.rusheta.view.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.rusheta.service.model.UserMessage;
import com.example.rusheta.service.repository.MessagesRepository;

import java.util.List;

public class ActiveChatViewModel extends AndroidViewModel{
    private MessagesRepository messagesRepository;
    private LiveData<List<UserMessage>> allMessages;

    public ActiveChatViewModel(@NonNull Application application, String contactId) {
        super(application);
        messagesRepository = new MessagesRepository(application, contactId);
        allMessages = messagesRepository.getAllMessages();
    }

    public void insert(UserMessage userMessage){messagesRepository.insert(userMessage);}
    public void delete(UserMessage userMessage){messagesRepository.delete(userMessage);}
    public void deleteAll(){
        messagesRepository.deleteAll();
    }
    public LiveData<List<UserMessage>> getAllMessages(){
        return allMessages;
    }
}
