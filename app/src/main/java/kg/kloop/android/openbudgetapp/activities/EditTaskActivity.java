package kg.kloop.android.openbudgetapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.objects.TenderTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditTaskActivity extends AppCompatActivity implements LifecycleOwner {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        final EditText editTaskEditText = findViewById(R.id.edit_task_edit_text);
        Button submitButton = findViewById(R.id.edit_task_submit_button);
        editTaskEditText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Intent intent = getIntent();
        TenderTask task = (TenderTask) intent.getSerializableExtra("task");
        final DocumentReference taskDocRef = db.document("tasks/" + task.getId());
        taskDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null && task.getResult().exists()) {
                        TenderTask task1 = task.getResult().toObject(TenderTask.class);
                        editTaskEditText.setText(task1.getDescription());
                    }
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskDocRef.update("description", editTaskEditText.getText().toString()).addOnSuccessListener(EditTaskActivity.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), R.string.changes_applied, Toast.LENGTH_SHORT).show();
                        Intent editDataIntent = new Intent();
                        editDataIntent.putExtra("task_description", editTaskEditText.getText().toString());
                        setResult(RESULT_OK, editDataIntent);
                        finish();
                    }
                });
            }
        });


    }
}
