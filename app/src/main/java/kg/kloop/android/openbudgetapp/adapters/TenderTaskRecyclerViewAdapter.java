package kg.kloop.android.openbudgetapp.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.activities.WorkActivity;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.User;

public class TenderTaskRecyclerViewAdapter extends RecyclerView.Adapter<TenderTaskRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TenderTask> tenderTaskArrayList;
    private Tender tender;
    private User currentUser;

    public TenderTaskRecyclerViewAdapter(Context context, ArrayList<TenderTask> tenderTaskArrayList, Tender tender, User currentUser) {
        this.context = context;
        this.tenderTaskArrayList = tenderTaskArrayList;
        this.tender = tender;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public TenderTaskRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.tender_task_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TenderTaskRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        TenderTask task = tenderTaskArrayList.get(i);
        viewHolder.taskTextView.setText(task.getDescription());
        if (task.getAuthor() != null) {
            viewHolder.taskAuthorTextView.setText(task.getAuthor().getName());
        }
    }

    @Override
    public int getItemCount() {
        return tenderTaskArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView taskTextView;
        TextView taskAuthorTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTextView = itemView.findViewById(R.id.tender_task_item_text_view);
            taskAuthorTextView = itemView.findViewById(R.id.tender_task_item_author_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context.getApplicationContext(), WorkActivity.class);
            TenderTask task = tenderTaskArrayList.get(getAdapterPosition());
            intent.putExtra("task_lat", task.getLatitude());
            intent.putExtra("task_lng", task.getLongitude());
            intent.putExtra("task_description", task.getDescription());
            intent.putExtra("task_id", task.getId());
            intent.putExtra("tender_num", tender.getTender_num());
            intent.putExtra("user", currentUser);
            context.startActivity(intent);
        }
    }
}
