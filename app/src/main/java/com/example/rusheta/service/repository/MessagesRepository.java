package com.example.rusheta.service.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import com.example.rusheta.service.local.MessagesDao;
import com.example.rusheta.service.local.MessagesDatabase;
import com.example.rusheta.service.model.UserMessage;

import java.util.List;

public class MessagesRepository {
    private MessagesDao messagesDao;
    private LiveData<List<UserMessage>> allMessages;

    public MessagesRepository(Application application,String contactId) {
        MessagesDatabase messagesDatabase = MessagesDatabase.getInstance(application);
        messagesDao = messagesDatabase.messagesDao();
        allMessages = messagesDao.getAllMessages(contactId);
    }

    private static class InsertMessageAsyncTask extends AsyncTask<UserMessage, Void, Void> {
        private MessagesDao messagesDao;

        private InsertMessageAsyncTask(MessagesDao messagesDao) {
            this.messagesDao = messagesDao;
        }

        @Override
        protected Void doInBackground(UserMessage... Messages) {
            return messagesDao.insert(Messages[0]);
        }
    }

    private static class DeleteMessageAsyncTask extends AsyncTask<UserMessage, Void, Integer> {
        private MessagesDao messagesDao;

        private DeleteMessageAsyncTask(MessagesDao messagesDao) {
            this.messagesDao = messagesDao;
        }

        @Override
        protected Integer doInBackground(UserMessage... Messages) {
            return messagesDao.delete(Messages[0]);
        }
    }

    private static class DeleteAllMessageAsyncTask extends AsyncTask<Void, Void, Integer> {
        private MessagesDao messagesDao;

        private DeleteAllMessageAsyncTask(MessagesDao messagesDao) {
            this.messagesDao = messagesDao;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return messagesDao.deleteAll();
        }
    }

    public void insert(UserMessage userMessage) {
        new MessagesRepository.InsertMessageAsyncTask(messagesDao).execute(userMessage);
    }

    public void delete(UserMessage userMessage) {
        new MessagesRepository.DeleteMessageAsyncTask(messagesDao).execute(userMessage);
    }

    public void deleteAll() {
        new MessagesRepository.DeleteAllMessageAsyncTask(messagesDao).execute();
    }

    public LiveData<List<UserMessage>> getAllMessages() {
        return allMessages;
    }
}
