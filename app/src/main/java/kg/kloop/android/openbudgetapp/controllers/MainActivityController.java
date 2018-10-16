package kg.kloop.android.openbudgetapp.controllers;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.Annotation;

import kg.kloop.android.openbudgetapp.objects.User;
import kg.kloop.android.openbudgetapp.utils.Constants;
import kg.kloop.android.openbudgetapp.utils.MainViewModel;

public class MainActivityController {
    private static final String TAG = MainActivityController.class.getSimpleName();
    private CollectionReference usersCollectionRef;
    private FirebaseFirestore db;
    private User user;

    public MainActivityController(final MainViewModel mainViewModel) {
        db = FirebaseFirestore.getInstance();
        mainViewModel.setDb(db);
        MutableLiveData<FirebaseUser> firebaseUserMutableLiveData = mainViewModel.getFirebaseUserMutableLiveData();
        usersCollectionRef = db.collection("users");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mainViewModel.getFirebaseUserMutableLiveData().setValue(firebaseUser);
        Log.v(TAG, "current user: " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        if (firebaseUserMutableLiveData.getValue() != null) {
            DocumentReference userDocRef = db.document("users/" + firebaseUserMutableLiveData.getValue().getUid());
            userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        mainViewModel.getUserLiveData().postValue(user);
                        if (user.getRole().equals(Constants.USER)) {
                            mainViewModel.getUserRoleMutableLiveData().postValue(Constants.USER);
                        } else if (user.getRole().equals(Constants.EDITOR)) {
                            mainViewModel.getUserRoleMutableLiveData().postValue(Constants.EDITOR);
                        }
                    }
                }
            });
        }
    }

    public void addUserToDbIfNew(final FirebaseUser firebaseUser) {
        DocumentReference userDocRef = db.document("users/" + firebaseUser.getUid());
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().exists()) {
                        final User newUser = new User();
                        newUser.setId(firebaseUser.getUid());
                        newUser.setName(firebaseUser.getDisplayName());
                        newUser.setEmail(firebaseUser.getEmail());
                        newUser.setPhoneNumber(firebaseUser.getPhoneNumber());
                        newUser.setPhotoUrl(String.valueOf(firebaseUser.getPhotoUrl()));
                        usersCollectionRef.document(newUser.getId()).set(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.v(TAG, "User is saved to db");
                                }
                            }
                        });

                    } else {
                        Log.v(TAG, "User is already in db");
                    }
                }
            }
        });
    }
}
