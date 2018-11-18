package kg.kloop.android.openbudgetapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
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
    private ImageView imageView;
    private InputStream inputStream;
    private ProgressBar horizontalProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_task);

        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images");
        Intent intent = getIntent();
        String tenderNum = intent.getStringExtra("tender_num");
        String taskId = intent.getStringExtra("task_id");
        taskDocRef = db.document("tenders_db/" + tenderNum + "/tasks/" + taskId);

        final TextView tasksTextView = findViewById(R.id.do_task_task_text_view);
        progressBar = findViewById(R.id.do_task_progress_bar);
        progressBar.setVisibility(View.GONE);
        doTaskEditText = findViewById(R.id.do_task_edit_text);
        ImageButton attachFileImageButton = findViewById(R.id.add_attachment_image_button);
        imageView = findViewById(R.id.do_task_image_view);
        horizontalProgressBar = findViewById(R.id.do_task_horizontal_progress_bar);
        horizontalProgressBar.setIndeterminate(true);
        horizontalProgressBar.setVisibility(View.GONE);
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
        /*intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);*/
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
        String imageEncoded;
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                /*try {
                    // Get the Image from data

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    ArrayList<String> imagesEncodedList = new ArrayList<>();
                    //single photo
                    if (data.getData() != null) {
                        // Get the cursor
                        Cursor cursor = getContentResolver().query(data.getData(),
                                filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageEncoded = cursor.getString(columnIndex);
                        cursor.close();
                        Log.v(TAG, "image: " + imageEncoded);

                        //multiple photos
                    } else if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<>();
                        Log.v(TAG, "count: " + mClipData.getItemCount());
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            Log.v(TAG, "uri: " + uri);
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            Log.v(TAG, "cursor: " + cursor.getCount());
                            imageEncoded = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            Log.v(TAG, "image path " + imageEncoded);

                            InputStream stream = new FileInputStream(imageEncoded);
                            cursor.close();
                            saveWithFiles(stream);

                        }
                        Log.v(TAG, "Selected Images" + mArrayUri.size());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                            .show();
                }*/
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
                        inputStream = new FileInputStream(cursor.getString(columnIndex));
                        File file = new File(cursor.getString(columnIndex));
                        cursor.close();
                        showImage(imageView, file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showImage(ImageView imageView, File file) {
        Glide.with(getApplicationContext())
                .load(file)
                .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        })
                .into(imageView);
    }

    private void saveWithFiles(InputStream stream, final MenuItem item) {
        horizontalProgressBar.setVisibility(View.VISIBLE);
        UploadTask uploadTask = storageReference.child(UUID.randomUUID().toString()).putStream(stream);
        uploadTask.addOnSuccessListener(DoTaskActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.v(TAG, "photo url: " + uri);
                        photoUrl = uri.toString();
                        saveTender(photoUrl, item);
                    }
                });
            }
        });/*.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.v(TAG, "total: " + taskSnapshot.getTotalByteCount());
                Log.v(TAG, "transferred: " + taskSnapshot.getBytesTransferred());
                Log.v(TAG, "progress: " + progress);
                progressBar.setProgress((int)progress);
                if (progressBar.getProgress() > 99) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });*/
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
                if (doTaskEditText.getText().toString().length() > 0) {
                    if (inputStream != null) {
                        item.setEnabled(false);
                        saveWithFiles(inputStream, item);
                    } else {
                        item.setEnabled(false);
                        saveTender("", item);
                    }

                }
                break;
        }
        return true;
    }

    private void saveTender(String photoUrl, MenuItem item) {
        horizontalProgressBar.setVisibility(View.VISIBLE);
        item.setEnabled(false);
        CollectionReference taskWorkCollectionRef = taskDocRef.collection("work");
        TenderTaskWork tenderTaskWork = new TenderTaskWork();
        tenderTaskWork.setId(taskWorkCollectionRef.document().getId());
        tenderTaskWork.setText(doTaskEditText.getText().toString());
        tenderTaskWork.setPhotoUrl(photoUrl);
        taskWorkCollectionRef.document(tenderTaskWork.getId()).set(tenderTaskWork);
        setResult(RESULT_OK);
        horizontalProgressBar.setVisibility(View.GONE);
        item.setEnabled(true);
        finish();
    }
}
