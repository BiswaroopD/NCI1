package com.theappfactory.ChattrBox.SimpleNewsChat;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;

@Database(entities = {Article.class, Source.class, Comment.class}, version = 20, exportSchema = false)
public abstract class NCIRoomDatabase extends RoomDatabase {

    public abstract ArticleDAO getArticleDAO();
    public abstract CommentDAO getCommentDAO();
//    public abstract UserDAO getUserDAO();
//    public abstract ChatDAO getChatDAO();

    private static NCIRoomDatabase nciRoomDatabase;

    static NCIRoomDatabase getDatabase(final Context context) {

        if (nciRoomDatabase == null) {

                if (nciRoomDatabase == null) {
                    nciRoomDatabase = Room.databaseBuilder(context,
                            NCIRoomDatabase.class,"nci_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
//                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        return nciRoomDatabase;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            new PopulateDbAsync(nciRoomDatabase).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ArticleDAO articleDAO;


        PopulateDbAsync(NCIRoomDatabase db) {
            articleDAO = db.getArticleDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            articleDAO.getAllArticles();

//            Word word = new Word("Hello");
//            mDao.insert(word);
//            word = new Word("World");
//            mDao.insert(word);
            return null;
        }
    }
}
