package kg.kloop.android.openbudgetapp.activities;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import kg.kloop.android.openbudgetapp.controllers.MainActivityController;
import kg.kloop.android.openbudgetapp.utils.Constants;
import kg.kloop.android.openbudgetapp.models.MainViewModel;
import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.adapters.TenderFragmentSimpleUsersPageAdapter;
import kg.kloop.android.openbudgetapp.adapters.TendersFragmentEditorsPageAdapter;

public class MainActivity extends AppCompatActivity implements LifecycleOwner {

    private static final int RC_SIGN_IN = 100;
    private static final String TAG = MainActivity.class.getSimpleName();
    private MainViewModel mainViewModel;
    private MainActivityController controller;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.main_view_pager);
        tabLayout = findViewById(R.id.main_tab_layout);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.main_drawer_layout);

        setSupportActionBar(toolbar);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        controller = new MainActivityController(mainViewModel);

        mainViewModel.getFirebaseUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(@Nullable FirebaseUser firebaseUser) {
                if (firebaseUser == null) {
                    signIn();
                } else {
                    updateLayout(firebaseUser);
                }
            }
        });
        navigationView = findViewById(R.id.main_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.log_out_nav_drawer_item:
                        drawerLayout.closeDrawers();
                        AuthUI.getInstance().signOut(MainActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), getString(R.string.yout_signed_out), Toast.LENGTH_SHORT).show();
                                            controller.signOut();
                                            //hideUi();
                                            signIn();
                                        } else {
                                            Log.v(TAG, task.getException().getMessage());
                                        }
                                    }
                                });
                        break;

                }
                return true;
            }
        });
    }

    private void updateLayout(final FirebaseUser firebaseUser) {
        mainViewModel.getUserRoleMutableLiveData().observe(MainActivity.this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String userRole) {
                ((TextView)drawerLayout.findViewById(R.id.nav_name_text_view)).setText(firebaseUser.getDisplayName());
                ((TextView)drawerLayout.findViewById(R.id.nav_email_text_view)).setText(firebaseUser.getEmail());
                ((TextView)drawerLayout.findViewById(R.id.nav_role_text_view)).setText(userRole);
                viewPager.setOffscreenPageLimit(3);
                if (userRole.equals(Constants.USER)) {
                    viewPager.setAdapter(new TenderFragmentSimpleUsersPageAdapter(getSupportFragmentManager()));
                } else if (userRole.equals(Constants.EDITOR)) {
                    viewPager.setAdapter(new TendersFragmentEditorsPageAdapter(getSupportFragmentManager()));
                } else {
                    viewPager.setAdapter(new TenderFragmentSimpleUsersPageAdapter(getSupportFragmentManager()));
                    signIn();
                }
                tabLayout.setupWithViewPager(viewPager);
            }
        });
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
            //IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                controller.signIn();
                mainViewModel.getFirebaseUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
                    @Override
                    public void onChanged(@Nullable FirebaseUser firebaseUser) {
                        if (firebaseUser != null) {
                            controller.saveUserToDb(firebaseUser);
                        }
                        updateLayout(firebaseUser);
                    }
                });
            }
        }
    }

    private void hideUi() {
        LinearLayout linearLayout = findViewById(R.id.main_activity_linear_layout);
        linearLayout.removeAllViews();
    }
}
