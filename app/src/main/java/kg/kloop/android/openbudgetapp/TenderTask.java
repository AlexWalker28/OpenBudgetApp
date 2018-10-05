package kg.kloop.android.openbudgetapp;

import java.io.Serializable;
import java.util.ArrayList;

public class TenderTask implements Serializable {
    private String id;
    private String description;
    private ArrayList<String> attachmentTypes;

    public TenderTask() {
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

    public ArrayList<String> getAttachmentTypes() {
        return attachmentTypes;
    }

    public void setAttachmentTypes(ArrayList<String> attachmentTypes) {
        this.attachmentTypes = attachmentTypes;
    }
}
