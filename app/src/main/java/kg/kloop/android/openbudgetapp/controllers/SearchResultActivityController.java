package kg.kloop.android.openbudgetapp.controllers;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import kg.kloop.android.openbudgetapp.interfaces.DbApi;
import kg.kloop.android.openbudgetapp.models.SearchResultActivityModel;
import kg.kloop.android.openbudgetapp.objects.Tender;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultActivityController {
    private static final String TAG = SearchResultActivityController.class.getSimpleName();
    private DbApi dbApi;
    private SearchResultActivityModel model;
    private static final String BASE_URL = "https://us-central1-openbudgetapp.cloudfunctions.net/";
    private CollectionReference tendersDbColRef;

    public SearchResultActivityController() {
    }

    public SearchResultActivityController(SearchResultActivityModel model) {
        this.model = model;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        dbApi = retrofit.create(DbApi.class);
    }

    public void getTenderFromDb(String searchWords) {
        Call<List<Tender>> getTender = dbApi.loadTender(searchWords.replace(" ", ""));
        getTender.enqueue(new Callback<List<Tender>>() {
            @Override
            public void onResponse(Call<List<Tender>> call, Response<List<Tender>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse() returned: " + response.body().size());
                    model.getTendersFromDb().setValue(response.body());
                } else Log.d(TAG, "onResponse() returned: " + response.message());
            }

            @Override
            public void onFailure(Call<List<Tender>> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void saveTenderToFirestore(final Tender tender) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        tendersDbColRef = db.collection("tenders_db");
        OnCompleteListener<QuerySnapshot> onCompleteListener = new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    if (task.getResult().isEmpty()) { //no such tender in Firestore
                        tendersDbColRef.document(tender.getTender_num()).set(tender); //save it to Firestore
                    }
                } else Log.d(TAG, "onComplete() returned: " + task.getException().getMessage());
            }
        };
        tendersDbColRef.whereEqualTo("tender_num", tender.getTender_num()).get().addOnCompleteListener(onCompleteListener);
    }
}
