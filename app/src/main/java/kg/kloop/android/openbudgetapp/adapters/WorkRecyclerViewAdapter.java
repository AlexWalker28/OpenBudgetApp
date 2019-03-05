package kg.kloop.android.openbudgetapp.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.activities.ImageViewActivity;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;
import kg.kloop.android.openbudgetapp.utils.DateConverter;

public class WorkRecyclerViewAdapter extends RecyclerView.Adapter<WorkRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = WorkRecyclerViewAdapter.class.getSimpleName();
    private ArrayList<TenderTaskWork> workArrayList;
    private Context context;

    public WorkRecyclerViewAdapter(Context context, ArrayList<TenderTaskWork> workArrayList) {
        this.context = context;
        this.workArrayList = workArrayList;
    }

    @NonNull
    @Override
    public WorkRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.work_activity_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WorkRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        TenderTaskWork work = workArrayList.get(i);
        viewHolder.workTextView.setText(work.getText());
        viewHolder.timeTextView.setVisibility(View.GONE);
        if (work.getAuthor() != null) {
            viewHolder.authorTextView.setText(work.getAuthor().getName());
        }
        if(work.getCreateTime() > 0) {
            viewHolder.timeTextView.setVisibility(View.VISIBLE);
            String time = DateConverter.getRelativeDateTimeString(context, work.getCreateTime());
            viewHolder.timeTextView.setText(String.valueOf(time));
        }

        if (work.getPhotoUrlList() != null && !work.getPhotoUrlList().isEmpty()) {
            Glide.with(context)
                    .load(work.getPhotoUrlList().get(0))
                    .into(viewHolder.workImageView);
            int photosCount = work.getPhotoUrlList().size();
            if (photosCount > 1) {
                viewHolder.counterTextView.setVisibility(View.VISIBLE);
                viewHolder.counterTextView.setText("+" + (photosCount - 1));
            }
        } else {
            viewHolder.counterTextView.setVisibility(View.GONE);
            viewHolder.workImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return workArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView workTextView;
        TextView authorTextView;
        TextView counterTextView;
        TextView timeTextView;
        ImageView workImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            workTextView = itemView.findViewById(R.id.work_activity_text_view);
            authorTextView = itemView.findViewById(R.id.work_activity_item_author_text_view);
            workImageView = itemView.findViewById(R.id.work_activity_item_image_view);
            timeTextView = itemView.findViewById(R.id.work_activity_item_time_stamp_text_view);
            counterTextView = itemView.findViewById(R.id.work_activity_item_photos_counter_text_view);
            counterTextView.setVisibility(View.GONE);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            TenderTaskWork work = workArrayList.get(getAdapterPosition());
            if (work.getPhotoUrlList() != null && !work.getPhotoUrlList().isEmpty()) {
                Intent intent = new Intent(context, ImageViewActivity.class);
                ArrayList<String> urls = new ArrayList<>(work.getPhotoUrlList());
                intent.putStringArrayListExtra("urls", urls);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }
}
