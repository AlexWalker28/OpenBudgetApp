package kg.kloop.android.openbudgetapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class TenderActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Tender tender;
    private CollectionReference tasksCollectionReference;
    private DocumentReference tenderDocumentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tender);

        Intent intent = getIntent();
        tender = (Tender) intent.getSerializableExtra("tender");
        tasksCollectionReference = db.collection("tenders/" + tender.getId() + "/tasks/");
        tenderDocumentReference = db.document("tenders/" + tender.getId());
        TextView purchaseTextView = findViewById(R.id.tender_purchase_text_view);
        TextView orgNameTextView = findViewById(R.id.tender_org_name_text_view);
        purchaseTextView.setText(tender.getPurchase());
        orgNameTextView.setText(tender.getOrgName());
        tasksCollectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    updateTaskViews(queryDocumentSnapshots);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_task_menu_item:
                Intent intent = new Intent(TenderActivity.this, AddTaskActivity.class);
                intent.putExtra("tender_id", tender.getId());
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.close_tender_menu_item:
                tenderDocumentReference.update("isCompleted", true)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.tender_closed), Toast.LENGTH_SHORT).show();
                            //TODO: add snackbar to cancel this
                            finish();
                        }
                    }
                });
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {
            String taskId = data.getStringExtra("task_id");
            tasksCollectionReference.document(taskId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        if (snapshot.exists()) {
                            addTask(snapshot.toObject(TenderTask.class));
                        }
                    }
                }
            });
        }


    }

    private void addTask(TenderTask task) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tender_task, null);
        TextView textView = view.findViewById(R.id.tender_task_text_view);
        textView.setText(task.getDescription());
        LinearLayout linearLayout = findViewById(R.id.tender_task_linear_layout);
        linearLayout.addView(view);
    }

    private void updateTaskViews(QuerySnapshot queryDocumentSnapshots) {
        LinearLayout linearLayout = findViewById(R.id.tender_task_linear_layout);
        linearLayout.removeAllViews();
        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
            addTask(snapshot.toObject(TenderTask.class));
        }
    }
}
