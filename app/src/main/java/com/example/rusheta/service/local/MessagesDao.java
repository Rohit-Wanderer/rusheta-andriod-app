package com.example.rusheta.service.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.rusheta.service.model.UserMessage;

import java.util.List;
@Dao
public interface MessagesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Void insert(UserMessage userMessage);

    @Delete
    int delete(UserMessage userMessage);

    @Query("DELETE FROM messages_table")
    int deleteAll();

    @Query("SELECT * FROM messages_table where contactId = :contactId")
    LiveData<List<UserMessage>> getAllMessages(String contactId);

}
