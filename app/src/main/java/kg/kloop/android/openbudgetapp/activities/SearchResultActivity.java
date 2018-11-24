package kg.kloop.android.openbudgetapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.adapters.TendersRecyclerViewAdapter;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.User;

public class SearchResultActivity extends AppCompatActivity {

    private ArrayList<Tender> tenderArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        setSupportActionBar((Toolbar) findViewById(R.id.activity_search_toolbar));
        tenderArrayList = new ArrayList<>();
        Intent intent = getIntent();
        Tender tender = (Tender) intent.getSerializableExtra("tender");
        User user = (User) intent.getSerializableExtra("current_user");


        RecyclerView searchResultRecyclerView = findViewById(R.id.search_result_activity_recycler_view);
        TendersRecyclerViewAdapter simpleAdapter = new TendersRecyclerViewAdapter(getApplicationContext(), tenderArrayList, user);
        searchResultRecyclerView.setAdapter(simpleAdapter);
        searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tenderArrayList.add(0, tender);
        simpleAdapter.notifyDataSetChanged();

    }
}
