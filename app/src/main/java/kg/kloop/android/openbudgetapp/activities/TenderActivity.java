package kg.kloop.android.openbudgetapp.activities;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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

    private static final String TAG = TenderActivity.class.getSimpleName();
    private TenderActivityController controller;
    private TenderActivityModel model;
    private User currentUser;
    private Tender tender;
    private ArrayList<TenderTaskWork> tenderTaskWorkArrayList;
    private TenderWorkRecyclerViewAdapter taskWorkAdapter;
    private TenderTaskRecyclerViewAdapter taskAdapter;
    private ArrayList<TenderTask> tenderTaskArrayList;
    private boolean mIsTenderClosed;

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
        Toolbar toolbar = findViewById(R.id.tender_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tender.getNumber());

        TextView purchaseTextView = findViewById(R.id.tender_purchase_text_view);
        TextView plannedSumTextView = findViewById(R.id.tender_planned_sum_text_view);
        TextView orgNameTextView = findViewById(R.id.tender_org_name_text_view);
        TextView addressTextView = findViewById(R.id.tender_address_text_view);
        RecyclerView tasksRecyclerView = findViewById(R.id.tender_task_recycler_view);
        RecyclerView workRecyclerView = findViewById(R.id.tender_work_recycler_view);

        purchaseTextView.setText(tender.getProcurement_object());
        plannedSumTextView.setText(model.getPlannedSum());
        orgNameTextView.setText(tender.getProcuring_entity());
        addressTextView.setText(tender.getRegion());
        taskAdapter = new TenderTaskRecyclerViewAdapter(getApplicationContext(), tenderTaskArrayList, currentUser);
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
                    tenderTaskWorkArrayList.addAll(tenderTaskWorks);
                    taskWorkAdapter.notifyDataSetChanged();
                }
            }
        });
        model.getTenderClosed().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isTenderClosed) {
                try {
                    mIsTenderClosed = isTenderClosed;
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (currentUser != null) {
            if (currentUser.getRole().equals(Constants.EDITOR) || currentUser.getRole().equals(Constants.MODERATOR)) {
                getMenuInflater().inflate(R.menu.editor_menu, menu);
                final MenuItem item = menu.findItem(R.id.close_tender_menu_item);
                if (mIsTenderClosed) {
                    item.setTitle(getString(R.string.open_tender));
                } else {
                    item.setTitle(getString(R.string.close_tender));
                }

            } else if (currentUser.getRole().equals(Constants.USER)) {
                getMenuInflater().inflate(R.menu.user_tender_menu, menu);
                model.getMyTender().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean isMyTender) {
                        if (isMyTender) {
                            MenuItem menuItem = menu.findItem(R.id.accept_tender_menu_item);
                            menuItem.setIcon(R.drawable.ic_bookmark_black_24dp);
                        }
                    }
                });
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_task_menu_item:
                Intent intent = new Intent(TenderActivity.this, AddTaskActivity.class);
                intent.putExtra("current_user", currentUser);
                intent.putExtra("number", tender.getNumber());
                startActivity(intent);
                break;
            case R.id.close_tender_menu_item:
                if (mIsTenderClosed) {
                    controller.openTender();
                    item.setTitle(getString(R.string.close_tender));
                    Toast.makeText(getApplicationContext(), getString(R.string.tender_opened), Toast.LENGTH_SHORT).show();
                    //TODO: add snackbar to cancel this
                } else {
                    controller.closeTender();
                    item.setTitle(getString(R.string.open_tender));
                    Toast.makeText(getApplicationContext(), getString(R.string.tender_closed), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.accept_tender_menu_item:
                controller.acceptTender();
                model.getTenderAccepted().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean isTenderAccepted) {
                        if (isTenderAccepted) {
                            item.setIcon(R.drawable.ic_bookmark_black_24dp);
                            Toast.makeText(getApplicationContext(), getString(R.string.tender_added_to_favorites), Toast.LENGTH_SHORT).show();
                        } else {
                            item.setIcon(R.drawable.ic_bookmark_border_black_24dp);
                            Toast.makeText(getApplicationContext(), getString(R.string.tender_removed_from_favorites), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.open_tender_page_menu_item:
                Intent openTenderIntent = new Intent(Intent.ACTION_VIEW);
                openTenderIntent.setData(Uri.parse(tender.getUrl()));
                startActivity(openTenderIntent);
                break;
        }
        return true;
    }

}
