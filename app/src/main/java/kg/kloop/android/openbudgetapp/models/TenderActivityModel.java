package kg.kloop.android.openbudgetapp.models;

import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;
import kg.kloop.android.openbudgetapp.objects.User;

public class TenderActivityModel {
    private Tender tender;
    private User currentUser;
    private boolean myTender;
    private boolean tenderClosed;
    private boolean tenderAccepted;
    private MutableLiveData<ArrayList<TenderTask>> tenderTaskArrayListMutableLiveData;
    private MutableLiveData<ArrayList<TenderTaskWork>> tenderTaskWorkArrayList;
    private boolean taskAdded;
    private TenderTask addedTask;

    public TenderActivityModel() {
        tenderTaskArrayListMutableLiveData = new MutableLiveData<>();
        tenderTaskWorkArrayList = new MutableLiveData<>();
    }

    public Tender getTender() {
        return tender;
    }

    public void setTender(Tender tender) {
        this.tender = tender;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String getTenderSum() {
        return tender.getPlanSum() + " " + tender.getCurrency();
    }

    public boolean isMyTender() {
        return myTender;
    }

    public void setMyTender(boolean myTender) {
        this.myTender = myTender;
    }

    public boolean isTenderClosed() {
        return tenderClosed;
    }

    public void setTenderClosed(boolean tenderClosed) {
        this.tenderClosed = tenderClosed;
    }

    public boolean isTenderAccepted() {
        return tenderAccepted;
    }

    public void setTenderAccepted(boolean tenderAccepted) {
        this.tenderAccepted = tenderAccepted;
    }

    public void setTenderTaskArrayListMutableLiveData(MutableLiveData<ArrayList<TenderTask>> tenderTaskArrayListMutableLiveData) {
        this.tenderTaskArrayListMutableLiveData = tenderTaskArrayListMutableLiveData;
    }

    public MutableLiveData<ArrayList<TenderTask>> getTenderTaskArrayListMutableLiveData() {
        return tenderTaskArrayListMutableLiveData;
    }

    public MutableLiveData<ArrayList<TenderTaskWork>> getTenderTaskWorkArrayList() {
        return tenderTaskWorkArrayList;
    }

    public void setTenderTaskWorkArrayList(MutableLiveData<ArrayList<TenderTaskWork>> tenderTaskWorkArrayList) {
        this.tenderTaskWorkArrayList = tenderTaskWorkArrayList;
    }

    public boolean isTaskAdded() {
        return taskAdded;
    }

    public void setTaskAdded(boolean taskAdded) {
        this.taskAdded = taskAdded;
    }

    public TenderTask getAddedTask() {
        return addedTask;
    }
}
