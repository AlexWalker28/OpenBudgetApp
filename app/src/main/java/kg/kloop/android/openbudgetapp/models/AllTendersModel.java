package kg.kloop.android.openbudgetapp.models;

import android.arch.lifecycle.MutableLiveData;

public class AllTendersModel {
    private MutableLiveData<Boolean> isTenderLoaded;

    public AllTendersModel() {
        isTenderLoaded = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getIsTenderLoaded() {
        return isTenderLoaded;
    }

    public void setIsTenderLoaded(MutableLiveData<Boolean> isTenderLoaded) {
        this.isTenderLoaded = isTenderLoaded;
    }
}
