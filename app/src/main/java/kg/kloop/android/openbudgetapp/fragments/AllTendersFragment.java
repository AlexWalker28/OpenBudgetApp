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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import kg.kloop.android.openbudgetapp.objects.User;

public class AllTendersFragment extends Fragment implements LifecycleOwner {

    private static final String TAG = AllTendersFragment.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TenderFirestorePagingAdapter adapter;
    private ArrayList<Tender> tenderArrayList;
    private CollectionReference tendersDbColRef;

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
        tendersDbColRef = db.collection("tenders_db");
        tenderArrayList = new ArrayList<>();
        MainViewModel viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        MutableLiveData<User> userLiveData = viewModel.getUserLiveData();
        userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(@android.support.annotation.Nullable User user) {
                setHasOptionsMenu(true);
                //adapter = new TendersRecyclerViewAdapter(getContext(), tenderArrayList, user);

                // The "base query" is a query with no startAt/endAt/limit clauses that the adapter can use
                // to form smaller queries for each page.  It should only include where() and orderBy() clauses
                Query baseQuery = tendersDbColRef.orderBy("tender_num", Query.Direction.DESCENDING);

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
                /*tendersDbColRef
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                //TODO: implement correctly https://firebase.google.com/docs/firestore/query-data/listen
                                tenderArrayList.clear();
                                tenderArrayList.addAll(queryDocumentSnapshots.toObjects(Tender.class));
                                adapter.notifyDataSetChanged();
                            }
                        });*/
                tendersDbColRef.orderBy("planSum", Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()) {
                                        Log.v(TAG, "list size: " + task.getResult().size());
                                        tenderArrayList.clear();
                                        tenderArrayList.addAll(task.getResult().toObjects(Tender.class));
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Log.v(TAG, task.getException().getMessage());
                                }
                            }
                        });

                allTendersRecyclerView.setAdapter(adapter);
                allTendersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "item clicked");
        switch (item.getItemId()) {
            case R.id.filter_menu_item:
                tendersDbColRef.orderBy("planSum", Query.Direction.ASCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()) {
                                        Log.v(TAG, "list size: " + task.getResult().size());
                                        tenderArrayList.clear();
                                        tenderArrayList.addAll(task.getResult().toObjects(Tender.class));
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Log.v(TAG, task.getException().getMessage());
                                }
                            }
                        });
        }
        return true;
    }
}
