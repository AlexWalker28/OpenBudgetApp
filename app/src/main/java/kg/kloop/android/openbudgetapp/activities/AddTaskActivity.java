package kg.kloop.android.openbudgetapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.objects.TenderTask;

public class AddTaskActivity extends AppCompatActivity {

    private static final String TAG = AddTaskActivity.class.getSimpleName();
    private static final int LOCATION_REQUEST_CODE = 123;
    private static final int GPS_PERMISSION_REQUEST_CODE = 1;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText taskEditText;
    private RadioButton photoRadioButton;
    private RadioButton videoRadioButton;
    private RadioButton audioRadioButton;
    private ImageView locationImageView;
    private String tenderId;
    private CollectionReference collectionReference;
    private TenderTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskEditText = findViewById(R.id.add_task_edit_text);
        photoRadioButton = findViewById(R.id.photo_radio_button);
        videoRadioButton = findViewById(R.id.video_radio_button);
        audioRadioButton = findViewById(R.id.audion_radio_button);
        locationImageView = findViewById(R.id.add_task_location_image_view);
        Intent intent = getIntent();
        task = new TenderTask();
        tenderId = intent.getStringExtra("tender_id");
        collectionReference = db.collection("tenders/" + tenderId + "/tasks/");

        locationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasGPSPermission()) {
                    openMapsActivity();
                } else requestGPSPermission();
            }
        });

    }

    private void openMapsActivity() {
        startActivityForResult(new Intent(AddTaskActivity.this, MapsActivity.class), LOCATION_REQUEST_CODE);
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
                case LOCATION_REQUEST_CODE:
                    if (data != null) {
                        LatLng latLng = new LatLng(data.getDoubleExtra("lat", 0), data.getDoubleExtra("lng", 0));
                        task.setLocation(latLng);
                        Toast.makeText(getApplicationContext(), "location: " + latLng.toString(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done_add_task_menu_item:
                Intent intent = new Intent();
                task.setDescription(taskEditText.getText().toString());
                task.setAttachmentTypes(getAttachmentTypes());
                task.setTenderId(tenderId);
                String taskId = collectionReference.document().getId();
                task.setId(taskId);
                collectionReference.document(taskId).set(task);
                intent.putExtra("task_id", taskId);
                setResult(RESULT_OK, intent);
                finish();
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
