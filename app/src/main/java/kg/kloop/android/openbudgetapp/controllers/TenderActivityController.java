package kg.kloop.android.openbudgetapp.controllers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kg.kloop.android.openbudgetapp.models.TenderActivityModel;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;
import kg.kloop.android.openbudgetapp.objects.User;

public class TenderActivityController {
    private static final String TAG = TenderActivityController.class.getSimpleName();
    private Tender tender;
    private CollectionReference tasksCollectionReference;
    private DocumentReference tenderDocumentReference;
    private DocumentReference userDocRef;
    private User currentUser;
    private FirebaseFirestore db;
    private ArrayList<TenderTask> taskArrayList;
    private TenderActivityModel model;
    private CollectionReference workCollectionReference;

    public TenderActivityController(final TenderActivityModel model, Intent intent) {
        this.model = model;
        db = FirebaseFirestore.getInstance();
        currentUser = (User) intent.getSerializableExtra("current_user");
        model.setCurrentUser(currentUser);
        tender = (Tender) intent.getSerializableExtra("tender");
        model.setTender(tender);
        tasksCollectionReference = db.collection("tenders_db/" + tender.getTender_num() + "/tasks/");
        tenderDocumentReference = db.document("tenders_db/" + tender.getTender_num());
        userDocRef = db.document("users/" + currentUser.getId());
        taskArrayList = new ArrayList<>();

        tasksCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    taskArrayList.addAll(queryDocumentSnapshots.toObjects(TenderTask.class));
                    model.getTenderTaskArrayListMutableLiveData().setValue(taskArrayList);

                    String taskId = taskArrayList.get(0).getId(); // just for first task for now TODO: fix this
                    workCollectionReference = tasksCollectionReference.document(taskId).collection("work");
                    for (TenderTask task : taskArrayList) {
                        workCollectionReference = tasksCollectionReference.document(task.getId()).collection("work");
                        workCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    model.getTenderTaskWorkArrayList().setValue((ArrayList<TenderTaskWork>) task.getResult().toObjects(TenderTaskWork.class));
                                    Log.v(TAG, "tender works: " + model.getTenderTaskWorkArrayList().getValue().size());
                                    //updateWorkViews(task.getResult().getDocuments(), linearLayout);
                                } else Log.v(TAG, task.getException().getMessage());
                            }
                        });
                    }
                }
            }
        });


        DocumentReference tendersDocRef = db.document("users/" + currentUser.getId() + "/tenders/" + tender.getId());

        tendersDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    model.getMyTender().postValue(true);
                }
            }
        });

    }

    public void closeTender() {
        tenderDocumentReference.update("isCompleted", true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            model.getTenderClosed().postValue(true);
                        } else Log.v(TAG, task.getException().getMessage());
                    }
                });
    }

    public void acceptTender() {
        Map<String, String> tenderIdMap = new HashMap<>();
        tenderIdMap.put("id", tender.getId());
        userDocRef.collection("tenders").document(tender.getId()).set(tenderIdMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    model.getTenderAccepted().postValue(true);
                } else Log.v(TAG, task.getException().getMessage());
            }
        });
    }

    public void addTask(Intent data) {
        String taskId = data.getStringExtra("task_id");
        tasksCollectionReference.document(taskId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        model.setAddedTask(snapshot.toObject(TenderTask.class));
                        model.getTaskAdded().setValue(true);
                    }
                }
            }
        });
    }

    public void updateTenderWork() {
        //TODO: updateTenderWork()
    }
}
