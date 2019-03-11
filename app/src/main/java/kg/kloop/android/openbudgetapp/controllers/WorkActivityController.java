package kg.kloop.android.openbudgetapp.controllers;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import kg.kloop.android.openbudgetapp.models.WorkActivityModel;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;

public class WorkActivityController {

    private static final String TAG = WorkActivityController.class.getSimpleName();
    private WorkActivityModel model;
    private FirebaseFirestore firebaseFirestore;

    public WorkActivityController(WorkActivityModel model) {
        this.model = model;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void getWorkForTask(String taskId) {
        CollectionReference workCollectionRef = firebaseFirestore.collection("tasks/" + taskId + "/work");
        workCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {Log.v(TAG, e.getMessage());}
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    model.getWorkArrayList().setValue((ArrayList<TenderTaskWork>) queryDocumentSnapshots.toObjects(TenderTaskWork.class));

                    Log.v(TAG, "workArrayListMutableLiveData: " + model.getWorkArrayList().getValue().size());
                }
            }
        });
    }
}
