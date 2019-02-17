package kg.kloop.android.openbudgetapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.adapters.TenderTaskRecyclerViewAdapter;
import kg.kloop.android.openbudgetapp.models.MainViewModel;
import kg.kloop.android.openbudgetapp.models.TasksMapActivityViewModel;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksMapFragment extends Fragment {


    public TasksMapFragment() {
    }

    public static TasksMapFragment newInstance() {
        return new TasksMapFragment();
    }
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tasks_map, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.tasks_map_fragment_recycler_view);
        ViewModel viewModel = ViewModelProviders.of(getActivity()).get(TasksMapActivityViewModel.class);
        ArrayList<TenderTask> tasks = ((TasksMapActivityViewModel) viewModel).getSamePlaceTasks().getValue();
        User user = ((TasksMapActivityViewModel) viewModel).getUser().getValue();
        TenderTaskRecyclerViewAdapter adapter = new TenderTaskRecyclerViewAdapter(getActivity(), tasks, user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

}
