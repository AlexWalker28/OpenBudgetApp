package kg.kloop.android.openbudgetapp.objects;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "tender_tasks_table")
public class TenderTask implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted;

    //@ColumnInfo(name = "attachment_types")
    @Ignore
    private ArrayList<String> attachmentTypes;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "tender_number")
    private String tenderNumber;

    //@ColumnInfo(name = "author")
    @Ignore
    private User author;

    //@ColumnInfo(name = "description")
    private boolean needModeration;

    @ColumnInfo(name = "place_name")
    private String placeName;

    @ColumnInfo(name = "has_work")
    private boolean hasWork;

    @ColumnInfo(name = "create_time_milli")
    private long createTime;

    @ColumnInfo(name = "update_time_milli")
    private long updateTime;

    public TenderTask() {
        isCompleted = false;
        createTime = 0;
        updateTime = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public ArrayList<String> getAttachmentTypes() {
        return attachmentTypes;
    }

    public void setAttachmentTypes(ArrayList<String> attachmentTypes) {
        this.attachmentTypes = attachmentTypes;
    }

    public String getTenderNumber() {
        return tenderNumber;
    }

    public void setTenderNumber(String tenderNumber) {
        this.tenderNumber = tenderNumber;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public boolean isNeedModeration() {
        return needModeration;
    }

    public void setNeedModeration(boolean needModeration) {
        this.needModeration = needModeration;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public boolean isHasWork() {
        return hasWork;
    }

    public void setHasWork(boolean hasWork) {
        this.hasWork = hasWork;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
