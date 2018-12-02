package kg.kloop.android.openbudgetapp.models;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;

public class WorkActivityModel {

    private TenderTask task;

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

    public void setTask(TenderTask task) {
        this.task = task;
    }

    public TenderTask getTask() {
        return task;
    }
}
