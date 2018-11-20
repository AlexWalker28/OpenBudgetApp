package kg.kloop.android.openbudgetapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.TenderTaskWork;

public class TenderWorkRecyclerViewAdapter extends RecyclerView.Adapter<TenderWorkRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = TenderWorkRecyclerViewAdapter.class.getSimpleName();
    private ArrayList<TenderTaskWork> tenderTaskWorkArrayList;
    private Context context;

    public TenderWorkRecyclerViewAdapter(Context context, ArrayList<TenderTaskWork> tenderTaskWorkArrayList) {
        this.tenderTaskWorkArrayList = tenderTaskWorkArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.tender_work_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TenderTaskWork work = tenderTaskWorkArrayList.get(i);
        viewHolder.tenderWorkTextView.setText(work.getText());
        if (work.getAuthor() != null) {
            viewHolder.authorTextView.setText(work.getAuthor().getName());
        }
        Glide.with(context)
                .load(tenderTaskWorkArrayList.get(i).getPhotoUrl())
                .into(viewHolder.tenderWorkImageView);

    }

    @Override
    public int getItemCount() {
        return tenderTaskWorkArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenderWorkTextView;
        TextView authorTextView;
        ImageView tenderWorkImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenderWorkTextView = itemView.findViewById(R.id.tender_work_item_text_view);
            authorTextView = itemView.findViewById(R.id.tender_work_item_author_text_view);
            tenderWorkImageView = itemView.findViewById(R.id.tender_work_item_image_view);
        }
    }
}
