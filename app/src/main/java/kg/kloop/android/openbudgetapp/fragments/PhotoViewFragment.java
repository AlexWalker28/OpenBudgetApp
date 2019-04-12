package kg.kloop.android.openbudgetapp.fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import kg.kloop.android.openbudgetapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoViewFragment extends Fragment {


    public PhotoViewFragment() {
    }

    public static PhotoViewFragment newInstance() {

        Bundle args = new Bundle();

        PhotoViewFragment fragment = new PhotoViewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_photo_view, container, false);

        PhotoView photoView = view.findViewById(R.id.fragment_photo_view);
        final ProgressBar progressBar = view.findViewById(R.id.fragment_photo_view_progress_bar);
        String url = getArguments().getString("photo_url");
        if (url != null) {
            Glide.with(this)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_LONG).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(photoView);
        }

        return view;
    }

}
