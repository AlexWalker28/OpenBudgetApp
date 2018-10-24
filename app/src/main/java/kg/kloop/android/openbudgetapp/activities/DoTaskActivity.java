package kg.kloop.android.openbudgetapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;

public class DoTaskActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 200;
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 300;
    private static final String TAG = DoTaskActivity.class.getSimpleName();
    private FirebaseFirestore db;
    private ArrayList<TenderTask> tasks;
    private DocumentReference taskDocRef;
    private EditText doTaskEditText;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String photoUrl;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_task);

        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images");
        Intent intent = getIntent();
        String tenderId = intent.getStringExtra("tender_id");
        String taskId = intent.getStringExtra("task_id");
        taskDocRef = db.document("tenders/" + tenderId + "/tasks/" + taskId);

        final TextView tasksTextView = findViewById(R.id.do_task_task_text_view);
        progressBar = findViewById(R.id.do_task_progress_bar);
        progressBar.setVisibility(View.GONE);
        doTaskEditText = findViewById(R.id.do_task_edit_text);
        ImageButton attachFileImageButton = findViewById(R.id.add_attachment_image_button);
        tasks = new ArrayList<>();

        taskDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    tasksTextView.setText(documentSnapshot.toObject(TenderTask.class).getDescription());
                }
            }
        });
        attachFileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasPermission()) {
                    pickImage();
                } else {
                    getPermissionToReadFiles();
                }
            }
        });
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE);
    }

    private boolean hasPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else return true;
    }

    private void getPermissionToReadFiles() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            } else {
                // showRationale = false if user clicks Never Ask Again, otherwise true
                boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);

                if (showRationale) {
                    // do something here to handle degraded mode
                } else {
                    Toast.makeText(this, "Read storage permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                if (cursor == null || cursor.getCount() < 1) {
                    Log.e(TAG, "No data for cursor");
                    return;
                }
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                if (columnIndex < 0) { // no column index
                    Log.e(TAG, "No column index");
                    return;
                }
                try {
                    InputStream stream = new FileInputStream(cursor.getString(columnIndex));
                    //File file = new File(cursor.getString(columnIndex));
                    cursor.close();
                    uploadToFireStorage(stream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadToFireStorage(InputStream stream) {
        progressBar.setVisibility(View.VISIBLE);
        UploadTask uploadTask = storageReference.child(UUID.randomUUID().toString()).putStream(stream);
        uploadTask.addOnSuccessListener(DoTaskActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), getString(R.string.upload_finished), Toast.LENGTH_SHORT).show();
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        photoUrl = uri.toString();
                        Toast.makeText(getApplicationContext(), "photoUrl: " + photoUrl, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.do_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.do_task_send_menu_item:
                CollectionReference taskWorkCollectionRef = taskDocRef.collection("work");
                TenderTaskWork tenderTaskWork = new TenderTaskWork();
                tenderTaskWork.setId(taskWorkCollectionRef.document().getId());
                tenderTaskWork.setText(doTaskEditText.getText().toString());
                tenderTaskWork.setPhotoUrl(photoUrl);
                taskWorkCollectionRef.document(tenderTaskWork.getId()).set(tenderTaskWork);
                setResult(RESULT_OK);
                finish();
                break;
        }
        return true;
    }
}
