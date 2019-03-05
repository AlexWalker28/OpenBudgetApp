package kg.kloop.android.openbudgetapp.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.activities.WorkActivity;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.User;
import kg.kloop.android.openbudgetapp.utils.Constants;
import kg.kloop.android.openbudgetapp.utils.DateConverter;

public class TenderTaskRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<TenderTask> tenderTaskArrayList;
    private User currentUser;
    public static final int BASIC_TYPE = 1;
    public static final int MODERATOR_TYPE = 2;

    public TenderTaskRecyclerViewAdapter(Context context, ArrayList<TenderTask> tenderTaskArrayList, User currentUser) {
        this.context = context;
        this.tenderTaskArrayList = tenderTaskArrayList;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == MODERATOR_TYPE) {
            return new ModeratorViewHolder(LayoutInflater.from(context).inflate(R.layout.tender_task_moderator_item, viewGroup, false));
        } else return new BasicViewHolder(LayoutInflater.from(context).inflate(R.layout.tender_task_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        TenderTask task = tenderTaskArrayList.get(i);
        if (viewHolder instanceof BasicViewHolder) {
            ((BasicViewHolder) viewHolder).timeTextView.setVisibility(View.GONE);
            ((BasicViewHolder)viewHolder).taskTextView.setText(task.getDescription());
            if (task.getCreateTime() > 0) {
                ((BasicViewHolder) viewHolder).timeTextView.setVisibility(View.VISIBLE);
                String time = DateConverter.getRelativeDateTimeString(context, task.getCreateTime());
                ((BasicViewHolder) viewHolder).timeTextView.setText(time);
            }
            if (task.getAuthor() != null) ((BasicViewHolder)viewHolder).taskAuthorTextView.setText(task.getAuthor().getName());

        } else if (viewHolder instanceof ModeratorViewHolder) {
            ((ModeratorViewHolder) viewHolder).taskTextView.setText(task.getDescription());
            ((ModeratorViewHolder) viewHolder).timeTextView.setVisibility(View.GONE);
            if (task.getCreateTime() > 0) {
                ((ModeratorViewHolder) viewHolder).timeTextView.setVisibility(View.VISIBLE);
                String time = DateConverter.getRelativeDateTimeString(context, task.getCreateTime());
                ((ModeratorViewHolder) viewHolder).timeTextView.setText(time);
            }
            if (task.getAuthor() != null) ((ModeratorViewHolder)viewHolder).taskAuthorTextView.setText(task.getAuthor().getName());

        }
    }

    @Override
    public int getItemCount() {
        return tenderTaskArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        TenderTask task = tenderTaskArrayList.get(position);
        if (task.isNeedModeration()) {
            return MODERATOR_TYPE;
        } else return BASIC_TYPE;
    }

    public class BasicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView taskTextView;
        TextView taskAuthorTextView;
        TextView timeTextView;
        public BasicViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTextView = itemView.findViewById(R.id.tender_task_item_text_view);
            taskAuthorTextView = itemView.findViewById(R.id.tender_task_item_author_text_view);
            timeTextView = itemView.findViewById(R.id.tender_task_item_time_stamp_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            openTask(getAdapterPosition());
        }
    }

    public class ModeratorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView taskTextView;
        TextView taskAuthorTextView;
        TextView timeTextView;
        public ModeratorViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTextView = itemView.findViewById(R.id.tender_task_moderator_item_text_view);
            taskAuthorTextView = itemView.findViewById(R.id.tender_task_moderator_item_author_text_view);
            timeTextView = itemView.findViewById(R.id.tender_task_moderator_item_time_stamp_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (!currentUser.getRole().equals(Constants.MODERATOR)) {
                Toast.makeText(context, R.string.task_for_moderator, Toast.LENGTH_SHORT).show();
            } else {
                openTask(getAdapterPosition());
            }
        }
    }

    private void openTask(int adapterPosition) {
        Intent intent = new Intent(context.getApplicationContext(), WorkActivity.class);
        TenderTask task = tenderTaskArrayList.get(adapterPosition);
        intent.putExtra("task", task);
        intent.putExtra("user", currentUser);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
