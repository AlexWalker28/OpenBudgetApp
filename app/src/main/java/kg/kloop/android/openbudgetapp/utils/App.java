package kg.kloop.android.openbudgetapp.utils;

import android.app.Application;

import androidx.room.Room;

import kg.kloop.android.openbudgetapp.database.TendersDatabase;

public class App extends Application {

    public static App instance;

    private TendersDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(
                getApplicationContext(),
                TendersDatabase.class,
                "tenders_database")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public TendersDatabase getDatabase() {
        return database;
    }
}
