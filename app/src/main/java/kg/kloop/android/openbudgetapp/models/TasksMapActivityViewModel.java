package kg.kloop.android.openbudgetapp.models;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.User;

public class TasksMapActivityViewModel extends ViewModel {

    private MutableLiveData<ArrayList<TenderTask>> samePlaceTasks;
    private MutableLiveData<User> user;

    public TasksMapActivityViewModel() {
        samePlaceTasks = new MutableLiveData<>();
        user = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<TenderTask>> getSamePlaceTasks() {
        return samePlaceTasks;
    }

    public MutableLiveData<User> getUser() {
        return user;
    }
}
