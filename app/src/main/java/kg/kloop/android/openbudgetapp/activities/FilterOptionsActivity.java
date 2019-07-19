package kg.kloop.android.openbudgetapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.database.TendersDatabaseDao;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.utils.App;

public class FilterOptionsActivity extends AppCompatActivity {

    private static final String TAG  = FilterOptionsActivity.class.getSimpleName();
    private TendersDatabaseDao databaseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_options);

        databaseDao = App.getInstance().getDatabase().tendersDatabaseDao();
        setSupportActionBar((Toolbar) findViewById(R.id.filter_options_toolbar));
        getSupportActionBar().setTitle(getString(R.string.search_tenders_with_tasks));

        final EditText searchEditText = findViewById(R.id.filter_activity_ecit_text);
        Button okButton = findViewById(R.id.filter_activity_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = searchEditText.getText().toString();
                if (word.length() < 1) {
                    Toast.makeText(FilterOptionsActivity.this, getString(R.string.enter_words), Toast.LENGTH_SHORT).show();
                } else {
                    new GetTenderAsyncTask().execute(word + "," + word);
                }
            }
        });

    }



    private class GetTenderAsyncTask extends AsyncTask<String, Void, List<Tender>> {

        @Override
        protected List<Tender> doInBackground(String... strings) {
            Log.v(TAG, strings[0]);
            return databaseDao.getTenders(strings[0].split(",")[0], strings[0].split(",")[1]);
        }

        @Override
        protected void onPostExecute(List<Tender> tenders) {
            Intent data = new Intent();
            data.putParcelableArrayListExtra("tenders", new ArrayList<Parcelable>(tenders));
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
