package kg.kloop.android.openbudgetapp;

import java.io.Serializable;
import java.util.ArrayList;

public class TenderTask implements Serializable {
    private String id;
    private String description;
    private boolean isCompleted;
    private ArrayList<String> attachmentTypes;
    private String tenderId;

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
}
