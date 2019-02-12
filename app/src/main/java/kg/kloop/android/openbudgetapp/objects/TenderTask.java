package kg.kloop.android.openbudgetapp.objects;


import java.io.Serializable;
import java.util.ArrayList;

public class TenderTask implements Serializable {
    private String id;
    private String description;
    private boolean isCompleted;
    private ArrayList<String> attachmentTypes;
    private double latitude;
    private double longitude;
    private String tenderId;
    private User author;
    private boolean needModeration;
    private String placeName;
    private boolean hasWork;

    public TenderTask() {
        isCompleted = false;
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

    public String getTenderId() {
        return tenderId;
    }

    public void setTenderId(String tenderId) {
        this.tenderId = tenderId;
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
}
