package kg.kloop.android.openbudgetapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseFirestore db;
    private CollectionReference usersCollectionRef;
    private DocumentReference userDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.main_view_pager);
        TabLayout tabLayout = findViewById(R.id.main_tab_layout);

        viewPager.setAdapter(new TendersFragmentPageAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        db = FirebaseFirestore.getInstance();
        usersCollectionRef = db.collection("users");
        //add more in the future
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
                        User newUser = new User();
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
