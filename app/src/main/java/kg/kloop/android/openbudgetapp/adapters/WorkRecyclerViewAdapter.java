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

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.activities.ImageViewActivity;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;

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
        if (work.getAuthor() != null) {
            viewHolder.authorTextView.setText(work.getAuthor().getName());
        }
        if (!work.getPhotoUrlList().isEmpty()) {
            Glide.with(context)
                    .load(work.getPhotoUrlList().get(0))
                    .into(viewHolder.workImageView);
            int photosCount = work.getPhotoUrlList().size();
            if (photosCount > 1) {
                viewHolder.counterTextView.setVisibility(View.VISIBLE);
                viewHolder.counterTextView.setText("+" + (photosCount - 1));
            }
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
        ImageView workImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            workTextView = itemView.findViewById(R.id.work_activity_text_view);
            authorTextView = itemView.findViewById(R.id.work_activity_item_author_text_view);
            workImageView = itemView.findViewById(R.id.work_activity_item_image_view);
            counterTextView = itemView.findViewById(R.id.work_activity_item_photos_counter_text_view);
            counterTextView.setVisibility(View.GONE);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, ImageViewActivity.class);
            ArrayList<String> urls = new ArrayList<>(workArrayList.get(getAdapterPosition()).getPhotoUrlList());
            intent.putStringArrayListExtra("urls", urls);
            context.startActivity(intent);
        }
    }
}
