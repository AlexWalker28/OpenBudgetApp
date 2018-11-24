package kg.kloop.android.openbudgetapp.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DbApi {
    @GET("tenders_get/")
    Call<String> loadTender(@Query("words") String tenderNumber);
}
