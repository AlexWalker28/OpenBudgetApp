package kg.kloop.android.openbudgetapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.objects.TenderTask;

public class TenderTaskRecyclerViewAdapter extends RecyclerView.Adapter<TenderTaskRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TenderTask> tenderTaskArrayList;

    public TenderTaskRecyclerViewAdapter(Context context, ArrayList<TenderTask> tenderTaskArrayList) {
        this.context = context;
        this.tenderTaskArrayList = tenderTaskArrayList;
    }

    @NonNull
    @Override
    public TenderTaskRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.tender_task_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TenderTaskRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        viewHolder.taskTextView.setText(tenderTaskArrayList.get(i).getDescription());
    }

    @Override
    public int getItemCount() {
        return tenderTaskArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTextView = itemView.findViewById(R.id.tender_task_item_text_view);
        }
    }
}
