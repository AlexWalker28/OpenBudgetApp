package kg.kloop.android.openbudgetapp.fragments;


import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.activities.SearchResultActivity;
import kg.kloop.android.openbudgetapp.adapters.TenderFirestorePagingAdapter;
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
    private RecyclerView allTendersRecyclerView;

    public AllTendersFragment() {
    }

    public static Fragment newInstance() {
        return new AllTendersFragment();
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_all_tenders, container, false);
        allTendersRecyclerView = view.findViewById(R.id.all_tenders_recycler_view);
        tendersDbColRef = db.collection("tenders_db");
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        MutableLiveData<User> userLiveData = viewModel.getUserLiveData();
        userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(@androidx.annotation.Nullable User user) {
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
        return view;
    }

    private void showSearchDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        SearchTenderDialogFragment searchTenderDialogFragment = SearchTenderDialogFragment.newInstance("Search");
        searchTenderDialogFragment.show(fm, "fragment_search");
    }
}
