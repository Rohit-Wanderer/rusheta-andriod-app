package com.example.rusheta;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Void insert(Chat chat);

    @Delete
    int delete(Chat chat);

    @Query("DELETE FROM chats_table")
    int deleteAll();

    @Query("SELECT * FROM chats_table")
    LiveData<List<Chat>> getAllChats();

}
