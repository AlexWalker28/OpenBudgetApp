package kg.kloop.android.openbudgetapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class AllTendersFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TendersRecyclerViewAdapter adapter;

    public AllTendersFragment() {
    }

    public static Fragment newInstance() {
        return new AllTendersFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_all_tenders, container, false);
        RecyclerView allTendersRecyclerView = view.findViewById(R.id.all_tenders_recycler_view);
        CollectionReference collectionReference = db.collection("tenders");
        final ArrayList<Tender> tenderArrayList = new ArrayList<>();
        adapter = new TendersRecyclerViewAdapter(getContext(), tenderArrayList);
        collectionReference.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                tenderArrayList.addAll(queryDocumentSnapshots.toObjects(Tender.class));
                adapter.notifyDataSetChanged();
            }
        });
        allTendersRecyclerView.setHasFixedSize(true);
        allTendersRecyclerView.setAdapter(adapter);
        allTendersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

}
