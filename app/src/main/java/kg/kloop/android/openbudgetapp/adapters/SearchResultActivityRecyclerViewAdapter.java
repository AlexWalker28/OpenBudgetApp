package kg.kloop.android.openbudgetapp.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.activities.TenderActivity;
import kg.kloop.android.openbudgetapp.controllers.SearchResultActivityController;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.User;

public class SearchResultActivityRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultActivityRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = SearchResultActivityRecyclerViewAdapter.class.getSimpleName();
    private ArrayList<Tender> tenderArrayList;
    private Context context;
    private User currentUser;
    private SearchResultActivityController controller;

    public SearchResultActivityRecyclerViewAdapter(Context context, ArrayList<Tender> tenderArrayList, User currentUser, SearchResultActivityController controller) {
        this.tenderArrayList = tenderArrayList;
        this.context = context;
        this.currentUser = currentUser;
        this.controller = controller;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_all_tenders, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Tender tender = tenderArrayList.get(i);
        viewHolder.purchaseTextView.setText(tender.getProcurement_object());
        viewHolder.orgNameTextView.setText(tender.getProcuring_entity());
        viewHolder.addressTextView.setText(tender.getRegion());
        String sum = tender.getPlanned_sum_int() + " " + tender.getCurrency();
        viewHolder.plannedSumTextView.setText(sum);
    }


    @Override
    public int getItemCount() {
        return tenderArrayList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView purchaseTextView;
        TextView orgNameTextView;
        TextView plannedSumTextView;
        TextView addressTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            purchaseTextView = itemView.findViewById(R.id.item_purchase_text_view);
            orgNameTextView = itemView.findViewById(R.id.item_org_name_text_view);
            plannedSumTextView = itemView.findViewById(R.id.item_planned_sum_text_view);
            addressTextView = itemView.findViewById(R.id.item_address_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Tender tender = tenderArrayList.get(getAdapterPosition());
            controller.saveTenderToFirestore(tender);
            Intent intent = new Intent(context, TenderActivity.class);
            intent.putExtra("tender", (Serializable) tender);
            if (currentUser != null) {
                intent.putExtra("current_user", currentUser);
                Log.v(TAG, "current_user: " + currentUser.getName());
            }
            context.startActivity(intent);
        }
    }

}
