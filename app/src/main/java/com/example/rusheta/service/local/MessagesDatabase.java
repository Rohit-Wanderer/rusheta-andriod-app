package com.example.rusheta.service.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.rusheta.service.model.UserMessage;

@Database(entities = UserMessage.class,version = 5 , exportSchema = false)
public abstract class MessagesDatabase extends RoomDatabase {
    private static MessagesDatabase instance;

    public abstract MessagesDao messagesDao();

    public static synchronized MessagesDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MessagesDatabase.class,"messages_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
