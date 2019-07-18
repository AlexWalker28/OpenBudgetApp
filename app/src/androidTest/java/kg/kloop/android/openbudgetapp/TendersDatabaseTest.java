package kg.kloop.android.openbudgetapp;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import kg.kloop.android.openbudgetapp.database.TendersDatabase;
import kg.kloop.android.openbudgetapp.database.TendersDatabaseDao;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.TenderTask;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class TendersDatabaseTest {
    private TendersDatabaseDao tendersDao;
    private TendersDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, TendersDatabase.class)
                .allowMainThreadQueries()
                .build();
        tendersDao = db.tendersDatabaseDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertAndGetTenderTask() {
        TenderTask task = new TenderTask();
        task.setId("dgjekbe");
        tendersDao.insertTask(task);
        TenderTask anotherTask = tendersDao.getLastTask();
        assertEquals(anotherTask.getCreateTime(), 0L);
    }

    @Test
    public void insertAndGetTender() {
        Tender tender = new Tender();
        tender.setNumber("asd;klfh;a");
        tendersDao.insertTender(tender);
        Tender anotherTender = tendersDao.getLastTender();
        assertEquals(anotherTender.getCreateTime(), 0L);
    }
}
