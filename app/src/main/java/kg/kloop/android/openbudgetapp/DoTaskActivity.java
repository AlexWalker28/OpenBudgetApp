package kg.kloop.android.openbudgetapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DoTaskActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ArrayList<TenderTask> tasks;
    private CollectionReference taskCollectionRef;
    private EditText doTaskEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_task);

        db = FirebaseFirestore.getInstance();
        String tenderId = getIntent().getStringExtra("tender_id");
        taskCollectionRef = db.collection("tenders/" + tenderId + "/tasks");

        final TextView tasksTextView = findViewById(R.id.do_task_task_text_view);
        doTaskEditText = findViewById(R.id.do_task_edit_text);
        ImageButton attachFileImageButton = findViewById(R.id.add_attachment_image_button);
        tasks = new ArrayList<>();

        taskCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                tasks.addAll(queryDocumentSnapshots.toObjects(TenderTask.class));
                for (TenderTask task : tasks) {
                    tasksTextView.append(task.getDescription() + "\n");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.do_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.do_task_send_menu_item:
                CollectionReference taskWorkCollectionRef = taskCollectionRef
                        .document(tasks.get(0).getId())
                        .collection("work");
                TenderTaskWork tenderTaskWork = new TenderTaskWork();
                tenderTaskWork.setId(taskWorkCollectionRef.document().getId());
                tenderTaskWork.setText(doTaskEditText.getText().toString());
                taskWorkCollectionRef.document(tenderTaskWork.getId()).set(tenderTaskWork);
                break;
        }
        return true;
    }
}
