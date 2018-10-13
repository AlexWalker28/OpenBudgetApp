package kg.kloop.android.openbudgetapp;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private MutableLiveData<String> userIdLiveData;

    public MainViewModel() {
        userIdLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<String> getUserIdLiveData() {
        return userIdLiveData;
    }

    public void setUserIdLiveData(MutableLiveData<String> userIdLiveData) {
        this.userIdLiveData = userIdLiveData;
    }
}
