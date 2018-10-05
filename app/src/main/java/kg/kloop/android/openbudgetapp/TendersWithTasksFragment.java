package kg.kloop.android.openbudgetapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class TendersWithTasksFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TendersRecyclerViewAdapter adapter;
    private ArrayList<Tender> tenderArrayList;


    public TendersWithTasksFragment() {
    }

    public static Fragment newInstance() {
        return new TendersWithTasksFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tenders_with_tasks, container, false);
        RecyclerView tendersWithTasksRecyclerView = view.findViewById(R.id.tenders_with_tasks_recycler_view);
        CollectionReference collectionReference = db.collection("tenders");
        tenderArrayList = new ArrayList<>();
        adapter = new TendersRecyclerViewAdapter(getContext(), tenderArrayList);
        collectionReference
                .whereEqualTo("isCompleted", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                //TODO: implement correctly https://firebase.google.com/docs/firestore/query-data/listen
                tenderArrayList.clear();
                tenderArrayList.addAll(queryDocumentSnapshots.toObjects(Tender.class));
                adapter.notifyDataSetChanged();
            }
        });
        tendersWithTasksRecyclerView.setHasFixedSize(true);
        tendersWithTasksRecyclerView.setAdapter(adapter);
        tendersWithTasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

}
