package kg.kloop.android.openbudgetapp.activities;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

import kg.kloop.android.openbudgetapp.utils.Constants;
import kg.kloop.android.openbudgetapp.utils.MainViewModel;
import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.adapters.TenderFragmentSimpleUsersPageAdapter;
import kg.kloop.android.openbudgetapp.adapters.TendersFragmentEditorsPageAdapter;
import kg.kloop.android.openbudgetapp.objects.User;

public class MainActivity extends AppCompatActivity implements LifecycleOwner {

    private static final int RC_SIGN_IN = 100;
    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseFirestore db;
    private CollectionReference usersCollectionRef;
    private DocumentReference userDocRef;
    private FirebaseUser firebaseUser;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewPager viewPager = findViewById(R.id.main_view_pager);
        final TabLayout tabLayout = findViewById(R.id.main_tab_layout);

        db = FirebaseFirestore.getInstance();
        usersCollectionRef = db.collection("users");
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            signIn();
        } else {
            userDocRef = db.document("users/" + firebaseUser.getUid());
            userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        mainViewModel.getUserLiveData().postValue(user);
                        if (user.getRole().equals(Constants.USER)) {
                            viewPager.setAdapter(new TenderFragmentSimpleUsersPageAdapter(getSupportFragmentManager()));
                        } else if (user.getRole().equals(Constants.EDITOR)) {
                            viewPager.setAdapter(new TendersFragmentEditorsPageAdapter(getSupportFragmentManager()));
                        } else signIn();
                        tabLayout.setupWithViewPager(viewPager);
                    }
                }
            });
        }



    }

    private void signIn() {
        //add more providers in the future
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                addUserToDbIfNew(user);

            }
        }
    }

    private void addUserToDbIfNew(final FirebaseUser firebaseUser) {
        userDocRef = db.document("users/" + firebaseUser.getUid());
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
