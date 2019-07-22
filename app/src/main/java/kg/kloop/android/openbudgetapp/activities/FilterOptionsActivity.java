package kg.kloop.android.openbudgetapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
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
    private String procuringEntity;
    private String procurementObject;
    private String region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_options);

        databaseDao = App.getInstance().getDatabase().tendersDatabaseDao();
        setSupportActionBar((Toolbar) findViewById(R.id.filter_options_toolbar));
        getSupportActionBar().setTitle(getString(R.string.search_tenders_with_tasks));

        final EditText procurementObjectEditText = findViewById(R.id.filter_activity_procurement_object_edit_text);
        final EditText procuringEntityEditText = findViewById(R.id.filter_activity_procuring_entity_edit_text);
        final EditText regionEditText = findViewById(R.id.filter_activity_region_edit_text);
        Button okButton = findViewById(R.id.filter_activity_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                procurementObject = procurementObjectEditText.getText().toString();
                procuringEntity = procuringEntityEditText.getText().toString();
                region = regionEditText.getText().toString();
                if (procuringEntity.length() < 1 && procurementObject.length() < 1 && region.length() < 1) {
                    Toast.makeText(FilterOptionsActivity.this, getString(R.string.enter_words), Toast.LENGTH_SHORT).show();
                } else {
                    new GetTenderAsyncTask().execute(procurementObject, procuringEntity, region);
                }
            }
        });

    }



    private class GetTenderAsyncTask extends AsyncTask<String, Void, List<Tender>> {

        @Override
        protected List<Tender> doInBackground(String... strings) {
            if (strings[0] != null && strings[1] != null && strings[2] != null) {
                return databaseDao.getTenders(strings[0], strings[1], strings[2]);
            } else if (strings[0] != null && strings[1] != null) {
                return databaseDao.getTendersObjEnt(strings[0], strings[1]);
            } else if (strings[0] != null && strings[2] != null) {
                return databaseDao.getTendersObjReg(strings[0], strings[2]);
            } else if (strings[1] != null && strings[2] != null) {
                return databaseDao.getTendersEntReg(strings[1], strings[2]);
            } else if (strings[1] != null) {
                return databaseDao.getTendersEnt(strings[1]);
            } else if (strings[2] != null) {
                return databaseDao.getTendersReg(strings[2]);
            } else {
                return databaseDao.getTendersObj(strings[0]);
            }
        }

        @Override
        protected void onPostExecute(List<Tender> tenders) {
            Intent data = new Intent();
            data.putParcelableArrayListExtra("tenders", new ArrayList<Parcelable>(tenders));
            data.putExtra("search_word", buildString(procurementObject, procuringEntity, region));
            setResult(RESULT_OK, data);
            finish();
        }
    }

    private String buildString(String procurementObject, String procuringEntity, String region) {
        ArrayList<String> words = new ArrayList<>();
        if (procurementObject.length() > 0) words.add(procurementObject);
        if (procuringEntity.length() > 0) words.add(procuringEntity);
        if (region.length() > 0) words.add(region);
        String result = "";
        for (int i = 0; i < words.size(); i++) {
            if (i < words.size() - 1 && words.get(i).length() > 0) {
                result = result.concat(words.get(i) + ", ");
            } else {
                result = result.concat(words.get(i));
            }
        }
        return result;
    }

}
