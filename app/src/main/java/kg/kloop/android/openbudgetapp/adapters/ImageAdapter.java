package kg.kloop.android.openbudgetapp.adapters;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter{
    private Context mContext;
    private ArrayList<String> urls;

    public ImageAdapter(Context context, ArrayList<String> urls) {
        this.mContext = context;
        this.urls = urls;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((PhotoView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(mContext);
        Glide.with(mContext)
                .load(urls.get(position))
                .into(photoView);
        ((ViewPager) container).addView(photoView, 0);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return urls.size();
    }
}