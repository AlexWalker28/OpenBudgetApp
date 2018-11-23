package kg.kloop.android.openbudgetapp.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DbApi {
    @GET(".")
    Call<String> loadTender(@Query("tender_num") String tenderNumber);
}
