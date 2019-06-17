package kg.kloop.android.openbudgetapp.controllers;

import android.content.Intent;
import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kg.kloop.android.openbudgetapp.models.TenderActivityModel;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;
import kg.kloop.android.openbudgetapp.objects.User;
import kg.kloop.android.openbudgetapp.utils.Constants;

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
    private Query query;

    public TenderActivityController(final TenderActivityModel model, Intent intent) {
        this.model = model;
        db = FirebaseFirestore.getInstance();
        currentUser = (User) intent.getSerializableExtra("current_user");
        model.setCurrentUser(currentUser);
        tender = (Tender) intent.getSerializableExtra("tender");
        model.setTender(tender);
        tasksCollectionReference = db.collection("tasks/");
        tenderDocumentReference = db.document("tenders/" + tender.getNumber());
        userDocRef = db.document("users/" + currentUser.getId());
        taskArrayList = new ArrayList<>();

        query = tasksCollectionReference.whereEqualTo("tenderNumber", tender.getNumber());

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    Log.d(TAG, "tasks: " + queryDocumentSnapshots.size());
                    taskArrayList.clear();
                    taskArrayList.addAll(queryDocumentSnapshots.toObjects(TenderTask.class));
                    model.getTenderTaskArrayListMutableLiveData().setValue(taskArrayList);

                    for (TenderTask task : taskArrayList) { //TODO: there is probably better solution on db level
                        workCollectionReference = db.collection("tasks/").document(task.getId()).collection("work");
                        workCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    model.getTenderTaskWorkArrayList().setValue((ArrayList<TenderTaskWork>) task.getResult().toObjects(TenderTaskWork.class));
                                    //Log.v(TAG, "tender works: " + model.getTenderTaskWorkArrayList().getValue().size());
                                    //updateWorkViews(task.getResult().getDocuments(), linearLayout);
                                } else Log.v(TAG, task.getException().getMessage());
                            }
                        });
                    }
                }
            }
        });


        DocumentReference tendersDocRef = db.document("users/" + currentUser.getId() + "/tenders/" + tender.getNumber());

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

    public void openTender() {
        tenderDocumentReference.update("isCompleted", false)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            model.getTenderClosed().postValue(false);
                        } else Log.v(TAG, task.getException().getMessage());
                    }
                });
    }

    public void acceptTender() {
        //saving the whole tender to user's collection
        final DocumentReference tenderRef = userDocRef.collection("tenders").document(tender.getNumber());
        tenderRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    tenderRef.delete();
                    model.getTenderAccepted().postValue(false);
                } else {
                    tenderRef.set(tender).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                model.getTenderAccepted().postValue(true);
                            } else Log.v(TAG, task.getException().getMessage());
                        }
                    });
                }
            }
        });
    }
}
