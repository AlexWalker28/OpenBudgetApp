package kg.kloop.android.openbudgetapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TenderActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tender);

        Intent intent = getIntent();
        Tender tender = (Tender) intent.getSerializableExtra("tender");
        TextView purchaseTextView = findViewById(R.id.tender_purchase_text_view);
        TextView orgNameTextView = findViewById(R.id.tender_org_name_text_view);
        purchaseTextView.setText(tender.getPurchase());
        orgNameTextView.setText(tender.getOrgName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_task_menu_item:
                startActivityForResult(new Intent(TenderActivity.this, AddTaskActivity.class), REQUEST_CODE);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            addTask((Task) data.getSerializableExtra("task"));
        }

    }

    private void addTask(Task task) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tender_task, null);
        TextView textView = view.findViewById(R.id.tender_task_text_view);
        textView.setText(task.getDescription());
        LinearLayout linearLayout = findViewById(R.id.tender_task_linear_layout);
        linearLayout.addView(view);
    }
}
