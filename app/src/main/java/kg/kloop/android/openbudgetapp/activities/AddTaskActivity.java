package kg.kloop.android.openbudgetapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.objects.TenderTask;

public class AddTaskActivity extends AppCompatActivity {

    private static final String TAG = AddTaskActivity.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText taskEditText;
    private RadioButton photoRadioButton;
    private RadioButton videoRadioButton;
    private RadioButton audioRadioButton;
    private String tenderId;
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskEditText = findViewById(R.id.add_task_edit_text);
        photoRadioButton = findViewById(R.id.photo_radio_button);
        videoRadioButton = findViewById(R.id.video_radio_button);
        audioRadioButton = findViewById(R.id.audion_radio_button);
        Intent intent = getIntent();
        tenderId = intent.getStringExtra("tender_id");
        collectionReference = db.collection("tenders/" + tenderId + "/tasks/");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done_add_task_menu_item:
                Intent intent = new Intent();
                TenderTask task = new TenderTask();
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
