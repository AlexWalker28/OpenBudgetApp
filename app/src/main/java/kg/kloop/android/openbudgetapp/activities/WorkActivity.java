package kg.kloop.android.openbudgetapp.activities;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.adapters.WorkRecyclerViewAdapter;
import kg.kloop.android.openbudgetapp.controllers.WorkActivityController;
import kg.kloop.android.openbudgetapp.models.WorkActivityModel;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;

public class WorkActivity extends AppCompatActivity {

    private WorkActivityController controller;
    private WorkActivityModel model;
    private WorkRecyclerViewAdapter adapter;
    private ArrayList<TenderTaskWork> workArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        model = new WorkActivityModel();
        controller = new WorkActivityController(model);
        workArrayList = new ArrayList<>();
        RecyclerView workRecyclerView = findViewById(R.id.work_activity_recycler_view);
        adapter = new WorkRecyclerViewAdapter(getApplicationContext(), workArrayList);
        workRecyclerView.setAdapter(adapter);
        workRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String taskId = intent.getStringExtra("task_id");
        String tenderId = intent.getStringExtra("tender_id");
        controller.getWorkForTask(tenderId, taskId);
        MutableLiveData<ArrayList<TenderTaskWork>> liveData = model.getWorkArrayList();
        liveData.observe(this, new Observer<ArrayList<TenderTaskWork>>() {
            @Override
            public void onChanged(@Nullable ArrayList<TenderTaskWork> tenderTaskWorks) {
                if (tenderTaskWorks != null) {
                    workArrayList.addAll(tenderTaskWorks);
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }
}
