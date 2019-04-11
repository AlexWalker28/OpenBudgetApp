package kg.kloop.android.openbudgetapp.fragments;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

public class MyTendersFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TendersRecyclerViewAdapter adapter;
    private ArrayList<Tender> tenderArrayList;
    private static final String TAG = MyTendersFragment.class.getSimpleName();
    private User currentUser;

    public MyTendersFragment() {
    }

    public static Fragment newInstance() {
        return new MyTendersFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_tenders, container, false);
        final RecyclerView myTendersRecyclerView = view.findViewById(R.id.my_tenders_recycler_view);
        //final CollectionReference tendersCollectionReference = db.collection("tenders_db");
        MainViewModel viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        MutableLiveData<User> userLiveData = viewModel.getUserLiveData();
        tenderArrayList = new ArrayList<>();
        userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(@androidx.annotation.Nullable User user) {
                currentUser = user;
                if (user != null) {
                    CollectionReference usersTendersCollectionRef = db.collection("users/" + currentUser.getId() + "/tenders");
                    usersTendersCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                tenderArrayList.clear();
                                tenderArrayList.addAll(queryDocumentSnapshots.toObjects(Tender.class));
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                    adapter = new TendersRecyclerViewAdapter(getContext(), tenderArrayList, currentUser);
                    myTendersRecyclerView.setHasFixedSize(true);
                    myTendersRecyclerView.setAdapter(adapter);
                    myTendersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }
        });
        return view;
    }

}
