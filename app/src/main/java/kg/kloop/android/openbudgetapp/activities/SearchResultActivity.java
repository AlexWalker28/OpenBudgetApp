package kg.kloop.android.openbudgetapp.activities;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.adapters.SearchResultActivityRecyclerViewAdapter;
import kg.kloop.android.openbudgetapp.controllers.SearchResultActivityController;
import kg.kloop.android.openbudgetapp.models.SearchResultActivityModel;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.User;

public class SearchResultActivity extends AppCompatActivity implements LifecycleOwner {

    private static final String TAG = SearchResultActivity.class.getSimpleName();
    private ArrayList<Tender> tenderArrayList;
    private ProgressBar searchProgressBar;
    private SearchResultActivityRecyclerViewAdapter simpleAdapter;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        final SearchResultActivityModel model = new SearchResultActivityModel();
        SearchResultActivityController controller = new SearchResultActivityController(model);
        setSupportActionBar((Toolbar) findViewById(R.id.activity_search_toolbar));
        searchProgressBar = findViewById(R.id.search_result_activity_progress_bar);
        textView = findViewById(R.id.search_result_activity_text_view);
        textView.setVisibility(View.GONE);
        Intent intent = getIntent();
        String searchWords = intent.getStringExtra("search_words");
        User user = (User) intent.getSerializableExtra("current_user");
        tenderArrayList = new ArrayList<>();
        RecyclerView searchResultRecyclerView = findViewById(R.id.search_result_activity_recycler_view);
        simpleAdapter = new SearchResultActivityRecyclerViewAdapter(getApplicationContext(), tenderArrayList, user, controller);
        searchResultRecyclerView.setAdapter(simpleAdapter);
        searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        model.setSearchWords(searchWords);
        controller.getTenderFromDb(model.getSearchWords());
        model.getTendersFromDb().observe(this, new Observer<List<Tender>>() {
            @Override
            public void onChanged(@Nullable List<Tender> tenders) {
                if (tenders != null) {
                    if (tenders.size() == 0) {
                        searchProgressBar.setVisibility(View.GONE);
                        textView.setText(getString(R.string.nothing_found));
                    } else {
                        tenderArrayList.clear();
                        tenderArrayList.addAll(tenders);
                        simpleAdapter.notifyDataSetChanged();
                        searchProgressBar.setVisibility(View.GONE);
                        model.setSearchWords(null);
                    }
                } else Log.i(TAG, "onChanged: tenders are null");
            }
        });
    }
}
