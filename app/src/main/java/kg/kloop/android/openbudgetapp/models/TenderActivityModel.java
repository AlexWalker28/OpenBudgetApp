package kg.kloop.android.openbudgetapp.models;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;
import kg.kloop.android.openbudgetapp.objects.User;

public class TenderActivityModel {
    private Tender tender;
    private User currentUser;
    private MutableLiveData<Boolean> myTender;
    private MutableLiveData<Boolean> tenderClosed;
    private MutableLiveData<Boolean> tenderAccepted;
    private MutableLiveData<ArrayList<TenderTask>> tenderTaskArrayListMutableLiveData;
    private MutableLiveData<ArrayList<TenderTaskWork>> tenderTaskWorkArrayList;

    public TenderActivityModel() {
        tenderTaskArrayListMutableLiveData = new MutableLiveData<>();
        tenderTaskWorkArrayList = new MutableLiveData<>();
        myTender = new MutableLiveData<>();
        tenderClosed = new MutableLiveData<>();
        tenderAccepted = new MutableLiveData<>();
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

    public MutableLiveData<Boolean> getMyTender() {
        return myTender;
    }

    public void setMyTender(MutableLiveData<Boolean> myTender) {
        this.myTender = myTender;
    }

    public MutableLiveData<Boolean> getTenderClosed() {
        return tenderClosed;
    }

    public void setTenderClosed(MutableLiveData<Boolean> tenderClosed) {
        this.tenderClosed = tenderClosed;
    }

    public MutableLiveData<Boolean> getTenderAccepted() {
        return tenderAccepted;
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
}
