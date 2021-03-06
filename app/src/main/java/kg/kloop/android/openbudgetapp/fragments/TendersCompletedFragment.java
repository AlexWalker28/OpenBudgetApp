package kg.kloop.android.openbudgetapp.fragments;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import kg.kloop.android.openbudgetapp.models.MainViewModel;
import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.adapters.TendersRecyclerViewAdapter;
import kg.kloop.android.openbudgetapp.objects.User;

public class TendersCompletedFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TendersRecyclerViewAdapter adapter;
    private ArrayList<Tender> tenderArrayList;
    private User currentUser;

    public TendersCompletedFragment() {
    }

    public static Fragment newInstance() {
        return new TendersCompletedFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tenders_completed, container, false);
        final RecyclerView tendersCompletedRecyclerView = view.findViewById(R.id.tenders_completed_recycler_view);
        final CollectionReference collectionReference = db.collection("tenders");
        tenderArrayList = new ArrayList<>();
        MainViewModel viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        MutableLiveData<User> userLiveData = viewModel.getUserLiveData();
        userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(@androidx.annotation.Nullable User user) {
                adapter = new TendersRecyclerViewAdapter(getContext(), tenderArrayList, user);
                collectionReference
                        .whereEqualTo("isCompleted", true)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                //TODO: implement correctly https://firebase.google.com/docs/firestore/query-data/listen
                                tenderArrayList.clear();
                                tenderArrayList.addAll(queryDocumentSnapshots.toObjects(Tender.class));
                                adapter.notifyDataSetChanged();
                            }
                        });

                tendersCompletedRecyclerView.setHasFixedSize(true);
                tendersCompletedRecyclerView.setAdapter(adapter);
                tendersCompletedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });
        return view;
    }




}
