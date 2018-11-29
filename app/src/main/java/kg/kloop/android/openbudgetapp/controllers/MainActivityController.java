package kg.kloop.android.openbudgetapp.controllers;

import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
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

import kg.kloop.android.openbudgetapp.objects.User;
import kg.kloop.android.openbudgetapp.utils.Constants;
import kg.kloop.android.openbudgetapp.models.MainViewModel;

public class MainActivityController {
    private static final String TAG = MainActivityController.class.getSimpleName();
    private CollectionReference usersCollectionRef;
    private FirebaseFirestore db;
    private User user;
    private MainViewModel mainViewModel;
    private FirebaseUser firebaseUser;

    public MainActivityController(final MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
        db = FirebaseFirestore.getInstance();
        mainViewModel.setDb(db);
        usersCollectionRef = db.collection("users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mainViewModel.getFirebaseUserMutableLiveData().setValue(firebaseUser);

        getUserData();
    }

    private void getUserData() {
        MutableLiveData<FirebaseUser> firebaseUserMutableLiveData = mainViewModel.getFirebaseUserMutableLiveData();
        if (firebaseUserMutableLiveData.getValue() != null) {
            DocumentReference userDocRef = db.document("users/" + firebaseUserMutableLiveData.getValue().getUid());
            userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        mainViewModel.getUserLiveData().setValue(user);
                        if (user.getRole().equals(Constants.USER)) {
                            mainViewModel.getUserRoleMutableLiveData().setValue(Constants.USER);
                        } else if (user.getRole().equals(Constants.EDITOR)) {
                            mainViewModel.getUserRoleMutableLiveData().setValue(Constants.EDITOR);
                        }
                    }
                }
            });
        }
    }

    public void saveUserToDb(final FirebaseUser firebaseUser) {
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
                        newUser.setRole(Constants.USER);
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

    public void signOut() {
        mainViewModel.getUserLiveData().setValue(null);
    }

    public void signIn() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mainViewModel.getFirebaseUserMutableLiveData().setValue(firebaseUser);
        getUserData();
    }
}
