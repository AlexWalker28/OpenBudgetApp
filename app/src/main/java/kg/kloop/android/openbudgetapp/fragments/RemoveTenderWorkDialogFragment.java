package kg.kloop.android.openbudgetapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;

public class RemoveTenderWorkDialogFragment extends DialogFragment {

    private static final String TAG = RemoveTenderWorkDialogFragment.class.getSimpleName();
    private FirebaseFirestore firebaseFirestore;
    private Button removeButton;
    private Button cancelButton;
    private TenderTask task;
    private TenderTaskWork work;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;


    public RemoveTenderWorkDialogFragment() {
    }

    public static RemoveTenderWorkDialogFragment newInstance(TenderTask task, TenderTaskWork work) {
        RemoveTenderWorkDialogFragment frag = new RemoveTenderWorkDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("task", task);
        args.putSerializable("work", work);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_remove_work, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images");
        task = (TenderTask) getArguments().getSerializable("task");
        work = (TenderTaskWork) getArguments().getSerializable("work");
        removeButton = view.findViewById(R.id.ok_remove_work_dialog_fragment_button);
        cancelButton = view.findViewById(R.id.cancel_remove_work_dialog_fragment_button);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeWork(work, task);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    private void removeWork(TenderTaskWork work, final TenderTask task) {
        final DocumentReference workDocRef = firebaseFirestore.collection("tasks").document(task.getId()).collection("work").document(work.getId());
        // delete photos
        workDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.i(TAG, "onSuccess: success");
                if (documentSnapshot.exists()) {
                    TenderTaskWork taskWork = documentSnapshot.toObject(TenderTaskWork.class);
                    if (taskWork.getPhotoUrlList() != null && !taskWork.getPhotoUrlList().isEmpty()) {
                        for (String url : taskWork.getPhotoUrlList()) {
                            Log.i(TAG, "onSuccess: url: " + url);
                            Log.i(TAG, "onSuccess: file name: " + getChildName(url));
                            storageReference.child(getChildName(url)).delete();
                        }

                    }
                    // delete work itself
                    workDocRef.delete();
                    //check if any work left and update tender info
                    updateTenderStatus();
                }
            }
        });

    }

    private void updateTenderStatus() {
        CollectionReference workColRef = firebaseFirestore.collection("tasks").document(task.getId()).collection("work");
        workColRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                    // set hasWork of tender to false if no work left
                    Log.i(TAG, "onSuccess: no work left");
                    DocumentReference tenderDocRef = firebaseFirestore.collection("tenders_db").document(task.getTenderId());
                    tenderDocRef.update("hasWork", false);
                    Toast.makeText(getContext(), getString(R.string.work_removed), Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
    }

    private String getChildName(String url) {
        int start = url.indexOf("%2F") + "%2F".length();
        int end = url.indexOf("?", start);
        return url.substring(start, end);
    }
}
