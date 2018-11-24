package kg.kloop.android.openbudgetapp.fragments;


import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.activities.SearchResultActivity;
import kg.kloop.android.openbudgetapp.adapters.TenderFirestorePagingAdapter;
import kg.kloop.android.openbudgetapp.controllers.AllTendersController;
import kg.kloop.android.openbudgetapp.models.AllTendersModel;
import kg.kloop.android.openbudgetapp.models.MainViewModel;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.User;

public class AllTendersFragment extends Fragment implements LifecycleOwner {

    private static final String TAG = AllTendersFragment.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TenderFirestorePagingAdapter adapter;
    private CollectionReference tendersDbColRef;
    private MainViewModel viewModel;
    private User mUser;
    private AllTendersController allTendersController;
    private AllTendersModel allTendersModel;

    public AllTendersFragment() {
    }

    public static Fragment newInstance() {
        return new AllTendersFragment();
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_all_tenders, container, false);
        allTendersModel = new AllTendersModel();
        allTendersController = new AllTendersController(allTendersModel);
        final RecyclerView allTendersRecyclerView = view.findViewById(R.id.all_tenders_recycler_view);
        tendersDbColRef = db.collection("tenders_db");
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        MutableLiveData<User> userLiveData = viewModel.getUserLiveData();
        userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(@android.support.annotation.Nullable User user) {
                setHasOptionsMenu(true);
                mUser = user;
                // The "base query" is a query with no startAt/endAt/limit clauses that the adapter can use
                // to form smaller queries for each page.  It should only include where() and orderBy() clauses
                Query baseQuery = tendersDbColRef.orderBy("planSum", Query.Direction.DESCENDING);

                PagedList.Config config = new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPrefetchDistance(10)
                        .setPageSize(10)
                        .build();

                FirestorePagingOptions<Tender> options = new FirestorePagingOptions.Builder<Tender>()
                        .setLifecycleOwner(AllTendersFragment.this)
                        .setQuery(baseQuery, config, Tender.class)
                        .build();
                adapter = new TenderFirestorePagingAdapter(getContext(), user, options);

                allTendersRecyclerView.setAdapter(adapter);
                allTendersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

        viewModel.getTenderNum().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String tenderNumber) {
                if (tenderNumber != null) {
                    Log.i(TAG, "onChanged: tenderNum - " + tenderNumber);
                    loadTender(tenderNumber);
                    viewModel.getTenderNum().setValue(null);
                }
            }
        });
        return view;
    }

    private void loadTender(final String tenderNumber) {
        tendersDbColRef.document(tenderNumber).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                    Tender tender = documentSnapshot.toObject(Tender.class);
                    intent.putExtra("tender", tender);
                    intent.putExtra("current_user", mUser);
                    getActivity().startActivity(intent);
                } else {
                    Toast.makeText(getContext(), getString(R.string.loading_tender), Toast.LENGTH_SHORT).show();
                    allTendersController.getTenderFromDb(tenderNumber);
                    allTendersModel.getIsTenderLoaded().observe(AllTendersFragment.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(@Nullable Boolean isLoaded) {
                            if (isLoaded) {
                                loadTender(tenderNumber);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_menu_item:
                showSearchDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSearchDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        SearchTenderDialogFragment searchTenderDialogFragment = SearchTenderDialogFragment.newInstance("Search");
        searchTenderDialogFragment.show(fm, "fragment_search");
    }
}
