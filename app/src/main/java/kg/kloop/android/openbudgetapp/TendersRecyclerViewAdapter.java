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
        TextView textView = viewHolder.textView;
        textView.setText(tenderArrayList.get(i).getText());
    }


    @Override
    public int getItemCount() {
        return tenderArrayList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_all_tenders_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, TenderActivity.class);
            intent.putExtra("tender_data", tenderArrayList.get(getAdapterPosition()).getText());
            context.startActivity(intent);
        }
    }

}
