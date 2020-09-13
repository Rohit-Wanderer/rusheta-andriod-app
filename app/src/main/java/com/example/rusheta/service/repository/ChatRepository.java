package com.example.rusheta.service.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.rusheta.service.model.Chat;
import com.example.rusheta.service.local.ChatDao;
import com.example.rusheta.service.local.ChatsDatabase;

import java.util.List;

public class ChatRepository {
    private ChatDao chatDao;
    private LiveData<List<Chat>> allChats;

    public ChatRepository(Application application){
        ChatsDatabase chatsDatabase = ChatsDatabase.getInstance(application);
        chatDao = chatsDatabase.chatDao();
        allChats = chatDao.getAllChats();
    }

    private static class InsertChatAsyncTask extends AsyncTask<Chat,Void,Void>{
        private ChatDao chatDao;

        private InsertChatAsyncTask(ChatDao chatDao){
            this.chatDao = chatDao;
        }
        @Override
        protected Void doInBackground(Chat... chats) {
            return chatDao.insert(chats[0]);
        }
    }

    private static class DeleteChatAsyncTask extends AsyncTask<Chat, Void, Integer> {
        private ChatDao chatDao;

        private DeleteChatAsyncTask(ChatDao chatDao){
            this.chatDao = chatDao;
        }
        @Override
        protected Integer doInBackground(Chat... chats) {
            return chatDao.delete(chats[0]);
        }
    }

    private static class DeleteAllChatAsyncTask extends AsyncTask<Void,Void,Integer>{
        private ChatDao chatDao;

        private DeleteAllChatAsyncTask(ChatDao chatDao){
            this.chatDao = chatDao;
        }
        @Override
        protected Integer doInBackground(Void... voids) {
            return chatDao.deleteAll();
        }
    }



    public void insert(Chat chat){
        new InsertChatAsyncTask(chatDao).execute(chat);
    }

    public void delete(Chat chat){
        new DeleteChatAsyncTask(chatDao).execute(chat);
    }

    public void deleteAll(){
        new DeleteAllChatAsyncTask(chatDao).execute();
    }

    public LiveData<List<Chat>> getAllChats(){
        return allChats;
    }


}