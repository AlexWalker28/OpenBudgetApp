package kg.kloop.android.openbudgetapp.activities;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.adapters.WorkRecyclerViewAdapter;
import kg.kloop.android.openbudgetapp.controllers.WorkActivityController;
import kg.kloop.android.openbudgetapp.models.WorkActivityModel;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;
import kg.kloop.android.openbudgetapp.objects.User;
import kg.kloop.android.openbudgetapp.utils.Constants;

public class WorkActivity extends AppCompatActivity {

    private static final String TAG = WorkActivity.class.getSimpleName();
    public static final int EDIT_TASK = 100;
    private WorkActivityController controller;
    private WorkActivityModel model;
    private WorkRecyclerViewAdapter adapter;
    private ArrayList<TenderTaskWork> workArrayList;
    private SupportMapFragment mMapFragment;
    private User currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference taskDocRef;
    private TextView taskDescriptionTextView;
    private TextView tenderNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);


        model = new WorkActivityModel();
        controller = new WorkActivityController(model);
        Intent intent = getIntent();
        currentUser = (User) intent.getSerializableExtra("user");
        TenderTask task = (TenderTask) intent.getSerializableExtra("task");
        model.setTask(task);
        workArrayList = new ArrayList<>();
        setSupportActionBar((Toolbar) findViewById(R.id.work_toolbar));
        RecyclerView workRecyclerView = findViewById(R.id.work_activity_recycler_view);
        FloatingActionButton fab = findViewById(R.id.do_work_fab);
        tenderNumberTextView = findViewById(R.id.tender_number_text_view);
        tenderNumberTextView.setText(String.valueOf(model.getTask().getTenderId()));
        taskDescriptionTextView = findViewById(R.id.work_activity_task_description_text_view);
        adapter = new WorkRecyclerViewAdapter(WorkActivity.this, workArrayList, model.getTask(), controller, currentUser, getSupportFragmentManager());
        workRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        workRecyclerView.setLayoutManager(layoutManager);
        workRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        taskDocRef = db.document("tasks/" + model.getTask().getId());

        controller.getWorkForTask(model.getTask().getId());
        taskDescriptionTextView.setText(model.getTask().getDescription());
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
                intent.putExtra("tender_num", model.getTask().getTenderId());
                intent.putExtra("task_id", model.getTask().getId());
                intent.putExtra("user", currentUser);
                startActivity(intent);
            }
        });

        Log.v(TAG, "lat: " + model.getTask().getLatitude());
        if (model.getTask().getLatitude() != 0) {
            final LatLng taskLatLng = new LatLng(model.getTask().getLatitude(), model.getTask().getLongitude());
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
                    googleMap.addMarker(new MarkerOptions().position(taskLatLng).title(model.getTask().getPlaceName()));
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentUser.getRole().equals(Constants.MODERATOR) && model.getTask().isNeedModeration()) {
            getMenuInflater().inflate(R.menu.task_moderator_publish_menu, menu);
        } else if (currentUser.getRole().equals(Constants.MODERATOR) && !model.getTask().isNeedModeration()) {
            getMenuInflater().inflate(R.menu.task_moderate_menu, menu);
        } else if (currentUser.getRole().equals(Constants.USER)) {
            getMenuInflater().inflate(R.menu.user_work_activity_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_TASK) {
                taskDescriptionTextView.setText(data.getStringExtra("task_description"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.publish_task_moderator_menu_item:
                taskDocRef.update("needModeration", false).addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), R.string.task_is_published, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                break;
            case R.id.task_moderate_menu_item:
                taskDocRef.update("needModeration", true).addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), R.string.task_sent_to_moderator, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                break;
            case R.id.open_tender_page_menu_item:
                Intent openTenderIntent = new Intent(Intent.ACTION_VIEW);
                openTenderIntent.setData(Uri.parse("http://zakupki.gov.kg/popp/view/order/view.xhtml?id=" + model.getTask().getTenderId().substring(6)));
                startActivity(openTenderIntent);
                break;

            case R.id.remove_task_moderator_menu_item:
                if (currentUser.getRole().equals(Constants.MODERATOR)) {
                    taskDocRef.delete().addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), R.string.task_removed, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } else Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_task_moderator_menu_item:
                Intent intent = new Intent(WorkActivity.this, AddTaskActivity.class);
                intent.putExtra("task", model.getTask());
                intent.putExtra("isEdit", true);
                startActivityForResult(intent, EDIT_TASK);

            case R.id.user_work_activity_open_tender_page_menu_item:
                CollectionReference tenderColRef = db.collection("tenders_db");
                tenderColRef.whereEqualTo("tender_num", model.getTask().getTenderId())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    Tender tender = queryDocumentSnapshots.toObjects(Tender.class).get(0);
                                    openTenderPage(tender);
                                }
                            }
                        });
        }
        return true;
    }

    private void openTenderPage(Tender tender) {
        Intent openTenderIntent = new Intent(Intent.ACTION_VIEW);
        openTenderIntent.setData(Uri.parse("http://zakupki.gov.kg/popp/view/order/view.xhtml?id=" + tender.getTender_num().substring(6)));
        startActivity(openTenderIntent);
    }
}
