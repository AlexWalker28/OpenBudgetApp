package kg.kloop.android.openbudgetapp.activities;

import android.Manifest;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
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
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;
import kg.kloop.android.openbudgetapp.objects.User;
import kg.kloop.android.openbudgetapp.utils.Glide4Engine;

public class DoTaskActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 200;
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 300;
    private static final String TAG = DoTaskActivity.class.getSimpleName();
    private FirebaseFirestore db;
    private DocumentReference taskDocRef;
    private EditText doTaskEditText;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ProgressBar progressBar;
    private ImageView imageView;
    private ArrayList<InputStream> inputStreamArrayList;
    private ProgressBar horizontalProgressBar;
    private User currentUser;
    private TextView imageCounterTextView;
    private List<Uri> selectedPhotosUris;
    private MutableLiveData<ArrayList<String>> photoUrls;
    private MutableLiveData<Boolean> isUploadFinished;
    private String tenderNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_task);

        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images");
        final Intent intent = getIntent();
        tenderNum = intent.getStringExtra("number");
        String taskId = intent.getStringExtra("task_id");
        currentUser = (User) intent.getSerializableExtra("user");
        taskDocRef = db.document("/tasks/" + taskId);
        inputStreamArrayList = new ArrayList<>();
        photoUrls = new MutableLiveData<>();
        isUploadFinished = new MutableLiveData<>();
        isUploadFinished.setValue(false);
        photoUrls.setValue(new ArrayList<String>());

        setSupportActionBar((Toolbar) findViewById(R.id.do_task_toolbar));
        final TextView tasksTextView = findViewById(R.id.do_task_task_text_view);
        progressBar = findViewById(R.id.do_task_progress_bar);
        progressBar.setVisibility(View.GONE);
        imageCounterTextView = findViewById(R.id.do_task_activity_image_counter_text_view);
        imageCounterTextView.setVisibility(View.GONE);
        doTaskEditText = findViewById(R.id.do_task_edit_text);
        ImageButton attachFileImageButton = findViewById(R.id.add_attachment_image_button);
        imageView = findViewById(R.id.do_task_image_view);
        horizontalProgressBar = findViewById(R.id.do_task_horizontal_progress_bar);
        horizontalProgressBar.setIndeterminate(true);
        horizontalProgressBar.setVisibility(View.GONE);

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

        isUploadFinished.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean loaded) {
                if (loaded) {
                        saveTender(photoUrls.getValue());
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: click");
                Intent showPhotosIntent = new Intent(DoTaskActivity.this, ImageViewActivity.class);
                ArrayList<Uri> selectedPhotos = new ArrayList<>(selectedPhotosUris);
                showPhotosIntent.putParcelableArrayListExtra("uris", selectedPhotos);
                startActivity(showPhotosIntent);

            }
        });

    }

    private void pickImage() {
        Matisse.from(this)
                .choose(MimeType.ofAll())
                .countable(true)
                .maxSelectable(9)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .forResult(PICK_IMAGE);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE_PERMISSION_REQUEST);
            }
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
                boolean showRationale = false;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                }

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
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                selectedPhotosUris = Matisse.obtainResult(data);
                Log.d("Matisse", "selectedPhotosUris: " + selectedPhotosUris);
                showImage(imageView, selectedPhotosUris.get(0));
                for (Uri uri : selectedPhotosUris) {
                    try {

                        inputStreamArrayList.add(new FileInputStream(getRealPath(uri)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                if (selectedPhotosUris.size() > 1) {
                    imageCounterTextView.setVisibility(View.VISIBLE);
                    imageCounterTextView.setText("+" + (selectedPhotosUris.size() - 1));
                } else {
                    imageCounterTextView.setVisibility(View.GONE);
                }

            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getRealPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            Log.d(TAG, "getRealPath() returned: " + cursor.getString(column_index));
            return cursor.getString(column_index);
        }
    }

    private void showImage(ImageView imageView, Uri uri) {
        Glide.with(getApplicationContext())
                .load(uri)
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

    private void saveWithFiles(ArrayList<InputStream> streams) {
        horizontalProgressBar.setVisibility(View.VISIBLE);
        for (InputStream inputStream : streams) {
            UploadTask uploadTask = storageReference.child(UUID.randomUUID().toString()).putStream(inputStream);
            uploadTask.addOnSuccessListener(DoTaskActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.v(TAG, "photo url: " + uri);
                            photoUrls.getValue().add(uri.toString());
                            Log.i(TAG, "photoUrls size: " + photoUrls.getValue().size());
                            if (photoUrls.getValue().size() == selectedPhotosUris.size()) {
                                isUploadFinished.setValue(true);
                            }
                            //saveTender(photoUrl, item);
                        }
                    });
                }
            });
        }
        /*.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
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
                    if (inputStreamArrayList != null && !inputStreamArrayList.isEmpty()) {
                        //item.setEnabled(false);
                        saveWithFiles(inputStreamArrayList);
                    } else {
                        //item.setEnabled(false);
                        saveTender(null);
                    }

                }
                break;
        }
        return true;
    }

    private void saveTender(ArrayList<String> photoUrls) {
        horizontalProgressBar.setVisibility(View.VISIBLE);
        //item.setEnabled(false);
        CollectionReference taskWorkCollectionRef = taskDocRef.collection("work");
        CollectionReference tendersColRef = db.collection("tenders");
        tendersColRef.document(tenderNum).update("hasWork", true);
        TenderTaskWork tenderTaskWork = new TenderTaskWork();
        tenderTaskWork.setId(taskWorkCollectionRef.document().getId());
        tenderTaskWork.setText(doTaskEditText.getText().toString());
        tenderTaskWork.setPhotoUrlList(photoUrls);
        tenderTaskWork.setAuthor(currentUser);
        tenderTaskWork.setCreateTime(System.currentTimeMillis());
        taskWorkCollectionRef.document(tenderTaskWork.getId()).set(tenderTaskWork);
        setResult(RESULT_OK);
        horizontalProgressBar.setVisibility(View.GONE);
        //item.setEnabled(true);
        finish();
    }
}
