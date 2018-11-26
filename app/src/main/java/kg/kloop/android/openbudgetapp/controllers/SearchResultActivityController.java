package kg.kloop.android.openbudgetapp.controllers;

import android.util.Log;

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
        Call<List<Tender>> getTender = dbApi.loadTender(searchWords);
        getTender.enqueue(new Callback<List<Tender>>() {
            @Override
            public void onResponse(Call<List<Tender>> call, Response<List<Tender>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse() returned: " + response.body().size());
                    model.getTendersFromDb().setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Tender>> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
