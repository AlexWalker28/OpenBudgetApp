package kg.kloop.android.openbudgetapp.models;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;

public class WorkActivityModel {

    private String tenderNum;
    private String taskId;
    private String taskDescription;
    private double taskLat;
    private double taskLng;

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

    public void setTenderNum(String tenderNum) {
        this.tenderNum = tenderNum;
    }

    public String getTenderNum() {
        return tenderNum;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskLat(double taskLat) {
        this.taskLat = taskLat;
    }

    public double getTaskLat() {
        return taskLat;
    }

    public void setTaskLng(double taskLng) {
        this.taskLng = taskLng;
    }

    public double getTaskLng() {
        return taskLng;
    }
}
