package kg.kloop.android.openbudgetapp.utils;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import kg.kloop.android.openbudgetapp.objects.User;

public class MainViewModel extends ViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();
    private MutableLiveData<User> userLiveData;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private MutableLiveData<String> userRoleMutableLiveData;
    private FirebaseFirestore db;


    public MainViewModel() {
        userLiveData = new MutableLiveData<>();
        firebaseUserMutableLiveData = new MutableLiveData<>();
        userRoleMutableLiveData = new MutableLiveData<>();


    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public void setUserLiveData(MutableLiveData<User> userLiveData) {
        this.userLiveData = userLiveData;
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public void setFirebaseUserMutableLiveData(MutableLiveData<FirebaseUser> firebaseUserMutableLiveData) {
        this.firebaseUserMutableLiveData = firebaseUserMutableLiveData;
    }

    public MutableLiveData<String> getUserRoleMutableLiveData() {
        return userRoleMutableLiveData;
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }
}
