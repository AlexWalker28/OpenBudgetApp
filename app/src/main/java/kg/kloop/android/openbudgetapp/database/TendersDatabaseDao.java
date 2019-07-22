package kg.kloop.android.openbudgetapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.TenderTask;

@Dao
public interface TendersDatabaseDao {

    @Insert
    void insertTask(TenderTask tenderTask);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTender(Tender tender);

    @Update
    void updateTask(TenderTask tenderTask);

    @Update
    void updateTender(Tender tender);

    @Query("SELECT * FROM tender_tasks_table ORDER BY update_time_milli DESC")
    LiveData<List<TenderTask>> sortByTime();

    @Query("SELECT * FROM tender_tasks_table ORDER BY create_time_milli DESC LIMIT 1")
    TenderTask getLastTask();

    @Query("SELECT * FROM tenders_table ORDER BY create_time_milli DESC LIMIT 1")
    Tender getLastTender();

    @Query("SELECT * FROM tender_tasks_table WHERE description LIKE '%квартиры%'")
    List<TenderTask> getTasks();

    @Query("SELECT * FROM tenders_table " +
            "WHERE " +
            "procurement_object LIKE '%' || :procurementObject || '%' AND " +
            "procuring_entity LIKE '%' || :procuringEntity || '%' AND " +
            "region LIKE '%' || :region || '%'")
    List<Tender> getTenders(String procurementObject, String procuringEntity, String region);

    @Query("SELECT * FROM tenders_table " +
            "WHERE " +
            "procurement_object LIKE '%' || :procurementObject || '%' AND " +
            "procuring_entity LIKE '%' || :procuringEntity || '%'")
    List<Tender> getTendersObjEnt(String procurementObject, String procuringEntity);

    @Query("SELECT * FROM tenders_table " +
            "WHERE " +
            "procurement_object LIKE '%' || :procurementObject || '%' AND " +
            "region LIKE '%' || :region || '%'")
    List<Tender> getTendersObjReg(String procurementObject, String region);

    @Query("SELECT * FROM tenders_table " +
            "WHERE " +
            "procurement_object LIKE '%' || :procurementObject || '%'")
    List<Tender> getTendersObj(String procurementObject);

    @Query("SELECT * FROM tenders_table " +
            "WHERE " +
            "procuring_entity LIKE '%' || :procuringEntity || '%' AND " +
            "region LIKE '%' || :region || '%'")
    List<Tender> getTendersEntReg(String procuringEntity, String region);

    @Query("SELECT * FROM tenders_table " +
            "WHERE " +
            "procuring_entity LIKE '%' || :procuringEntity || '%'")
    List<Tender> getTendersEnt(String procuringEntity);

    @Query("SELECT * FROM tenders_table " +
            "WHERE " +
            "region LIKE '%' || :region || '%'")
    List<Tender> getTendersReg(String region);

    @Query("DELETE FROM tenders_table")
    void clearTendersTable();

}
