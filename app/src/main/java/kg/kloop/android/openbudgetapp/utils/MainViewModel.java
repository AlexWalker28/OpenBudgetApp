package kg.kloop.android.openbudgetapp.utils;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import kg.kloop.android.openbudgetapp.objects.User;

public class MainViewModel extends ViewModel {
    private MutableLiveData<User> userLiveData;

    public MainViewModel() {
        userLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public void setUserLiveData(MutableLiveData<User> userLiveData) {
        this.userLiveData = userLiveData;
    }
}
