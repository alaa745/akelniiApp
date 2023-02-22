package com.example.myapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class , Cart.class , GoogleUserCart.class , Product.class , GoogleUser.class}, version = 15, exportSchema = true)
public abstract class RoomDb extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract GoogleUserDao googleUserDao();
    public abstract CartDao cartDao();
    public abstract GoogleCartDao googleCartDao();
    public abstract ProductDao productDao();

    private static volatile RoomDb INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static RoomDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDb.class, "Users_db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
                // comment out the following block
                databaseWriteExecutor.execute(() -> {
                    // Populate the database in the background.
                    // If you want to start with more words, just add them.

                });
        }
    };
}