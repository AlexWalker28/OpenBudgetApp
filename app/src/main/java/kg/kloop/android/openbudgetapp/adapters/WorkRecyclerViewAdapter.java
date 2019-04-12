package kg.kloop.android.openbudgetapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.activities.ImageViewActivity;
import kg.kloop.android.openbudgetapp.controllers.WorkActivityController;
import kg.kloop.android.openbudgetapp.fragments.ImageViewFragment;
import kg.kloop.android.openbudgetapp.fragments.PhotoViewFragment;
import kg.kloop.android.openbudgetapp.fragments.RemoveTenderWorkDialogFragment;
import kg.kloop.android.openbudgetapp.objects.TenderTask;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;
import kg.kloop.android.openbudgetapp.objects.User;
import kg.kloop.android.openbudgetapp.utils.Constants;
import kg.kloop.android.openbudgetapp.utils.DateConverter;

public class WorkRecyclerViewAdapter extends RecyclerView.Adapter<WorkRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = WorkRecyclerViewAdapter.class.getSimpleName();
    private ArrayList<TenderTaskWork> workArrayList;
    private Context context;
    private TenderTask task;
    private WorkActivityController controller;
    private User currentUser;
    private FragmentManager fragmentManager;

    public WorkRecyclerViewAdapter(Context context, ArrayList<TenderTaskWork> workArrayList, TenderTask task, WorkActivityController controller, User currentUser, FragmentManager supportFragmentManager) {
        this.context = context;
        this.workArrayList = workArrayList;
        this.task = task;
        this.controller = controller;
        this.currentUser = currentUser;
        this.fragmentManager = supportFragmentManager;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
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
            if (currentUser.getRole().equals(Constants.MODERATOR)) {
                itemView.setOnLongClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            TenderTaskWork work = workArrayList.get(getAdapterPosition());
            // if there is more than one photo, show them in recycler view as list
            if (work.getPhotoUrlList() != null && !work.getPhotoUrlList().isEmpty() && work.getPhotoUrlList().size() > 1) {
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                Bundle bundle = new Bundle();
                ArrayList<String> urls = new ArrayList<>(work.getPhotoUrlList());
                bundle.putStringArrayList("urls", urls);
                ImageViewFragment fragment = ImageViewFragment.newInstance();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .add(R.id.work_activity_constraint_layout, fragment)
                        .addToBackStack("photos")
                        .commit();

            //if there is just one photo, open it in photoview right away
            } else if (work.getPhotoUrlList() != null && !work.getPhotoUrlList().isEmpty() && work.getPhotoUrlList().size() == 1) {
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("photo_url", work.getPhotoUrlList().get(0));
                PhotoViewFragment fragment = PhotoViewFragment.newInstance();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .add(R.id.work_activity_constraint_layout, fragment)
                        .addToBackStack("photo")
                        .commit();
            }
        }

        @Override
        public boolean onLongClick(View view) {
            TenderTaskWork work = workArrayList.get(getAdapterPosition());
            if (currentUser.getRole().equals(Constants.MODERATOR)) {
                RemoveTenderWorkDialogFragment removeTenderWorkDialogFragment = RemoveTenderWorkDialogFragment.newInstance(task, work);
                removeTenderWorkDialogFragment.show(fragmentManager, "fragment_search");
            }
            return true;
        }
    }
}

