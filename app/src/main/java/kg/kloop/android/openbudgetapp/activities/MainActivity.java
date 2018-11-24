package kg.kloop.android.openbudgetapp.activities;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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

    }

    private void updateLayout(final FirebaseUser firebaseUser) {
        mainViewModel.getUserRoleMutableLiveData().observe(MainActivity.this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String userRole) {
                ((TextView)drawerLayout.findViewById(R.id.nav_email_text_view)).setText(firebaseUser.getEmail());
                ((TextView)drawerLayout.findViewById(R.id.nav_name_text_view)).setText(firebaseUser.getDisplayName());
                if (userRole.equals(Constants.USER)) {
                    viewPager.setAdapter(new TenderFragmentSimpleUsersPageAdapter(getSupportFragmentManager()));
                    ((TextView)drawerLayout.findViewById(R.id.nav_role_text_view)).setText(userRole);
                } else if (userRole.equals(Constants.EDITOR)) {
                    viewPager.setAdapter(new TendersFragmentEditorsPageAdapter(getSupportFragmentManager()));
                    ((TextView)drawerLayout.findViewById(R.id.nav_role_text_view)).setText(userRole);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu_item:
                AuthUI.getInstance().signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.yout_signed_out), Toast.LENGTH_SHORT).show();
                                    controller.signOut();
                                    hideUi();
                                    signIn();
                                } else {
                                    Log.v(TAG, task.getException().getMessage());
                                }
                            }
                        });
                return true;
        }
        return false;
    }

    private void hideUi() {
        LinearLayout linearLayout = findViewById(R.id.main_activity_linear_layout);
        linearLayout.removeAllViews();
    }
}
