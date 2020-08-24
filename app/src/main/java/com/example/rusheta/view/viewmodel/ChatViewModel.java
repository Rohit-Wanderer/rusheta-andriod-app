package com.example.rusheta.view.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.rusheta.service.model.Chat;
import com.example.rusheta.service.repository.ChatRepository;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private ChatRepository chatRepository;
    private LiveData<List<Chat>> allChats;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        chatRepository = new ChatRepository(application);
        allChats = chatRepository.getAllChats();
    }

    public void insert(Chat chat){chatRepository.insert(chat);}
    public void delete(Chat chat){chatRepository.delete(chat);}
    public void deleteAll(){
        chatRepository.deleteAll();
    }
    public LiveData<List<Chat>> getAllChats(){
        return allChats;
    }

}
