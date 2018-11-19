package kg.kloop.android.openbudgetapp.activities;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.adapters.TenderTaskRecyclerViewAdapter;
import kg.kloop.android.openbudgetapp.adapters.TenderWorkRecyclerViewAdapter;
import kg.kloop.android.openbudgetapp.controllers.TenderActivityController;
import kg.kloop.android.openbudgetapp.models.TenderActivityModel;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;
import kg.kloop.android.openbudgetapp.objects.User;
import kg.kloop.android.openbudgetapp.utils.Constants;

public class TenderActivity extends AppCompatActivity implements LifecycleOwner {

    private static final int ADD_TASK_REQUEST_CODE = 100;
    private static final int DO_TASK_REQUEST_CODE = 101;
    private static final String TAG = TenderActivity.class.getSimpleName();
    private TenderActivityController controller;
    private TenderActivityModel model;
    private User currentUser;
    private Tender tender;
    private ArrayList<TenderTaskWork> tenderTaskWorkArrayList;
    private TenderWorkRecyclerViewAdapter taskWorkAdapter;
    private TenderTaskRecyclerViewAdapter taskAdapter;
    private ArrayList<TenderTask> tenderTaskArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tender);

        Intent intent = getIntent();
        model = new TenderActivityModel();
        controller = new TenderActivityController(model, intent);

        tender = model.getTender();
        currentUser = model.getCurrentUser();
        tenderTaskWorkArrayList = new ArrayList<>();
        tenderTaskArrayList = new ArrayList<>();

        TextView purchaseTextView = findViewById(R.id.tender_purchase_text_view);
        TextView plannedSumTextView = findViewById(R.id.tender_planned_sum_text_view);
        TextView orgNameTextView = findViewById(R.id.tender_org_name_text_view);
        RecyclerView tasksRecyclerView = findViewById(R.id.tender_task_recycler_view);
        RecyclerView workRecyclerView = findViewById(R.id.tender_work_recycler_view);

        purchaseTextView.setText(tender.getPurchase());
        plannedSumTextView.setText(model.getTenderSum());
        orgNameTextView.setText(tender.getOrgName());
        taskAdapter = new TenderTaskRecyclerViewAdapter(getApplicationContext(), tenderTaskArrayList, tender);
        taskWorkAdapter = new TenderWorkRecyclerViewAdapter(getApplicationContext(), tenderTaskWorkArrayList);
        workRecyclerView.setAdapter(taskWorkAdapter);
        workRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksRecyclerView.setAdapter(taskAdapter);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        MutableLiveData<ArrayList<TenderTask>> tasks = model.getTenderTaskArrayListMutableLiveData();
        tasks.observe(this, new Observer<ArrayList<TenderTask>>() {
            @Override
            public void onChanged(@Nullable ArrayList<TenderTask> tenderTasks) {
                if (tenderTasks != null && !tenderTasks.isEmpty()) {
                    Log.v(TAG, "tasks: " + tenderTasks.size());
                    tenderTaskArrayList.clear();
                    tenderTaskArrayList.addAll(tenderTasks);
                    taskAdapter.notifyDataSetChanged();
                }
            }
        });
        MutableLiveData<ArrayList<TenderTaskWork>> works = model.getTenderTaskWorkArrayList();
        works.observe(this, new Observer<ArrayList<TenderTaskWork>>() {
            @Override
            public void onChanged(@Nullable ArrayList<TenderTaskWork> tenderTaskWorks) {
                if (tenderTaskWorks != null && !tenderTaskWorks.isEmpty()) {
                    Log.v(TAG, "works: " + tenderTaskWorks.size());
                    tenderTaskWorkArrayList.clear();
                    tenderTaskWorkArrayList.addAll(tenderTaskWorks);
                    taskWorkAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (currentUser != null) {
            if (currentUser.getRole().equals(Constants.EDITOR)) {
                getMenuInflater().inflate(R.menu.editor_menu, menu);
            } else if (currentUser.getRole().equals(Constants.USER)) {
                getMenuInflater().inflate(R.menu.user_tender_menu, menu);
                model.getMyTender().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean isMyTender) {
                        if (isMyTender) {
                            MenuItem menuItem = menu.findItem(R.id.accept_tender_menu_item);
                            menuItem.setIcon(R.drawable.ic_bookmark_white_24dp);
                        }
                    }
                });
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_task_menu_item:
                Intent intent = new Intent(TenderActivity.this, AddTaskActivity.class);
                intent.putExtra("tender_num", tender.getTender_num());
                startActivityForResult(intent, ADD_TASK_REQUEST_CODE);
                break;
            case R.id.close_tender_menu_item:
                controller.closeTender();
                model.getTenderClosed().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean isTenderClosed) {
                        if (isTenderClosed) {
                            Toast.makeText(getApplicationContext(), getString(R.string.tender_closed), Toast.LENGTH_SHORT).show();
                            //TODO: add snackbar to cancel this
                            finish();
                        }
                    }
                });

                break;
            case R.id.accept_tender_menu_item:
                controller.acceptTender();
                model.getTenderAccepted().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean isTenderAccepted) {
                        if (isTenderAccepted) {
                            Toast.makeText(getApplicationContext(), getString(R.string.tender_accepted), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {
            switch (requestCode) {
                case ADD_TASK_REQUEST_CODE:
                    controller.addTask(data);
                    model.getTaskAdded().observe(this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(@Nullable Boolean isTaskAdded) {
                            if (isTaskAdded) {
                                /*tenderTaskArrayList.add(model.getAddedTask());
                                taskAdapter.notifyDataSetChanged();*/
                            }
                        }
                    });
                    break;

            }
        }
        if (requestCode == DO_TASK_REQUEST_CODE) {
            controller.updateTenderWork();
            model.getTenderTaskWorkArrayList().observe(this, new Observer<ArrayList<TenderTaskWork>>() {
                @Override
                public void onChanged(@Nullable ArrayList<TenderTaskWork> tenderTasks) {
                    //showTenderWork(tenderTasks);
                }
            });
        }


    }

}
