package kg.kloop.android.openbudgetapp.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.fragments.TasksMapFragment;
import kg.kloop.android.openbudgetapp.models.TasksMapActivityViewModel;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TasksMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = TasksMapActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CollectionReference tasksColRef;
    private User user;
    private TasksMapActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.task_map);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        tasksColRef = db.collection("tasks");
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        viewModel = ViewModelProviders.of(this).get(TasksMapActivityViewModel.class);
        viewModel.getUser().setValue(user);

        setActionBar((Toolbar) findViewById(R.id.tasks_map_toolbar));
        getActionBar().setTitle(R.string.tasks);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng country = new LatLng(41.4313021, 72.6613778);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(country, 7.14f));

        final ArrayList<TenderTask> tasks = new ArrayList<>();
        tasksColRef
                .whereEqualTo("completed", false)
                .whereEqualTo("needModeration", false)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        tasks.clear();
                        tasks.addAll(queryDocumentSnapshots.toObjects(TenderTask.class));
                        displayMarkers(tasks);
                    }
                });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.i(TAG, "onInfoWindowClick: click");
                try {
                    Intent intent = new Intent(TasksMapActivity.this, WorkActivity.class);
                    intent.putExtra("task", tasks.get(Integer.valueOf(marker.getTag().toString())));
                    intent.putExtra("user", user);
                    startActivity(intent);
                    marker.getTag().toString();
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ArrayList<TenderTask> samePlaceTasks = new ArrayList<>();
                for (TenderTask task : tasks) {
                    if (task.getLatitude() == marker.getPosition().latitude && task.getLongitude() == marker.getPosition().longitude) {
                        samePlaceTasks.add(task);
                    }
                }
                if (samePlaceTasks.size() > 1) {
                    viewModel.getSamePlaceTasks().setValue(samePlaceTasks);
                    Fragment fragment = TasksMapFragment.newInstance();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.tasks_map_frame_layout, fragment).addToBackStack("tasks");
                    ft.commit();
                } else marker.showInfoWindow();
                return true;
            }
        });
    }

    private void displayMarkers(ArrayList<TenderTask> tasks) {
        LatLng country = new LatLng(41.4313021, 72.6613778);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(country, 7.14f));
        for (TenderTask task : tasks) {
            LatLng position = new LatLng(task.getLatitude(), task.getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(position).title(task.getDescription()));
            marker.setTag(tasks.indexOf(task));
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, @NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onMenuItemSelected(featureId, item);
    }
}

