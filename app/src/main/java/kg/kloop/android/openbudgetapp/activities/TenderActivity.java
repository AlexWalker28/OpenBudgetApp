package kg.kloop.android.openbudgetapp.activities;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kg.kloop.android.openbudgetapp.utils.Constants;
import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;
import kg.kloop.android.openbudgetapp.objects.User;

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
    private ArrayList<TenderTask> taskArrayList;
    private CollectionReference workCollectionReference;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tender);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        taskArrayList = new ArrayList<>();

        Intent intent = getIntent();
        currentUser = (User) intent.getSerializableExtra("current_user");
        tender = (Tender) intent.getSerializableExtra("tender");
        tasksCollectionReference = db.collection("tenders/" + tender.getId() + "/tasks/");
        tenderDocumentReference = db.document("tenders/" + tender.getId());
        userDocRef = db.document("users/" + currentUser.getId());
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
        if (currentUser != null) {
            if (currentUser.getRole().equals(Constants.USER)) {
                DocumentReference documentReference = db.document("users/" + currentUser.getId() + "/tenders/" + tender.getId());
                getMenuInflater().inflate(R.menu.user_tender_menu, menu);
                documentReference.get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            MenuItem menuItem = menu.findItem(R.id.accept_tender_menu_item);
                            menuItem.setIcon(R.drawable.ic_bookmark_white_24dp);
                        }
                    }
                });
            } else if (currentUser.getRole().equals(Constants.EDITOR)) {
                getMenuInflater().inflate(R.menu.editor_menu, menu);
            }
        }
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
                Map<String, String> tenderIdMap = new HashMap<>();
                tenderIdMap.put("id", tender.getId());
                userDocRef.collection("tenders").document(tender.getId()).set(tenderIdMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.tender_accepted), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
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
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tender_work, (ViewGroup) layout.getRootView(), false);
        TextView textView = view.findViewById(R.id.tender_work_text_view);
        textView.setText(tenderTaskWork.getText());
        if (tenderTaskWork.getPhotoUrl() != null) {
            ImageView imageView = view.findViewById(R.id.tender_work_image_view);
            Glide.with(this)
                    .load(tenderTaskWork.getPhotoUrl())
                    .into(imageView);
        }
        layout.addView(view);
    }
}