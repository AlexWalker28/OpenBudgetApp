package kg.kloop.android.openbudgetapp.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.activities.FilterOptionsActivity;
import kg.kloop.android.openbudgetapp.activities.TasksMapActivity;
import kg.kloop.android.openbudgetapp.adapters.TenderTaskRecyclerViewAdapter;
import kg.kloop.android.openbudgetapp.adapters.TendersRecyclerViewAdapter;
import kg.kloop.android.openbudgetapp.database.TendersDatabaseDao;
import kg.kloop.android.openbudgetapp.models.MainViewModel;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.User;
import kg.kloop.android.openbudgetapp.utils.App;

import static android.app.Activity.RESULT_OK;

public class TendersWithTasksFragment extends Fragment {

    private static final String TAG = TendersWithTasksFragment.class.getSimpleName();
    private static final int FILTER_REQUEST = 34523;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TendersRecyclerViewAdapter adapter;
    private ArrayList<Tender> tenderArrayList;
    private MutableLiveData<User> userLiveData;
    private RecyclerView tendersWithTasksRecyclerView;
    private CollectionReference tendersColRef;
    private TextView infoTextView;
    private TendersDatabaseDao databaseDao;
    private boolean isFiltered = false;


    public TendersWithTasksFragment() {
    }

    public static Fragment newInstance() {
        return new TendersWithTasksFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tenders_with_tasks, container, false);
        setHasOptionsMenu(true);

        databaseDao = App.getInstance().getDatabase().tendersDatabaseDao();
        infoTextView = view.findViewById(R.id.tenders_with_tasks_info_text_view);
        infoTextView.setVisibility(View.GONE);
        tendersWithTasksRecyclerView = view.findViewById(R.id.tenders_with_tasks_recycler_view);
        tendersColRef = db.collection("tenders");
        tenderArrayList = new ArrayList<>();
        MainViewModel viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        userLiveData = viewModel.getUserLiveData();
        updateContent();

        return view;
    }

    private void updateContent() {
        userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(@androidx.annotation.Nullable User user) {
                adapter = new TendersRecyclerViewAdapter(getContext(), tenderArrayList, user);
                /*tendersColRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        long time = 0;
                        for (Tender tender : queryDocumentSnapshots.toObjects(Tender.class)) {
                            DocumentReference tenderDocRef = tendersColRef.document(tender.getTender_num());
                            tenderDocRef.update("updateTime", time += 1);
                            Log.i(TAG, "onSuccess: update time");
                        }
                    }
                });*/
                tendersColRef
                        .whereEqualTo("hasTasks", true)
                        .whereEqualTo("isCompleted", false)
                        // doesn't work yet
                        //.orderBy("updateTime", Query.Direction.DESCENDING)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                //TODO: implement correctly https://firebase.google.com/docs/firestore/query-data/listen
                                try {
                                    tenderArrayList.clear();
                                    tenderArrayList.addAll(queryDocumentSnapshots.toObjects(Tender.class));
                                    // just a quick solution
                                    rewriteTendersTable();
                                    adapter.notifyDataSetChanged();

                                } catch (NullPointerException npe) {
                                    npe.printStackTrace();
                                }
                            }
                        });

                tendersWithTasksRecyclerView.setHasFixedSize(true);
                tendersWithTasksRecyclerView.setAdapter(adapter);
                tendersWithTasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_task_menu_item:
                userLiveData.observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        Intent intent = new Intent(getActivity(), TasksMapActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.filter_task_menu_item:
                // in case I'll need to save tasks
                /*db.collection("tasks").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<TenderTask> tasks = queryDocumentSnapshots.toObjects(TenderTask.class);
                        for (TenderTask task : tasks) {
                            new AddTasksAsyncTask().execute(task);
                        }
                    }
                });*/
                if (!isFiltered) {
                    tendersColRef
                            .whereEqualTo("hasWork", true)
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

                infoTextView.setVisibility(View.VISIBLE);
                infoTextView.setText(getString(R.string.filter_by_work_presence));
                isFiltered = true;
                } else {
                    updateContent();
                    infoTextView.setVisibility(View.GONE);
                    infoTextView.setText("");
                    isFiltered = false;
                }
                break;

            case R.id.search_tenders_menu_item:
                startActivityForResult(new Intent(getActivity(), FilterOptionsActivity.class), FILTER_REQUEST);
        }
        return true;
    }

    private void rewriteTendersTable() {
        synchronized (TendersWithTasksFragment.class) {
            new ClearTendersTableAsyncTask();
        }
        synchronized (this) {
            for (Tender tender : tenderArrayList) {
                new AddTendersAsyncTask().execute(tender);
            }
        }
    }

    private class GetTenderTaskAsyncTask extends AsyncTask<Void, Void, List<TenderTask>> {

        @Override
        protected List<TenderTask> doInBackground(Void... voids) {
            return databaseDao.getTasks();
        }

        @Override
        protected void onPostExecute(List<TenderTask> tenderTasks) {
            TenderTaskRecyclerViewAdapter recyclerViewAdapter =
                    new TenderTaskRecyclerViewAdapter(getActivity(),
                            new ArrayList<>(tenderTasks),
                            userLiveData.getValue());
            tendersWithTasksRecyclerView.setHasFixedSize(true);
            tendersWithTasksRecyclerView.setAdapter(recyclerViewAdapter);
            tendersWithTasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    private class ClearTendersTableAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            databaseDao.clearTendersTable();
            return null;
        }
    }

    private class AddTasksAsyncTask extends AsyncTask<TenderTask, Void, Void> {

        @Override
        protected Void doInBackground(TenderTask... tenderTasks) {
            for (TenderTask task : tenderTasks) {
                Log.v(TAG, task.toString());
                databaseDao.insertTask(task);
            }
            return null;
        }
    }

    private class AddTendersAsyncTask extends AsyncTask<Tender, Void, Void> {
        @Override
        protected Void doInBackground(Tender... tenders) {
            for (Tender tender : tenders) {
                databaseDao.insertTender(tender);
            }
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILTER_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    tenderArrayList.clear();
                    tenderArrayList.addAll(data.<Tender>getParcelableArrayListExtra("tenders"));
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}

