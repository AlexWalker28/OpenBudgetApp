package kg.kloop.android.openbudgetapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.TenderTask;


@Database(entities = {Tender.class, TenderTask.class}, version = 1, exportSchema = false)
public abstract class TendersDatabase extends RoomDatabase {
    public abstract TendersDatabaseDao tendersDatabaseDao();
    private static TendersDatabase INSTANCE = null;
    /*public TendersDatabase getInstance(Context context) {
        synchronized (this) {
            TendersDatabase instance = INSTANCE;
            if (instance == null) {
                instance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        TendersDatabase.class,
                        "tenders_database")
                        .fallbackToDestructiveMigration()
                        .build();
            }
            INSTANCE = instance;
            return instance;
        }
    }*/
}
