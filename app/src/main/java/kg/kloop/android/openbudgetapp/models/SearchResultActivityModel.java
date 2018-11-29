package kg.kloop.android.openbudgetapp.models;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import kg.kloop.android.openbudgetapp.objects.Tender;

public class SearchResultActivityModel {
    private String searchWords;
    private MutableLiveData<List<Tender>> tendersFromDb;

    public SearchResultActivityModel() {
        tendersFromDb = new MutableLiveData<>();
    }

    public void setSearchWords(String searchWords) {
        this.searchWords = searchWords;
    }

    public String getSearchWords() {
        return searchWords;
    }

    public MutableLiveData<List<Tender>> getTendersFromDb() {
        return tendersFromDb;
    }

    public void setTendersFromDb(MutableLiveData<List<Tender>> tendersFromDb) {
        this.tendersFromDb = tendersFromDb;
    }
}
