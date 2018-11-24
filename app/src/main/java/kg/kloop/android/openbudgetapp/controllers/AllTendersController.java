package kg.kloop.android.openbudgetapp.controllers;

import android.util.Log;

import kg.kloop.android.openbudgetapp.interfaces.DbApi;
import kg.kloop.android.openbudgetapp.models.AllTendersModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllTendersController {

    private static final String BASE_URL = "https://us-central1-openbudgetapp.cloudfunctions.net/";
    private static final String TAG = AllTendersController.class.getSimpleName();
    private DbApi dbApi;
    private AllTendersModel model;

    public AllTendersController(AllTendersModel model) {
        this.model = model;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        dbApi = retrofit.create(DbApi.class);
    }


    public void getTenderFromDb(String tenderNumber) {
        Call<String> getTender = dbApi.loadTender(tenderNumber);
        getTender.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().length() > 0) {
                    model.getIsTenderLoaded().setValue(true);
                }
                Log.d(TAG, "onResponse() returned: " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
