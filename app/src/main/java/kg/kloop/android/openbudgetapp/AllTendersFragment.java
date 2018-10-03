package kg.kloop.android.openbudgetapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AllTendersFragment extends Fragment {


    public AllTendersFragment() {
    }

    public static Fragment newInstance() {
        return new AllTendersFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_all_tenders, container, false);
        RecyclerView allTendersRecyclerView = view.findViewById(R.id.all_tenders_recycler_view);
        ArrayList<Tender> tenderArrayList = new ArrayList<>();
        Tender tender = new Tender();
        tender.setPurchase("Purchase");
        tender.setOrgName("Org name");
        tenderArrayList.add(tender);
        allTendersRecyclerView.setHasFixedSize(true);
        allTendersRecyclerView.setAdapter(new TendersRecyclerViewAdapter(getContext(), tenderArrayList));
        allTendersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

}
