package kg.kloop.android.openbudgetapp.interfaces;

import org.json.JSONArray;

import java.util.List;

import kg.kloop.android.openbudgetapp.objects.Tender;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DbApi {
    @GET("tenders_get/")
    Call<List<Tender>> loadTender(@Query("words") String tenderNumber);
}
