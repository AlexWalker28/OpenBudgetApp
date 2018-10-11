package kg.kloop.android.openbudgetapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TenderActivity extends AppCompatActivity {

    private static final int ADD_TASK_REQUEST_CODE = 100;
    private static final int DO_TASK_REQUEST_CODE = 101;
    private static final String TAG = TenderActivity.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Tender tender;
    private CollectionReference tasksCollectionReference;
    private DocumentReference tenderDocumentReference;
    private FirebaseUser firebaseUser;
    private DocumentReference userDocRef;
    private User user;
    private ArrayList<TenderTask> taskArrayList;
    private CollectionReference workCollectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tender);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        taskArrayList = new ArrayList<>();
        tender = (Tender) intent.getSerializableExtra("tender");
        tasksCollectionReference = db.collection("tenders/" + tender.getId() + "/tasks/");
        tenderDocumentReference = db.document("tenders/" + tender.getId());

        TextView purchaseTextView = findViewById(R.id.tender_purchase_text_view);
        TextView plannedSumTextView = findViewById(R.id.tender_planned_sum_text_view);
        TextView orgNameTextView = findViewById(R.id.tender_org_name_text_view);
        purchaseTextView.setText(tender.getPurchase());
        String sum = tender.getPlanSum() + " " + tender.getCurrency();
        plannedSumTextView.setText(sum);
        orgNameTextView.setText(tender.getOrgName());
        tasksCollectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    updateTaskViews(queryDocumentSnapshots);
                    taskArrayList.addAll(queryDocumentSnapshots.toObjects(TenderTask.class));
                    showTenderWork(taskArrayList);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        userDocRef = db.document("users/" + firebaseUser.getUid());
        userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    if (user.getRole().equals(Constants.USER)) {
                        getMenuInflater().inflate(R.menu.user_tender_menu, menu);
                        menu.findItem(R.id.deny_tender_menu_item).setVisible(false);
                    } else if (user.getRole().equals(Constants.EDITOR)) {
                        getMenuInflater().inflate(R.menu.editor_menu, menu);
                    }
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_task_menu_item:
                Intent intent = new Intent(TenderActivity.this, AddTaskActivity.class);
                intent.putExtra("tender_id", tender.getId());
                startActivityForResult(intent, ADD_TASK_REQUEST_CODE);
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
            case R.id.accept_tender_menu_item:
                userDocRef.collection("tenders").document(tender.getId()).set(tender).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), getString(R.string.tender_accepted), Toast.LENGTH_SHORT).show();
                        //invalidateOptionsMenu();
                    }
                });
                break;
            case R.id.add_tender_task_data_menu_item:
                Intent intent1 = new Intent(TenderActivity.this, DoTaskActivity.class);
                intent1.putExtra("tender_id", tender.getId());
                startActivityForResult(intent1, DO_TASK_REQUEST_CODE);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {
            String taskId = data.getStringExtra("task_id");
            switch (requestCode) {
                case ADD_TASK_REQUEST_CODE:
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
                    break;

            }
        }
        if (requestCode == DO_TASK_REQUEST_CODE) {
            String taskId = taskArrayList.get(0).getId(); // just for first task for now TODO: fix this
            workCollectionReference = tasksCollectionReference.document(taskId).collection("work");
            workCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        taskArrayList.addAll(task.getResult().toObjects(TenderTask.class));
                        showTenderWork(taskArrayList);
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


    private void showTenderWork(ArrayList<TenderTask> taskArrayList) {
        final LinearLayout linearLayout = findViewById(R.id.tender_work_linear_layout);
        linearLayout.removeAllViews();
        for (TenderTask task : taskArrayList) {
            workCollectionReference = tasksCollectionReference.document(task.getId()).collection("work");
            workCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        updateWorkViews(task.getResult().getDocuments(), linearLayout);
                    } else Log.v(TAG, task.getException().getMessage());
                }
            });
        }
    }

    private void updateWorkViews(List<DocumentSnapshot> documentSnapshots, LinearLayout linearLayout) {
        for (DocumentSnapshot snapshot : documentSnapshots) {
            addWorkView(snapshot.toObject(TenderTaskWork.class), linearLayout);
        }
    }

    private void addWorkView(TenderTaskWork tenderTaskWork, ViewGroup layout) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tender_work, null);
        TextView textView = view.findViewById(R.id.tender_work_text_view);
        textView.setText(tenderTaskWork.getText());
        layout.addView(view);
    }
}
