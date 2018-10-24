package kg.kloop.android.openbudgetapp.models;

import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;

public class WorkActivityModel {

    private String tenderId;
    private String taskId;

    public WorkActivityModel() {
        workArrayList = new MutableLiveData<>();
    }

    private MutableLiveData<ArrayList<TenderTaskWork>> workArrayList;

    public MutableLiveData<ArrayList<TenderTaskWork>> getWorkArrayList() {
        return workArrayList;
    }

    public void setWorkArrayList(MutableLiveData<ArrayList<TenderTaskWork>> workArrayList) {
        this.workArrayList = workArrayList;
    }

    public void setTenderId(String tenderId) {
        this.tenderId = tenderId;
    }

    public String getTenderId() {
        return tenderId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }
}
