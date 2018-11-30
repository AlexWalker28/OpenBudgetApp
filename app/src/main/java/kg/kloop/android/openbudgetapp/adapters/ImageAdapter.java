package kg.kloop.android.openbudgetapp.adapters;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter{
    private Context mContext;
    private ArrayList<String> urls;
    private ArrayList<Uri> uris;

    public ImageAdapter(Context context, ArrayList<String> urls) {
        this.mContext = context;
        this.urls = urls;
    }
    //isLocal is just to distinguish between basically the same constructors
    public ImageAdapter(Context context, ArrayList<Uri> uris, boolean isLocal) {
        this.mContext = context;
        this.uris = uris;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((PhotoView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(mContext);
        if (urls != null) {
            Glide.with(mContext)
                    .load(urls.get(position))
                    .into(photoView);
            ((ViewPager) container).addView(photoView, 0);
        } else {
            Glide.with(mContext)
                    .load(uris.get(position))
                    .into(photoView);
            ((ViewPager) container).addView(photoView, 0);
        }

        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        if (urls != null) {
            return urls.size();
        } else return uris.size();
    }
}