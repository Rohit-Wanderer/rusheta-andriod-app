package com.example.rusheta.service.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.rusheta.service.model.Chat;

@Database(entities = Chat.class,version = 4, exportSchema = false)
public abstract class ChatsDatabase extends RoomDatabase {

    private static ChatsDatabase instance;

    public abstract ChatDao chatDao();

    public static synchronized ChatsDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ChatsDatabase.class,"chats_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
