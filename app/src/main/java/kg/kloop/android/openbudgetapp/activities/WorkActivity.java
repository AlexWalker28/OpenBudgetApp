package kg.kloop.android.openbudgetapp.activities;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.adapters.WorkRecyclerViewAdapter;
import kg.kloop.android.openbudgetapp.controllers.WorkActivityController;
import kg.kloop.android.openbudgetapp.models.WorkActivityModel;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;
import kg.kloop.android.openbudgetapp.objects.User;

public class WorkActivity extends AppCompatActivity {

    private static final String TAG = WorkActivity.class.getSimpleName();
    private WorkActivityController controller;
    private WorkActivityModel model;
    private WorkRecyclerViewAdapter adapter;
    private ArrayList<TenderTaskWork> workArrayList;
    private SupportMapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        model = new WorkActivityModel();
        controller = new WorkActivityController(model);
        workArrayList = new ArrayList<>();
        RecyclerView workRecyclerView = findViewById(R.id.work_activity_recycler_view);
        FloatingActionButton fab = findViewById(R.id.do_work_fab);
        TextView taskDescriptionTextView = findViewById(R.id.work_activity_task_description_text_view);
        adapter = new WorkRecyclerViewAdapter(getApplicationContext(), workArrayList);
        workRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        workRecyclerView.setLayoutManager(layoutManager);
        workRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        Intent intent = getIntent();
        String taskId = intent.getStringExtra("task_id");
        String tenderNum = intent.getStringExtra("tender_num");
        String tenderDescription = intent.getStringExtra("task_description");
        final User currentUser = (User) intent.getSerializableExtra("user");
        double taskLat = intent.getDoubleExtra("task_lat", 0);
        double taskLng = intent.getDoubleExtra("task_lng", 0);

        model.setTenderNum(tenderNum);
        model.setTaskId(taskId);
        model.setTaskDescription(tenderDescription);
        model.setTaskLat(taskLat);
        model.setTaskLng(taskLng);
        controller.getWorkForTask(tenderNum, taskId);
        taskDescriptionTextView.setText(model.getTaskDescription());
        MutableLiveData<ArrayList<TenderTaskWork>> liveData = model.getWorkArrayList();
        liveData.observe(this, new Observer<ArrayList<TenderTaskWork>>() {
            @Override
            public void onChanged(@Nullable ArrayList<TenderTaskWork> tenderTaskWorks) {
                if (tenderTaskWorks != null) {
                    workArrayList.clear();
                    workArrayList.addAll(tenderTaskWorks);
                    adapter.notifyDataSetChanged();
                }

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkActivity.this, DoTaskActivity.class);
                intent.putExtra("tender_num", model.getTenderNum());
                intent.putExtra("task_id", model.getTaskId());
                intent.putExtra("user", currentUser);
                startActivity(intent);
            }
        });
        Log.v(TAG, "lat: " + model.getTaskLat());
        if (model.getTaskLat() != 0) {
            final LatLng taskLatLng = new LatLng(model.getTaskLat(), model.getTaskLng());
            mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
            mMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    Log.i(TAG, "onMapReady: map is ready");
                    UiSettings uiSettings = googleMap.getUiSettings();
                    uiSettings.setCompassEnabled(true);
                    uiSettings.setAllGesturesEnabled(true);
                    uiSettings.setMyLocationButtonEnabled(true);
                    uiSettings.setZoomControlsEnabled(true);
                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(taskLatLng, 14));
                    googleMap.addMarker(new MarkerOptions().position(taskLatLng).title("Kloop"));
                }
            });
        }

    }
}
