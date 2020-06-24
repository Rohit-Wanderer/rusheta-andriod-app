package com.example.rusheta;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Chat.class,version = 3, exportSchema = false)
public abstract class ChatsDatabase extends RoomDatabase {

    private static ChatsDatabase instance;

    public abstract ChatDao chatDao();

    public static synchronized ChatsDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ChatsDatabase.class,"chats_database")
                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

//    private static  RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
//
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            new PopulateDatabaseAsyncTask(instance).execute();
//        }
//    };

//    private static class PopulateDatabaseAsyncTask extends AsyncTask<Void,Void,Void>{
//        private ChatDao chatDao;
//
//        private PopulateDatabaseAsyncTask(ChatsDatabase chatsDatabase){
//            this.chatDao = chatsDatabase.chatDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            chatDao.insert(new Chat("123456789","Rohit"));
//            chatDao.insert(new Chat("1234235789","Ritsaf"));
//            chatDao.insert(new Chat("123455389","Rodfgt"));
//
//            return null;
//        }
//    }
}
