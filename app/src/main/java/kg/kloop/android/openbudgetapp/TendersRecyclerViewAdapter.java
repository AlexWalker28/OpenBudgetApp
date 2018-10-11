package kg.kloop.android.openbudgetapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TendersRecyclerViewAdapter extends RecyclerView.Adapter<TendersRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Tender> tenderArrayList;
    private Context context;

    TendersRecyclerViewAdapter(Context context, ArrayList<Tender> tenderArrayList) {
        this.tenderArrayList = tenderArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_all_tenders, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Tender tender = tenderArrayList.get(i);
        TextView purchaseTextView = viewHolder.purchaseTextView;
        TextView orgNameTextView = viewHolder.orgNameTextView;
        TextView plannedSumTextView = viewHolder.plannedSumTextView;
        purchaseTextView.setText(tender.getPurchase());
        orgNameTextView.setText(tender.getOrgName());
        String sum = tender.getPlanSum() + " " + tender.getCurrency();
        plannedSumTextView.setText(sum);
    }


    @Override
    public int getItemCount() {
        return tenderArrayList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView purchaseTextView;
        TextView orgNameTextView;
        TextView plannedSumTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            purchaseTextView = itemView.findViewById(R.id.item_purchase_text_view);
            orgNameTextView = itemView.findViewById(R.id.item_org_name_text_view);
            plannedSumTextView = itemView.findViewById(R.id.item_planned_sum_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, TenderActivity.class);
            intent.putExtra("tender", tenderArrayList.get(getAdapterPosition()));
            context.startActivity(intent);
        }
    }

}
