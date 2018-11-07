package kg.kloop.android.openbudgetapp.fragments;


import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import kg.kloop.android.openbudgetapp.adapters.TenderFirestorePagingAdapter;
import kg.kloop.android.openbudgetapp.models.MainViewModel;
import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.adapters.TendersRecyclerViewAdapter;
import kg.kloop.android.openbudgetapp.objects.User;

public class AllTendersFragment extends Fragment implements LifecycleOwner {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TenderFirestorePagingAdapter adapter;
    private ArrayList<Tender> tenderArrayList;

    public AllTendersFragment() {
    }

    public static Fragment newInstance() {
        return new AllTendersFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_all_tenders, container, false);
        final RecyclerView allTendersRecyclerView = view.findViewById(R.id.all_tenders_recycler_view);
        final CollectionReference collectionReference = db.collection("tenders_db");
        tenderArrayList = new ArrayList<>();
        MainViewModel viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        MutableLiveData<User> userLiveData = viewModel.getUserLiveData();
        userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(@android.support.annotation.Nullable User user) {
                //adapter = new TendersRecyclerViewAdapter(getContext(), tenderArrayList, user);

                // The "base query" is a query with no startAt/endAt/limit clauses that the adapter can use
                // to form smaller queries for each page.  It should only include where() and orderBy() clauses
                Query baseQuery = collectionReference.orderBy("tender_num", Query.Direction.DESCENDING);

                PagedList.Config config = new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPrefetchDistance(10)
                        .setPageSize(10)
                        .build();

                FirestorePagingOptions<Tender> options = new FirestorePagingOptions.Builder<Tender>()
                        .setLifecycleOwner(AllTendersFragment.this)
                        .setQuery(baseQuery, config, Tender.class)
                        .build();
                adapter = new TenderFirestorePagingAdapter(getContext(), tenderArrayList, user, options);
                collectionReference
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                //TODO: implement correctly https://firebase.google.com/docs/firestore/query-data/listen
                                tenderArrayList.clear();
                                tenderArrayList.addAll(queryDocumentSnapshots.toObjects(Tender.class));
                                adapter.notifyDataSetChanged();
                            }
                        });

                allTendersRecyclerView.setHasFixedSize(true);
                allTendersRecyclerView.setAdapter(adapter);
                allTendersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

        return view;
    }

}
