package kg.kloop.android.openbudgetapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.adapters.TenderTaskRecyclerViewAdapter;
import kg.kloop.android.openbudgetapp.adapters.TendersRecyclerViewAdapter;
import kg.kloop.android.openbudgetapp.models.MainViewModel;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.User;

public class TasksForModerationFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TenderTaskRecyclerViewAdapter adapter;
    private ArrayList<TenderTask> tenderTasks;

    public TasksForModerationFragment() {
    }

    public static Fragment newInstance() {
        return new TasksForModerationFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks_for_moderation, container, false);

        final RecyclerView tendersCompletedRecyclerView = view.findViewById(R.id.tasks_for_moderation_recycler_view);
        final CollectionReference tasksColRef = db.collection("tasks/");
        tenderTasks = new ArrayList<>();
        MainViewModel viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        MutableLiveData<User> userLiveData = viewModel.getUserLiveData();
        userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(@androidx.annotation.Nullable User user) {
                adapter = new TenderTaskRecyclerViewAdapter(getContext(), tenderTasks, user);
                tasksColRef
                        .whereEqualTo("needModeration", true)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                //TODO: implement correctly https://firebase.google.com/docs/firestore/query-data/listen
                                tenderTasks.clear();
                                tenderTasks.addAll(queryDocumentSnapshots.toObjects(TenderTask.class));
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
