package kg.kloop.android.openbudgetapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.User;
import kg.kloop.android.openbudgetapp.utils.Constants;


public class AddTaskActivity extends AppCompatActivity {

    private static final String TAG = AddTaskActivity.class.getSimpleName();
    private static final int LOCATION_REQUEST_CODE = 123;
    private static final int GPS_PERMISSION_REQUEST_CODE = 1;
    private static final int PLACE_PICKER_REQUEST = 234;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText taskEditText;
    private RadioButton photoRadioButton;
    private RadioButton videoRadioButton;
    private RadioButton audioRadioButton;
    private ImageView locationImageView;
    private TextView placeTextView;
    private String tenderNum;
    private TenderTask task;
    private User user;
    private CollectionReference tasksCollectionReference;
    private DocumentReference tenderDocRef;
    private DocumentReference taskDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        taskEditText = findViewById(R.id.add_task_edit_text);
        photoRadioButton = findViewById(R.id.photo_radio_button);
        videoRadioButton = findViewById(R.id.video_radio_button);
        audioRadioButton = findViewById(R.id.audion_radio_button);
        locationImageView = findViewById(R.id.add_task_location_image_view);
        placeTextView = findViewById(R.id.add_task_place_text_view);
        setSupportActionBar((Toolbar) findViewById(R.id.add_task_toolbar));
        Intent intent = getIntent();

        // edit task
        if (intent.getSerializableExtra("task") != null) {
            getSupportActionBar().setTitle(R.string.edit);
            task = (TenderTask) intent.getSerializableExtra("task");
            placeTextView.setText(task.getPlaceName());
            taskDocRef = db.document("tasks/" + task.getId());
            taskDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && task.getResult().exists()) {
                            TenderTask task1 = task.getResult().toObject(TenderTask.class);
                            taskEditText.setText(task1.getDescription());
                            taskEditText.setText(task1.getDescription());
                        }
                    }
                }
            });
        } else { // add new task
            getSupportActionBar().setTitle(R.string.add_task);
            task = new TenderTask();
            tenderNum = intent.getStringExtra("tender_num");
            tasksCollectionReference = db.collection("tasks/");
            tenderDocRef = db.document("tenders_db/" + tenderNum);
        }

        user = (User) intent.getSerializableExtra("current_user");

        locationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasGPSPermission()) {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(AddTaskActivity.this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                    //openMapsActivity();
                } else requestGPSPermission();
            }
        });

    }

    private void requestGPSPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case GPS_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(AddTaskActivity.this, MapsActivity.class));
                }
                break;
        }
    }

    private boolean hasGPSPermission() {
        return ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_task_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                /*case LOCATION_REQUEST_CODE:
                    if (data != null) {
                        task.setLatitude(data.getDoubleExtra("lat", 0));
                        task.setLongitude(data.getDoubleExtra("lng", 0));
                    }
                    break;*/
                case  PLACE_PICKER_REQUEST:
                    Place place = PlacePicker.getPlace(this, data);
                    task.setPlaceName(place.getName().toString());
                    placeTextView.setText(task.getPlaceName());
                    task.setLatitude(place.getLatLng().latitude);
                    task.setLongitude(place.getLatLng().longitude);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done_add_task_menu_item:
                // update task
                if (getIntent().getBooleanExtra("isEdit", false)) {
                    Map<String, Object> updatedTask = new HashMap<>();
                    long time = System.currentTimeMillis();
                    updatedTask.put("description", taskEditText.getText().toString());
                    updatedTask.put("latitude", task.getLatitude());
                    updatedTask.put("longitude", task.getLongitude());
                    updatedTask.put("placeName", task.getPlaceName());
                    updatedTask.put("editTime", time);
                    tenderDocRef.update("updateTime", time);
                    taskDocRef.update(updatedTask).addOnSuccessListener(AddTaskActivity.this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), R.string.changes_applied, Toast.LENGTH_SHORT).show();
                            Intent editDataIntent = new Intent();
                            editDataIntent.putExtra("task_description", taskEditText.getText().toString());
                            setResult(RESULT_OK, editDataIntent);
                            finish();
                        }
                    });
                } else { // add new task
                    long time = System.currentTimeMillis();
                    task.setDescription(taskEditText.getText().toString());
                    task.setAttachmentTypes(getAttachmentTypes());
                    task.setTenderId(tenderNum);
                    String taskId = tasksCollectionReference.document().getId();
                    task.setId(taskId);
                    task.setAuthor(user);
                    task.setCreateTime(System.currentTimeMillis());
                    if (!user.getRole().equals(Constants.MODERATOR)) task.setNeedModeration(true);
                    tasksCollectionReference.document(task.getId()).set(task);
                    tenderDocRef.update("hasTasks", true);
                    tenderDocRef.update("isCompleted", false);
                    tenderDocRef.update("updateTime", time);
                    finish();
                }
        }
        return true;
    }

    private ArrayList<String> getAttachmentTypes() {
        ArrayList<String> types = new ArrayList<>();
        if (photoRadioButton.isChecked()) types.add(getString(R.string.photo));
        if (videoRadioButton.isChecked()) types.add(getString(R.string.video));
        if (audioRadioButton.isChecked()) types.add(getString(R.string.audio));
        return types;
    }
}
