package kg.kloop.android.openbudgetapp.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.fragments.PhotoViewFragment;

public class ImageViewFragmentAdapter extends RecyclerView.Adapter<ImageViewFragmentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> urls;

    public ImageViewFragmentAdapter(Context context, ArrayList<String> urls) {
        this.context = context;
        this.urls = urls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        ImageView imageView = holder.imageView;
        if (urls != null) {
            Glide.with(context)
                    .load(urls.get(position))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image_view);
            progressBar = itemView.findViewById(R.id.item_image_view_progress_bar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            Bundle bundle = new Bundle();
            String url = urls.get(getAdapterPosition());
            bundle.putString("photo_url", url);
            PhotoViewFragment fragment = PhotoViewFragment.newInstance();
            fragment.setArguments(bundle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragment_image_view_frame_layout, fragment).addToBackStack("photo");
            transaction.commit();
        }
    }

}
